package simm.learning.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import simm.learning.biz.dao.UserBiz;
import simm.learning.biz.entity.RequestJson;
import simm.learning.biz.entity.authority.User;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserBiz userBiz;
    //@Autowired
    //ConfigApiMapper configApiMapper;

    @RequestMapping("/list")
    @ResponseBody
    public List<User> getList(@RequestBody RequestJson<User> req){
        List<User> users = userBiz.selectAll();
        return users;
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

