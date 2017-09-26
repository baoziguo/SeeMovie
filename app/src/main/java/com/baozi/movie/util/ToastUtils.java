package com.baozi.movie.util;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.baozi.movie.BmobIMApplication;
import com.baozi.seemovie.R;

/**
 * @author: Jason
 * @date: 15/9/10.
 * @time: 14:49.
 * toast工具类
 */
public class ToastUtils {


    private static Toast mToast;

    public static void showStringToast(Context context, int showtime,
                                       String showstring) {
        Toast.makeText(context, showstring, showtime).show();
    }

    public static void showShortToast(Context context, String showString) {
        if (!TextUtils.isEmpty(showString)){
            Toast.makeText(context, showString, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showShortToast(String showString) {
        if (!TextUtils.isEmpty(showString)){
//            showShortToast(WeiliCaiApplication.getInstance().getApplicationContext(), showString);
            showCustomToast(showString);
        }
    }

    public static void showShortToast(Context context, int resourceID) {
        Toast.makeText(context, resourceID, Toast.LENGTH_SHORT).show();
    }

    public static void showShortToast(int resourceID) {
        Toast.makeText(BmobIMApplication.INSTANCE(), resourceID, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context context, String showString) {

        Toast.makeText(context, showString, Toast.LENGTH_LONG).show();
    }

    /**
     * 自定义toast且防止重复toast
     * @param msg
     */
    @SuppressWarnings("unused")
    public static void showCustomToast(final String msg) {
        LayoutInflater inflate = LayoutInflater.from(BmobIMApplication.INSTANCE());
        View view = inflate.inflate(R.layout.toast_rect_bg, null);
        TextView textView = (TextView) view.findViewById(R.id.toast_content);
        textView.setText(msg);
        if (mToast == null) {
            mToast = new Toast(BmobIMApplication.INSTANCE());
            mToast.setGravity(Gravity.BOTTOM, 0, DensityUtil.dip2px(BmobIMApplication.INSTANCE(), 80));
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.setView(view);
        mToast.show();
    }

    /**
     * 自定义toast且防止重复toast
     * @param msg
     */
    @SuppressWarnings("unused")
    public static void showCustomLongToast(final String msg) {
        LayoutInflater inflate = LayoutInflater.from(BmobIMApplication.INSTANCE());
        View view = inflate.inflate(R.layout.toast_rect_bg, null);
        TextView textView = (TextView) view.findViewById(R.id.toast_content);
        textView.setText(msg);
        if (mToast == null) {
            mToast = new Toast(BmobIMApplication.INSTANCE());
            mToast.setGravity(Gravity.BOTTOM, 0, DensityUtil.dip2px(BmobIMApplication.INSTANCE(), 80));
            mToast.setDuration(Toast.LENGTH_LONG);
        }
        mToast.setView(view);
        mToast.show();
    }

    /**
     * 防止重复toast
     */
     public static void showToast(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(BmobIMApplication.INSTANCE(), msg,
                    Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

}
