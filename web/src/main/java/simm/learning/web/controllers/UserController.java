package simm.learning.web.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import simm.learning.biz.dao.UserBiz;
import simm.learning.biz.entity.RequestJson;
import simm.learning.biz.entity.authority.User;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
@Api("用户信息管理API")
public class UserController {
    @Autowired
    UserBiz userBiz;
    //@Autowired
    //ConfigApiMapper configApiMapper;

    @PostMapping("/list")
    @ApiOperation(value = "查询用户信息", notes = "获取对应的用户数据")
    @ApiImplicitParam(name = "req", value = "学生ID", paramType = "param", required = true, dataType = "RequestJson<User>")
    @ResponseBody
    public List<User> getList(@RequestBody RequestJson<User> req){
        List<User> users = userBiz.selectAll();
        return users;
    }
    @ApiOperation(value = "获取所有用户", notes = "返回数据库中所有的用户列表")
    @GetMapping("/list2")
    @ResponseBody
    public List<User> getList2(){
        List<User> users = userBiz.selectAll();
        boolean bwe = true;
        return users;
    }

    @GetMapping("/index")
    public String index(Map<String,Object> map){
        map.put("name","大王");
        return "index";
    }
    /*
    @RequestMapping("/configlist")
    @ResponseBody
    public List<ConfigApi> getConfigList(){
        List<ConfigApi> list = configApiMapper.selectAll();
        return list;
    }
    */
}

