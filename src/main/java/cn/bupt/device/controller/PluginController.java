package cn.bupt.device.controller;


import cn.bupt.device.data.MailData;
import cn.bupt.device.pluginmanager.Plugin;
import cn.bupt.device.sendEmailMethod.SendMail;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plugin")
@Plugin(pluginInfo = "mailplugin", registerAddr = "10.108.218.108:2181")
@Slf4j
public class PluginController {

    @RequestMapping(value = "/sendMail", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public void sendMail(@RequestBody String jsonStr) throws Exception {
        JsonObject jsonObj = (JsonObject)new JsonParser().parse(jsonStr);
        MailData mailData = new MailData(jsonObj);

        List<String> toList=mailData.getTo();
        String[] to = new String[toList.size()];
        toList.toArray(to);

        SendMail sendMail=new SendMail();
        String s=sendMail.sendEmail(to,mailData.getSubject(),mailData.getText());
    }
}

