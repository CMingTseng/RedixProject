package com.booxtown.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.booxtown.api.ServiceGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.booxtown.R;

import com.booxtown.model.Book;
import com.squareup.picasso.Picasso;

/**
 * Created by Administrator on 29/08/2016.
 */
public class CustomPagerAdapter extends PagerAdapter {
    String[] url;
    Context mContext;
    String username;
    LayoutInflater mLayoutInflater;
    Book book;
    public CustomPagerAdapter(Context context,Book book) {
        mContext = context;
        this.book= book;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {

        return book.getPhoto().split(";").length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        try {
            String[] image= book.getPhoto().split(";");
            int index=image[position].indexOf("_+_");
            if(index>0 && image[position].length() >3 ) {
                String img = image[position].substring(index+3, image[position].length());
                Picasso.with(mContext). load(ServiceGenerator.API_BASE_URL+"booxtown/rest/getImage?username=" + image[position].substring(0,index) + "&image=" +  img  + "").placeholder(R.drawable.blank_image).
                        into(imageView);
            }
            else{
                Picasso.with(mContext). load(R.drawable.blank_image).into(imageView);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}