package com.guigu.bigfactory.juc.aqs;

public class ThreadLocalDemo1Test {


    public static  ThreadLocal<String> t = new ThreadLocal<String>(){
        @Override
        protected String initialValue(){
            System.out.println("Initial Value run .........,线程名字:"+Thread.currentThread().getName());
            return Thread.currentThread().getName();
        }

    };

    public static void main(String[] args) {
        System.out.println(t.get());
        t.set("hello world");//set(T value) key：Thread.currentThread()  val:value
        System.out.println(t.get());
    }





}
