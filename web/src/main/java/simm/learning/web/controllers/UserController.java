package simm.learning.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import simm.learning.biz.entity.ConfigApi;
import simm.learning.biz.entity.User;
import simm.learning.biz.mapper.master1.UserMapper;
import simm.learning.biz.mapper.master2.ConfigApiMapper;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserMapper userMapper;
    @Autowired
    ConfigApiMapper configApiMapper;

    @RequestMapping("/list")
    @ResponseBody
    public List<User> getList(){
        List<User> users = userMapper.selectAll();
        return users;
    }

    @RequestMapping("/configlist")
    @ResponseBody
    public List<ConfigApi> getConfigList(){
        List<ConfigApi> list = configApiMapper.selectAll();
        return list;
    }
}

