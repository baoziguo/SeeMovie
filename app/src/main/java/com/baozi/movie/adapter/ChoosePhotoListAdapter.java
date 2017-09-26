package com.baozi.movie.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.baozi.seemovie.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.finalteam.galleryfinal.model.PhotoInfo;

public class ChoosePhotoListAdapter extends BaseAdapter {
    private List<PhotoInfo> mList;
    private LayoutInflater mInflater;
    private ChoosePhotoListItemClickListener mChoosePhotoListItemClickListener;


    public ChoosePhotoListAdapter(Activity activity, List<PhotoInfo> list, ChoosePhotoListItemClickListener choosePhotoListItemClickListener) {
        this.mList = list;
        this.mInflater = LayoutInflater.from(activity);
        mChoosePhotoListItemClickListener = choosePhotoListItemClickListener;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnFail(R.drawable.ic_gf_default_photo)
                .showImageForEmptyUri(R.drawable.ic_gf_default_photo)
                .showImageOnLoading(R.drawable.ic_gf_default_photo).build();

        View view = mInflater.inflate(R.layout.adapter_photo_list_item, null);
        ImageView ivPhoto = (ImageView) view.findViewById(R.id.iv_photo);
        if (position == mList.size() - 1) {
            ivPhoto.setBackgroundResource(R.drawable.act_image_add);
            ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mChoosePhotoListItemClickListener.onClick();
                }
            });
        } else {
            PhotoInfo photoInfo = mList.get(position);
            ImageLoader.getInstance().displayImage("file:/" + photoInfo.getPhotoPath(), ivPhoto, options);
        }
        return ivPhoto;
    }

    public interface ChoosePhotoListItemClickListener{
        void onClick();
    }

}
