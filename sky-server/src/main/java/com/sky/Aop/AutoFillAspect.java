package com.sky.Aop;

/*自定义切面，实现公共功能（公共字段自动填充）*/

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;


/**
 * @Aspect 注解用来描述一个切面类，定义切面类的时候需要打上这个注解。
 * @Component 注解将该类交给 Spring 来管理
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    /**
     * 切入点
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")//一个锁定包 一个锁定那个注解
    public void autoFillPointCut(){
    }

    //前置通知
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint){
        log.info("公共字段自动填充");

        //获取到当前被拦截的方法上的数据库操作类型
            //通过反射获取接口方法
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();//方法签名对象
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);//获取方法上的注解对象
        OperationType operationType = autoFill.value();//获取数据库操作类型

        //获取到当前被拦截的方法的参数---实体对象
        Object[] args = joinPoint.getArgs();
        if (args==null||args.length==0){
            return;
        }

        Object entity = args[0];//0获取到数组第一位，然后因为有的是菜品有的是员工的实体类，所以为了防止混乱，就用Object来接收



        //准备赋值的数据

        LocalDateTime now = LocalDateTime.now();

        Long currentId = BaseContext.getCurrentId();

        //根据当前不同操作类型，为对应的属性通过反射来赋值
        if (operationType==OperationType.INSERT){
            //为4个公共字段赋值
            try {
                //getDeclaredMethod 方法接受一个或多个参数，用于指定要获取的方法的名称和参数类型(全部方法)
                //getMethod （非所有方法）
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                //通过反射为对象属性赋值
                setCreateTime.invoke(entity, now);
                setUpdateTime.invoke(entity,now);
                setCreateUser.invoke(entity,currentId);
                setUpdateUser.invoke(entity,currentId);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if (operationType==OperationType.UPDATE){
            //为2个公共字段赋值
            try {
                //getDeclaredMethod 方法接受一个或多个参数，用于指定要获取的方法的名称和参数类型(全部方法)
                //getMethod （非所有方法）

                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                //通过反射为对象属性赋值

                setUpdateTime.invoke(entity,now);

                setUpdateUser.invoke(entity,currentId);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }















}
