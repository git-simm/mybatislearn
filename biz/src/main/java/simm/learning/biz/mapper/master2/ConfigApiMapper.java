package simm.learning.biz.mapper.master2;

import simm.learning.biz.entity.ConfigApi;

public interface ConfigApiMapper {
    int deleteByPrimaryKey(Integer apiId);

    int insert(ConfigApi record);

    int insertSelective(ConfigApi record);

    ConfigApi selectByPrimaryKey(Integer apiId);

    int updateByPrimaryKeySelective(ConfigApi record);

    int updateByPrimaryKey(ConfigApi record);
}