package com.guigu.bigfactory.spring;

public class UserDaoImpl implements UserDao{


    @Override
    public int add(int a, int b) {
        System.out.println("add()方法执行...");
        return a+b;
    }

    @Override
    public String update(String id) {
        System.out.println("update()方法执行...");
        return id;
    }
}
