package cn.bupt.device.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class MailData implements Serializable{
    private final JsonObject jsonObj ;
    private final String subject ;
    private final String text ;
    private final List<String> to=new ArrayList<>();

    public MailData(JsonObject jsonObj) {
        this.jsonObj = jsonObj ;
        this.subject = jsonObj.get("subject").getAsString() ;
        this.text = jsonObj.get("text").getAsString() ;
        JsonArray array = jsonObj.get("to").getAsJsonArray();
        for(JsonElement element:array)
        {
            this.to.add(element.getAsString());
        }
    }
}
