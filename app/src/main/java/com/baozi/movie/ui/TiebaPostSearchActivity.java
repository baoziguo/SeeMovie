package com.baozi.movie.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.baozi.movie.base.BaseActivity;
import com.baozi.seemovie.R;
import com.koushikdutta.ion.Ion;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * by:baozi
 * create on 2017/5/25 14:54
 */

public class TiebaPostSearchActivity extends BaseActivity {

    @Bind(R.id.tv_content)
    TextView tvContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tieba_post_search);
        ButterKnife.bind(this);
    }

    public void onClick(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                            String test_url = "http://tieba.baidu.com/p/4911885706?lp=5027&mo_device=1&is_jingpost=1&pn=2&&red_tag=2391548032";
                    String test_url = "https://tieba.baidu.com/p/5253806234?pn=";
                    String s = Ion.with(TiebaPostSearchActivity.this).load(test_url + 1).setHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:32.0) Gecko/    20100101 Firefox/32.0").asString().get();
                    String text = Jsoup.parse(s).toString();
                    int max_page = getPage(text);
                    for (int page = 1; page < max_page; page++) {
                        String hh_url = test_url + page;
                        String stringResponseFuture = null;
                        stringResponseFuture = Ion.with(TiebaPostSearchActivity.this).load(hh_url).setHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:32.0) Gecko/    20100101 Firefox/32.0").asString().get();
//                                Connection conn = Jsoup.connect(hh_url);
                        Document doc = Jsoup.parse(stringResponseFuture);
                        // 修改http包中的header,伪装成浏览器进行抓取
//                            conn.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
//                                Document doc = conn.get();
                        Elements divs = doc.getElementsByTag("div");
                        for (Element div : divs) {
                            String linkText = div.text();
                            if (linkText.contains("楼") && linkText.contains("回复") && linkText.contains("代练通")) {
                                com.orhanobut.logger.Logger.d("外观查询：" + linkText);
                                if (!hasDigit(linkText.substring(8, 20))) {
                                    com.orhanobut.logger.Logger.d("过滤：" + linkText);
                                }
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 获取贴吧帖子总页数
     *
     * @param content
     * @return
     */
    private int getPage(String content) {
        int page = 0;
        Pattern pattern = Pattern.compile("max-page=\"(\\d+)\"");
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            page = Integer.parseInt(matcher.group(1));
        }
        return page;
    }

    // 判断一个字符串是否含有数字

    public boolean hasDigit(String content) {

        boolean flag = false;

        Pattern p = Pattern.compile(".*\\d+.*");

        Matcher m = p.matcher(content);

        if (m.matches())

            flag = true;

        return flag;

    }
}
