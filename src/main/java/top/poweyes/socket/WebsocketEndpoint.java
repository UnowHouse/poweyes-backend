package top.poweyes.socket;

import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import top.poweyes.dto.AlarmMessage;
import top.poweyes.mapper.ManagerMapper;
import top.poweyes.mq.RecordListener;
import top.poweyes.mq.SubscribeListener;
import top.poweyes.pojo.Manager;
import top.poweyes.service.MessagePublicService;
import top.poweyes.utils.DataUtils;
import top.poweyes.utils.SpringUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

/*
 *  @项目名：  poweyes
 *  @包名：    top.poweyes.socket
 *  @文件名:   WebsocketEndpoint
 *  @创建者:   ouyangxiong
 *  @创建时间:  2019-04-19 13:58
 *  @描述：    报警服务器，当边缘端程序开始报警时，通过连接报警服务器，将报警数据发送到云监管平台
 */
@Component
@ServerEndpoint(value="/alarm/{topic}/{id}")
public class WebsocketEndpoint {

    /**
     * 云监管平台前端页面连接id
     */
    private static final String SHOW_CENTER = "10086";

    /**
     * 当日报警数
     */
    private static final String POWEYES_INSTANCE_KEY = "POWEYES_INSTANCE";

    /**
     * 当日报警数据详情redis的key
     */
    private static final String POWEYES_DAY_DATA_KEY = "POWEYES_DAY_DATA";
    /**
     * 因为@ServerEndpoint不支持注入，所以使用SpringUtils获取IOC实例
     */
    private StringRedisTemplate redisTampate = SpringUtils.getBean(StringRedisTemplate.class);

    /**
     * 操作安保人员数据库表对象
     */
    private ManagerMapper managerMapper = SpringUtils.getBean(ManagerMapper.class);

    /**
     * 使用线程安全到ConcurrentHashMap，存储不同报警连接发送来到报警数据
     */
    private static ConcurrentHashMap<String, ArrayList<String>> eyesLogMessage = new ConcurrentHashMap();

    /**
     * redis消息监听容器
     */
    private RedisMessageListenerContainer redisMessageListenerContainer = SpringUtils.getBean(RedisMessageListenerContainer.class);

    /**
     * 存放该服务器该ws的所有连接。用处：比如向所有连接该ws的用户发送通知消息。
     */
    private static CopyOnWriteArraySet<WebsocketEndpoint> sessions = new CopyOnWriteArraySet();

    /**
     * 当前连接session
     */
    private Session session;

    /**
     * 当前连接id
     */
    private String id;

    /**
     * 当前报警连接匹配到的治安人员
     */
    private Manager manager;

    /**
     * 当前报警连接的消息监听器
     */
    private SubscribeListener subscribeListener;

    /**
     * 当前报警连接的记录存储监听起
     */
    private RecordListener recordListener;

    /**
     * 日志打印
     */
    private static Logger log = LogManager.getLogger(WebsocketEndpoint.class);

    @OnOpen
    public void onOpen(Session session, @PathParam("topic") String topic, @PathParam("id")String id){

        // 该次报警连接存入到sessions容器
        this.session = session;
        this.id = id;
        sessions.add(this);

        // 云监管平台前端报警首页面连接到云监管平台后端（取数据）
        if(topic.equals("showCenter")){
            log.info("云监管平台"+session.getId()+"前端页面：开启监听");
            subscribeListener = new SubscribeListener();
            subscribeListener.setSession(session);
            subscribeListener.setStringRedisTemplate(redisTampate);
            redisMessageListenerContainer.addMessageListener(subscribeListener, new ChannelTopic(SHOW_CENTER));
        }
        // 云监管平台前端单个报警详情页面连接到云监管平台后端（取数据）
        else if(topic.equals("showDetail")){
            this.id = "detail-"+id;
            subscribeListener = new SubscribeListener();
            subscribeListener.setSession(session);
            subscribeListener.setStringRedisTemplate(redisTampate);
            redisMessageListenerContainer.addMessageListener(subscribeListener, new ChannelTopic(this.id));
            session.getAsyncRemote().sendText(this.eyesLogMessage.get(id).toString());
        }
        // 边缘端报警连接（送数据）
        else{
            log.info("异常报警：本次报警id为："+id+"");
            recordListener = new RecordListener();
            redisMessageListenerContainer.addMessageListener(recordListener, new ChannelTopic(id));
            this.eyesLogMessage.put(id,new ArrayList<String>(256));
            Long current = new Date().getTime();
            pushCacheDayData(POWEYES_INSTANCE_KEY, current.toString(),current);
        }

    }

    @OnMessage
    public void onMessage(Session session,String message,@PathParam("topic")String topic,@PathParam("id")String id) {

        // 使用消息推送服务
        MessagePublicService messagePublicService = SpringUtils.getBean(MessagePublicService.class);
        AlarmMessage alarmMessage = JSONObject.parseObject(message, AlarmMessage.class);
        // 当前数据是报警中的实时数据
        if(alarmMessage.getIsAlarm()) {
            if (manager == null) {
                double longtitude = Double.parseDouble(alarmMessage.getLongitude());
                double latitude = Double.parseDouble(alarmMessage.getLatitude());
                this.manager = managerMapper.getNearByManager(longtitude, latitude);
                if (this.manager != null) {
                    managerMapper.updateManagerStatus(this.manager.getId(), false);
                }
            }

            alarmMessage.setManager(this.manager);
            message = JSONObject.toJSON(alarmMessage).toString();
            System.out.println(message);
        }
        //当前数据是报警结束综合数据
        else{
            long time = new Date().getTime();
            pushCacheDayData(POWEYES_DAY_DATA_KEY, message,time);
            messagePublicService.publish(id, message);
        }

        // 将数据推送到 云平台前端
        messagePublicService.publish(SHOW_CENTER, message);
        messagePublicService.publish("detail-"+id,message);
        this.eyesLogMessage.get(id).add(message);

    }

    @OnClose
    public void onClose(Session session,@PathParam("topic")String topic,@PathParam("id")String id){
        if(topic.equals("eye")){
            log.info("报警"+this.id+"，报警结束");
            if(manager != null)
                managerMapper.updateManagerStatus(this.manager.getId(),true);
            this.eyesLogMessage.remove(this.id);
        }
        else {
            log.info("云监管平台"+this.session.getId()+"前端监听：关闭");
        }
        if(subscribeListener != null)
            redisMessageListenerContainer.removeMessageListener(subscribeListener);
        if(recordListener != null)
            redisMessageListenerContainer.removeMessageListener(recordListener);
        sessions.remove(this);
    }

    @OnError
    public void onError(Session session,Throwable error) {
        if(!id.equals(SHOW_CENTER))
            log.error("发生错误：报警"+this.id+"，数据发送错误");
        else
            log.error("发生错误: 云监控平台"+session.getId()+"前端监听，发生错误");
        error.printStackTrace();
    }

    // 将当日数据存入redis缓存
    private void pushCacheDayData(String key, String value, long current){
        Boolean hasKey = redisTampate.hasKey(key);
        if(!hasKey){
            redisTampate.opsForList().leftPush(key, value);
            Long duration = DataUtils.surplusDayTime(current);
            redisTampate.expire(key,duration, TimeUnit.MILLISECONDS);
        }else{
            redisTampate.opsForList().leftPush(key,value);
        }
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }


}
