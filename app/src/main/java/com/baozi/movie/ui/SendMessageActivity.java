package com.baozi.movie.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.baozi.movie.adapter.ChoosePhotoListAdapter;
import com.baozi.movie.base.BaseActivity;
import com.baozi.movie.bean.ImageBean;
import com.baozi.movie.bean.Post;
import com.baozi.movie.bean.User;
import com.baozi.movie.ui.weight.HorizontalListView;
import com.baozi.movie.util.ToastUtils;
import com.baozi.seemovie.R;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * by:baozi
 * create on 2017/3/21 16:12
 */

public class SendMessageActivity extends BaseActivity {

    @Bind(R.id.lv_photo)
    HorizontalListView lvPhoto;
    @Bind(R.id.iv_choose)
    ImageView iv_choose;
    @Bind(R.id.et_send_content)
    EditText et_send_content;
    @Bind(R.id.donut_progress)
    DonutProgress donut_progress;
    private List<PhotoInfo> mPhotoList;
    private ChoosePhotoListAdapter mChoosePhotoListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
    }

    @Override
    protected void initView() {
        super.initView();
        showLeftButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        showRightTextButton("发送", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPhotoList != null && mPhotoList.size() > 0) {
                    mPhotoList.remove(mPhotoList.size() - 1);
                    if (mPhotoList.size() > 0)
                        upLoadFile();
                } else {
                    sendPost();
                }
            }
        });
        mPhotoList = new ArrayList<>();
        mChoosePhotoListAdapter = new ChoosePhotoListAdapter(this, mPhotoList, new ChoosePhotoListAdapter.ChoosePhotoListItemClickListener() {
            @Override
            public void onClick() {
                FunctionConfig config = new FunctionConfig.Builder()
                        .setMutiSelectMaxSize(15)
                        .setSelected(mPhotoList)
                        .setEnableCamera(true)//开启相机功能
                        .setEnablePreview(true)//开启预览功能
                        .build();
                GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, config, mOnHanlderResultCallback);
                mPhotoList.clear();
            }
        });
        lvPhoto.setAdapter(mChoosePhotoListAdapter);
    }

    private final int REQUEST_CODE_GALLERY = 1001;

    @OnClick(R.id.iv_choose)
    public void onClick() {
        FunctionConfig config = new FunctionConfig.Builder()
                .setMutiSelectMaxSize(15)
                .setSelected(mPhotoList)
                .setEnableCamera(true)//开启相机功能
                .setEnablePreview(true)//开启预览功能
                .build();
        GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, config, mOnHanlderResultCallback);
        mPhotoList.clear();
    }

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, final List<PhotoInfo> resultList) {
            if (resultList != null) {
                resultList.add(new PhotoInfo());
                mPhotoList.addAll(resultList);
                mChoosePhotoListAdapter.notifyDataSetChanged();
                iv_choose.setVisibility(View.GONE);
            } else {
                iv_choose.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            ToastUtils.showCustomToast(errorMsg);
        }
    };

    private void upLoadFile() {
//        mPhotoList.remove(mPhotoList.size() - 1);
        List<String> list = new ArrayList();
        for (int i = 0; i < mPhotoList.size(); i++) {
            list.add(mPhotoList.get(i).getPhotoPath());
        }
        final String[] photoPaths = list.toArray(new String[list.size()]);
        BmobFile.uploadBatch(SendMessageActivity.this, photoPaths, new UploadBatchListener() {

            @Override
            public void onSuccess(List<BmobFile> files, List<String> urls) {
                if (urls.size() == photoPaths.length) {//如果数量相等，则代表文件全部上传完成
                    Gson gson = new Gson();
                    ArrayList<ImageBean> imageBeenList = new ArrayList<>();
                    for (int i = 0; i < urls.size(); i++) {
                        ImageBean imageBean = new ImageBean();
                        imageBean.setImageUrl(urls.get(i));
                        imageBeenList.add(imageBean);
                    }
                    String json = gson.toJson(imageBeenList);
                    sendPost(json);
                }
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                log("errormsg:" + errormsg);
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                //1、curIndex--表示当前第几个文件正在上传
                //2、curPercent--表示当前上传文件的进度值（百分比）
                //3、total--表示总的上传文件数
                //4、totalPercent--表示总的上传进度（百分比）
                if (totalPercent > 0) {
                    if (donut_progress.getVisibility() == View.GONE)
                        donut_progress.setVisibility(View.VISIBLE);
                    donut_progress.setProgress(totalPercent);
                }
            }
        });
    }

    private void sendPost(String json) {
        String content = et_send_content.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            ToastUtils.showCustomToast("不说点什么嘛~");
            return;
        }
        Post post = new Post();
        User currentUser = BmobUser.getCurrentUser(SendMessageActivity.this, User.class);
        post.setAuthor(currentUser);
        post.setContent(content);
        post.setImageUrl(json);
        post.save(SendMessageActivity.this, new SaveListener() {
            @Override
            public void onSuccess() {
                donut_progress.setVisibility(View.GONE);
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtils.showCustomToast(s);
            }
        });
    }

    private void sendPost() {
        String content = et_send_content.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            ToastUtils.showCustomToast("不说点什么嘛~");
            return;
        }
        Post post = new Post();
        User currentUser = BmobUser.getCurrentUser(SendMessageActivity.this, User.class);
        post.setAuthor(currentUser);
        post.setContent(content);
        post.save(SendMessageActivity.this, new SaveListener() {
            @Override
            public void onSuccess() {
                donut_progress.setVisibility(View.GONE);
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtils.showCustomToast(s);
            }
        });
    }

}
