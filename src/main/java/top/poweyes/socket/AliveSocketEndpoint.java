package top.poweyes.socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import top.poweyes.mq.SubscribeListener;
import top.poweyes.utils.SpringUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/*
 *  @项目名：  poweyes
 *  @包名：    top.poweyes.socket
 *  @文件名:   AliveSocketEndpoint
 *  @创建者:   ouyangxiong
 *  @创建时间:  2019-04-20 21:47
 *  @描述：    示例服务器，让云监管平台得知目前有多少台边缘端程序连接上成功连接上
 */
@Component
@ServerEndpoint(value = "/alive/{id}")
public class AliveSocketEndpoint {

    /**
     * 云监管平台前端页面连接id
     */
    private static final String showCenter = "10086";

    /**
     * 存放该服务器该ws的所有连接。用处：比如向所有连接该ws的用户发送通知消息。
     */
    private static ConcurrentHashMap<String,AliveSocketEndpoint> sessions = new ConcurrentHashMap();

    /**
     * redis消息监听容器
     */
    private RedisMessageListenerContainer redisMessageListenerContainer = SpringUtils.getBean(RedisMessageListenerContainer.class);

    /**
     * 当前连接到云平台到边缘端程序
     */
    private static List<String> aliveList = new ArrayList();

    /**
     * 日志打印
     */
    private static Logger log = LogManager.getLogger(AliveSocketEndpoint.class);

    /**
     * 当前session
     */
    private Session session;

    /**
     * 消息监听器
     */
    private SubscribeListener subscribeListener;

    /**
     * 当前边缘端ip
     */
    private String ipMessage;

    @OnOpen
    public void onOpen(@PathParam("id")String id, Session session){
        log.info("监控端"+id+session.getId()+"连接到服务器");
        this.session = session;
        sessions.put(id,this);
        if(id.equals(showCenter)){
            session.getAsyncRemote().sendText(aliveList.toString());
        }else{
            subscribeListener = new SubscribeListener();
            subscribeListener.setSession(session);
            redisMessageListenerContainer.addMessageListener(subscribeListener, new ChannelTopic("alive"));
        }
    }

    @OnMessage
    public void onMessage(Session session, String message, @PathParam("id")String id){
        this.ipMessage = message;
        if(!id.equals(showCenter) ) {
            aliveList.add(message);
            AliveSocketEndpoint showCenterPoint = this.sessions.get(showCenter);
            if (showCenterPoint != null) {
                showCenterPoint.session.getAsyncRemote().sendText(aliveList.toString());
            }
        }
    }

    @OnClose
    public void onClose(Session session, @PathParam("id")String id){

        log.info("监控端"+id+"-"+session.getId()+"断开服务器");
        if(!id.equals(showCenter) ) {
            for (String s : aliveList) {
                if(s.equals(this.ipMessage)){
                    aliveList.remove(s);
                    break;
                }
            }
            AliveSocketEndpoint showCenterPoint = this.sessions.get(showCenter);
            if (showCenterPoint != null){
                showCenterPoint.session.getAsyncRemote().sendText(aliveList.toString());
            }
        }
        this.sessions.remove(id);
        if(subscribeListener != null)
            redisMessageListenerContainer.removeMessageListener(subscribeListener);

    }

    @OnError
    public void onError(Session session, @PathParam("id")String id,Throwable throwable){

        log.info("监控端"+id+"-"+session.getId()+"发生错误");
        if(!id.equals(showCenter)) {
            this.sessions.get(showCenter).session.getAsyncRemote().sendText(this.ipMessage);
        }
    }

}
