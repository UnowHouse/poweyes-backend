package top.poweyes.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;

/*
 *  @项目名：  poweyes
 *  @包名：    top.poweyes.pojo
 *  @文件名:   Manager
 *  @创建者:   ouyangxiong
 *  @创建时间:  2019-04-26 20:38
 *  @描述：    TODO
 */
@Data
@Table(name = "tb_manager")
public class Manager {
    private Integer id;
    private String name;
    private String phone;
    private String longitude;
    private String latitude;
    private Boolean status;
    private String province = "";
    private String city = "";
    private String district = "";
    private String streetNum = "";
    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double distance;
}
