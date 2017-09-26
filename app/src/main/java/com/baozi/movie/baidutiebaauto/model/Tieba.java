package com.baozi.movie.baidutiebaauto.model;

import android.os.Parcel;
import android.os.Parcelable;


public class Tieba implements Parcelable {
    private String mUid; // 对应的百度账号的uid;
    private String mName; // 贴吧名
    private String mLevel; // 所在贴吧等级
    private String mExp; // 经验值（experience的缩写）
    private String mFid; // forum id的缩写，这个安卓项目里暂时没有用到，可能以后需要用到，先放着
    private String levelup_score; // 当前等级经验总值
    private String avatar; // 贴吧头像
    private boolean mStatus; // 今日是否签到

    public Tieba(String uid, String name, String level, String exp, String levelup_score) {
        mUid = uid;
        mName = name;
        mLevel = level;
        mExp = exp;
        this.levelup_score = levelup_score;
    }

    protected Tieba(Parcel in) {
        mUid = in.readString();
        mName = in.readString();
        mLevel = in.readString();
        mExp = in.readString();
        mFid = in.readString();
        levelup_score = in.readString();
        avatar = in.readString();
        mStatus = in.readByte() != 0;
    }

    public static final Creator<Tieba> CREATOR = new Creator<Tieba>() {
        @Override
        public Tieba createFromParcel(Parcel in) {
            return new Tieba(in);
        }

        @Override
        public Tieba[] newArray(int size) {
            return new Tieba[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mUid);
        dest.writeString(mName);
        dest.writeString(mLevel);
        dest.writeString(mExp);
        dest.writeString(mFid);
        dest.writeString(levelup_score);
        dest.writeString(avatar);
        dest.writeByte((byte) (mStatus ? 1 : 0));
    }

    public String getUid() {
        return mUid;
    }

    public void setUid(String uid) {
        mUid = uid;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getLevel() {
        return mLevel;
    }

    public void setLevel(String level) {
        mLevel = level;
    }

    public String getExp() {
        return mExp;
    }

    public void setExp(String exp) {
        mExp = exp;
    }

    public String getFid() {
        return mFid;
    }

    public void setFid(String fid) {
        mFid = fid;
    }

    public String getLevelup_score() {
        return levelup_score;
    }

    public void setLevelup_score(String levelup_score) {
        this.levelup_score = levelup_score;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isStatus() {
        return mStatus;
    }

    public void setStatus(boolean status) {
        mStatus = status;
    }

}
