package cn.bupt.device.controller;


import cn.bupt.device.data.MailData;
import cn.bupt.device.pluginmanager.Plugin;
import cn.bupt.device.sendEmailMethod.SendMail;
import cn.bupt.device.sendEmailMethod.Timer;
import com.codahale.metrics.Counter;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.Future;

@RestController("PluginController")
@RequestMapping("/api/plugin")
@Plugin(pluginInfo = "MailPlugin", registerAddr = "10.108.218.108:2181", detailInfo = "10.108.218.108:8300|use for sending Email")
@Slf4j
public class PluginController {

    @Autowired
    SendMail sendMail;

    @Resource
    private Counter pendingJobs;

    @Timer
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
}

