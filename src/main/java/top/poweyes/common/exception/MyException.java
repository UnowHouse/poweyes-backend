package top.poweyes.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 *  @项目名：  porcelain-helper
 *  @包名：    top.unow.porcelain.common.exception
 *  @文件名:   MyException
 *  @创建者:   ouyangxiong
 *  @创建时间:  2019-03-17 15:33
 *  @描述：    TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyException extends RuntimeException{
    private ExceptionEnum exceptionEnum;
}
