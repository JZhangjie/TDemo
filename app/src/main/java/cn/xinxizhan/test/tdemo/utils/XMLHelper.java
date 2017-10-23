package cn.xinxizhan.test.tdemo.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.QName;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import cn.xinxizhan.test.tdemo.data.base.KeyValueItem;

/**
 * Created by admin on 2017/9/18.
 */

public class XMLHelper {
    public static List<KeyValueItem> getCodesFromXML(File f){
        List<KeyValueItem> list =null;
        try {
            SAXReader reader = new SAXReader();
            Document doc = reader.read(f);
            Element root = doc.getRootElement();
            list = getKeyValueItemFromElement(root,-1,null);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static List<KeyValueItem> getKeyValueItemFromElement(Element element, int level, String pcode) {
        List<KeyValueItem> list = new ArrayList<>();
        String name = element.attributeValue(new QName("name"));
        String code = element.attributeValue(new QName("code"));
        if(name==null)
            name="未定义";
        if(code==null)
            code="0000";
        int childn = element.elements().size();
        if(childn>0)
        {
            list.add(new KeyValueItem(code,name,level,pcode,true));
            for(Object o :element.elements())
            {
                Element ele = (Element)o;
                list.addAll(getKeyValueItemFromElement(ele,level+1,code));
            }
        }
        else
        {
            list.add(new KeyValueItem(code,name,level,pcode,false));
        }
        return list;
    }
}
