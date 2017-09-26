package com.baozi.movie.util;

import android.os.Build;

/**
 *  设备适配
 */
public class DeviceAdaptationUtils {

    public static boolean isSetTabHeight(int height) {

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
