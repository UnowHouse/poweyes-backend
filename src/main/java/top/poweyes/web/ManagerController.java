package top.poweyes.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.poweyes.dto.PageResult;
import top.poweyes.dto.ResultViewModel;
import top.poweyes.pojo.Manager;
import top.poweyes.service.ManagerService;
import top.poweyes.utils.ResultViewModelUtil;

/*
 *  @项目名：  poweyes
 *  @包名：    top.poweyes.service
 *  @文件名:   ManagerController
 *  @创建者:   ouyangxiong
 *  @创建时间:  2019-05-19 00:12
 *  @描述：    治安人员API
 */
@RestController
@RequestMapping("manager")
public class ManagerController {
    @Autowired
    private ManagerService managerService;

    /**
     * 获取治安人员列表
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("getManager")
    public ResultViewModel<PageResult<Manager>> getManager(@RequestParam(value = "page",defaultValue = "1")Integer page,
                                                                  @RequestParam(value = "limit",defaultValue = "10")Integer limit) {
        PageResult<Manager> managerPageResult = managerService.selectManager(page, limit);
        return ResultViewModelUtil.success(managerPageResult);
    }

    /**
     * 添加治安人员
     * @param manager
     * @return
     */
    @PostMapping("addManager")
    public ResultViewModel<Void> addManager(@RequestBody Manager manager){
        return managerService.insertManager(manager);
    }

    /**
     * 删除治安人员
     * @param id
     * @return
     */
    @DeleteMapping("removeManager/{id}")
    public ResultViewModel<Void> removeManager(@PathVariable("id")Integer id){
        return managerService.deleteManager(id);
    }

    /**
     * 更新治安人员信息
     * @param manager
     * @return
     */
    @PutMapping("updateManager")
    public ResultViewModel<Void> updateManager(@RequestBody Manager manager){
        return managerService.updateManager(manager);
    }
}
