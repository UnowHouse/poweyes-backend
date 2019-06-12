package top.poweyes.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 *  @项目名：  poweyes
 *  @包名：    top.poweyes.dto
 *  @文件名:   DaData
 *  @创建者:   ouyangxiong
 *  @创建时间:  2019-04-24 12:54
 *  @描述：    TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DaData {
    private String location;
    private String locationType;
    private Integer totalAlarm;
    private Integer maxHeadCount;
    private Integer avgHeadCount;
    private String province = "";
    private String city = "";
    private String district = "";
    private String township = "";
    private String streetNum = "";
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long startTime;
}
