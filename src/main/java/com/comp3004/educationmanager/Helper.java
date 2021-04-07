package com.comp3004.educationmanager;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;
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
    public static HashMap<String, String> stringToMap(String s) {
        String nString = s.substring(1, s.length()-1); //remove the first and last curly brace
        String[] sets = nString.split(",", 4); //need to make this replace only the first and last curly braces

        HashMap<String, String> m = new HashMap<>();
        for (String set : sets) {
            String[] spl = set.replace("\"", "").split(":");
            m.put(spl[0], spl[1]);
        }
        return m;
    }
}
