package com.guigu.bigfactory.spring;

/**
* @author:shaowangwu
* @Date: 2022/3/8 18:02
* Description:
*/
//IOC过程，第一步：xml配置文件，配置创建对象
//<bean id="user" class="com.guigu.bigfactory.spring.User"></bean>
    class UserFactory{
        public static  UserDao getDao() throws ClassNotFoundException,
                InstantiationException, IllegalAccessException {
            //1.xml解析,比如Log4j
            String classValue="class属性";
            //2.通过反射创建对象。降低了类之间耦合度，比如修改类的时候只要改配置文件，工厂代码获得对象不用改
            Class clazz = Class.forName(classValue);
            return (UserDao)clazz.newInstance();
        }
}


public class SpringIocDemo {


    /***
     * 1.IOC底层原理：
     * xml解析、工厂模式、反射
     *
     * IOC内容：
     * (1)IOC底层原理
     * (2)IOC接口(BeanFactory)
     * (3)IOC操作Bean管理(基于xml)
     * (4)IOC操作Bean管理(基于注解)
     *
     * 2.Spring提供了IOC容器实现两种方式(两个接口)
     * 2.1 BeanFactory:IOC容器最基本实现，是spring里面内部使用的接口，不推荐开发人员使用。如果使用的是BeanFactory.
     * 则使用的是懒加载，只加载配置文件，不创建对象，只有你获取对象getBean时候才创建。
     *
     * 2.2 ApplicationContext：是BeanFactory接口的子接口，提供了更多更强大的功能。一般是面向开发人员使用。
     *      实现类：FileSystemXmlApplicationContext(从磁盘加载配置文件，比如D盘下某某路径)、
     *      ClassPathXmlApplicationContext(从项目src目录开始加载)
     * */


}
