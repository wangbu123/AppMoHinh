package com.example.appmohinh.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.appmohinh.Model.photoBanner;
import com.example.appmohinh.R;

import java.util.List;

public class PhotoBannerAdapter extends PagerAdapter {
    private Context mContext;
    private List<photoBanner> list;

    public PhotoBannerAdapter(Context mContext, List<photoBanner> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_banner,container,false);
        ImageView img  = view.findViewById(R.id.img_banner);
        photoBanner photoBanner = list.get(position);
        if(photoBanner!=null){
            Glide.with(mContext).load(photoBanner.getResourceId()).into(img);
        }
        container.addView(view);
        return view;

    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;

    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
