package cn.bupt.device.controller;


import cn.bupt.device.sendEmailMethod.SendMail;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/plugin")
public class PluginController {
    @RequestMapping(value = "/sendMail", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public void sendMail(@RequestParam("to[]") String[] to, @RequestParam("subject") String subject, @RequestParam("text") String text) throws Exception {
        SendMail sendMail=new SendMail();
        String s=sendMail.sendEmail(to,subject,text);
    }
}

