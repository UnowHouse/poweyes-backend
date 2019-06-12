package top.poweyes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/*
 *  @项目名：  poweyes
 *  @包名：    top.poweyes
 *  @文件名:   PoweyesApplication
 *  @创建者:   ouyangxiong
 *  @创建时间:  2019-04-18 23:45
 *  @描述：    TODO
 */
@MapperScan("top.poweyes.mapper")
@SpringBootApplication
public class PoweyesApplication {
    public static void main(String [] args){
        SpringApplication.run(PoweyesApplication.class,args);
    }
}
