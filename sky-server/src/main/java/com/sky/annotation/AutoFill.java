package com.sky.annotation;

/*自定义注解，用于标识某个方法需要进行功能字段填充*/

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)//作用于方法上
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    //指定数据库类型
    //使用@Retention必须要为value指定成员变量的值，该成员变量为一个容器注解，这个容器注解包含多个OperationType
    OperationType value();
}
