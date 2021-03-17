package com.comp3004.educationmanager;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;

public class Helper {
    public String objectToJSONString(Object o){
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(JsonMethod.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonString = "";

        try {
            jsonString = mapper.writeValueAsString(o);
        } catch (Exception e){
            System.out.println(e);
        }

        return jsonString;
    }
}
