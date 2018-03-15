package com.baozi.movie.util;

import android.content.Context;
import android.os.Build;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;

/**
 *  设备适配
 */
public class DeviceAdaptationUtils {

    public static boolean isSetTabHeight(int height, Context context) {
        //通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
        boolean hasMenuKey = ViewConfiguration.get(context)
                .hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap
                .deviceHasKey(KeyEvent.KEYCODE_BACK);

        if (!hasMenuKey && !hasBackKey) {
            // 做任何你需要做的,这个设备有一个导航栏
            return true;
        }
        if (android.os.Build.MODEL.replace(" ", "").contains("X9i")){
            return false;
        }
        String deviceName = Build.MANUFACTURER.replace(" ", "");
        if (Build.VERSION.SDK_INT >= 21) {
            if (height > 0 && !NavbarUtil.isMeizu()) {
                if (deviceName.equals("HUAWEI") || deviceName.equals("Huawei")) {
                    return true;
                }
                return true;
            }
        } else {
            if (height > 0 && !NavbarUtil.isMeizu() && !deviceName.equals("HUAWEI")) {
                return true;
            }
        }
        return false;
    }
}
