package top.poweyes.web;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import top.poweyes.dto.ResultViewModel;
import top.poweyes.pojo.RecordMessage;
import top.poweyes.service.MessagePublicService;
import top.poweyes.utils.ResultViewModelUtil;

import java.util.List;
import java.util.stream.Collectors;

/*
 *  @项目名：  poweyes
 *  @包名：    top.poweyes.web
 *  @文件名:   DemoController
 *  @创建者:   ouyangxiong
 *  @创建时间:  2019-04-20 23:56
 *  @描述：    操作缓存API
 */
@RestController
@RequestMapping("cache")
public class RedisController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private MessagePublicService messagePublicService;


    private static final String POWEYES_INSTANCE_KEY = "POWEYES_INSTANCE";
    private static final String POWEYES_DAY_DATA_KEY = "POWEYES_DAY_DATA";
    private static final String POWEYES_THRESHOLD_KEY = "POWEYES_THRESHOLD";
    private static final String DEFAULT_THRESHOLD = "10";

    /**
     * 获取单日报警数
     * @return
     */
    @GetMapping("getDayAlarmCount")
    public ResultViewModel<Long> getDayAlarmCount(){
        Long size = stringRedisTemplate.opsForList().size(POWEYES_INSTANCE_KEY);
        return ResultViewModelUtil.success(size);
    }

    /**
     * 获取当日报警数据
     * @return
     */
    @GetMapping("getDayAlarmData")
    public ResultViewModel<RecordMessage> getDayAlarmData(){
        List<String> range = stringRedisTemplate.opsForList().range(POWEYES_DAY_DATA_KEY, 0, -1);
        if(!CollectionUtils.isEmpty(range)) {
            List<RecordMessage> collect = range.stream().map(s -> JSONObject.parseObject(s, RecordMessage.class)).collect(Collectors.toList());
            return ResultViewModelUtil.success(collect);
        }
        return ResultViewModelUtil.success();
    }

    /**
     * 获取系统报警阈值
     * @return
     */
    @GetMapping("getThreshold")
    public ResultViewModel<Integer> getThreshold(){
        String s = stringRedisTemplate.opsForValue().get(POWEYES_THRESHOLD_KEY);
        if(!StringUtils.isNotBlank(s)){
            stringRedisTemplate.opsForValue().set(POWEYES_THRESHOLD_KEY,DEFAULT_THRESHOLD);
            s = DEFAULT_THRESHOLD;
        }
        return ResultViewModelUtil.success(Integer.valueOf(s));
    }

    /**
     * 更新系统报警阈值
     * @param threshold
     * @return
     */
    @PutMapping("updateThreshold")
    public ResultViewModel<Void> updateThreshold(@RequestParam("threshold") String threshold){
        stringRedisTemplate.opsForValue().set(POWEYES_THRESHOLD_KEY,threshold);
        messagePublicService.publish("alive", threshold);
        return ResultViewModelUtil.success();
    }

}
