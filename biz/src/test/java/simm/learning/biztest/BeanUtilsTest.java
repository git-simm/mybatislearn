package simm.learning.biztest;

import org.junit.Test;
import org.springframework.beans.BeanUtils;
import simm.learning.biztest.entities.User1;
import simm.learning.biztest.entities.User2;

public class BeanUtilsTest {
    @Test
    public void testCopyProperties(){
        User1 user1 = new User1();
        user1.setName("simm");
        User2 user2 = new User2();
        //不同对象，属性一致也可以对拷，牛逼了
        BeanUtils.copyProperties(user1,user2);
        System.out.println(user2.getName());
    }
}
