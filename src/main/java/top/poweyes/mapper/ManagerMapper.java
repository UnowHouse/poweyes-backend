package top.poweyes.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;
import top.poweyes.pojo.Manager;

/*
 *  @项目名：  poweyes
 *  @包名：    top.poweyes.mapper
 *  @文件名:   ManagerMapper
 *  @创建者:   ouyangxiong
 *  @创建时间:  2019-04-26 21:27
 *  @描述：    TODO
 */
public interface ManagerMapper extends Mapper<Manager> {

    @Select("SELECT a.*,SQRT(POW(longitude-#{longitude} ,2)+POW(latitude-#{latitude} ,2)) AS distance " +
            "FROM " +
            "(SELECT * FROM `tb_manager` " +
            "WHERE " +
            "TRUNCATE(`longitude`,2) = TRUNCATE(#{longitude},2) " +
            "AND " +
            "TRUNCATE(`latitude`,2) = TRUNCATE(#{latitude},2) " +
            "AND " +
            "`status` = 1) a " +
            "ORDER BY `distance` ASC LIMIT 1")
    Manager getNearByManager(@Param("longitude") Double longitude, @Param("latitude") Double latitude);

    @Update("UPDATE `tb_manager` SET status = #{status} WHERE `id` = #{id}")
    void updateManagerStatus(@Param("id")Integer id,@Param("status")Boolean status);

}
