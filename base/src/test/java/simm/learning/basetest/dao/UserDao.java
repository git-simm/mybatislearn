package simm.learning.basetest.dao;

/**
 * 用户dao层
 */
public class UserDao implements IDao {
    /**
     * 添加一个用户
     * @param name
     * @param psw
     */
    public void add(String name,String psw){
        System.out.println(String.format("name:%s,psw:%s",name,psw));
    }
}
