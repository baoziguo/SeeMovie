package com.baozi.movie.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.baozi.seemovie.R;

/**
 * by:baozi
 * create on 2017/3/7 18:39
 */

public class DialogUtil {

    public static Dialog createDialog(Context context, View view) {

        Dialog dialog = new Dialog(context, R.style.myStyleDialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.setContentView(view, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT));
        return dialog;
    }

    /**
     * 知道了对话框
     *
     * @param context
     * @param content  显示的内容
     * @param isCenter 文字是否居中
     * @return
     */
    public static Dialog showKnowDialog(Context context, String content, boolean isCenter) {
        View view = View.inflate(context, R.layout.dialog_know, null);
        final Dialog dialog = createDialog(context, view);
        TextView tv_msg = (TextView) view.findViewById(R.id.tv_msg);
        if (isCenter) {
            tv_msg.setGravity(Gravity.CENTER);
        } else {
            tv_msg.setGravity(Gravity.NO_GRAVITY);
        }
        tv_msg.setText(content);
        TextView confirm = (TextView) view.findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        return dialog;
    }

    /**
     * 知道了对话框
     *
     * @param context
     * @param content  显示的内容
     * @param isCenter 文字是否居中
     * @return
     */
    public static Dialog showDialog(Context context, String content, boolean isCenter, final DialogClickListener dialogClickListener) {
        View view = View.inflate(context, R.layout.dialog_common, null);
        final Dialog dialog = createDialog(context, view);
        TextView tv_msg = (TextView) view.findViewById(R.id.tv_msg);
        if (isCenter) {
            tv_msg.setGravity(Gravity.CENTER);
        } else {
            tv_msg.setGravity(Gravity.NO_GRAVITY);
        }
        tv_msg.setText(content);
        TextView confirm = (TextView) view.findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogClickListener.confirm();
                dialog.dismiss();
            }
        });
        TextView cancel = (TextView) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogClickListener.cancel();
                dialog.dismiss();
            }
        });
        dialog.show();
        return dialog;
    }

    public interface DialogClickListener{
        void confirm();
        void cancel();
    }
}
