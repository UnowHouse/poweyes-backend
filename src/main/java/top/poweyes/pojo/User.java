package top.poweyes.pojo;

/*
 *  @项目名：  poweyes
 *  @包名：    top.poweyes.pojo
 *  @文件名:   User
 *  @创建者:   ouyangxiong
 *  @创建时间:  2019-04-26 20:16
 *  @描述：    TODO
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@Table(name = "tb_user")
public class User {

    private Integer id;
    @Length(max = 30, min = 4, message = "用户名长度只能在4-30之间")
    private String username;

    @JsonIgnore
    @Length(max = 30, min = 4, message = "密码长度只能在4-30之间")
    private String password;
    private String face;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean keepAlive;
}
