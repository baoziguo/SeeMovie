package com.baozi.movie.baidutiebaauto.utils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.baozi.movie.baidutiebaauto.database.TiebaSQLiteDao;
import com.baozi.movie.baidutiebaauto.model.Tieba;
import com.baozi.movie.baidutiebaauto.model.User;
import com.baozi.movie.bean.TiebaForimEntity;
import com.baozi.movie.bean.TiebaPost;
import com.baozi.movie.util.CacheUtils;
import com.baozi.movie.util.GsonUtil;
import com.baozi.movie.util.PreferencesUtil;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import okhttp3.Cookie;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BaiduTiebaUtils {
    public static final String TAG = "BaiduTiebaUtils";

    private Context mContext;
    private static BaiduTiebaUtils mBaiduTiebaUtils;
    private CacheUtils cacheUtils;

    /**
     * 私有化构构造器
     */
    private BaiduTiebaUtils(Context context) {
        mContext = context;
    }

    public static BaiduTiebaUtils getInstance(Context context) {
        if (mBaiduTiebaUtils == null)
            mBaiduTiebaUtils = new BaiduTiebaUtils(context);
        return mBaiduTiebaUtils;
    }

    /**
     * 获取登录后的Cookies
     */
    public boolean login(String account, String password) throws IOException, JSONException {

        // 创建一个cookies容器，因为获取token参数时需要cookies
        MyCookieJar cookieJar = new MyCookieJar();

        // 打开百度贴吧手机版网页，然后获取到cookies（其实www.baidu.com也可以，只不过手机版省流量）
        HttpUtils.sendGetRequest("http://tieba.baidu.com/mo/", cookieJar);

        // 第二步获取token
        Response response = HttpUtils.sendGetRequest("https://passport.baidu.com/v2/api/?getapi&tpl=pp&apiver=v3", cookieJar);
        // 解析响应的json，拿到token
        JSONObject data = new JSONObject(response.body().string()).getJSONObject("data");
        String token = data.getString("token");

        // 登录百度账号时需要POST的参数
        RequestBody formBody = new FormBody.Builder()
                .add("staticpage", "https://passport.baidu.com/static/passpc-account/html/V3Jump.html")
                .add("token", token)
                .add("tpl", "pp")
                .add("username", account)
                .add("password", password)
                .add("loginmerge", "true")
                .add("mem_pass", "on")
                .build();

        Headers headers = new Headers.Builder()
                .set("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .set("Host", "passport.baidu.com")
                .set("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                .build();

        // 登录百度账号
        HttpUtils.sendPostRequest("https://passport.baidu.com/v2/api/?login", cookieJar, formBody, headers);
        // 检查cookies中是否有BDUSS参数，有说明登录成功
        Iterator<Cookie> iterator = cookieJar.cookies.iterator();
        HashMap<String, String> tempMap = new HashMap<>();
        StringBuffer tempsb = new StringBuffer();
        while (iterator.hasNext()) {
            Cookie cookie = iterator.next();
            tempMap.put(cookie.name(), cookie.value());
            tempsb.append(cookie.name() + "=" + cookie.value() + ";");
//            if (cookie.name().equals("BDUSS")) {
//                // 获取BDUSS
//                String bduss = cookie.value();
//
//                // 获取用户名
//
////                response = HttpUtils.sendGetRequest("http://tieba.baidu.com/f/user/json_userinfo/", cookieJar);//会经常报错
////                byte[] bytes = response.body().bytes();
////                JSONObject result = new JSONObject(new String(bytes, "GBK"));
////                String uid = result.getJSONObject("data").getString("open_uid");
////                String name = result.getJSONObject("data").getString("user_name_show");
//                String uid = "2701399324";
//                String name = "虚灵子";
//
//                // 存入数据库
//                User user = new User(uid, name, bduss);
//                TiebaSQLiteDao dao = TiebaSQLiteDao.getInstance(mContext);
//                dao.addUser(user);
//
//                return true;
//            }
        }
        Logger.d("tempsb" + tempsb.toString());
        // 生成POST的参数  http://c.tieba.baidu.com/c/s/login 获取贴吧客户端uid
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("bdusstoken", tempMap.get("BDUSS"));
        treeMap.put("_client_type", "2");
        treeMap.put("_client_version", "4.0.0");
        treeMap.put("_phone_imei", "868030022185183");
        treeMap.put("from", "mini_ad_xiaomi");
        treeMap.put("model", "MI 5");
        treeMap.put("stErrorNums", "0");
        treeMap.put("subapp_type", "mini");
        FormBody.Builder builder = new FormBody.Builder();
        StringBuffer sb = new StringBuffer();

        Set<Map.Entry<String, String>> set = treeMap.entrySet();
        for (Map.Entry<String, String> entry : set) {
            builder.add(entry.getKey(), entry.getValue());
            sb.append(entry.getKey() + "=" + entry.getValue());
        }
        String SIGNKEY = "tiebaclient!!!";
        String sign = Md5Encoder.encode(sb.toString() + SIGNKEY);

        RequestBody requestBody = builder.add("sign", sign).build();


        // 获取uid
        Response response1 = HttpUtils.sendPostRequest("http://c.tieba.baidu.com/c/s/login", cookieJar, requestBody);//客户端
        String string = response1.body().string();
        Logger.d("string" + string);
        JSONObject jsonObject = new JSONObject(string);
        JSONObject json_user = jsonObject.getJSONObject("user");
        String uid = json_user.getString("id");//这是要拿来获取用户关注的贴吧的，用户id 832122980
        String name = json_user.getString("name");
        PreferencesUtil.putPreferences(uid, tempsb);
        if (cacheUtils == null)
            cacheUtils = CacheUtils.get(mContext, 1);
        cacheUtils.put(uid, tempsb.toString());
        //获取网页版uid
//        Document doc = Jsoup.connect("http://tieba.baidu.com/f/user/json_userinfo/").cookies(tempMap).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
//                .get();
//        JSONObject result = new JSONObject(doc.text());
//        String uid = result.getJSONObject("data").getString("open_uid");
//        String name = result.getJSONObject("data").getString("user_name_show");
        // 存入数据库
        String bduss = tempMap.get("BDUSS");
        if (TextUtils.isEmpty(bduss))
            return false;
        User user = new User(uid, name, bduss);
        TiebaSQLiteDao dao = TiebaSQLiteDao.getInstance(mContext);
        dao.addUser(user);
        return true;
    }

    /**
     * 网页签到，先放着，以后扩展功能的时候可能用到
     */
    public void signTiebaWithWepPage(User user, Tieba tieba) throws IOException, JSONException {
        // cookies中加入bduss参数
        MyCookieJar cookieJar = addBduss(user.getBduss());

        // 签到时需要POST的参数
        RequestBody requestBody = new FormBody.Builder()
                .add("ie", "utf-8")
                .add("kw", tieba.getName())
                .build();

        Response response = HttpUtils.sendPostRequest("http://tieba.baidu.com/sign/add", cookieJar, requestBody);

        String json = new String(response.body().bytes(), "unicode");
        JSONObject result = new JSONObject(json);
        if (result.getInt("no") == 0) {
            // 签到成功
        } else if (result.getInt("no") == 1101) {
            // 亲，你之前已经签过了

        } else {
            // 签到失败

        }
    }

    /**
     * 客户端签到
     *
     * @param user
     * @param tieba
     * @return 返回0表示已经签到，1表示签到成功，2表示签到错误
     * @throws IOException
     * @throws JSONException
     */
    public int signTiebaWithClient(User user, Tieba tieba) throws IOException, JSONException {
        String bduss = user.getBduss();
        MyCookieJar cookieJar = addBduss(bduss); // cookies中加入bduss参数

        // 获取tbs参数
        Response response = HttpUtils.sendGetRequest("http://tieba.baidu.com/dc/common/tbs", cookieJar);
        String tbs = new JSONObject(response.body().string()).getString("tbs");

        // 生成POST的参数
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("BDUSS", bduss);
        treeMap.put("_client_type", "4");
        treeMap.put("_client_version", "1.2.1.17");
        treeMap.put("kw", tieba.getName());
        treeMap.put("net_type", "3");
        treeMap.put("tbs", tbs);

        FormBody.Builder builder = new FormBody.Builder();
        StringBuffer sb = new StringBuffer();

        Set<Map.Entry<String, String>> set = treeMap.entrySet();
        for (Map.Entry<String, String> entry : set) {
            builder.add(entry.getKey(), entry.getValue());
            sb.append(entry.getKey() + "=" + entry.getValue());
        }

        String SIGNKEY = "tiebaclient!!!";
        String sign = Md5Encoder.encode(sb.toString() + SIGNKEY);

        RequestBody requestBody = builder.add("sign", sign).build();


        // 执行签到
        response = HttpUtils.sendPostRequest("http://c.tieba.baidu.com/c/c/forum/sign/", cookieJar, requestBody);
        String json = response.body().string();

        JSONObject result = new JSONObject(json);
        String errorCode = result.getString("error_code");
        if (errorCode.equals("0")) {
            tieba.setStatus(true); // 更新签到状态

            TiebaSQLiteDao dao = TiebaSQLiteDao.getInstance(mContext);
            dao.addTieba(tieba); // 存入数据库

            return 1;// 返回1表示签到成功
        } else if (errorCode.equals("160002")) {
            return 0; // 返回0表示已经签到
        } else {
            return 2; // 返回2表示签到错误
        }
    }

    /**
     * 获取某个账户已关注的贴吧信息，然后存入数据库
     */
    public boolean getLikeForum(User user) throws IOException, JSONException {
        // 把BDUSS参数添加到cookies中
        MyCookieJar cookieJar = addBduss(user.getBduss());

        // 生成客户端POST的参数
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("BDUSS", user.getBduss());
        treeMap.put("_client_id", "wappc_1495011878277_642");
        treeMap.put("_client_type", "2");
        treeMap.put("_client_version", "6.9.2.1");
        treeMap.put("page_no", "1");
        treeMap.put("page_size", "100");
        treeMap.put("uid", user.getUid());//此uid为贴吧客户端uid

        FormBody.Builder builder = new FormBody.Builder();
        StringBuffer sb = new StringBuffer();

        Set<Map.Entry<String, String>> set = treeMap.entrySet();
        for (Map.Entry<String, String> entry : set) {
            builder.add(entry.getKey(), entry.getValue());
            sb.append(entry.getKey() + "=" + entry.getValue());
        }

        String SIGNKEY = "tiebaclient!!!";
        String sign = Md5Encoder.encode(sb.toString() + SIGNKEY);

        RequestBody requestBody = builder.add("sign", sign).build();


        // 获取已关注的贴吧
//        Response response = HttpUtils.sendGetRequest("http://tieba.baidu.com/p/getLikeForum?uid=" + user.getUid(), cookieJar);//网页版
        Response response = HttpUtils.sendPostRequest("http://tieba.baidu.com/c/f/forum/like", cookieJar, requestBody);//客户端
        String string = response.body().string();
        Logger.d("tiebaList------------->" + string);
        List<Tieba> tiebas = new ArrayList<>();
        //网页版解析
//        JSONArray array = new JSONObject(response.body().string()).getJSONObject("data").getJSONArray("info");
//        for (int i = 0; i < array.length(); i++) {
//            JSONObject info = array.getJSONObject(i);
//            String name = info.getString("forum_name");
//            String level = info.getString("user_level");
//            String exp = info.getString("user_exp");
//            String fid = info.getString("id");
//            Tieba tieba = new Tieba(user.getUid(), name, level, exp, "5201314");
//            tieba.setFid(fid);
//            tiebas.add(tieba);
//        }
        //客户端解析
        TiebaForimEntity tiebaForimEntity = GsonUtil.GsonToBean(string, TiebaForimEntity.class);
        List<TiebaForimEntity.ForumListBean.NongconforumBean> nongconforum = tiebaForimEntity.getForum_list().getNongconforum();
        for (int i = 0; i < nongconforum.size(); i++) {
            Tieba tieba = new Tieba(user.getUid(), nongconforum.get(i).getName(), nongconforum.get(i).getLevel_id(), nongconforum.get(i).getCur_score(), nongconforum.get(i).getLevelup_score());
            tieba.setFid(nongconforum.get(i).getId());
            tieba.setAvatar(nongconforum.get(i).getAvatar());
            tiebas.add(tieba);
        }
//        JSONArray array = new JSONObject(string).getJSONObject("forum_list").getJSONArray("non-gconforum");
//        for (int i = 0; i < array.length(); i++) {
//            JSONObject info = array.getJSONObject(i);
//            String name = info.getString("name");//贴吧名字
//            String level = info.getString("level_id");//用户当前当前等级
//            String exp = info.getString("cur_score");//用户当前贴吧经验
//            String fid = info.getString("id");//贴吧id
//            String levelup_score = info.getString("levelup_score");//用户当前贴吧等级经验
//            Logger.d("name", name);
//            Tieba tieba = new Tieba(user.getUid(), name, level, exp, levelup_score);
//            tieba.setFid(fid);
//            tiebas.add(tieba);
//        }


        TiebaSQLiteDao dao = TiebaSQLiteDao.getInstance(mContext);
        dao.addAllTieba(tiebas);

        return true;
    }

    /**
     * 客户端回帖
     *
     * @param user
     * @return 1表示回帖成功，2表示回帖失败
     * @throws IOException
     * @throws JSONException
     */
    public int addPostTiebaWithClient(User user, TiebaPost post) throws IOException, JSONException {
        String bduss = user.getBduss();
        MyCookieJar cookieJar = addBduss(bduss); // cookies中加入bduss参数
        // 获取tbs参数
        Response response = HttpUtils.sendGetRequest("http://tieba.baidu.com/dc/common/tbs", cookieJar);
        String tbs = new JSONObject(response.body().string()).getString("tbs");
        long time = System.currentTimeMillis();
        // 生成POST的参数
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("BDUSS", bduss);
        treeMap.put("_client_id", "wappc_1495515566636_659");
        treeMap.put("_client_type", "2");
        treeMap.put("_client_version", "6.7.0");
        treeMap.put("anonymous", "1");
        treeMap.put("content", "试试看？阿西吧。");
        treeMap.put("fid", post.getTiebaFid());//贴吧id
        treeMap.put("is_ad", "0");
        treeMap.put("kw", post.getTiebaFname());//贴名
        treeMap.put("new_vcode", "1");
        treeMap.put("tbs", tbs);
        treeMap.put("tid", post.getTiebaTid());//帖子id
        treeMap.put("vcode_tag", "11");

        FormBody.Builder builder = new FormBody.Builder();
        StringBuffer sb = new StringBuffer();

        Set<Map.Entry<String, String>> set = treeMap.entrySet();
        for (Map.Entry<String, String> entry : set) {
            builder.add(entry.getKey(), entry.getValue());
            sb.append(entry.getKey() + "=" + entry.getValue());
        }

        String SIGNKEY = "tiebaclient!!!";
        String sign = Md5Encoder.encode(sb.toString() + SIGNKEY);

        RequestBody requestBody = builder.add("sign", sign).build();


        // 执行回帖
        response = HttpUtils.sendPostRequest("http://c.tieba.baidu.com/c/c/post/add", cookieJar, requestBody);
        String json = response.body().string();
        Log.d("post", json);
        JSONObject result = new JSONObject(json);
        String errorCode = result.getString("error_code");
        if (errorCode.equals("0")) {
            return 1;// 返回1表示回帖成功
        } else {//error_code = 5 为需要验证码
            return 2; // 返回2表示回帖错误
        }
    }

    /**
     * 网页回帖
     */
    public void addPostWebTiebaWithClient(User user, TiebaPost tiebaPost) throws IOException, JSONException {
        // cookies中加入bduss参数
        MyCookieJar cookieJar = addBduss(user.getBduss());

        // 获取tbs参数
        Response response = HttpUtils.sendGetRequest("http://tieba.baidu.com/dc/common/tbs", cookieJar);
        String tbs = new JSONObject(response.body().string()).getString("tbs");

        // 签到时需要POST的参数
        RequestBody requestBody = new FormBody.Builder()
                .add("ie", "utf-8")
                .add("kw", tiebaPost.getTiebaFname())
                .add("fid", tiebaPost.getTiebaFid())
                .add("tid", tiebaPost.getTiebaTid())
                .add("floor_num", "18")
                .add("rich_text", "1")
                .add("tbs", tbs)
                .add("content", "瞧一瞧看一看")
                .add("basilisk", "1")
                .add("sign_id", "55941442")
                .add("mouse_pwd", "96,97,101,121,103,100,102,99,97,92,100,121,101,121,100,121,101,121,100,121,101,121,100,121,101,121,100,121,101,92,100,109,108,97,99,92,100,108,103,101,121,100,101,109,101,14962093019850")
                .add("mouse_pwd_t", "1496209301985")
                .add("mouse_pwd_isclick", "0")
                .add("__type__", "reply")
                .build();

        Response response1 = HttpUtils.sendPostRequest("https://tieba.baidu.com/f/commit/post/add", cookieJar, requestBody);

        String json = new String(response1.body().bytes(), "unicode");
        JSONObject result = new JSONObject(json);
        Logger.d("result------------->" + result);
        if (result.getInt("no") == 0) {
            // 签到成功
        } else if (result.getInt("no") == 1101) {
            // 亲，你之前已经签过了

        } else {
            // 签到失败

        }
    }

    public void xiii(Activity activity, User user, TiebaPost post){
        // cookies中加入bduss参数
        MyCookieJar cookieJar = addBduss(user.getBduss());

        // 获取tbs参数
        Response response = null;
        String tbs = "";
        try {
            response = HttpUtils.sendGetRequest("http://tieba.baidu.com/dc/common/tbs", cookieJar);
            tbs = new JSONObject(response.body().string()).getString("tbs");
            Logger.d("tbs--->" + tbs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String uid = user.getUid();
        if (cacheUtils == null)
            cacheUtils = CacheUtils.get(mContext, 1);
        String cookie = cacheUtils.getAsString(uid);
        if (TextUtils.isEmpty(cookie)){
            return;
        }
        Logger.d("cookie---------->" + cookie);
        Ion.with(activity).load("https://tieba.baidu.com/f/commit/post/add")
                .setHeader("Cookie", cookie)
//                .setHeader("Cookie", "PSTM=1434023966; TIEBA_USERTYPE=69b534f871ff7e900c852a00; bdshare_firstime=1462946327440; H_WISE_SIDS=106504_106581_102570_102063_100039_102478_100287_106369_105186_103550_106481_106666_104341_106322_106703_103999_104845_104613_104638_106072_100458; BAIDUID=78E3A6A8C269D10BC99B9B969D6249FC:FG=1; BIDUPSID=DE0A0305A4A07D119655AF8129F5BBA7; IS_NEW_USER=910e14b8c731cf165ae18db6; bottleBubble=1; MCITY=-%3A; 832122980_FRSVideoUploadTip=1; FP_UID=6e04c5f8a1c423888ecb35ae68a97087; 12820827_FRSVideoUploadTip=1; CLIENTWIDTH=891; CLIENTHEIGHT=1920; SEENKW=%E6%88%91%E7%88%B1%E6%AC%A1%E5%8C%85%E5%AD%90%23%E5%94%AF%E6%BB%A1%E4%BE%A0; LASW=1920; baidu_broswer_setup_ä¸¶å\u008C\u0085å\u00AD\u0090é\u0094\u0085=0; rpln_guide=1; mo_originid=2; baidu_broswer_setup_è\u0099\u009Aç\u0081µå\u00AD\u0090=0; bazhu_show=15; NO_UNAME=1; 949828449_FRSVideoUploadTip=1; BDUSS=JkZ2tBUVU0RX5oeX5qUnNzUHJ-N3c5ekFRSlY3aW15SzhURzdWbUtrR1o5VTFaSVFBQUFBJCQAAAAAAAAAAAEAAABkMJkx2Lyw~NfTufgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAJloJlmZaCZZaT")
//                .setHeader("Cookie", "IS_NEW_USER=4324a78ca16854a48aebc326;BAIDU_WISE_UID=wapp_1496217406765_52;BAIDUID=B0FE8B45FA937E64A5E712526379DC9D:FG=1;HOSUPPORT=1;DVID=1496217407115%7Cf1550595-6a14-4415-8e32-e07b71d3bff8;HISTORY=e33ac23a13634750a80c86c20ca811a957ae1a7eab55df64;PTOKEN=deleted;BDUSS=TRqTks5dTMxZUZ2djZUbEc5cXRoMldPTzFvSVZsRXlkRzRLeXZGd080d35CRlpaSVFBQUFBJCQAAAAAAAAAAAEAAABkMJkx2Lyw~NfTufgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAD93Llk~dy5Zb;SAVEUSERID=e0402ff05f86f2c06729e480f38f5579574500de;USERNAMETYPE=2;PTOKEN=5168b06c9bdb1bb41536fbd6b4779280;STOKEN=6da8fee51bd8758e72d12d554966dcb8f8dbae6fe6afda0bde0066623714771c;UBI=fi_PncwhpxZ%7ETaJc0ALgY95qgSG8pGW99pX%7Ep5SGmIhngrJ2-5LEs0E6FseO8Fi6473J1s5VW88aYJEnYxnpr9tFV4AFlqpwtgEhfENhaumIpxzXFAOQ9P03IT0P5LqvX7VQmq-qvYWP50VFyIZte2FGDsOLA__;PASSID=xaii3r")
                .setHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:32.0) Gecko/    20100101 Firefox/32.0")
                .setBodyParameter("ie", "utf-8")
                .setBodyParameter("kw", post.getTiebaFname())
                .setBodyParameter("fid", post.getTiebaFid())
                .setBodyParameter("tid", post.getTiebaTid())
                .setBodyParameter("vcode_md5", "")
                .setBodyParameter("floor_num", "15")
                .setBodyParameter("rich_text", "1")
                .setBodyParameter("tbs", tbs)
                .setBodyParameter("content", PreferencesUtil.getPreferences("content", ""))
                .setBodyParameter("basilisk", "1")
                .setBodyParameter("files", "[]")
                .setBodyParameter("sign_id", "55941442")
                .setBodyParameter("mouse_pwd", "25,25,28,3,30,27,26,25,29,38,30,3,31,3,30,3,31,3,30,3,31,3,30,3,31,3,30,3,31,38,22,26,25,25,31,38,30,22,29,31,3,30,31,23,31,14945769563470")
                .setBodyParameter("mouse_pwd_t", "1494576956347")
                .setBodyParameter("mouse_pwd_isclick", "0")
                .setBodyParameter("__type__", "reply").asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject jsonObject) {
                Logger.d("result" + jsonObject);
            }
        });
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 把bduss参数添加到cookies容器中
     *
     * @param bduss
     * @return 返回一个带有bduss参数的cookies容器
     */
    private MyCookieJar addBduss(String bduss) {
        // 把BDUSS参数添加到cookies中
        MyCookieJar cookieJar = new MyCookieJar();
        Cookie cookie = new Cookie.Builder()
                .name("BDUSS")
                .value(bduss)
                .domain("baidu.com")
                .build();
        cookieJar.cookies.add(cookie);

        return cookieJar;
    }

}
