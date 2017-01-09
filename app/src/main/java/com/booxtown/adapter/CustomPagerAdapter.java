package com.booxtown.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
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

import java.io.IOException;

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



                final String imageLink= ServiceGenerator.API_BASE_URL+"booxtown/rest/getImage?username=" + image[position].substring(0,index) + "&image=" +  img  + "";
                try {
                    GetWithHeight getWithHeight= new GetWithHeight(mContext,imageLink,imageView);
                    getWithHeight.execute();

                }catch (Exception exx){
                    String err= exx.getMessage();
                }
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
    class GetWithHeight extends AsyncTask<Bitmap,Void,Bitmap> {

        Context context;
        String imageUrl;
        ImageView img_book;
        public GetWithHeight(Context context, String imageUrl,ImageView img_book){
            this.context = context;
            this.imageUrl=imageUrl;
            this.img_book=img_book;
        }

        @Override
        protected Bitmap doInBackground(Bitmap... Bitmap) {
            Bitmap downloadedImage = null;
            try {
                downloadedImage = Picasso.with(context).load(imageUrl).get();
                return downloadedImage;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Bitmap downloadedImage) {
            try {
                int width = downloadedImage.getWidth();
                int height = downloadedImage.getHeight();
                if (width> height) {
                    img_book.setRotation(90f);
                    img_book.setImageBitmap(downloadedImage);
                    /*Picasso.with(mContext).load(imageUrl).rotate(90f).placeholder(R.drawable.blank_image).
                            into(img_book);*/
                } else {
                    img_book.setImageBitmap(downloadedImage);
                    /*Picasso.with(mContext).load(imageUrl).placeholder(R.drawable.blank_image).
                            into(img_book);*/
                }
            }catch (Exception e){}
        }
    }
}