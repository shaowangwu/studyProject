package com.guigu.bigfactory.jvm;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Administrator
 */
public class OOMDemo {

    /***vm option eg:
     * -Xmx3550m -Xms3550m -Xss128k -XX:NewRatio=4 -XX:SurvivorRatio=4 -XX:MaxPermSize=16m -XX:MaxTenuringThreshold=15 -XX:+PrintGCDetails -XX:+UseParallelGC -XX:MetaspaceSize=512m -XX:MaxHeapSize=1024m
     * 对OOM认识：
     * 1.java.lang.StackOverflowError
     * 2.java.lang.OutOfMemoryError:Java heap space
     * //注意：上面两个也是Error,不是Exception
     *Throwable
     * 1)Error 1.1 StackOverflowError 1.2 OutOfMemoryError
     * 2)Exception 2.1 RuntimeException
     *
     * 3.java.lang.OutOfMemoryError:GC overhead limit exceeded
     *
     * 4.java.lang.OutOfMemoryError:Direct buffer memory
     *
     * 5.java.lang.OutOfMemoryError:unable to create new native thread
     *
     * 6.java.lang.OutOfMemoryError:Metaspace
     *
     * **/


    public static void main(String[] args) {

        //1.java.lang.StackOverflowError,栈大小默认512k-1024k
        //stackOverflowError();

        //2.java.lang.OutOfMemoryError:Java heap space,
        // 不修改的话默认是1/4内存，比如4G.所以修改参数,方便报错： -Xms10m -Xmx10m
        //oomJavaHeapSpace();

        //3.java.lang.OutOfMemoryError:GC overhead limit exceeded
        //指的是比如说98%资源都用去垃圾回收了，回收效果却很不好，导致GC一直在运行
        //gcOverheadLimit();

        //4.java.lang.OutOfMemoryError:Direct buffer memory
        //directBufferMemory();

        //5.java.lang.OutOfMemoryError:unable to create new native thread
        //unableToCreateNewNativeThread();


        /***
         * 6.java.lang.OutOfMemoryError:Metaspace,是方法区在Hotspot中实现，它与持久代最大区别是：
         * (java8)Metaspace并不在虚拟机内存中而是使用的本地内存
         * 使用java -XX:+PrintFlagsInitial 命令查看本机初始化参数， -XX:MetaspaceSize大约为21m
         *
         *永久代(java8后被元空间取代了)存放了以下信息：
         *虚拟机加载的类信息
         *常量池
         *静态变量
         *即时编译后的代码
         *
         * 模拟溢出：修改初始和默认值，vm options参数,
         * -XX:MetaspaceSize=8m -XX:MaxMetaspaceSize=8m
         *
         * **/
        MetaspaceOOMTest metaspaceOOMTest=new MetaspaceOOMTest();
        metaspaceOOMTest.metaspaceTest(args);




    }

    private static void unableToCreateNewNativeThread() {


    }

    /**
    *@Date 2022/3/7 9:46
     * 4.java.lang.OutOfMemoryError:Direct buffer memory
    *配置参数：
    *-Xms10m -Xmx10m -XX:+PrintGCDetails -XX:MaxDirectMemorySize=5m
    *故障现象：
     * Exception in thread "main" java.lang.OutOfMemory:Direct buffer memory
    *
     * 原因分析：
     * 写NIO程序经常使用ByteBuffer来读取或写入数据，这是一种基于通道(Channel)与缓冲区(Buffer)的I/O方式，
    *  它可以使用native函数库直接分配堆外内存，然后通过一个存储在Java堆里面的DirectByteBuffer对象作为这块内存的引用进行操作。
     *  这样能在一些场景中显著提高性能，因为避免了在Java堆和Native堆中来回复制数据。
     *
     *  ByteBuffer.allocate(capability)第一种方式是分配JVM堆内存，属于GC管辖范围，由于拷贝所以速度较慢。
     *
     *  ByteBuffer.allocateDirect(capability) 第二种方式是分配OS本地内存，不属于GC管辖范围，
     *  由于不需要内存拷贝所以速度相对较快。
     *  但如果不断分配本地内存，堆内存很少使用。那么JVM就不需要执行GC,DirectByteBuffer对象们就不会回收，
     *  这时候堆内存充足，但本地内存可能已经使用光了，再次尝试分配本地内存就会出现OutOfMemoryError:Direct buffer memory,
     *  程序崩溃。
     *  
    */
    private static void directBufferMemory() {

        //查看初始配置的最大直接内存，配置的maxDirectMemory:3500.5 MB，大约16G内存的1/4。
        // 修改方法 -Xmx10m -Xms10m -XX:MaxDirectMemorySize=5m
        System.out.println("配置的maxDirectMemory:"+sun.misc.VM.maxDirectMemory()/(double)1024/1024+" MB");
        //修改成5M后分配6M，报错
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(6 * 1024 * 1024);
        System.out.println("byteBuffer: "+byteBuffer);
    }

    /***
     * 3.java.lang.OutOfMemoryError:GC overhead limit exceeded
     * JVM参数配置演示：（直接内存，比如常量池存储在元空间，使用的是直接内存）
     * -Xms10m -Xmx10m -XX:MaxDirectMemorySize=5m
     *
     * GC回收时间过长时会抛出OutOfMemoryError。过长的定义是，超过98%的时间用来做GC并且回收了不到2%的堆内存，连续多次GC
     * 都回收了不到2%的极端情况下才会抛出。
     * 假如不抛出GC overhead limit错误会发生什么情况呢？
     * 那就是GC清理的这么点内存很快就会被再次填满，这样就形成恶性循环，CPU使用率一直是100%，而GC却没有任何成果。
     * ***/
    private static void gcOverheadLimit() {
        int i =0;
        List<String> list = new ArrayList<>();
        try {
            while (true){//不能这样写代码，死循环了。。模拟报错
                //String#intern方法中看到，这个方法是一个 native 的方法，但注释写的非常明了。
                // “如果常量池中存在当前字符串, 就会直接返回当前字符串. 如果常量池中没有此字符串,
                // 会将此字符串放入常量池中后, 再返回”
                list.add(String.valueOf(++i).intern());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /****
     * Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
     * at com.guigu.bigfactory.jvm.OOMDemo.oomJavaHeapSpace(OOMDemo.java:42)
     * at com.guigu.bigfactory.jvm.OOMDemo.main(OOMDemo.java:35)
     * */
    private static void oomJavaHeapSpace() {
        //（1）大对象字节数组
        byte[] bytes = new byte[80 * 1024 * 1024];
        //（2）循环创建对象
        String str = "guigu.zhouyang";
        while (true) {
            str += str + new Random().nextInt(1111111111) + new Random().nextInt(222222222);
        }
    }

    private static void stackOverflowError() {
        /***
         * 1.java.lang.StackOverflowError,栈（调用方法执行）大小默认512k-1024k
         * Exception in thread "main" java.lang.StackOverflowError
         * at com.guigu.bigfactory.jvm.OOMDemo.stackOverflowError(OOMDemo.java:28)
         * **/
        stackOverflowError();
    }

}



