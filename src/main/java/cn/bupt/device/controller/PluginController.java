package cn.bupt.device.controller;


import cn.bupt.device.common.JsonUtils;
import cn.bupt.device.common.ZKConstant;
import cn.bupt.device.data.MailData;
import cn.bupt.device.pluginmanager.Plugin;
import cn.bupt.device.sendEmailMethod.ConfirmActive;
import cn.bupt.device.sendEmailMethod.SendMail;
import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;
import java.util.concurrent.Future;

@RestController("PluginController")
@RequestMapping("/api/plugin")
@Plugin(pluginInfo = "MailPlugin", registerAddr = ZKConstant.ZK_ADDRESS, detailInfo = "use for sending Email")
@Slf4j
public class PluginController {
    private final String controllerName = PluginController.class.getName() ;

    @Autowired
    private SendMail sendMail;

    @Autowired
    WebApplicationContext applicationContext;

    private MetricRegistry metrics ;
    private Counter pendingJobs ;

    @Autowired
    public void setMetrics(MetricRegistry metrics) {
        this.metrics = metrics ;
        this.pendingJobs = this.metrics.counter(controllerName) ;
    }

    @ConfirmActive
    @ApiOperation(value = "send a mail", notes = "send a mail api")
    @ApiImplicitParam(name = "jsonStr", value = "{\n" +
            "\t\"to\": [\"liyou@bupt.edu.cn\"],\n" +
            "\t\"subject\": \"传感器运行情况报告\",\n" +
            "\t\"text\": \"运行情况良好，祝你一路顺风\"\n" +
            "}", required = true)
    @RequestMapping(value = "/sendMail", method = RequestMethod.POST)
    @ResponseBody
    public Future<String> sendMail(@RequestBody String jsonStr) throws Exception {
        JsonObject jsonObj = (JsonObject)new JsonParser().parse(jsonStr);
        MailData mailData = new MailData(jsonObj);

        List<String> toList=mailData.getTo();
        String[] to = new String[toList.size()];
        toList.toArray(to);

        pendingJobs.inc();

        String s=sendMail.sendEmail(to,mailData.getSubject(),mailData.getText());
        return new AsyncResult<String>("发送成功");
    }

    @RequestMapping(value = "/active", method = RequestMethod.POST)
    @ResponseBody
    public String setActive(){
        sendMail.setState("ACTIVE");
        return "Plugin active";
    }

    @RequestMapping(value = "/suspend", method = RequestMethod.POST)
    @ResponseBody
    public String setSuspend(){
        sendMail.setState("SUSPEND");
        return "Plugin suspended";
    }

    @RequestMapping(value = "/state", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getState(){
        return sendMail.getState();
    }

    @RequestMapping(value = "/metrics", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getMetrics() {
        HashMap<String, Long> result = new HashMap<>() ;
        Map<String, Counter> counters = metrics.getCounters(
                (name, metrics) -> (name.equals(controllerName))?(true):(false)
        );
        counters.forEach(
                (k, v) -> {
                    result.put(k, v.getCount()) ;
                }
        );

        return JsonUtils.map2json(result).toString() ;
    }

    @RequestMapping(value = "/allUrls", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public List<String> getAllUrls(){
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        //获取url与类和方法的对应信息
        Map<RequestMappingInfo, org.springframework.web.method.HandlerMethod> map = mapping.getHandlerMethods();
        List<String> urlList = new ArrayList<>();
        for (RequestMappingInfo info : map.keySet()){
            //获取url的Set集合，一个方法可能对应多个url
            Set<String> patterns = info.getPatternsCondition().getPatterns();
            for (String url : patterns){
                if (url.startsWith("/api"))
                urlList.add(url);
            }
        }
        return urlList;
    }
}

