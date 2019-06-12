package top.poweyes.config.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/*
 *  @项目名：  poweyes
 *  @包名：    top.poweyes.config.redis
 *  @文件名:   RedisConfig
 *  @创建者:   ouyangxiong
 *  @创建时间:  2019-04-19 13:50
 *  @描述：    TODO
 */
@Configuration
public class RedisConfig {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    /**
     * @deprecated  描述：需要手动注册RedisMessageListenerContainer加入IOC容器
     * @return
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer() {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();

        container.setConnectionFactory(redisConnectionFactory);

        return container;

    }
}
