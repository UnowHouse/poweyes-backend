package top.poweyes.config.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
 *  @项目名：  poweyes
 *  @包名：    top.poweyes.config.filter
 *  @文件名:   WebSecurityConfig
 *  @创建者:   ouyangxiong
 *  @创建时间:  2019-04-30 14:21
 *  @描述：    TODO
 */
@Configuration
public class WebSecurityConfig implements WebMvcConfigurer {
    @Autowired
    private PermissionFilter permissionFilter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(permissionFilter);
        interceptorRegistration.addPathPatterns("/**").excludePathPatterns("/user/**").excludePathPatterns("/cache/getThreshold");
    }
}
