package com.guigu.bigfactory.jvm;

/**
 * @author:shaowangwu
 * @Date: 2022/3/6 7:04
 * Description:
 */
public class GcRootDemo {

    /***
     *如何判断对象是否可以被回收？
     *(1)引用计数法(了解即可，实际中不使用。没法解决对象循环依赖问题)
     *(2)枚举根节点做可达性分析(根搜索路径)
     *为了解决引用计数法的循环引用问题，Java使用了可达性分析的方法。
     * 所谓"GC roots"就是一组必须活跃的引用。
     * 基本思路就是通过一系列名为"GC Roots"对象作为起始点,从这个被称为GC Roots的对象开始向下搜索,如果一个对象到GC Roots
     * 没有任何引用链连接，则说明该对象不可用。
     * 也就是给定一个集合的引用作为根出发，通过引用关系遍历对象图，能被遍历到的(可到达的)对象就被判定为存活，没有被遍历
     * 到的就自然被判定为死亡。
     *
     * 可以做为GC Roots的对象：
     * 1.虚拟机栈(栈帧中的局部变量区，也叫做局部变量表)中引用的对象。
     * 2.方法区中类静态属性引用的对象。
     * 3.方法区中常量引用的对象。
     * 4.本地方法栈中JNI(Native方法)引用的对象。
     * */
    public static void main(String[] args) {

        System.out.println("2022-3-6" +"\t");
        try {
            m1();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
    *@Description
    *@Author shaowangwu
    *@Date 2022/3/6 11:30
    *@Param main方法调用m1方法，而m1()方法是在虚拟机栈中的，m1方法中引用了t1这个对象，符合第1条,对象可达。
    *@Return
    *@Exception
    */
    public static void m1() throws InterruptedException {
        GcRootDemo t1 = new GcRootDemo();
        System.gc();
        System.out.println("第一次GC完成.");
        /*while (true){

        }*/
        Thread.sleep(Integer.MAX_VALUE);
    }

    /***
     * JVM参数类型：
     * 1.标配参数 -version  -help  java -show version
     * 2.X参数(了解) -Xint 解释执行  -Xcomp 第一次使用就编译成本地代码   -Xmixed   混合模式
     * 3.XX参数
     * (1)Boolean类型
     * 公式：-XX:+或者-某个属性  （+表示开启，-表示关闭）  Case:是否打印GC收集细节、是否使用串行垃圾回收器
     * (2)KV设值类型，写法公式：-XX:属性key=属性值value
     * eg: -XX:MetaspaceSize=128m  -XX:MaxTenuringThreshold=15
     * terminal窗口如下：
     * jps -l
     * ....26237
     *
     * jinfo -flag MetaspaceSize 26237
     * --XX:MetaspaceSize=21807104
     * (3)jinfo举例 jinfo [option] <pid>，-flag <name>,如何查看当前运行程序的配置
     *
     * terminal控制台 查看有没开启打印GC回收，结果可知：-XX:-PrintGCDetails，没有开启：
     * F:\IdeaProjects\studyProject\bigFactoryInterviewFromZhouYang>jps -l
     * 27920 org.jetbrains.idea.maven.server.RemoteMavenServer36
     * 33328 org.jetbrains.jps.cmdline.Launcher
     * 33924 com.guigu.bigfactory.jvm.GcRootDemo
     * 31256 sun.tools.jps.Jps
     * 30076
     *
     * F:\IdeaProjects\studyProject\bigFactoryInterviewFromZhouYang>jinfo -flag PrintGCDetails 33924
     * -XX:-PrintGCDetails
     *
     * (4)题外话(坑题)：两个经典参数: -Xms和-Xmx,  -Xms1024 -Xmx1024m 属于上面1,2,3的哪一种？
     * 这个如何解释？（还是属于-XX,不属于-X参数类型）
     * -Xms  等价于  -XX:InitialHeapSize
     * -Xmx  等价于  -XX:MaxHeapSize
     * */


    /***
     * 查看JVM默认值，主要是查看初始默认
     * 公式：
     *java -XX:+PrintFlagsInitial -version
     *java -XX:+PrintFlagsInitial
     **
     * -XX:+PrintFlagsFinal  主要查看修改更新，公式： java -XX:+PrintFlagsFinal -version
     *打印出来的如果只是"="那就是没有修改过的初始值，如果是":="那就是修改过的。
     *
     * -XX:+PrintCommandLineFlags
     * **/


}
