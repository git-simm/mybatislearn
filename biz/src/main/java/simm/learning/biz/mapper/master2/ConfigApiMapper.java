package simm.learning.biz.mapper.master2;

import org.springframework.stereotype.Service;
import simm.learning.biz.entity.ConfigApi;

import java.util.List;

@Service
public interface ConfigApiMapper {
    int deleteByPrimaryKey(Integer apiId);

    int insert(ConfigApi record);

    int insertSelective(ConfigApi record);

    List<ConfigApi> selectAll();

    ConfigApi selectByPrimaryKey(Integer apiId);

    int updateByPrimaryKeySelective(ConfigApi record);

    int updateByPrimaryKey(ConfigApi record);
}