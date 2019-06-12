package top.poweyes.mq;

import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import top.poweyes.mapper.RecordMessageMapper;
import top.poweyes.pojo.RecordMessage;
import top.poweyes.utils.DataUtils;
import top.poweyes.utils.SpringUtils;

/*
 *  @项目名：  poweyes
 *  @包名：    top.poweyes.mq
 *  @文件名:   RecordListener
 *  @创建者:   ouyangxiong
 *  @创建时间:  2019-04-22 00:37
 *  @描述：    报警记录存储监听器，即监听报警是否结束，结束则将综合数据进行存储
 */

public class RecordListener implements MessageListener {

    private static Logger log = LogManager.getLogger(RecordListener.class);

    private RecordMessageMapper recordMessageMapper = SpringUtils.getBean(RecordMessageMapper.class);

    @Override
    public void onMessage(Message message, byte[] bytes) {

        RecordMessage recordMessage = JSONObject.parseObject(message.getBody(), RecordMessage.class);
        recordMessage.setAlarmDuration(recordMessage.getEndTime()-recordMessage.getStartTime());
        recordMessage.setTimeClass(DataUtils.getTimeHour(recordMessage.getStartTime()));
        if(DataUtils.isOtherLocationType(recordMessage.getLocationType())){
            recordMessage.setLocationType("其他");
        }
        recordMessageMapper.insert(recordMessage);
        log.info("成功向数据库插入数据:"+recordMessage.getLocation());
    }


}
