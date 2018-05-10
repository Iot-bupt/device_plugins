package cn.bupt.device.controller;


import cn.bupt.device.data.MailData;
import cn.bupt.device.pluginmanager.Plugin;
import cn.bupt.device.sendEmailMethod.SendMail;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plugin")
@Plugin(pluginInfo = "MailPlugin", registerAddr = "10.108.218.108:2181", detailInfo = "hahahaha|dfadsfasdfasdf|")
@Slf4j
public class PluginController {

    @Autowired
    SendMail sendMail;

    @RequestMapping(value = "/sendMail", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String sendMail(@RequestBody String jsonStr) throws Exception {
        JsonObject jsonObj = (JsonObject)new JsonParser().parse(jsonStr);
        MailData mailData = new MailData(jsonObj);

        List<String> toList=mailData.getTo();
        String[] to = new String[toList.size()];
        toList.toArray(to);

        String s=sendMail.sendEmail(to,mailData.getSubject(),mailData.getText());
        return "发送成功";
    }

    @RequestMapping(value = "/active", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String setActive(){
        sendMail.setState("ACTIVE");
        return "Plugin active";
    }

    @RequestMapping(value = "/suspend", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String setSuspend(){
        sendMail.setState("SUSPEND");
        return "Plugin suspended";
    }
}

