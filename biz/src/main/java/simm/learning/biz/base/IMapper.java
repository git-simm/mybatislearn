package simm.learning.biz.base;

import java.util.List;

/**
 * Mapper基础接口
 * @param <T>
 */
public interface IMapper<T> {
    int deleteByPrimaryKey(Integer key);

    int insert(T record);

    int insertSelective(T record);

    T selectByPrimaryKey(Integer key);

    List<T> selectAll();

    int updateByPrimaryKeySelective(T record);

    int updateByPrimaryKey(T record);
}
