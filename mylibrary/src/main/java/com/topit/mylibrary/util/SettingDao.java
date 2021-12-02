package com.topit.mylibrary.util;


/**
 * Created by Iverson on 2018/12/14 1:42 PM
 * 此类用于：
 */
public class SettingDao {

    /*********************用户设置*******************/
    public static final String USER_SETTING = "user_setting";
    public static final String CHANGE_GOODS_LIST= "selected_app_package";
    public static final String USER_SETTING_LAST_MD5= "user_setting_last_md5";
    public static final String USER_SETTING_FIRST_INSTALL= "user_setting_first_install";
    public static final String LAST_VERSION_CODE= "last_version_code";//上次版本号


    //
//    选择的APP的包名的list
    public static void setSelectedAppPackage(String appPackageList){
        SharedPreferenceUtil.saveValue(USER_SETTING, CHANGE_GOODS_LIST,appPackageList);
    }
    //
    public static String getSelectedAppPackage(){
        return SharedPreferenceUtil.getStringValueByKey(USER_SETTING,CHANGE_GOODS_LIST);
    }

    //根据包名保存 设置该APP icon的路径
    public static void setUserStrings(String appPackageName , String path){
        SharedPreferenceUtil.saveValue(USER_SETTING, appPackageName,path);
    }

    public static String getUserStrings(String appPackageName){
        return SharedPreferenceUtil.getStringValueByKey(USER_SETTING,appPackageName);
    }


    //设置版本号
    public static void setVersionCode( String versionCode){
        SharedPreferenceUtil.saveValue(USER_SETTING, LAST_VERSION_CODE,versionCode);
    }

    //获取版本号
    public static String getVersionCode(){
        return SharedPreferenceUtil.getStringValueByKey(USER_SETTING,LAST_VERSION_CODE);
    }


    public static void CleanAll(){
        SharedPreferenceUtil.clearObject(USER_SETTING);
    }



}
