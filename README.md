SSM整合要实现的功能：
显示mysql数据库中的用户列表信息
```
CREATE TABLE `persons` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `age` int(11) NOT NULL,
  `birth` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
```
![用户列表](http://img.blog.csdn.net/20170705092718466?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQveF9peWE=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

----------
由于使用Gradle构建SSM项目，我们先来编写build.gradle

```
group 'com.xiya'
version '1.0-SNAPSHOT'

apply plugin: 'idea'
apply plugin: 'java'
apply plugin: 'war'
apply plugin: "org.akhikhl.gretty"

sourceCompatibility = 1.8

buildscript {
    repositories {
        maven {
			url 'http://maven.aliyun.com/nexus/content/groups/public/'
		}
    }
    dependencies {
        classpath 'org.akhikhl.gretty:gretty:+'
    }
}

repositories {
    maven {
        url 'http://maven.aliyun.com/nexus/content/groups/public/'
    }
}

dependencies {
    testCompile group: 'junit', name: 'junit', version: '4.12'
    compile group: 'mysql', name: 'mysql-connector-java', version: '5.1.42'
    compile group: 'org.springframework', name: 'spring-context', version: '4.3.9.RELEASE'
    compile group: 'org.springframework', name: 'spring-jdbc', version: '4.3.9.RELEASE'
    compile group: 'org.springframework', name: 'spring-webmvc', version: '4.3.9.RELEASE'
    compile group: 'org.mybatis', name: 'mybatis', version: '3.4.4'
    compile group: 'org.mybatis', name: 'mybatis-spring', version: '1.3.1'
    compile group: 'org.apache.commons', name: 'commons-dbcp2', version: '2.1.1'
    compile group: 'log4j', name: 'log4j', version: '1.2.17'
    providedCompile group: 'javax.servlet.jsp.jstl', name: 'jstl', version: '1.2'
}

tasks.withType(JavaCompile) {
    options.encoding = "UTF-8"
}
```
-------------------------------------

创建目录结构：
![目录结构](http://img.blog.csdn.net/20170705095253387?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQveF9peWE=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

由于我们只是简单的搭建SSM框架，我们就不写service层了。

--------------------------------------
使用MyBatis Plugin生成Dao、Model、Mapper相关文件。

```
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE generatorConfiguration PUBLIC
        "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd" >
<generatorConfiguration>

    <!-- !!!! Driver Class Path !!!! -->
    <classPathEntry location="D:\Java\gradle\home\caches\modules-2\files-2.1\mysql\mysql-connector-java\5.1.42\80a448a3ec2178b649bb2e3cb3610fab06e11669\mysql-connector-java-5.1.42.jar"/>

    <context id="context" targetRuntime="MyBatis3">
        <commentGenerator>
            <property name="suppressAllComments" value="true"/>
            <property name="suppressDate" value="true"/>
        </commentGenerator>

        <!-- !!!! Database Configurations !!!! -->
        <jdbcConnection driverClass="com.mysql.jdbc.Driver" connectionURL="jdbc:mysql://localhost:3306/test?useUnicode=true&amp;characterEncoding=UTF-8&amp;useSSL=true"
                        userId="root" password="lgh123"/>

        <javaTypeResolver>
            <property name="forceBigDecimals" value="false"/>
        </javaTypeResolver>

        <!-- !!!! Model Configurations !!!! -->
        <javaModelGenerator targetPackage="cn.bjut.entity" targetProject="src/main/java">
            <property name="enableSubPackages" value="true"/>
            <property name="trimStrings" value="true"/>
        </javaModelGenerator>

        <!-- !!!! Mapper XML Configurations !!!! -->
        <sqlMapGenerator targetPackage="cn.bjut.mapper" targetProject="src/main/resources">
            <property name="enableSubPackages" value="true"/>
        </sqlMapGenerator>

        <!-- !!!! Mapper Interface Configurations !!!! -->
        <javaClientGenerator targetPackage="cn.bjut.mapper" targetProject="src/main/java" type="XMLMAPPER">
            <property name="enableSubPackages" value="true"/>
        </javaClientGenerator>

        <!-- !!!! Table Configurations !!!! -->
        <table tableName="persons" domainObjectName="Person" enableCountByExample="false" enableDeleteByExample="false" enableSelectByExample="false"
               enableUpdateByExample="false"/>
    </context>
</generatorConfiguration>
```
所生成文件：
```
package cn.bjut.entity;

import java.time.ZoneId;
import java.util.Date;

public class Person {
    private Integer id;

    private String name;

    private Integer age;

    private Date birth;

    public Integer getId() {
//        System.out.println("getId");
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", birth=" + birth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() +
                '}';
    }
}
```

PersonMapper.java

```
package cn.bjut.mapper;

import cn.bjut.entity.Person;

import java.util.List;

public interface PersonMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Person record);

    int insertSelective(Person record);

    Person selectByPrimaryKey(Integer id);

    List<Person> selectAllPersons();

    int updateByPrimaryKeySelective(Person record);

    int updateByPrimaryKey(Person record);
}
```

PersonMapper.xml

```
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="cn.bjut.mapper.PersonMapper" >
  <resultMap id="BaseResultMap" type="cn.bjut.entity.Person" >
    <id column="id" property="id" jdbcType="INTEGER" />
    <result column="name" property="name" jdbcType="VARCHAR" />
    <result column="age" property="age" jdbcType="INTEGER" />
    <result column="birth" property="birth" jdbcType="DATE" />
  </resultMap>
  <sql id="Base_Column_List" >
    id, name, age, birth
  </sql>
  <select id="selectByPrimaryKey" resultMap="BaseResultMap" parameterType="java.lang.Integer" >
    select 
    <include refid="Base_Column_List" />
    from persons
    where id = #{id,jdbcType=INTEGER}
  </select>
    <select id="selectAllPersons" resultType="cn.bjut.entity.Person">
      select * from persons;
    </select>
    <delete id="deleteByPrimaryKey" parameterType="java.lang.Integer" >
    delete from persons
    where id = #{id,jdbcType=INTEGER}
  </delete>
  <insert id="insert" parameterType="cn.bjut.entity.Person" >
    insert into persons (id, name, age, 
      birth)
    values (#{id,jdbcType=INTEGER}, #{name,jdbcType=VARCHAR}, #{age,jdbcType=INTEGER}, 
      #{birth,jdbcType=DATE})
  </insert>
  <insert id="insertSelective" parameterType="cn.bjut.entity.Person" >
    insert into persons
    <trim prefix="(" suffix=")" suffixOverrides="," >
      <if test="id != null" >
        id,
      </if>
      <if test="name != null" >
        name,
      </if>
      <if test="age != null" >
        age,
      </if>
      <if test="birth != null" >
        birth,
      </if>
    </trim>
    <trim prefix="values (" suffix=")" suffixOverrides="," >
      <if test="id != null" >
        #{id,jdbcType=INTEGER},
      </if>
      <if test="name != null" >
        #{name,jdbcType=VARCHAR},
      </if>
      <if test="age != null" >
        #{age,jdbcType=INTEGER},
      </if>
      <if test="birth != null" >
        #{birth,jdbcType=DATE},
      </if>
    </trim>
  </insert>
  <update id="updateByPrimaryKeySelective" parameterType="cn.bjut.entity.Person" >
    update persons
    <set >
      <if test="name != null" >
        name = #{name,jdbcType=VARCHAR},
      </if>
      <if test="age != null" >
        age = #{age,jdbcType=INTEGER},
      </if>
      <if test="birth != null" >
        birth = #{birth,jdbcType=DATE},
      </if>
    </set>
    where id = #{id,jdbcType=INTEGER}
  </update>
  <update id="updateByPrimaryKey" parameterType="cn.bjut.entity.Person" >
    update persons
    set name = #{name,jdbcType=VARCHAR},
      age = #{age,jdbcType=INTEGER},
      birth = #{birth,jdbcType=DATE}
    where id = #{id,jdbcType=INTEGER}
  </update>
</mapper>
```

以上参考：[http://blog.csdn.net/x_iya/article/details/73087378](http://blog.csdn.net/x_iya/article/details/73087378)

------------------------------------------

日志配置文件log4j.properties

```
log4j.rootLogger=DEBUG, Console
#Console
log4j.appender.Console=org.apache.log4j.ConsoleAppender
log4j.appender.Console.layout=org.apache.log4j.PatternLayout
log4j.appender.Console.layout.ConversionPattern=%d [%t] %-5p [%c] - %m%n

log4j.logger.java.sql.ResultSet=INFO
log4j.logger.org.apache=INFO
log4j.logger.java.sql.Connection=DEBUG
log4j.logger.java.sql.Statement=DEBUG
log4j.logger.java.sql.PreparedStatement=DEBUG
```

数据库配置文件jdbc.properties

```
jdbc.driverClassName=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8&useSSL=true
jdbc.username=root
jdbc.password=lgh123
```

Spring配置文件applicationContext.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd">

    <!-- 加载数据库配置文件 -->
    <context:property-placeholder location="classpath:jdbc.properties"/>

    <!-- 配置数据源 -->
    <bean id="dataSource" class="org.apache.commons.dbcp2.BasicDataSource">
        <property name="driverClassName" value="${jdbc.driverClassName}"/>
        <property name="url" value="${jdbc.url}"/>
        <property name="username" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>
    </bean>

    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="dataSource"/>
        <property name="typeAliasesPackage" value="cn.bjut.entity"/>
        <property name="configLocation" value="classpath:mybatis-config.xml"/>
    </bean>

    <bean id="mapperScannerConfigurer" class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="basePackage" value="cn.bjut.mapper"/>
        <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/>
    </bean>
</beans>
```

MyBatis配置文件 mybatis-config.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
</configuration>
```

测试：

```
import cn.bjut.entity.Person;
import cn.bjut.mapper.PersonMapper;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * Created by N3verL4nd on 2017/7/4.
 */
public class T {
    private static ApplicationContext ctx;

    static
    {
        ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    }

    @Test
    public void test() {
        PersonMapper personMapper = ctx.getBean(PersonMapper.class);
        List<Person> personList = personMapper.selectAllPersons();
        personList.forEach(System.out::println);
    }
}
```
![SpringMyBtis](http://img.blog.csdn.net/20170705095715937?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQveF9peWE=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

以上就完成了Spring与MyBatis的整合。

-------------------------------

整合SpringMVC

编写SpringMVC配置文件springmvc.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd">

    <context:component-scan base-package="cn.bjut.controller"/>

    <bean id="viewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="prefix" value="/WEB-INF/views/"/>
        <property name="suffix" value=".jsp"/>
    </bean>
</beans>
```

编写web.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
         version="3.1">

    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:applicationContext.xml</param-value>
    </context-param>

    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>

    <servlet>
        <servlet-name>SSM</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:springmvc.xml</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>
    
    <servlet-mapping>
        <servlet-name>SSM</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
</web-app>
```

编写Controller

```
package cn.bjut.controller;

import cn.bjut.mapper.PersonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by N3verL4nd on 2017/7/4.
 */
@Controller
public class TestController {
    private PersonMapper personMapper;

    @Autowired
    public void setPersonMapper(PersonMapper personMapper) {
        this.personMapper = personMapper;
    }

    @RequestMapping("/list")
    public String listPerson(Model model) {
        model.addAttribute("persons", personMapper.selectAllPersons());
        return "list";
    }
}
```
在/WEB-INF/views/目录下编写list.jsp测试文件

```
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%--
  Created by IntelliJ IDEA.
  User: N3verL4nd
  Date: 2017/7/5
  Time: 8:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html>
<head>
    <title>用户列表</title>
</head>
<body>
    <table border="1">
        <tr>
            <th>id</th>
            <th>name</th>
            <th>age</th>
            <th>birth</th>
        </tr>
        <c:forEach items="${requestScope.persons}" var="person">
            <tr>
                <td>${person.id}</td>
                <td>${person.name}</td>
                <td>${person.age}</td>
                <td>${person.birth}</td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>

```
测试：
项目目录下执行gradle appRun
![测试](http://img.blog.csdn.net/20170705131254431?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQveF9peWE=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

![SSM](http://img.blog.csdn.net/20170705100201435?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQveF9peWE=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

至此就简单的完成了SSM框架的整合。

Demo:
[https://github.com/N3verL4nd/SSM.git](https://github.com/N3verL4nd/SSM.git)



