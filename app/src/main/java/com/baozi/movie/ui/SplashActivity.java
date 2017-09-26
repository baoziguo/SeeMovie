package com.baozi.movie.ui;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.baozi.movie.base.BaseActivity;
import com.baozi.movie.bean.User;
import com.baozi.movie.model.UserModel;
import com.baozi.movie.util.DialogUtil;
import com.baozi.movie.util.StatusBarUtil;
import com.baozi.seemovie.R;
import com.koushikdutta.ion.Ion;
import butterknife.Bind;

/**
 * 启动界面
 *
 * @author :smile
 * @project:SplashActivity
 * @date :2016-01-15-18:23
 */
public class SplashActivity extends BaseActivity {

    @Bind(R.id.iv_splash)
    ImageView ivSplash;
    private Dialog dialog;

    private static final int REQUEST_CODE_SETTING = 300;

    String[] PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Ion.with(ivSplash).load("http://p1.bqimg.com/1949/710abb7da53cf22f.gif");
        checkSelfPermission();
    }

    private void checkSelfPermission() {
        if (lacksPermissions(PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
        } else {
            startActivity();
        }
    }

    private void startActivity() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                User user = UserModel.getInstance().getCurrentUser();
                if (user == null) {
                    startActivity(com.baozi.movie.ui.materiallogin.MainActivity.class, null, true);
                } else {
                    startActivity(HomeNewActivity.class, null, true);
                }
            }
        }, 2900);
    }

    @Override
    public void setStatusState(int color) {
        StatusBarUtil.setTransparent(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1 && hasAllPermissionsGranted(grantResults)) {
            startActivity();
        } else {
            showMissingPermissionDialog();
        }
    }


    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        int length = grantResults.length;
        for (int i = 0; i < length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }
    private void showMissingPermissionDialog() {
        View view = View.inflate(this, R.layout.dialog_permission, null);
        dialog = DialogUtil.createDialog(this, view);
        TextView exit = (TextView) view.findViewById(R.id.exit);
        TextView setting = (TextView) view.findViewById(R.id.setting);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                dialog.dismiss();
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAppSettings();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void startAppSettings() {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse(new StringBuilder("package:").append(getPackageName()).toString()));
        startActivityForResult(intent, REQUEST_CODE_SETTING);
    }

    private boolean lacksPermission(String string) {
        return ContextCompat.checkSelfPermission(this, string) == PackageManager.PERMISSION_DENIED;
    }

    /**
     * 权限数组检查
     *
     * @param lacksPermissions
     */
    public boolean lacksPermissions(String... lacksPermissions) {
        for (String lacksPermission : lacksPermissions) {
            if (lacksPermission(lacksPermission)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_SETTING: {
                checkSelfPermission();
                break;
            }
        }
    }

}
