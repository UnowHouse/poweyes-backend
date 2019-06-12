package top.poweyes.web;

/*
 *  @项目名：  poweyes
 *  @包名：    top.poweyes.web
 *  @文件名:   RecordMessageController
 *  @创建者:   ouyangxiong
 *  @创建时间:  2019-04-22 00:34
 *  @描述：    报警记录数据API
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.poweyes.dto.PageResult;
import top.poweyes.dto.ResultViewModel;
import top.poweyes.pojo.RecordMessage;
import top.poweyes.service.RecordMessageService;
import top.poweyes.utils.ResultViewModelUtil;

import java.util.Map;

@RestController
@RequestMapping("history")
public class RecordMessageController {
    @Autowired
    private RecordMessageService recordMessageService;

    /**
     * 获取历史报警记录
     * @param page
     * @param limit
     * @param startDate
     * @param endDate
     * @param location
     * @param locationType
     * @param order
     * @param orderColumn
     * @return
     */
    @GetMapping("getRecord")
    public ResultViewModel<PageResult<RecordMessage>> getRecordMessages(@RequestParam(value = "page",defaultValue = "1")Integer page,
                                                                        @RequestParam(value = "limit",defaultValue = "10")Integer limit,
                                                                        @RequestParam(value = "startDate",required = false)Long startDate,
                                                                        @RequestParam(value = "endDate",required = false)Long endDate,
                                                                        @RequestParam(value = "location",required = false)String location,
                                                                        @RequestParam(value = "locationType",required = false)String locationType,
                                                                        @RequestParam(value = "order",defaultValue = "descending")String order,
                                                                        @RequestParam(value = "orderColumn",defaultValue = "startTime")String orderColumn

    ){
        PageResult<RecordMessage> recordMessages = recordMessageService.getRecordMessages(page, limit, startDate, endDate, location, locationType, order, orderColumn);
        return ResultViewModelUtil.success(recordMessages);
    }

    /**
     * 获取数据分析结果
     * @return
     */
    @GetMapping("getSatisticData")
    public ResultViewModel<Map<String, Object>> getSatisticData(){
        Map<String, Object> satisticData = recordMessageService.getSatisticData();
        return ResultViewModelUtil.success(satisticData);
    }


}
