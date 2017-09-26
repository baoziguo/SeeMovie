package com.baozi.movie;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;

import com.baozi.movie.base.UILImageLoader;
import com.baozi.movie.base.UILPauseOnScrollListener;
import com.baozi.movie.base.UniversalImageLoader;
import com.baozi.movie.db.dao.DaoSession;
import com.danikula.videocache.HttpProxyCacheServer;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import cn.bmob.newim.BmobIM;
import cn.bmob.push.BmobPush;
import cn.bmob.v3.BmobInstallation;
import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;
import im.fir.sdk.FIR;

/**
 * @author :smile
 * @project:BmobIMApplication
 * @date :2016-01-13-10:19
 */
public class BmobIMApplication extends Application{
    private HttpProxyCacheServer proxy;
    private static BmobIMApplication INSTANCE;
    public static SharedPreferences preferences;
    //建立全局的daoSession
    public static DaoSession daoSession;
    public static SQLiteDatabase db;

    public static BmobIMApplication INSTANCE(){
        return INSTANCE;
    }
    private void setInstance(BmobIMApplication app) {
        setBmobIMApplication(app);
    }
    private static void setBmobIMApplication(BmobIMApplication a) {
        BmobIMApplication.INSTANCE = a;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setInstance(this);
        //初始化
        Logger.init("bz");
        Config.DEBUG = false;
        UMShareAPI.get(this);
//        PlatformConfig.setSinaWeibo("915518745", "f609a0a4e9b6fb8c69afe1d925414d60","http://sns.whalecloud.com");
        PlatformConfig.setWeixin("wxdc1e388c3822c80b", "3baf1193c85774b3fd9d18447d76cab0");
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad","http://sns.whalecloud.com");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        FIR.init(this);
        //初始化全局异常管理setCrashSaveTargetFolder
//        CrashHandler.getInstance().setCrashSaveTargetFolder(Environment.getExternalStorageDirectory().getAbsolutePath() + "/baozi/crash").setCrashSave(true).init(this);
        //只有主进程运行的时候才需要初始化
        if (getApplicationInfo().packageName.equals(getMyProcessName())){
            //im初始化
            BmobIM.init(this);
            //注册消息接收器
            BmobIM.registerDefaultMessageHandler(new DemoMessageHandler(this));
        }
        //uil初始化
        UniversalImageLoader.initImageLoader(this);


        // 使用推送服务时的初始化操作
        BmobInstallation.getCurrentInstallation(this).save();

        // 启动推送服务
        BmobPush.startWork(this);

//        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        preferences = getSharedPreferences("user", Context.MODE_PRIVATE);

        initGalleryFinay();
    }

    public static HttpProxyCacheServer getProxy(Context context) {
        BmobIMApplication app = (BmobIMApplication) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer(this);
    }

    /**
     * 获取当前运行的进程名
     * @return
     */
    public static String getMyProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    @Override
//    protected void attachBaseContext(Context base) {
//        MultiDex.install(base);
//        super.attachBaseContext(base);
//    }

    private void initGalleryFinay(){
        //设置主题
//        ThemeConfig theme = ThemeConfig.CYAN;
        ThemeConfig theme = new ThemeConfig.Builder()
                .setTitleBarBgColor(Color.rgb(0xFF, 0x40, 0x81))
                .setTitleBarTextColor(Color.WHITE)
                .setTitleBarIconColor(Color.WHITE)
                .setFabNornalColor(0xFFFF4081)
                .setFabPressedColor(0xFF6627A2)
                .setCheckNornalColor(0xFFd2d2d7)
                .setCheckSelectedColor(0xFFFF4081)
                .build();
        //配置功能
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setCropSquare(true)
                .setEnablePreview(true)
                .build();
        CoreConfig coreConfig = new CoreConfig.Builder(this, new UILImageLoader(), theme)
                .setFunctionConfig(functionConfig)
                .setPauseOnScrollListener(new UILPauseOnScrollListener(false, true))
                .build();
        GalleryFinal.init(coreConfig);
    }
}
