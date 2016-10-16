package com.booxtown.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.booxtown.activity.ListingCollectionActivity;
import com.booxtown.activity.ListingsDetailActivity;
import com.booxtown.model.Explore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.booxtown.R;

/**
 * Created by Administrator on 29/08/2016.
 */
public class AdapterListings extends BaseAdapter {
    private Context mContext;
    private ArrayList<Explore> listExplore;


    public AdapterListings(Context c, ArrayList<Explore> list_explores) {
        mContext = c;
        this.listExplore = list_explores;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listExplore.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return listExplore.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Explore ex= listExplore.get(position);
        Hoder hoder = new Hoder();
        convertView = inflater.inflate(R.layout.custom_gridview_listings, null);
        hoder.txt_title_book = (TextView) convertView.findViewById(R.id.txt_title_book_listings);
        hoder.txt_author_book = (TextView) convertView.findViewById(R.id.txt_author_book_listings);
        hoder.txt_price_book=(TextView) convertView.findViewById(R.id.txt_price_listings);

        hoder.img_book = (ImageView)convertView.findViewById(R.id.img_book);
        hoder.img_swap = (ImageView)convertView.findViewById(R.id.img_explore_swap_listings);
        hoder.img_free = (ImageView)convertView.findViewById(R.id.img_explore_free_listings);
        hoder.img_buy = (ImageView)convertView.findViewById(R.id.img_explore_buy_listing);
        hoder.img_edit = (ImageView)convertView.findViewById(R.id.img_listings_edit);
        if(position%2==0){
            Picasso.with(mContext).load(R.drawable.img_temp1).into(hoder.img_book);
            hoder.txt_title_book.setText("The Las Painting of Sara de Vos");
            hoder.txt_author_book.setText("buy Gandalf");
        }
        else
        {
            Picasso.with(mContext).load(R.drawable.img_temp2).into(hoder.img_book);
            hoder.txt_title_book.setText("Gandalf the first");
            hoder.txt_author_book.setText("buy Ptit");
        }
        if(!ex.isBuy()) {
            hoder.txt_price_book.setVisibility(View.INVISIBLE);
        }
        if(ex.isSwap()){
            Picasso.with(mContext).load(R.drawable.explore_btn_swap_active).into(hoder.img_buy);
        }
        else {
            Picasso.with(mContext).load(R.drawable.explore_btn_swap_dis_active).into(hoder.img_swap);

        }
        if(ex.isFree()){
            Picasso.with(mContext).load(R.drawable.explore_btn_free_active).into(hoder.img_buy);
        }
        else {
            Picasso.with(mContext).load(R.drawable.explore_btn_free_dis_active).into(hoder.img_free);
        }
        if(ex.isBuy()){
            Picasso.with(mContext).load(R.drawable.listing_btn_buy).into(hoder.img_buy);
        }
        else {
            Picasso.with(mContext).load(R.drawable.explore_btn_buy_dis_active).into(hoder.img_buy);
        }

        Picasso.with(mContext).load(R.drawable.listing_btn_edit).into(hoder.img_edit);

        hoder.img_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(mContext, ListingsDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("type","1");
            }
        });
        hoder.img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Explore book = (Explore) listExplore.get(position);
                Log.d("boooook",String.valueOf(book.getPrice_book()));
                Bundle bundle = new Bundle();
                bundle.putString("activity","edit");
                bundle.putSerializable("book",book);
                ListingCollectionActivity listingCollectionActivity = new ListingCollectionActivity();
                listingCollectionActivity.setArguments(bundle);
                callFragment(listingCollectionActivity);
            }
        });


        hoder.img_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListingsDetailActivity fragment = new ListingsDetailActivity();
                Bundle bundle = new Bundle();
                bundle.putString(String.valueOf(R.string.valueListings),"2");
                fragment.setArguments(bundle);
                callFragment(fragment);
            }
        });


        return convertView;
    }
    public void callFragment(Fragment fragment ){
        FragmentManager manager = ((AppCompatActivity) mContext).getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //Khi được goi, fragment truyền vào sẽ thay thế vào vị trí FrameLayout trong Activity chính
        transaction.replace(R.id.frame_main_all, fragment);
        transaction.commit();
    }

    public class Hoder{

        TextView txt_title_book ;
        TextView txt_author_book ;
        TextView txt_price_book;

        ImageView img_book ;
        ImageView img_swap;
        ImageView img_free;
        ImageView img_buy ;
        ImageView img_edit ;

    }
}
