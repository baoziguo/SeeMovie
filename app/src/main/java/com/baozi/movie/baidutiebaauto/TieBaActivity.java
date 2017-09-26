package com.baozi.movie.baidutiebaauto;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.baozi.movie.adapter.TieBaListAdapter;
import com.baozi.movie.baidutiebaauto.database.TiebaSQLiteDao;
import com.baozi.movie.baidutiebaauto.model.User;
import com.baozi.movie.baidutiebaauto.utils.BaiduTiebaUtils;
import com.baozi.movie.base.BaseActivity;
import com.baozi.movie.bean.TiebaPost;
import com.baozi.movie.ui.WebViewActivity;
import com.baozi.movie.ui.weight.refreshrecyclerview.RefreshRecyclerView;
import com.baozi.movie.util.PreferencesUtil;
import com.baozi.seemovie.R;

import org.apache.commons.codec.binary.Hex;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class TieBaActivity extends BaseActivity {

    @Bind(R.id.recycler_view)
    RefreshRecyclerView mRecyclerView;
    @Bind(R.id.et_content)
    EditText etContent;
    private TieBaListAdapter mAdapter;
    private TiebaSQLiteDao mDao;
    private List<TiebaPost> mPosts;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tieba);
        user = getIntent().getParcelableExtra("user");
        // 从数据库中获取所有帖子
        String content = PreferencesUtil.getPreferences("content", "");
        if (!TextUtils.isEmpty(content)){
            etContent.setText(content);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDao = TiebaSQLiteDao.getInstance(this);
        mPosts = mDao.getAllPost();
        mAdapter = new TieBaListAdapter(this);
        mRecyclerView.setSwipeRefreshColors(0xFFFF4081, 0xFFE44F98, 0xFF2FAC21);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.clear();
        mAdapter.addAll(mPosts);
    }

    /**
     * md5加密
     *
     * @param string
     * @return
     */
    private String MD5Encrypt(String string) {
        final MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(string.getBytes(Charset.forName("UTF-8")));
            final byte[] resultByte = messageDigest.digest();
            char[] result = Hex.encodeHex(resultByte);
            String s = new String(result);
            return s;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    @OnClick({R.id.bt_send, R.id.bt_edit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_send:
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("url", "https://tieba.baidu.com/mo/q/searchpage");
                intent.putExtra("type", "tieba");
                startActivity(intent);
                break;
            case R.id.bt_edit:
                String content = etContent.getText().toString().trim();
                PreferencesUtil.putPreferences("content", content);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BaiduTiebaUtils instance = BaiduTiebaUtils.getInstance(TieBaActivity.this);
                        for (TiebaPost post : mPosts) {
                            instance.xiii(TieBaActivity.this, user, post);
                        }
                    }
                }).start();
                break;
        }
    }
}
