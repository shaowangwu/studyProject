package com.guigu.bigfactory.jvm;
/**
* @author:shaowangwu
* @Date: 2022/3/7 12:27
* Description:
*/

import java.util.concurrent.TimeUnit;

/***
 * 高并发请求服务器时，经常出现如下异常：java.lang.OutOfMemoryError:unable to create new native thread
 * 准确来讲该native thread异常(其实Error，口语话说异常而已)与队友的平台有光
 *
 * 导致原因：
 * 1.你的应用创建太多线程了，超过了系统承载的极限
 * 2.你的服务器并不允许你的应用程序创建这么多线程，Linux系统默认允许单个进程创建的线程数是1024个，你的应用创建
 * 这么多线程就会报java.lang.OutOfMemoryError:unable to create new native thread
 *
 * 解决办法：
 * 1.想办法降低你应用程序创建线程的数量，分析应用是否真的需要创建这么多个线程
 * 2.对于有的应用，确实需要创建很多线程，远超过Linux系统默认的1024个线程的限制，可以通过修改Linux服务器配置，扩大
 * Linux默认限制
 * ***/
public class OOMOfUnableCreateNewThreadDemo {


    /***
     * 服务器上linux系统默认单个进程线程数是1024，linux上查看命令： ulimit -u,除了root用户无上限其他都限制在1024以内
     * linux 系统中单个进程的最大线程数有其最大的限制 PTHREAD_THREADS_MAX，
     * 这个限制可以在 /usr/include/bits/local_lim.h 中查看，对 linuxthreads 这个值一般是 1024，
     * 修改限制线程数的文件：
     * vim /etc/security/limits.d/90-nproc.conf
     *
     * 报错,注意我这个是在window系统跑的，如下：
     * ************ i= 94470
     * Exception in thread "main" java.lang.OutOfMemoryError: unable to create new native thread
     * **/
    public static void main(String[] args) {
        for (int i = 1; ; i++) {//；；没有小于等于多少，循环没退出条件
            System.out.println("************ i= "+i);

            new Thread(()->{
                try {
                    Thread.sleep(Integer.MAX_VALUE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            },""+i).start();
        }
    }

    
    
    
    
    
}
