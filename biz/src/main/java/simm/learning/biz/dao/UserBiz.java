package simm.learning.biz.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import simm.learning.biz.base.BaseBiz;
import simm.learning.biz.entity.authority.Role;
import simm.learning.biz.entity.authority.User;
import simm.learning.biz.mapper.authority.RoleMapper;
import simm.learning.biz.mapper.authority.UserMapper;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserBiz extends BaseBiz<UserMapper,User> {
    @Autowired
    private RoleMapper roleMapper;
    /**
     * 获取所有的数据
     * @return
     */
    @Override
    public List<User> selectAll(){
        List<User> users = baseMapper.selectAll();
        List<Role> roles = new ArrayList<>();
        roles.add(roleMapper.selectByPrimaryKey(1));
        for(User user :users){
            user.setRoles(roles);
            user.setDefaultRole(roles.get(0));
        }
        return users;
    }
}
