package com.comp3004.educationmanager;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
public class Helper {
    public static String objectToJSONString(Object o){
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(JsonMethod.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonString = "";

        try {
            jsonString = mapper.writeValueAsString(o);
        } catch (Exception e){
            e.printStackTrace(System.out);
        }

        return jsonString;
    }

    public static HashMap<String, Object> stringToMap(String s) {
        HashMap<String, Object> m = null;
        try {
             m = new ObjectMapper().readValue(s, HashMap.class);
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
        return m;
    }
}
