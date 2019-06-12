package top.poweyes.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.poweyes.common.exception.ExceptionEnum;
import top.poweyes.dto.ResultViewModel;
import top.poweyes.pojo.User;
import top.poweyes.service.UserService;
import top.poweyes.utils.ResultViewModelUtil;

/*
 *  @项目名：  poweyes
 *  @包名：    top.poweyes.web
 *  @文件名:   UserController
 *  @创建者:   ouyangxiong
 *  @创建时间:  2019-04-26 22:11
 *  @描述：    TODO
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登陆
     * @param username
     * @param password
     * @param remember
     * @return
     */
    @PostMapping("login")
    public ResultViewModel<String> login(@RequestParam("username")String username,
                                         @RequestParam("password")String password,
                                         @RequestParam(value = "remember" , defaultValue = "false")Boolean remember){
        return userService.queryUser(username, password,remember);
    }

    /**
     * 获取用户信息
     * @param token
     * @return
     */
    @GetMapping("getInfo")
    public ResultViewModel<User> getUserInfo(@RequestParam("token")String token){
        User userInfo = userService.getUserInfo(token);
        if(userInfo == null){
            return ResultViewModelUtil.error(ExceptionEnum.UNAUTHORIZED_ERROR.getStatus(),ExceptionEnum.UNAUTHORIZED_ERROR.getMessage());
        }
        return ResultViewModelUtil.success(userInfo);
    }

    /**
     * 用户注销
     * @param token
     * @return
     */
    @GetMapping("logout")
    public ResultViewModel<Void> logout(@RequestParam("token")String token){
        userService.logout(token);
        return ResultViewModelUtil.success();

    }


}
