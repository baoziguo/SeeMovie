package com.baozi.movie.baidutiebaauto.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.baozi.movie.baidutiebaauto.model.Tieba;
import com.baozi.movie.baidutiebaauto.model.User;
import com.baozi.movie.bean.TiebaPost;


public class TiebaSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "tieba.db";
    private static final int VERSION = 1;

    // 用户表中的数据
    public static final String TABLE_USER = "user";
    public static final String COLUMN_USER_UID = "uid";
    public static final String COLUMN_USER_NAME = "name";
    public static final String COLUMN_USER_BDUSS = "bduss";

    // 贴吧表中的数据
    public static final String TABLE_TIEBA = "tieba";
    public static final String COLUMN_TIEBA_UID = "uid";
    public static final String COLUMN_TIEBA_FID = "fid";
    public static final String COLUMN_TIEBA_NAME = "forum_name";
    public static final String COLUMN_TIEBA_LEVEL = "user_level";
    public static final String COLUMN_TIEBA_EXP = "user_exp";
    public static final String COLUMN_TIEBA_LEVELUP_SCORE = "levelup_score";
    public static final String COLUMN_TIEBA_AVATAR = "avatar";
    public static final String COLUMN_TIEBA_STATUS = "status";

    //帖子表中的数据
    public static final String TABLE_POST = "post";
    public static final String COLUMN_POST_TID = "tieba_tid";
    public static final String COLUMN_POST_FID = "tieba_fid";
    public static final String COLUMN_POST_FNAME = "tieba_fname";
    public static final String COLUMN_POST_URL = "jump_url";
    public static final String COLUMN_POST_TITLE = "title";

    // 创建用户表
    private final String CREATE_USER = "create table user (" +
            "_id integer primary key autoincrement," +
            "uid text not null," +
            "name text not null," +
            "bduss text not null" +
            ")";

    // 创建贴吧表
    private final String CREATE_TIEBA = "create table tieba (" +
            "_id integer primary key autoincrement," +
            "uid text references user(uid)," + // 外键约束
            "fid text," +
            "forum_name text," +
            "user_level text," +
            "user_exp text," +
            "levelup_score text," +
            "avatar text," +
            "status integer" +
            ")";

    // 创建帖子表
    private final String CREATE_POST = "create table post (" +
            "_id integer primary key autoincrement," +
            "tieba_tid text," +
            "tieba_fid text," +
            "tieba_fname text," +
            "jump_url text," +
            "title text" +
            ")";

    // 创建触发器
    private final String CREATE_TRIGGER = "create trigger delete_user after delete on user " +
            "begin " +
            "delete from tieba where uid = old.uid;" +
            "end";


    public TiebaSQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_TIEBA);
        db.execSQL(CREATE_TRIGGER);
        db.execSQL(CREATE_POST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 查询所有百度账户
     *
     * @return
     */
    public Cursor queryAllUser() {
        return getWritableDatabase().query(TABLE_USER, null, null, null, null, null, null);
    }

    /**
     * 查询所有帖子
     *
     * @return
     */
    public Cursor queryAllPost() {
        return getWritableDatabase().query(TABLE_POST, null, null, null, null, null, null);
    }

    /**
     * 查询单个账户
     *
     * @param uid
     * @return
     */
    public Cursor queryUser(String uid) {
        return getWritableDatabase().query(TABLE_USER, null, COLUMN_USER_UID + "=?", new String[]{uid}, null, null, null);
    }

    /**
     * 往数据库中插入百度账户
     *
     * @param user
     * @return
     */
    public long insertUser(User user) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_UID, user.getUid());
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_BDUSS, user.getBduss());
        return getWritableDatabase().insert(TABLE_USER, null, values);
    }

    public int updateUser(User user) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_BDUSS, user.getBduss());
        return getWritableDatabase().update(TABLE_USER, values, COLUMN_USER_UID + "=?", new String[]{user.getUid()});
    }

    /**
     * 删除账户
     *
     * @param user
     * @return
     */
    public int deleteUser(User user) {
        return getWritableDatabase().delete(TABLE_USER, COLUMN_USER_UID + "=?", new String[]{user.getUid()});
    }


    /**
     * 查询某个账户已关注的贴吧
     *
     * @param uid
     * @return
     */
    public Cursor queryTieba(String uid) {
        return getWritableDatabase().query(TABLE_TIEBA, null, COLUMN_TIEBA_UID + "=?", new String[]{uid}, null, null, null);
    }

    /**
     * 查询贴吧是否存在
     *
     * @param tieba
     * @return
     */
    public Cursor queryTieba(Tieba tieba) {
        String uid = tieba.getUid();
        String name = tieba.getName();
        return getWritableDatabase().query(TABLE_TIEBA, null, COLUMN_TIEBA_UID + "=? and " + COLUMN_TIEBA_NAME + "=?", new String[]{uid, name}, null, null, null);
    }

    /**
     * 查询帖子是否存在
     *
     * @param tiebaPost
     * @return
     */
    public Cursor queryPost(TiebaPost tiebaPost) {
        String tiebaTid = tiebaPost.getTiebaTid();
        return getWritableDatabase().query(TABLE_POST, null, COLUMN_POST_TID + "=?", new String[]{tiebaTid}, null, null, null);
    }

    /**
     * 往数据库中插入关注的贴吧
     *
     * @param tieba
     * @return
     */
    public long insertTieba(Tieba tieba) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TIEBA_UID, tieba.getUid());
        values.put(COLUMN_TIEBA_FID, tieba.getFid());
        values.put(COLUMN_TIEBA_NAME, tieba.getName());
        values.put(COLUMN_TIEBA_LEVEL, tieba.getLevel());
        values.put(COLUMN_TIEBA_EXP, tieba.getExp());
        values.put(COLUMN_TIEBA_LEVELUP_SCORE, tieba.getLevelup_score());
        values.put(COLUMN_TIEBA_AVATAR, tieba.getAvatar());
        values.put(COLUMN_TIEBA_STATUS, tieba.isStatus());
        return getWritableDatabase().insert(TABLE_TIEBA, null, values);
    }

    /**
     * 往数据库中插入添加的帖子
     *
     * @param tiebaPost
     * @return
     */
    public long insertPost(TiebaPost tiebaPost) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_POST_TID, tiebaPost.getTiebaTid());
        values.put(COLUMN_POST_FID, tiebaPost.getTiebaFid());
        values.put(COLUMN_POST_FNAME, tiebaPost.getTiebaFname());
        values.put(COLUMN_POST_URL, tiebaPost.getJump_url());
        values.put(COLUMN_POST_TITLE, tiebaPost.getTitle());
        return getWritableDatabase().insert(TABLE_POST, null, values);
    }

    /**
     * 更新贴吧信息
     *
     * @param tiebaPost
     * @return
     */
    public int updatePost(TiebaPost tiebaPost) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_POST_TID, tiebaPost.getTiebaTid());
        values.put(COLUMN_POST_FID, tiebaPost.getTiebaFid());
        values.put(COLUMN_POST_FNAME, tiebaPost.getTiebaFname());
        values.put(COLUMN_POST_URL, tiebaPost.getJump_url());
        values.put(COLUMN_POST_TITLE, tiebaPost.getTitle());
        return getWritableDatabase().update(TABLE_POST, values, COLUMN_POST_TID + "=?", new String[]{tiebaPost.getTiebaTid()});
    }

    /**
     * 更新贴吧信息
     *
     * @param tieba
     * @return
     */
    public int updateTieba(Tieba tieba) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TIEBA_FID, tieba.getFid());
        values.put(COLUMN_TIEBA_LEVEL, tieba.getLevel());
        values.put(COLUMN_TIEBA_EXP, tieba.getExp());
        values.put(COLUMN_TIEBA_LEVELUP_SCORE, tieba.getLevelup_score());
        values.put(COLUMN_TIEBA_AVATAR, tieba.getAvatar());
        values.put(COLUMN_TIEBA_STATUS, tieba.isStatus());
        return getWritableDatabase().update(TABLE_TIEBA, values, COLUMN_TIEBA_UID + "=? and " + COLUMN_TIEBA_NAME + "=?", new String[]{tieba.getUid(), tieba.getName()});
    }

    /**
     * 删除某个贴吧
     *
     * @param tieba
     * @return
     */
    public int deleteTieba(Tieba tieba) {
        return getWritableDatabase().delete(TABLE_TIEBA, COLUMN_TIEBA_UID + "=? and " + COLUMN_TIEBA_NAME + "=?", new String[]{tieba.getUid(), tieba.getName()});
    }

    /**
     * 删除某个帖子
     *
     * @param tiebaPost
     * @return
     */
    public int deletePost(TiebaPost tiebaPost) {
        return getWritableDatabase().delete(TABLE_POST, COLUMN_POST_TID + "=?", new String[]{tiebaPost.getTiebaTid()});
    }

}
