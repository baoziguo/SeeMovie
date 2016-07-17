package com.baozi.movie.ui;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.baozi.movie.base.BaseActivity;
import com.baozi.movie.bean.User;
import com.baozi.movie.bean.kePao;
import com.baozi.movie.event.FinishEvent;
import com.baozi.movie.model.UserModel;
import com.baozi.seemovie.R;
import org.greenrobot.eventbus.Subscribe;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

/**登陆界面
 * @author :smile
 * @project:LoginActivity
 * @date :2016-01-15-18:23
 */
public class LoginActivity extends BaseActivity {

    @Bind(R.id.et_username)
    EditText et_username;
    @Bind(R.id.et_password)
    EditText et_password;
    @Bind(R.id.btn_login)
    Button btn_login;
    @Bind(R.id.tv_register)
    TextView tv_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @OnClick(R.id.btn_login)
    public void onLoginClick(View view){
        UserModel.getInstance().login(et_username.getText().toString(), et_password.getText().toString(), new LogInListener() {

            @Override
            public void done(Object o, BmobException e) {
                if (e == null) {
                    User user =(User)o;
                    BmobIM.getInstance().updateUserInfo(new BmobIMUserInfo(user.getObjectId(), user.getUsername(), user.getAvatar()));
                    startActivity(MainActivity.class, null, true);
                } else {
                    toast(e.getMessage() + "(" + e.getErrorCode() + ")");
                }
            }
        });
    }

    @OnClick(R.id.tv_register)
    public void onRegisterClick(View view){
//        startActivity(RegisterActivity.class, null, false);
        upLoadFile();
    }

    @Subscribe
    public void onEventMainThread(FinishEvent event){
        finish();
    }

    private void upLoadFile() {
        List<String> videoPathFromSD = getVideoPathFromSD();
        final String [] VideoPaths = videoPathFromSD.toArray(new String[videoPathFromSD.size()]);
        BmobFile.uploadBatch(LoginActivity.this, VideoPaths, new UploadBatchListener() {

            @Override
            public void onSuccess(List<BmobFile> files, List<String> urls) {
                Date now = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                String hehe = dateFormat.format( now );
                if(urls.size()==VideoPaths.length){//如果数量相等，则代表文件全部上传完成
                    Toast.makeText(LoginActivity.this, "已全部上传完毕", Toast.LENGTH_LONG).show();
                    for (int i = 0; i < files.size(); i++) {
                        kePao kePao = new kePao();
                        kePao.setFileUrl(files.get(i));
                        kePao.setName(hehe + "上传" + "第" + i + "个");
                        kePao.setUrl(urls.get(i));
                        kePao.save(LoginActivity.this, new SaveListener() {
                            @Override
                            public void onSuccess() {
                                Log.i("baozi","成功");
                            }

                            @Override
                            public void onFailure(int i, String s) {

                            }
                        });
                    }

                }
            }

            @Override
            public void onError(int statuscode, String errormsg) {

            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total,int totalPercent) {
                //1、curIndex--表示当前第几个文件正在上传
                //2、curPercent--表示当前上传文件的进度值（百分比）
                //3、total--表示总的上传文件数
                //4、totalPercent--表示总的上传进度（百分比）
            }
        });
    }

    // 从sd卡获取图片资源
    private List<String> getVideoPathFromSD() {
        // 图片列表
        List<String> picList = new ArrayList<String>();
        // 得到sd卡内路径
        String imagePath = Environment.getExternalStorageDirectory().toString()
                + "/tumblr_dongguazyy";
        // 得到该路径文件夹下所有的文件
        File mfile = new File(imagePath);
        File[] files = mfile.listFiles();

        // 将所有的文件存入ArrayList中,并过滤所有图片格式的文件
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
//            if (checkIsImageFile(file.getPath())) {
            picList.add(file.getPath());
            Log.d("baozi.......",  file.getPath());
//            }

        }
        // 返回得到的图片列表
        return picList;
    }

    // 检查扩展名，得到图片格式的文件
    private boolean checkIsImageFile(String fName) {
        boolean isImageFile = false;

        // 获取扩展名
        String FileEnd = fName.substring(fName.lastIndexOf(".") + 1,
                fName.length()).toLowerCase();
        if (FileEnd.equals("jpg") || FileEnd.equals("gif")
                || FileEnd.equals("png") || FileEnd.equals("jpeg")
                || FileEnd.equals("bmp")) {
            isImageFile = true;
        } else {
            isImageFile = false;
        }

        return isImageFile;

    }
}
