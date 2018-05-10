package cn.bupt.device.pluginmanager;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by tangjialiang on 2018/5/4.
 */
@Target({ElementType.TYPE}) // 指明该注解适用的范围
@Retention(RetentionPolicy.RUNTIME) // 什么时候使用该注解
@Component // for spring scanning.
public @interface Plugin {

    String pluginInfo() ; // 在使用这个注解的时候，不用显示的给value赋值
    String registerAddr() ; // 向这个地址注册;
}
