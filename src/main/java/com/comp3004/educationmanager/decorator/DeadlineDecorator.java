package com.comp3004.educationmanager.decorator;

import com.comp3004.educationmanager.Helper;
import com.comp3004.educationmanager.composite.Component;

import java.util.Calendar;

public class DeadlineDecorator extends Decorator {
    Calendar deadline = Calendar.getInstance();
    String dateString;

    public DeadlineDecorator(Component c) {
        super(c);
    }

    @Override
    public boolean setProperty(String property, Object value){
        if(property.equals("deadline")) {
            dateString = (String) value;
            String[] date = dateString.split("-");
            deadline.set(Calendar.YEAR, Integer.parseInt(date[0]));
            deadline.set(Calendar.MONTH, Integer.parseInt(date[1]) -1);
            deadline.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[2]));
            deadline.set(Calendar.HOUR_OF_DAY, Integer.parseInt(date[3]));
            deadline.set(Calendar.MINUTE, Integer.parseInt(date[4]));
            return true;
        } else {
            return wrappee.setProperty(property, value);
        }
    }

    @Override
    public Object getProperty(String property) {
        if(property.equals("deadline")) {
            return deadline;
        } else if(property.equals("dateString")) {
            return dateString;
        } else {
            return wrappee.getProperty(property);
        }
    }

    @Override
    public Object executeCommand(String command, Object value) {
        if(command.equals("isBeforeDeadline")) {
            return deadline.compareTo((Calendar) value) > 0;
        } else if(command.equals("findByPath")) {
            Component c = (Component) wrappee.executeCommand(command, value);
            if(c != null) {
                if(((String) c.getProperty("fullPath")).equals((String) getProperty("fullPath"))) {
                    return this;
                } else if(((String) c.getProperty("fullPath")).equals((String) value)) {
                    return c;
                }
            }
            return null;
        }  else if(command.equals("stringify")) {
            return Helper.objectToJSONString(this);
        } else {
            return wrappee.executeCommand(command, value);
        }
    }
}
