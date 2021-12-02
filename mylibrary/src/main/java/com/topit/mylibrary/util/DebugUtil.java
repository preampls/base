package com.topit.mylibrary.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;

/**
 * @ClassName: DebugUtil
 * @Description: java类作用描述
 * @Author: ls
 * @CreateDate: 2021/5/11 16:02
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/5/11 16:02
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class DebugUtil {

    /**
     * 判断当前应用是否是debug状态
     */
    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }
}
