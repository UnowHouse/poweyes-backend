package top.poweyes.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import top.poweyes.dto.DaData;
import top.poweyes.dto.PageResult;
import top.poweyes.dto.TimeData;
import top.poweyes.mapper.RecordMessageMapper;
import top.poweyes.pojo.RecordMessage;
import top.poweyes.utils.DataUtils;

import java.util.*;

/*
 *  @项目名：  poweyes
 *  @包名：    top.poweyes.service
 *  @文件名:   RecordMessageService
 *  @创建者:   ouyangxiong
 *  @创建时间:  2019-04-21 23:55
 *  @描述：    TODO
 */
@Service
public class RecordMessageService {
    @Autowired
    private RecordMessageMapper recordMessageMapper;

    public void insertRecordMessage(RecordMessage recordMessage){
        recordMessageMapper.insert(recordMessage);
    }


    public PageResult<RecordMessage> getRecordMessages(Integer page, Integer limit, Long startDate, Long endDate, String location, String locationType, String order, String orderColumn) {

        PageHelper.startPage(page,limit);

        Example example = new Example(RecordMessage.class);


        if(StringUtils.isNotBlank(location)){
            example.and(example.createCriteria().orLike("location", "%" + location + "%")
                    .orLike("locationType","%"+location+"%"));
        }


        if(StringUtils.isNotBlank(locationType)){
            Example.Criteria criteria = example.createCriteria();
            String[] locationTypes = locationType.split(",");
            for (String item : locationTypes) {
                criteria.orEqualTo("locationType",item);
            }
            example.and(criteria);
        }

        if(startDate != null){
            Example.Criteria criteria = example.createCriteria();
            criteria.andGreaterThanOrEqualTo("startTime",startDate);
            if(endDate != null){
                criteria.andLessThanOrEqualTo("endTime",endDate);
            }
            example.and(criteria);
        }

        if("descending".equals(order))
            example.setOrderByClause(DataUtils.camel2Underline(orderColumn)+ " " +"DESC");
        else
            example.setOrderByClause(DataUtils.camel2Underline(orderColumn)+ " " +"ASC");

        Page<RecordMessage> recordMessages = (Page<RecordMessage>)recordMessageMapper.selectByExample(example);

        return new PageResult(recordMessages.getTotal(),recordMessages.getResult());

    }

    public Map<String,Object> getSatisticData() {
        HashMap<String, Object> data = new HashMap<>();

        List<DaData> daDatas = recordMessageMapper.selectDaHistoryRecord();

//        Set<List<TimeData>> timeDatas = new HashSet<>();
//        for (DaData daData : daDatas) {
//            List<TimeData> timeData = recordMessageMapper.selectDaTimeRecord(daData.getLocation());
//            timeDatas.add(timeData);
//        }

        List<TimeData> timeDatas = recordMessageMapper.selectLocationTypeTimeRecord();

        Date date = new Date();
        HashMap<Integer, List<DaData>> lately7Data = new HashMap<>();
        for(int i = 0; i < 6; i++){
            Calendar instance = Calendar.getInstance();
            instance.setTime(date);
            int day = instance.get(Calendar.DATE);
            instance.set(Calendar.MINUTE,0);
            instance.set(Calendar.SECOND,0);
            instance.set(Calendar.DATE,day-i);
            instance.set(Calendar.HOUR_OF_DAY,0);

            long startTime = instance.getTimeInMillis();
            List<DaData> daData = recordMessageMapper.selectDaHistoryRecordByTime(startTime, startTime + 24 * 60 * 60 * 1000);
            lately7Data.put(i,daData);
        }
        List<TimeData> countItems = recordMessageMapper.selectCountItems();
        data.put("countItems",countItems);
        data.put("topItems",daDatas);
        data.put("timeItems",timeDatas);
        data.put("latelyItems",lately7Data);


        return data;
    }
}
