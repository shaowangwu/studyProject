package com.guigu.bigfactory.juc.aqs;

public class ThreadLocalDemo2Test {


    private static ThreadLocal<String> ss = new ThreadLocal<String>(){

        @Override
        protected String initialValue() {
            System.out.println("initialValue run,线程名:"+Thread.currentThread().getName());
            return Thread.currentThread().getName();
        }
    };


    public static void main(String[] args) {

        System.out.println(ss.get() + "\t");
        ss.set("good");
        /*new Thread(){
            @Override
            public void run(){
                System.out.println(ss.get());

            }
        }.start();*/
        //lambda表达式写法
        new Thread(()->{
            System.out.println("测试1-thread 尝试获取："+ss.get());//获取不到别的线程设置的值,只能活得init方法返回线程名
        },"测试1-thread").start();

        //上面ss.set("good") 使用的是main去赋值存储变量，
        // 而new Thread().start()多线程里面的ss.get()获取不到因为是不同线程
        System.out.println("当前线程为:"+Thread.currentThread().getName()+"获取值: "+ss.get());

        //使用完清除
        ss.remove();
        System.out.println("清除完后再获取为null"+",线程是："+Thread.currentThread().getName()
                +",只能得到init返回的线程名字:" +ss.get());

    }




}
