package simm.learning.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import simm.learning.biz.entity.User;
import simm.learning.biz.mapper.UserMapper;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserMapper userMapper;
    @RequestMapping("/list")
    @ResponseBody
    public List<User> getList(){
        List<User> users = userMapper.selectAll();
        return users;
    }
    @RequestMapping("/say/{name}")
    public String say(@PathVariable String name) {
        return "你好，" + name;
    }

    @RequestMapping("/say2/{name}")
    public String say22(@PathVariable String name) {
        return "hello ，" + name;
    }
}

