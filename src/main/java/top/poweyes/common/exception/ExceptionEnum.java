package top.poweyes.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
 *  @项目名：  porcelain-helper
 *  @包名：    top.unow.porcelain.common.exception
 *  @文件名:   ExceptionEnum
 *  @创建者:   ouyangxiong
 *  @创建时间:  2019-03-17 15:37
 *  @描述：    TODO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnum {
    USER_ERROR(400,"账号或错误错误"),
    PASSWORD_ERROR(400, "密码错误"),
    UNAUTHORIZED_ERROR(401, "无权限"),

    ;

    private int status;
    private String message;
}
