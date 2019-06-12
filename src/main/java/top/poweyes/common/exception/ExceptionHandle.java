package top.poweyes.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/*
 *  @项目名：  porcelain-helper
 *  @包名：    top.unow.porcelain.common.exception
 *  @文件名:   ExceptionHandle
 *  @创建者:   ouyangxiong
 *  @创建时间:  2019-03-17 15:35
 *  @描述：    TODO
 */
@ControllerAdvice
public class ExceptionHandle {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResult> handleException(MyException e){
        return ResponseEntity.status(e.getExceptionEnum().getStatus())
                .body(new ExceptionResult(e.getExceptionEnum()));
    }
}
