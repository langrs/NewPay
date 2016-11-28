package com.unioncloud.newpay.data.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cwj on 16/11/9.
 */

public class StringUtils {

    public static boolean isChineseChar(String str){
        boolean temp = false;
        Pattern p    = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m    = p.matcher(str);
        if(m.find()){
            temp =  true;
        }
        return temp;
    }
}
