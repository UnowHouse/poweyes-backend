package top.poweyes.dto;

import lombok.Data;
import top.poweyes.pojo.Manager;

/*
 *  @项目名：  poweyes
 *  @包名：    top.poweyes.dto
 *  @文件名:   AlarmMessage
 *  @创建者:   ouyangxiong
 *  @创建时间:  2019-04-29 13:21
 *  @描述：    TODO
 */
@Data
public class AlarmMessage {
    private String socketId;
    private String ipAddress;
    private String longitude;
    private String latitude;
    private Integer headCount;
    private Long startTime;
    private Boolean isAlarm;
    private String location;
    private String locationType;
    private Long currentTime;
    private String province;
    private String city;
    private String township;
    private String streetNum;
    private Manager manager;

}
