package top.poweyes.config.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import top.poweyes.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 *  @项目名：  poweyes
 *  @包名：    top.poweyes.config
 *  @文件名:   PermissionFilter
 *  @创建者:   ouyangxiong
 *  @创建时间:  2019-04-30 14:09
 *  @描述：    TODO
 */
@Component
public class PermissionFilter extends HandlerInterceptorAdapter {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("X-token");
        return userService.freshUserToken(token);
    }

}
