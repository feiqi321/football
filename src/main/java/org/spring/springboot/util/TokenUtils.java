package org.spring.springboot.util;

import java.util.Random;

/**
 * Created by betty on 10/05/2020.
 */
public class TokenUtils {


    /**
     * 根据openId 生成token
     * @param openId
     */
    public static String convertToken(String openId){

        return openId+getRandomString(6);


    }

    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

}
