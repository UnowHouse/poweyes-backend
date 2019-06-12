package top.poweyes.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.poweyes.dto.PageResult;
import top.poweyes.dto.ResultViewModel;
import top.poweyes.mapper.ManagerMapper;
import top.poweyes.pojo.Manager;
import top.poweyes.utils.ResultViewModelUtil;

/*
 *  @项目名：  poweyes
 *  @包名：    top.poweyes.service
 *  @文件名:   ManagerService
 *  @创建者:   ouyangxiong
 *  @创建时间:  2019-05-19 00:13
 *  @描述：    治安人员服务层
 */
@Service
public class ManagerService {

    @Autowired
    private ManagerMapper managerMapper;

    public PageResult<Manager> selectManager(Integer page, Integer limit) {

        PageHelper.startPage(page,limit);
        Page<Manager> managers = (Page<Manager>) managerMapper.selectAll();
        return new PageResult(managers.getTotal(),managers.getResult());
    }

    public ResultViewModel<Void> insertManager(Manager manager) {
        int insert = managerMapper.insert(manager);
        if( insert != 1){
            return ResultViewModelUtil.error(400, "请求参数格式错误");
        }
        return ResultViewModelUtil.success();
    }

    public ResultViewModel<Void> deleteManager(Integer id) {
        int i = managerMapper.deleteByPrimaryKey(id);
        if( i != 1){
            return ResultViewModelUtil.error(400,"要删除的管理员记录不存在");
        }
        return ResultViewModelUtil.success();
    }

    public ResultViewModel<Void> updateManager(Manager manager) {
        int i = managerMapper.updateByPrimaryKeySelective(manager);
        if ( i != 1){
            return ResultViewModelUtil.error(400, "请求参数格式错误");
        }
        return ResultViewModelUtil.success();
    }
}
