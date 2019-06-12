package top.poweyes.mq;

import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.websocket.Session;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/*
 *  @项目名：  poweyes
 *  @包名：    top.poweyes.mq
 *  @文件名:   SubscribeListener
 *  @创建者:   ouyangxiong
 *  @创建时间:  2019-04-19 13:54
 *  @描述：    监听发送到某channel频道中到消息，后将这些信息推送给该channel频道中所有到用户
 */
@Data
public class SubscribeListener implements MessageListener {

    private static Logger log = LogManager.getLogger(SubscribeListener.class);

    private StringRedisTemplate stringRedisTemplate;

    private Session session;


    public void onMessage(Message message, byte[] pattern) {
        String msg = null;
        try {
            msg = new String(message.getBody(),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        synchronized (session){
            if(null!=session && session.isOpen()){
                try {

                    session.getBasicRemote().sendText(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


}
