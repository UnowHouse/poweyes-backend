package top.poweyes.pojo;

import lombok.Data;

import javax.persistence.Table;

/*
 *  @项目名：  poweyes
 *  @包名：    top.poweyes.dto
 *  @文件名:   RecordMessage
 *  @创建者:   ouyangxiong
 *  @创建时间:  2019-04-21 13:54
 *  @描述：    TODO
 */
@Data
@Table(name = "record_message")
public class RecordMessage {
    private Integer id;
    private String ipAddress;
    private String longitude;
    private String latitude;
    private Integer avgHeadCount;
    private Integer maxHeadCount;
    private Long startTime;
    private Long endTime;
    private Long alarmDuration;
    private String location;
    private String locationType;
    private Integer timeClass;
    private String videoSrc;
    private String province;
    private String city;
    private String district;
    private String township;
    private String streetNum;
}
