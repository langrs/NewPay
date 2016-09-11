package com.unioncloud.newpay.data.utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by cwj on 16/7/21.
 */
public class XmlUtils {

    public static String createXml(Object target) {
        //解决XStream对出现双下划线的bug
        XStream xStream = new XStream(
                new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        //将数据对象转换成XML格式数据
        xStream.alias("xml", target.getClass());
        return xStream.toXML(target);
    }


    public static Map<String,Object> getMapFromXML(String xmlString)
            throws ParserConfigurationException, IOException, SAXException {
        //这里用Dom的方式解析回包的最主要目的是防止API新增回包字段
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream is =  Util.getStringStream(xmlString);
        Document document = builder.parse(is);

        //获取到document里面的全部结点
        NodeList allNodes = document.getFirstChild().getChildNodes();
        Node node;
        Map<String, Object> map = new HashMap<String, Object>();
        for (int i = 0, length = allNodes.getLength(); i < length; i++) {
            node = allNodes.item(i);
            if(node instanceof Element){
                map.put(node.getNodeName(),node.getTextContent());
            }
        }
        return map;
    }

    public static <T> T parseFromXML(String xml, String rootElementName, Class<T> tClass) {
        //将从API返回的XML数据映射到Java对象
        XStream xStream = new XStream();
        xStream.alias(rootElementName, tClass);
        xStream.ignoreUnknownElements();//暂时忽略掉一些新增的字段
        return (T) xStream.fromXML(xml);
    }

}
