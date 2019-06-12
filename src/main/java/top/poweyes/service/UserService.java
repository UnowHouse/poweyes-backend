package top.poweyes.service;

/*
 *  @项目名：  poweyes
 *  @包名：    top.poweyes.service
 *  @文件名:   UserService
 *  @创建者:   ouyangxiong
 *  @创建时间:  2019-04-26 21:58
 *  @描述：    TODO
 */

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import top.poweyes.common.exception.ExceptionEnum;
import top.poweyes.dto.ResultViewModel;
import top.poweyes.mapper.UserMapper;
import top.poweyes.pojo.User;
import top.poweyes.utils.ResultViewModelUtil;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Value("${poweyes.tokenFormat}")
    private String tokenFormat;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    public ResultViewModel<String> queryUser(String username, String password, Boolean remember){

        User record = new User();
        record.setUsername(username);
        record.setPassword(password);
        User user = userMapper.selectOne(record);
        if(user == null){
            return ResultViewModelUtil.error(ExceptionEnum.USER_ERROR.getStatus(),ExceptionEnum.USER_ERROR.getMessage());
        }
        String token = UUID.randomUUID().toString().replace("-","");
        user.setKeepAlive(remember);

        if(remember){
            stringRedisTemplate.opsForValue().set(token, JSONObject.toJSONString(user),7, TimeUnit.DAYS);
        }else{
            stringRedisTemplate.opsForValue().set(token,JSONObject.toJSONString(user),30,TimeUnit.MINUTES);
        }
        return ResultViewModelUtil.success(token);
    }


    public User getUserInfo(String token) {

        String s = stringRedisTemplate.opsForValue().get(token);
        User user = JSONObject.parseObject(s, User.class);
        return user;

    }

    public Boolean freshUserToken(String token){
        if(token==null){
            return false;
        }
        String user = stringRedisTemplate.opsForValue().get(token);
        if(user != null){
            User userInfo = JSONObject.parseObject(user, User.class);
            if(!userInfo.getKeepAlive()){
                stringRedisTemplate.opsForValue().set(token,user,30,TimeUnit.MINUTES);
            }
            return true;
        }
        return false;
    }

    public void logout(String token) {
        stringRedisTemplate.delete(token);
    }
}
