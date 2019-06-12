package top.poweyes.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 *  @项目名：  poweyes
 *  @包名：    top.poweyes.dto
 *  @文件名:   TimeData
 *  @创建者:   ouyangxiong
 *  @创建时间:  2019-04-24 14:48
 *  @描述：    TODO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeData {
//    private String location;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer timeClass;
    private String locationType;
    //    private Long startTime;
    private Integer alarmCount;
}
