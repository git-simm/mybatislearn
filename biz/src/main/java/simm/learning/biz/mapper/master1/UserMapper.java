package simm.learning.biz.mapper.master1;

import org.springframework.stereotype.Service;
import simm.learning.biz.entity.User;

import java.util.List;

@Service
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    List<User> selectAll();

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}