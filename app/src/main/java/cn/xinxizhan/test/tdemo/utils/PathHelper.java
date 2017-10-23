package cn.xinxizhan.test.tdemo.utils;

import java.io.File;

import cn.xinxizhan.test.tdemo.constant.ApplicationConstants;

/**
 * Created by admin on 2017/10/16.
 */

public class PathHelper {

    public static String getDBFilesUrl(){
        return ApplicationConstants.DBURL+"/"+ApplicationConstants.USER.getXzqhdm()+"/*";
    }

    public static File getDBFilesPath(){
        return FileHelper.getPath(ApplicationConstants.DBPATH+"/"+ApplicationConstants.USER.getXzqhdm());
    }

    public static File getImagePath(){
        return FileHelper.getPath(ApplicationConstants.IMAGEPATH+"/"+ApplicationConstants.USER.getXzqhdm());
    }
}
