package com.baozi.movie.util;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author 333
 */
public class MagicTextView extends TextView implements MagicScrollView.ScrollListener {
    // view 自身高度
    private int mHeight;
    // view 距离scrollView最顶端高度
    private int locHeight;
    // 递减/递增 的变量值
    private double mRate;
    // view 设置的值
    private double mValue;
    // 当前显示的值
    private double mCurValue;
    // 当前变化后最终状态的目标值
    private double mGalValue;
    // 控制加减法
    private int rate = 1;
    // 当前变化状态(增/减/不变)
    private int mState = 0;
    private boolean refreshing;
    private static final int REFRESH = 1;
    private static final int SCROLL = 2;
    private boolean flag = false;
    // 偏移量 主要用来进行校正距离。
    private static final int OFFSET = 20;
    DecimalFormat fnum = new DecimalFormat("#,##0.00");
    DecimalFormat fnum_1 = new DecimalFormat("#0.00");

    private Handler mHandler = new Handler() {
        public String format;

        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {

                case REFRESH:
                    if (rate * mCurValue < mGalValue) {
                        refreshing = true;
                        if (flag) {
                            setText(fnum.format(mCurValue));
//                            format = fnum.format(mCurValue);
//                            SpannableString styledText = new SpannableString(format);
//                            styledText.setSpan(new TextAppearanceSpan(activity, R.style.style1), format.length()-2, format.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                            setText(styledText, TextView.BufferType.SPANNABLE);
                        } else {
                            setText(fnum_1.format(mCurValue));
//                            format = fnum_1.format(mCurValue);
//                            SpannableString styledText = new SpannableString(format);
//                            styledText.setSpan(new TextAppearanceSpan(activity, R.style.style1), format.length()-2, format.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                            setText(styledText, TextView.BufferType.SPANNABLE);
                        }
                        mCurValue += mRate * rate;
                        mHandler.sendEmptyMessageDelayed(REFRESH, 50);
                    } else {
                        refreshing = false;
                        if (flag) {
                            setText(fnum.format(mGalValue));
//                            format = fnum.format(mCurValue);
//                            SpannableString styledText = new SpannableString(format);
//                            styledText.setSpan(new TextAppearanceSpan(activity, R.style.style1), format.length()-2, format.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                            setText(styledText, TextView.BufferType.SPANNABLE);
                        } else {
                            setText(fnum_1.format(mGalValue));
//                            format = fnum_1.format(mCurValue);
//                            SpannableString styledText = new SpannableString(format);
//                            styledText.setSpan(new TextAppearanceSpan(activity, R.style.style1), format.length()-2, format.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                            setText(styledText, TextView.BufferType.SPANNABLE);
                        }
                    }
                    break;

                case SCROLL:
                    doScroll(msg.arg1, msg.arg2);
                    break;

                default:
                    break;
            }
        }

        ;
    };
    private Activity activity;

    public MagicTextView(Context context) {
        super(context);
    }

    public MagicTextView(Context context, AttributeSet set) {
        super(context, set);
    }

    public MagicTextView(Context context, AttributeSet set, int defStyle) {
        super(context, set, defStyle);
    }

    public void setLocHeight(int height) {
        locHeight = height;
    }

    public void setValue(Activity activity, double value, boolean flag) {
        this.flag = flag;
        this.activity = activity;
        mCurValue = 0.00;
        mGalValue = isShown() ? value : 0.00;
        mValue = value;
        mRate = (double) (mValue / 20.00);
        BigDecimal b = new BigDecimal(mRate);
        mRate = b.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public void setValue(double value, boolean flag) {
        this.flag = flag;
        mCurValue = 0.00;
        mGalValue = isShown() ? value : 0.00;
        mValue = value;
        mRate = (double) (mValue / 20.00);
        BigDecimal b = new BigDecimal(mRate);
        mRate = b.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    @Override
    public void onScrollChanged(int state, int scroll) {
        Message msg = mHandler.obtainMessage();
        msg.what = SCROLL;
        msg.arg1 = state;
        msg.arg2 = scroll;
        mHandler.sendMessage(msg);
    }

    private void doScroll(int state, int scroll) {
        if (mState == state && refreshing)
            return;

        mState = state;
        if (doMinus(scroll)) {
            rate = -1;
            mGalValue = 0;
        } else if (doPlus(scroll)) {
            rate = 1;
            mGalValue = mValue;
        }
        mHandler.sendEmptyMessage(REFRESH);
    }

    private boolean doPlus(int scroll) {
//		 if (isShown() && (scroll + PayActivity.mWinheight > locHeight +
//		 OFFSET)
//		 && (mState == MagicScrollView.UP))
//		 return true;
//		 if (isShown() && (scroll < locHeight) && mState ==
//		 MagicScrollView.DOWN)
//		 return true;

        return false;
    }

    private boolean doMinus(int scroll) {
        // if (isShown() && (scroll > locHeight) && (mState ==
        // MagicScrollView.UP))
        // return true;
        //
        // if (isShown() && (scroll + PayActivity.mWinheight - mHeight <
        // locHeight - OFFSET)
        // && (mState == MagicScrollView.DOWN))
        // return true;

        return false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 执行完onMeasure后即可获得view的宽度
        mHeight = getMeasuredHeight();
    }
}
