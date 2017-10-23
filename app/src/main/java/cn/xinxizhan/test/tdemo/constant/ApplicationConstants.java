package cn.xinxizhan.test.tdemo.constant;

import java.util.ArrayList;
import java.util.List;

import cn.xinxizhan.test.tdemo.data.base.KeyValueItem;
import cn.xinxizhan.test.tdemo.data.model.User;
import cn.xinxizhan.test.tdemo.data.model.UserRepository;

/**
 * Created by admin on 2017/10/9.
 */

public class ApplicationConstants {
    public static final String ROOTPATH="GeoStarPatrol";
    public static final String DBPATH="db";
    public static final String IMAGEPATH="image";
    public static final String XMLPATH = "dlbm";
    public static final String LOGINURL="http://host.xinxizhan.cn:18158/api/tokens/";
    public static final String HOSTURL = "http://host.xinxizhan.cn:18158";
    public static String DBURL = "http://host.xinxizhan.cn:18158/api/files/7/";
    public static final String ZXSLURL="http://t0.tianditu.com/vec_c/wmts";
    public static final String ZXYXURL="http://t0.tianditu.com/img_c/wmts";

    public static final String FILELIST= "文件列表";
    public static final String CASEDETAIL= "图斑详情";
    public static final String CASELIST= "图斑列表";

    public static User USER;
    public static UserRepository HISTORYUSERS;

    public static List<KeyValueItem> CCBMList=new ArrayList<KeyValueItem>(){
        {
            add(new KeyValueItem("0000","请选择"));
            add(new KeyValueItem("0100","耕地",true));
            add(new KeyValueItem("0110","水田",1,"0100"));
            add(new KeyValueItem("0200","果园",true));
            add(new KeyValueItem("0211","乔灌果园",1,"0200"));
            add(new KeyValueItem("0212","藤本果园",1,"0200"));
            add(new KeyValueItem("0213","草本果园",1,"0200"));
            add(new KeyValueItem("0250","苗圃"));
            add(new KeyValueItem("0311","阔叶林"));
            add(new KeyValueItem("0312","针叶林"));
            add(new KeyValueItem("0321","阔叶灌木林"));
            add(new KeyValueItem("0340","竹林"));
            add(new KeyValueItem("0360","绿化林地"));
            add(new KeyValueItem("0411","高覆盖度草地"));
            add(new KeyValueItem("1001","水域"));
        }
    };
    public static  List<KeyValueItem> GDBMList=new ArrayList<KeyValueItem>(){
        {
            add(new KeyValueItem("0000","请选择"));
            add(new KeyValueItem("0100","耕地"));
            add(new KeyValueItem("0110","水田"));
            add(new KeyValueItem("0120","旱地"));
        }
    };
    public static  List<KeyValueItem> FGDBMList=new ArrayList<KeyValueItem>(){
        {
            add(new KeyValueItem("0000","请选择"));
            add(new KeyValueItem("0110","水田"));
            add(new KeyValueItem("0211","乔灌果园"));
            add(new KeyValueItem("0212","藤本果园"));
            add(new KeyValueItem("0213","草本果园"));
            add(new KeyValueItem("0250","苗圃"));
            add(new KeyValueItem("0311","阔叶林"));
            add(new KeyValueItem("0312","针叶林"));
            add(new KeyValueItem("0321","阔叶灌木林"));
            add(new KeyValueItem("0340","竹林"));
            add(new KeyValueItem("0360","绿化林地"));
            add(new KeyValueItem("0411","高覆盖度草地"));
            add(new KeyValueItem("1001","水域"));
        }
    };
}
