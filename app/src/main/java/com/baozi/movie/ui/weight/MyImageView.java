package com.baozi.movie.ui.weight;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class MyImageView extends ImageView {
	/** 图片宽和高的比例 */
	private float ratio = 1.0f;

	public void setRatio(float ratio) {
		this.ratio = ratio;
	}
	/** 
     * 默认构造函数 
     * @param context 
     */  
    public MyImageView(Context context){  
        super(context);  
    }  
    /** 
     * 该构造方法在静态引入XML文件中是必须的 
     * @param context 
     * @param paramAttributeSet 
     */  
    public MyImageView(Context context,AttributeSet paramAttributeSet){  
        super(context,paramAttributeSet);  
    }
    
	
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	Drawable d = getDrawable();
    	// 拿到图片的宽和高
		int dw = d.getIntrinsicWidth();
		int dh = d.getIntrinsicHeight();
		ratio = dw*1.0f/dh;
		if (ratio < 0.67f){//0.67为1/1.5。。。产品经理说高：宽最大是1.5，所以最大缩放1.5倍
			ratio = 0.67f;
		}
    	// 父容器传过来的宽度方向上的模式
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		// 父容器传过来的高度方向上的模式
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);

		// 父容器传过来的宽度的值
		int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft()
				- getPaddingRight();
		// 父容器传过来的高度的值
		int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingLeft()
				- getPaddingRight();

		if (widthMode == MeasureSpec.EXACTLY
				&& heightMode != MeasureSpec.EXACTLY && ratio != 0.0f) {
			// 判断条件为，宽度模式为Exactly，也就是填充父窗体或者是指定宽度；
			// 且高度模式不是Exaclty，代表设置的既不是fill_parent也不是具体的值，于是需要具体测量
			// 且图片的宽高比已经赋值完毕，不再是0.0f
			// 表示宽度确定，要测量高度
			height = (int) (width / ratio + 0.5f);
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
					MeasureSpec.EXACTLY);
		} else if (widthMode != MeasureSpec.EXACTLY
				&& heightMode == MeasureSpec.EXACTLY && ratio != 0.0f) {
			// 判断条件跟上面的相反，宽度方向和高度方向的条件互换
			// 表示高度确定，要测量宽度
			width = (int) (height * ratio + 0.5f);

			widthMeasureSpec = MeasureSpec.makeMeasureSpec(width,
					MeasureSpec.EXACTLY);
		}

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}