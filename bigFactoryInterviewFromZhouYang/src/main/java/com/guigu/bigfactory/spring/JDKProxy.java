package com.guigu.bigfactory.spring;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

class UserDaoProxy implements InvocationHandler{
    private Object object;
    //1.把创建的是谁的代理，把这个谁传过来
    //有参构造传递过来，比如UserDaoImpl userDaoImpl
    public UserDaoProxy (Object obj){
        this.object=obj;
    }

    //增强逻辑
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //方法之前
        System.out.println("方法之前执行...."+method.getName()+" :传递的参数,"+ Arrays.toString(args));
        //被增强的方法执行,可以根据method.getName()判断方法名执行不同处理
        Object rest = method.invoke(object, args);

        //方法之后
        System.out.println("方法之后执行..."+object);

        return rest;
    }
}

public class JDKProxy {
    public static void main(String[] args) {
        //创建接口实现类代理对象
        Class[] interfaces={UserDao.class};
        UserDaoImpl userDao = new UserDaoImpl();
        UserDao dao = (UserDao) Proxy.newProxyInstance(JDKProxy.class.getClassLoader(), interfaces, new UserDaoProxy(userDao));
        int add = dao.add(1, 2);
        System.out.println("result: "+add);
    }





}
