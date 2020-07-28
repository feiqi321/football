package org.spring.springboot.util;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by betty on 10/05/2020.
 */
public class LocalCache {

    public static ConcurrentHashMap currentHashMap = new ConcurrentHashMap();


    public static void setValue(String key,String value){

        currentHashMap.put(key,value);

    }

    public static void setObject(String key,Object value){

        currentHashMap.put(key,value);

    }

    public static String getValue(String key){
        Object obj = currentHashMap.get(key);
        if (obj==null){
            return null;
        }else{
            return (String)obj;
        }
    }

    public static Object getObject(String key){
        Object obj = currentHashMap.get(key);
        return obj;
    }

}
