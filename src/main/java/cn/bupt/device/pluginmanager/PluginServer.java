package cn.bupt.device.pluginmanager;

import cn.bupt.device.controller.PluginController;
import cn.bupt.device.sendEmailMethod.SendMail;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by tangjialiang on 2018/5/4.
 */
@Component
public class PluginServer implements ApplicationContextAware, InitializingBean {

    @Autowired
    SendMail sendMail;

    private String pluginInfo ;
    private String detailInfo ;

    private PluginRegistry pluginRegistry ;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (pluginRegistry != null) {
            pluginRegistry.register(pluginInfo,detailInfo); // 注册服务地址
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 得到所有RpcService注解的SpringBean
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(Plugin.class);
        if (MapUtils.isNotEmpty(beansWithAnnotation)) {
            for (Object serviceBean :
                    beansWithAnnotation.values()) {
                Class<?> aClass = null ;
                if (serviceBean.getClass().equals(PluginController.class)) {
                    aClass = serviceBean.getClass() ;
                } else if (serviceBean.getClass().getSuperclass().equals(PluginController.class)) {
                    aClass = serviceBean.getClass().getSuperclass() ;
                }

                String pluginInfo = aClass.getAnnotation(Plugin.class).pluginInfo();
                String registerAddr = aClass.getAnnotation(Plugin.class).registerAddr() ;
                String detailInfo = aClass.getAnnotation(Plugin.class).detailInfo() ;

                this.pluginInfo = pluginInfo ;
                this.detailInfo = detailInfo ;

                pluginRegistry = new PluginRegistry(registerAddr) ;
            }
        }
    }
}
