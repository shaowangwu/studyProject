package com.guigu.bigfactory.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.Test;

public class TestSpring5 {

    @Test
    public void testAdd(){
        System.out.println();
        //1.加载配置文件
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        //2.获取配置创建的对象
        User user = context.getBean("user",User.class);
        System.out.println(user);
        user.add();



    }

}
