package simm.learning.base.inteceptors;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * JDK实现日志代理
 */
public class LogJDKInteceptor implements InvocationHandler {
    private Object target;//被代理的对象

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    /**
     * 绑定业务对象并返回一个代理类
     */
    public Object bind(Object target) {
        this.target = target;  //接收业务实现类对象参数

        // 设置代理对象
        // 参数1:被代理对象的classloader,
        // 参数2:被代理对象所实现的接口（该对象必须要实现接口，不然无法产生代理）
        // 参数3：指定处理的InvocationHandler
        //通过反射机制，创建一个代理类对象实例并返回。用户进行方法调用时使用
        //创建代理对象时，需要传递该业务类的类加载器（用来获取业务实现类的元数据，在包装方法是调用真正的业务方法）、接口、handler实现类
        return Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(), this); }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        beforeMethod(method);//在方法执行前所要执行的业务逻辑
        long starttime=System.currentTimeMillis();

        method.invoke(target, args);

        long result=System.currentTimeMillis()-starttime;
        System.out.println("执行时间为："+result+"毫秒");
        afterMethod(method);//在方法执行后所要执行的业务逻辑
        return null;
    }
    public void beforeMethod(Method m){
        System.out.println(m.getName()+"执行before....");
    }

    public void afterMethod(Method m){
        System.out.println(m.getName()+"执行after...");
    }
}
