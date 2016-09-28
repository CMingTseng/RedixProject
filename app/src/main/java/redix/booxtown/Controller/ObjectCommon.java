package redix.booxtown.controller;

import java.lang.reflect.Field;
import java.util.Hashtable;

import redix.booxtown.model.User;

/**
 * Created by Administrator on 11/09/2016.
 */
public class ObjectCommon {
    public ObjectCommon(){

    }

    public static Hashtable ObjectDymanic(Object obj){
        Hashtable user= new Hashtable();
        Object someObject = obj;
        for (Field field : someObject.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value;
            try {
                value = field.get(someObject);
                if (value != null) {
                    user.put(field.getName(), value);
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        return  user;
    }
}
