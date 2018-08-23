package simm.learning.biz.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * 基础业务逻辑层
 * @param <TMapper>
 * @param <TEntity>
 */
public class BaseBiz<TMapper extends IMapper<TEntity>,TEntity> {
    @Autowired
    protected TMapper baseMapper;

    /**
     * 获取所有的数据
     * @return
     */
    public List<TEntity> selectAll(){
        return baseMapper.selectAll();
    }
    /**
     * 新增记录
     * @param entity
     * @return
     */
    //isolation 事务隔离级别
    //@Transactional(isolation = Isolation.SERIALIZABLE)
    @Transactional
    public int add(TEntity entity){
        return baseMapper.insertSelective(entity);
    }
    /**
     * 修改记录
     * @param entity
     * @return
     */
    @Transactional
    public int update(TEntity entity){
        return baseMapper.updateByPrimaryKeySelective(entity);
    }

    /**
     * 删除记录
     * @param id
     * @return
     */
    @Transactional
    public int delete(Integer id){
        return baseMapper.deleteByPrimaryKey(id);
    }

    /**
     * 获取记录
     * @param id
     * @return
     */
    public TEntity selectByPrimaryKey(Integer id){
        return baseMapper.selectByPrimaryKey(id);
    }
}
