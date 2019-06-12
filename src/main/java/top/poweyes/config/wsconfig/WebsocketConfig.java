package top.poweyes.config.wsconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/*
 *  @项目名：  poweyes
 *  @包名：    top.poweyes.config.wsconfig
 *  @文件名:   WebsocketConfig
 *  @创建者:   ouyangxiong
 *  @创建时间:  2019-04-19 13:56
 *  @描述：    TODO
 */
@Configuration
public class WebsocketConfig {
    /**
     * <br>描 述:    @Endpoint注解的websocket交给ServerEndpointExporter自动注册管理
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
}
