package com.baozi.movie.ui.multi.ui;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.baozi.movie.ui.multi.Info;
import com.baozi.movie.ui.multi.PhotoView;
import com.koushikdutta.ion.Ion;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


class ViewImageAdapter extends PagerAdapter {

    private static final String TAG = "ImageAdapter";
    private Map<String, WeakReference<PhotoView>> mCacheImages;
    private List<String> mImageUrls;
    private Context mContext;
    private Info mInfo;

    public ViewImageAdapter(Info info, List<String> data, Context context) {
        mImageUrls = data;
        mContext = context;
        mCacheImages = new HashMap<>(5);
        mInfo = info;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        String key = mImageUrls.get(position);
        PhotoView image;
        if (mCacheImages.containsKey(key)) {
            image = mCacheImages.get(key).get();
            if (image == null) {
                image = createImage(key, position);
            }
        } else {
            image = createImage(key, position);
        }
        container.addView(image);

        return image;
    }

    public PhotoView createImage(String url, int position) {
        PhotoView image = new PhotoView(mContext);
        image.enable();
//        image.setTransitionName("Animation");
        image.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        image.setAdjustViewBounds(true);
        image.setScaleType(ImageView.ScaleType.FIT_CENTER);
//        image.setImageUrl(url);
//        ImageLoader.getInstance().displayImage(url, image);
        Ion.with(image).load(url);
        if (mInfo != null){
            image.animaFrom(mInfo);
            mInfo = null;
        }
        mCacheImages.put(url, new WeakReference<>(image));
        return image;
    }

    @Override
    public int getCount() {
        return mImageUrls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}