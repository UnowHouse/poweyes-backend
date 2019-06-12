package top.poweyes.mapper;

/*
 *  @项目名：  poweyes
 *  @包名：    top.poweyes.mapper
 *  @文件名:   RecordMessageMapper
 *  @创建者:   ouyangxiong
 *  @创建时间:  2019-04-21 23:44
 *  @描述：    TODO
 */

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;
import top.poweyes.dto.DaData;
import top.poweyes.dto.TimeData;
import top.poweyes.pojo.RecordMessage;

import java.util.List;

public interface RecordMessageMapper extends Mapper<RecordMessage> {

    @Select("SELECT COUNT(`id`) `total_alarm`, MAX(`max_head_count`) max_head_count, AVG(`avg_head_count`) avg_head_count, `location` , `location_type`, `province`, `city`, `district`,`township`,`street_num` " +
            "FROM `record_message` GROUP BY `location`, `location_type` LIMIT 10")
    List<DaData> selectDaHistoryRecord();

    @Select("SELECT COUNT(`id`) `alarm_count`, `location_type` FROM `record_message` GROUP BY `location_type`")
    List<TimeData> selectCountItems();

    @Select("SELECT COUNT(`id`) `alarm_count`, `time_class` , `location`, `location_type` FROM `record_message` WHERE `location` = #{location} GROUP BY `time_class`,`location_type`")
    List<TimeData> selectDaTimeRecord(@Param("location")String location);

    @Select("SELECT `location`, `location_type`, COUNT(`id`) `total_alarm`,AVG(`avg_head_count`) `avg_head_count`,MAX(`max_head_count`) `max_head_count` " +
            "FROM `record_message` " +
            "WHERE `start_time` >= #{startDay} " +
            "AND `start_time` < #{endDay}  " +
            "GROUP BY `location`")
    List<DaData> selectDaHistoryRecordByTime(@Param("startDay")Long startDay, @Param("endDay")Long endDay);

    @Select("SELECT `location_type`, COUNT(`id`) `alarm_count`, `time_class`" +
            "FROM `record_message` GROUP BY `time_class`,`location_type`")
    List<TimeData> selectLocationTypeTimeRecord();

}
