package top.poweyes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/*
 *  @项目名：  poweyes
 *  @包名：    top.poweyes.service
 *  @文件名:   MessagePublicService
 *  @创建者:   ouyangxiong
 *  @创建时间:  2019-04-19 13:52
 *  @描述：    TODO
 */
@Service
public class MessagePublicService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    public void publish(String channel, Object message) {
        // 该方法封装的 connection.publish(rawChannel, rawMessage);
        redisTemplate.convertAndSend(channel, message);
    }

}
