package simm.learning.basetest.tests;

import org.junit.Test;
import simm.learning.base.inteceptors.LogCGLibInteceptor;
import simm.learning.basetest.dao.UserDao;

public class JDKInteceptorTest {
    @Test
    public void testProxy(){
       /* UserDao userDao=new UserDao();//被代理的对象
        LogJDKInteceptor logInteceptor= new LogJDKInteceptor();//获取日志的InvocationHandler
        logInteceptor.setTarget(userDao);//把被代理的对象设为userDao

        IDao userService= (IDao)logInteceptor.bind(userDao);
        userService.add("张三","sfsf@##@!");//执行方法 */
    }

    @Test
    public void cglibProxy() {
        //创建我们的代理类
        LogCGLibInteceptor proxy = new LogCGLibInteceptor();
        UserDao userDao = (UserDao)proxy.getProxy(UserDao.class);
        userDao.add("张三","sfsf@##@!");
    }
}
