package com.baozi.movie;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import com.baozi.movie.ui.WebViewActivity;
import com.baozi.movie.util.NotifyUtil;
import com.baozi.seemovie.R;
import org.json.JSONException;
import org.json.JSONObject;
import cn.bmob.push.PushConstants;

/**
 * by:baozi
 * create on 2017/3/10 11:56
 */

public class MyPushMessageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
            String stringExtra = intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
            Log.d("baozi", "收到消息：" + stringExtra);
            try {
                JSONObject jsonObject = new JSONObject(stringExtra);
                String alert = jsonObject.getString("alert");
                Intent intent_notify = new Intent(context, WebViewActivity.class);
                intent_notify.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent_notify.putExtra("url", "https://haimanchajian.com/richangtixing?day=today");
                PendingIntent pIntent = PendingIntent.getActivity(context,
                        (int) SystemClock.uptimeMillis(), intent_notify, PendingIntent.FLAG_UPDATE_CURRENT);
                int smallIcon = R.mipmap.snake;
                String ticker = "您有一条新通知";
                String title = "日常提醒！！！";
                String content = alert;
                //实例化工具类，并且调用接口
                NotifyUtil notify2 = new NotifyUtil(context, 2);
                notify2.notify_normail_moreline(pIntent, smallIcon, ticker, title, content, true, true, false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}