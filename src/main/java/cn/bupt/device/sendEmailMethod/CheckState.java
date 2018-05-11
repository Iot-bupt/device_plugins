package cn.bupt.device.sendEmailMethod;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CheckState {
    @Autowired
    SendMail sendMail;

    @Pointcut("@annotation(cn.bupt.device.sendEmailMethod.Timer)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object before(ProceedingJoinPoint point) throws Throwable {

        if(sendMail.getState().equals("ACTIVE")){
            return point.proceed();
        }
        else{
            return new AsyncResult<String>("邮件插件暂停中");
        }

    }
}
