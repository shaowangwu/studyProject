package com.guigu.bigfactory.jvm;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class MetaspaceOOMTest {


    //静态内部类
    static class OOMTest{

    }

    /**
    *@Description
    *@Author shaowangwu
    *@Date 2022/3/7 16:15
    *@Param
    *@Return
    *@Exception 报错信息：Caused by: java.lang.OutOfMemoryError: Metaspace
     *          多少次创建静态内部类代理之后发生了异常: 4881
    */
    public static void metaspaceTest(String[] args) {

        int i = 0;//模拟创建静态内部类多少次后产生异常
        try {
            while (true){
                i++;
                //依赖于cglib
                Enhancer enhancer = new Enhancer();
                enhancer.setSuperclass(OOMTest.class);
                enhancer.setUseCache(false);
                enhancer.setCallback(new MethodInterceptor() {
                    @Override
                    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                        return methodProxy.invokeSuper(o,args);
                    }
                });
                enhancer.create();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("多少次创建静态内部类代理之后发生了异常: "+i);
        }


    }









}
