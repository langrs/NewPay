package com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass;

import com.unioncloud.newpay.data.utils.XMLParser;
import com.unioncloud.newpay.domain.utils.MD5Utils;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by cwj on 16/7/21.
 */
public class Signature {

    public static String getSign(Object obj, String key) {
        ArrayList<String> list = new ArrayList<String>();
        Class cls = obj.getClass();
        Field[] fields = cls.getDeclaredFields();
        try {
            for (Field f : fields) {
                f.setAccessible(true);
                if (f.get(obj) != null && f.get(obj) != "") {
                    list.add(f.getName() + "=" + f.get(obj) + "&");
                }
            }
        } catch (IllegalAccessException e) {
            return null;
        }
        int size = list.size();
        String [] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i ++) {
            sb.append(arrayToSort[i]);
        }
        sb.append("key=").append(key);
        String result = sb.toString();
        result = MD5Utils.encode(result).toUpperCase();
        return result;
    }

    public static String getSign(Map<String,Object> map, String key){
        ArrayList<String> list = new ArrayList<String>();
        for(Map.Entry<String,Object> entry : map.entrySet()){
            String entryKey = entry.getKey();
            Object entryValue = entry.getValue();
            if(entryValue != null && !"".equals(entryValue) && !entryKey.equals("sign")){
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        int size = list.size();
        String [] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i ++) {
            sb.append(arrayToSort[i]);
        }
        sb.append("key=").append(key);
        String result = sb.toString();  // unsigned
        result = MD5Utils.encode(result).toUpperCase();
        return result;
    }

    /**
     * 检验API返回的数据里面的签名是否合法，避免数据在传输的过程中被第三方篡改
     * @param xml API返回的XML数据字符串
     * @return API签名是否合法
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static boolean checkXmlSign(String xml, String key)
            throws ParserConfigurationException, IOException, SAXException {
        Map<String,Object> map = XMLParser.getMapFromXML(xml);

        String signInXml = map.get("sign").toString();
        if(signInXml == null || "".equals(signInXml)){
            // API返回的数据签名数据不存在，有可能被第三方篡改!!!
            return false;
        }
        //清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名
        map.put("sign","");
        //将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
        String localSign = getSign(map, key);

        if(!localSign.equals(signInXml)){
            // API返回的数据签名验证不通过，有可能被第三方篡改!!!
            return false;
        }
        return true;
    }
}
