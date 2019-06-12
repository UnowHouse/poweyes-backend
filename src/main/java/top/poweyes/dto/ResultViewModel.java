package top.poweyes.dto;

import lombok.Data;

/*
 *  @项目名：  poweyes
 *  @包名：    top.poweyes.dto
 *  @文件名:   ResultViewModel
 *  @创建者:   ouyangxiong
 *  @创建时间:  2019-04-30 20:03
 *  @描述：    TODO
 */
@Data
public class ResultViewModel<T> {
    private Integer code;
    private String message;
    private T data;
}