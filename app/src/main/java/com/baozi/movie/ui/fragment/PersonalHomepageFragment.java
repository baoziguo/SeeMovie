package com.baozi.movie.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.baozi.movie.adapter.PictureAdapter;
import com.baozi.movie.baidutiebaauto.ui.activity.MainActivity;
import com.baozi.movie.base.BaseFragment;
import com.baozi.movie.bean.User;
import com.baozi.movie.ui.ConversationActivity;
import com.baozi.movie.ui.GetUserServerActivity;
import com.baozi.movie.ui.WebViewActivity;
import com.baozi.movie.util.BitmapUtil;
import com.baozi.movie.util.ToastUtils;
import com.baozi.seemovie.R;
import com.koushikdutta.ion.Ion;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import cn.carbs.android.avatarimageview.library.AvatarImageView;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * by:baozi
 * create on 2017/3/6 17:51
 */

public class PersonalHomepageFragment extends BaseFragment {

    @Bind(R.id.iv_bg)
    ImageView iv_bg;
    @Bind(R.id.iv_avatar)
    AvatarImageView iv_avatar;
    @Bind(R.id.gridView)
    GridView gridView;
    @Bind(R.id.tv_nick)
    TextView tv_nick;

    private float movement = 1.1f;
    //图片的文字标题
    private String[] titles = new String[]
            {"开服监控", "金价查询", "五毒吧", "配装查询", "加速宝典", "奇遇查询", "宠物监控", "日常提醒", "贴吧管理"};
    //图片ID数组
    private int[] images = new int[]{
            R.mipmap.aa, R.mipmap.bb, R.mipmap.cc,
            R.mipmap.dd, R.mipmap.ee, R.mipmap.ff,
            R.mipmap.gg, R.mipmap.hh, R.mipmap.ii
    };
    private User user;

    public static PersonalHomepageFragment newInstance() {
        return new PersonalHomepageFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_personal_homepage, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    protected void initView() {
        user = BmobUser.getCurrentUser(getActivity(), User.class);
        tv_nick.setText(user.getUsername());
        ImageLoader.getInstance().displayImage(user.getAvatar(), iv_avatar);
        Ion.with(getActivity()).load(user.getBg()).intoImageView(iv_bg);
        iv_bg.animate().scaleX(movement).scaleY(movement).setDuration(1500).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                movement = movement > 1.0f ? 1.0f : 1.1f;
                if (iv_bg != null)
                    iv_bg.animate().scaleX(movement).scaleY(movement).setDuration(1500).setListener(this).start();
            }
        }).start();
        PictureAdapter adapter = new PictureAdapter(titles, images, getActivity());
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                switch (position) {
                    case 0://开服监控
//                        startActivity(new Intent(getActivity(), OpenServiceMonitoringActivity.class));
                        startActivity(new Intent(getActivity(), GetUserServerActivity.class));
                        break;
                    case 1://金价查询
                        WebViewActivity.startWebActivity(getActivity(), "http://www.yxdr.com/bijiaqi/jian3/", "");
                        break;
                    case 2://五毒吧
//                        startActivity(new Intent(getActivity(), AppearanceActivity.class));
                        WebViewActivity.startWebActivity(getActivity(), "http://tieba.baidu.com/f?kw=%CE%E5%B6%BE&fr=ala0&tpl=5", "");
                        break;
                    case 3://配装查询
                        WebViewActivity.startWebActivity(getActivity(), "https://www.j3pz.com/", "");
                        break;
                    case 4://加速宝典
                        WebViewActivity.startWebActivity(getActivity(), "https://www.j3pz.com/tools/haste/", "");
                        break;
                    case 5://奇遇查询
                        WebViewActivity.startWebActivity(getActivity(), "http://jx3.derzh.com/serendipity/", "");
                        break;
                    case 6://宠物监控 http://www.ab72.top/#/
                        WebViewActivity.startWebActivity(getActivity(), "http://www.yayaquanzhidao.top/SelectPet.aspx", "");
                        break;
                    case 7://日常提醒
                        WebViewActivity.startWebActivity(getActivity(), "https://haimanchajian.com/richangtixing?day=today", "");
                        break;
                    case 8://贴吧管理
//                        startActivity(new Intent(getActivity(), OpenPushActivity.class));
                        startActivity(new Intent(getActivity(), MainActivity.class));
//                        startActivity(new Intent(getActivity(), TieBaActivity.class));
                        break;
                }
            }
        });
    }

    private final int REQUEST_CODE_AVATAR = 1001;
    private final int REQUEST_CODE_BG = 1002;
    BmobFile bmobFile;

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(final int reqeustCode, final List<PhotoInfo> resultList) {
            if (resultList != null) {
                String picPath = resultList.get(0).getPhotoPath();
                String smallBitmapPath = BitmapUtil.saveBitmap(picPath);
                if (TextUtils.isEmpty(smallBitmapPath)){
                    bmobFile = new BmobFile(new File(picPath));
                }else {
                    bmobFile = new BmobFile(new File(smallBitmapPath));
                }
                bmobFile.upload(getActivity(), new UploadFileListener() {
                    @Override
                    public void onSuccess() {
                        String fileUrl = bmobFile.getFileUrl(getActivity());
                        if (reqeustCode == REQUEST_CODE_AVATAR){
                            user.setAvatar(fileUrl);
                        }else if (reqeustCode == REQUEST_CODE_BG){
                            user.setBg(fileUrl);
                        }
                        user.update(getActivity(), new UpdateListener() {
                            @Override
                            public void onSuccess() {
                                if (reqeustCode == REQUEST_CODE_AVATAR){
                                    ImageLoader.getInstance().displayImage("file:/" + resultList.get(0).getPhotoPath(), iv_avatar);
                                }else if (reqeustCode == REQUEST_CODE_BG){
                                    ImageLoader.getInstance().displayImage("file:/" + resultList.get(0).getPhotoPath(), iv_bg);
                                }
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                if (reqeustCode == REQUEST_CODE_AVATAR){
                                    ToastUtils.showCustomToast("头像上传失败");
                                }else if (reqeustCode == REQUEST_CODE_BG){
                                    ToastUtils.showCustomToast("背景图上传失败");
                                }
                            }
                        });

                    }

                    @Override
                    public void onFailure(int i, String s) {
                        if (reqeustCode == REQUEST_CODE_AVATAR){
                            ToastUtils.showCustomToast("头像上传失败");
                        }else if (reqeustCode == REQUEST_CODE_AVATAR){
                            ToastUtils.showCustomToast("背景图上传失败");
                        }
                    }

                });

            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            ToastUtils.showCustomToast(errorMsg);
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.iv_avatar, R.id.tv_xiaozhitiao, R.id.iv_bg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_avatar:
                GalleryFinal.openGallerySingle(REQUEST_CODE_AVATAR, mOnHanlderResultCallback);
                break;
            case R.id.tv_xiaozhitiao:
                startActivity(new Intent(getActivity(), ConversationActivity.class));
                break;
            case R.id.iv_bg:
                GalleryFinal.openGallerySingle(REQUEST_CODE_BG, mOnHanlderResultCallback);
                break;
        }
    }

    /****************
     *
     * 发起添加群流程。群号：毒经永不为奴(537858873) 的 key 为： 8iFU_VBCOxmjMbX57xiJSO19oCO5EuhM
     * 调用 joinQQGroup(8iFU_VBCOxmjMbX57xiJSO19oCO5EuhM) 即可发起手Q客户端申请加群 毒经永不为奴(537858873)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

}
