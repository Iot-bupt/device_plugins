package cn.bupt.device.common;

import com.google.gson.JsonObject;

import java.util.Map;

/**
 * Created by tangjialiang on 2018/5/24.
 */
public class JsonUtils {
    private JsonUtils() {}

    public static JsonObject map2json(Map<String, String> map) {
        JsonObject jsonObject = new JsonObject();
        for(Object entry : map.entrySet()) {
            Map.Entry kv = (Map.Entry)entry;
//            jsonObject.addProperty(kv.getKey(), kv.getValue());
        }

        return jsonObject ;
    }
}
