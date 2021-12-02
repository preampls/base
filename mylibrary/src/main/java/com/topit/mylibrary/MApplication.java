package com.topit.mylibrary;

import android.app.Application;
import android.os.Environment;
import android.util.DisplayMetrics;


import com.topit.mylibrary.crash.CrashHandler;
import com.topit.mylibrary.util.DebugUtil;
import com.topit.mylibrary.util.EvtLog;
import com.topit.mylibrary.util.LogToFileUtils;
import com.topit.mylibrary.util.SharedPreferenceUtil;
import com.topit.mylibrary.util.Utils;

import java.io.File;


/**
 * @Description:
 * @Author: ls
 * @CreateDate: 2020/6/16 16:47
 * @UpdateUser: ls
 */
public class MApplication extends Application {


    // 参照16:9的比例
    public static final float UI_Design_Width = 1920;
    public static final float UI_Design_Height = 1080;
    // 屏幕宽度缩放比（相对于原设计图）
    public static float mScreenWidthScale = 1f;
    public static float mScreenHeightScale = 1f;

    public static String path = Environment.getExternalStorageDirectory() + "/信息公示";
    public static String Model_1_Path = path + "/可开放容量/";
    public static String Model_2_Path = path + "/信息公示/";
    public static String Model_3_Path = path + "/电子意见簿/";
    public static String Model_4_Path = path + "/信息查询/";

    public static String DJJSFBZ_PATH = Model_2_Path + "/电价及收费标准/";

    public static String DJJSFBZ_PATH_1 = DJJSFBZ_PATH + "工商业及其他/";
    public static String DJJSFBZ_PATH_2 = DJJSFBZ_PATH + "居民生活用电/";
    public static String DJJSFBZ_PATH_3 = DJJSFBZ_PATH + "农业生产用电/";
    public static String DJJSFBZ_PATH_4 = DJJSFBZ_PATH + "大工业用电/";
    public static String DJJSFBZ_PATH_5 = DJJSFBZ_PATH + "收费标准/";

    //服务规范
    public static String FWGF_PATH = Model_2_Path + "/服务规范/";
    public static String FWGF_PATH_1 = FWGF_PATH + "/十个不准/";
    public static String FWGF_PATH_2 = FWGF_PATH + "/十项承诺/";
    //举措
    public static String YHHJJC_PATH = Model_2_Path + "/优化环境举措/";
    //阳光业扩
    public static String YGYK_PATH_1 = Model_2_Path + "/阳光/";
    public static String YGYK_PATH_2 = Model_2_Path + "/表格附件/";
    public static String YGYK_PATH_3 = YHHJJC_PATH + "可开发容量/";
    public static String YGYK_PATH_4 = YHHJJC_PATH + "停电信息公告/";
    //用电政策
    public static String YDZC_PATH = Model_2_Path + "/用电政策/";

    @Override
    public void onCreate() {
        super.onCreate();
        // 日志
        CrashHandler crashHandler = CrashHandler.getInstance();
        if (!DebugUtil.isApkInDebug(this)) {
            crashHandler.init(this);
        }

        //测算屏幕的数据
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = Math.max(displayMetrics.widthPixels, displayMetrics.heightPixels);
        int height = Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels);
        mScreenWidthScale = width / UI_Design_Width;
        mScreenHeightScale = height / UI_Design_Height;


        SharedPreferenceUtil.getInstance().setsContext(this);
        Utils.init(this);
        LogToFileUtils.init(this);


    }


    private static MApplication instance;

    public static MApplication getInstance() {
        if (instance == null) {
            synchronized (MApplication.class) {
                if (instance == null) {
                    instance = new MApplication();
                }
            }
        }
        return instance;
    }





}
