package top.poweyes.common.exception;

import lombok.Data;

/*
 *  @项目名：  porcelain-helper
 *  @包名：    top.unow.porcelain.common.exception
 *  @文件名:   ExceptionResult
 *  @创建者:   ouyangxiong
 *  @创建时间:  2019-03-17 15:35
 *  @描述：    TODO
 */
@Data
public class ExceptionResult {
    private int status;
    private String message;
    private Long timestamp;
    public ExceptionResult(ExceptionEnum em){
        this.status = em.getStatus();
        this.message = em.getMessage();
        this.timestamp = System.currentTimeMillis();
    }

}
