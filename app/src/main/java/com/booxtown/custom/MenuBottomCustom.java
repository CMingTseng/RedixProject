package com.booxtown.custom;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.booxtown.fragment.MainFragment;
import com.booxtown.fragment.MyProfileFragment;
import com.booxtown.fragment.TopicFragment;
import com.booxtown.fragment.WishboardFragment;

import com.booxtown.R;

import com.booxtown.fragment.ListingsFragment;

/*
 * Created by Administrator on 26/08/2016.
 */
public class MenuBottomCustom{
    private ImageView btn_location;
    private ImageView btn_commnet;
    private ImageView btn_camera;
    private ImageView btn_bag;
    private ImageView btn_user;
    Context context;
    private int type;

    public MenuBottomCustom(View view, Context ct, final int type) {
        this.context=ct;
        this.type=type;
        btn_location = (ImageView) view.findViewById(R.id.img_menu_bottom_location);
        btn_commnet = (ImageView) view.findViewById(R.id.img_menu_bottom_comment);
        btn_camera = (ImageView) view.findViewById(R.id.img_menu_bottom_camera);
        btn_bag = (ImageView) view.findViewById(R.id.img_menu_bottom_bag);
        btn_user = (ImageView) view.findViewById(R.id.img_menu_bottom_user);

        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_location.setImageResource(R.drawable.icon_menu_bottom_location);
                if(type!=1){
                    Intent itent = new Intent(context, MainFragment.class);
                    context.startActivity(itent);
                }
                setDefaut(1);
            }
        });

        btn_commnet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type!=2){
                    Intent itent = new Intent(context, TopicFragment.class);
                    context.startActivity(itent);
                }
                setDefaut(2);
            }
        });

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(type!=3){
                    Intent itent = new Intent(context, ListingsFragment.class);
                    context.startActivity(itent);
                }
                setDefaut(3);
            }
        });
        btn_bag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type!=4){

                    Intent itent = new Intent(context, WishboardFragment.class);
                    context.startActivity(itent);
                }
                setDefaut(4);

            }
        });
        btn_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type!=5){
                    Intent itent = new Intent(context, MyProfileFragment.class);
                    context.startActivity(itent);
                }
                setDefaut(5);
            }
        });


    }


    public void setDefaut(int i){
            //set icon tab
        if(i==0) {
            btn_commnet.setImageResource(R.drawable.btn_locate_interact_not_active);
            btn_location.setImageResource(R.drawable.btn_locate_not_active);
            btn_camera.setImageResource(R.drawable.btn_locate_listing_not_active);
            btn_bag.setImageResource(R.drawable.btn_locate_wishbroad_not_active);
            btn_user.setImageResource(R.drawable.btn_locate_profile_not_active);
//            Glide.with(MainAllActivity.this).load(R.drawable.btn_locate_interact_not_active).diskCacheStrategy(DiskCacheStrategy.ALL).into(btn_commnet);
//            Glide.with(MainAllActivity.this).load(R.drawable.btn_locate_not_active).diskCacheStrategy(DiskCacheStrategy.ALL).into(btn_location);
//            Glide.with(MainAllActivity.this).load(R.drawable.btn_locate_listing_not_active).diskCacheStrategy(DiskCacheStrategy.ALL).into(btn_camera);
//            Glide.with(MainAllActivity.this).load(R.drawable.btn_locate_wishbroad_not_active).diskCacheStrategy(DiskCacheStrategy.ALL).into(btn_bag);
//            Glide.with(MainAllActivity.this).load(R.drawable.btn_locate_active).diskCacheStrategy(DiskCacheStrategy.ALL).into(btn_user);
        }
        else if(i==1) {
            btn_commnet.setImageResource(R.drawable.btn_locate_interact_not_active);
            btn_location.setImageResource(R.drawable.btn_locate_active);
            btn_camera.setImageResource(R.drawable.btn_locate_listing_not_active);
            btn_bag.setImageResource(R.drawable.btn_locate_wishbroad_not_active);
            btn_user.setImageResource(R.drawable.btn_locate_profile_not_active);

//            Glide.with(MainAllActivity.this).load(R.drawable.btn_locate_interact_not_active).diskCacheStrategy(DiskCacheStrategy.ALL).into(btn_commnet);
//            Glide.with(MainAllActivity.this).load(R.drawable.btn_locate_active).diskCacheStrategy(DiskCacheStrategy.ALL).into(btn_location);
//            Glide.with(MainAllActivity.this).load(R.drawable.btn_locate_listing_not_active).diskCacheStrategy(DiskCacheStrategy.ALL).into(btn_camera);
//            Glide.with(MainAllActivity.this).load(R.drawable.btn_locate_wishbroad_not_active).diskCacheStrategy(DiskCacheStrategy.ALL).into(btn_bag);
//            Glide.with(MainAllActivity.this).load(R.drawable.btn_locate_profile_not_active).diskCacheStrategy(DiskCacheStrategy.ALL).into(btn_bag);
        }
        else if(i==2) {
            btn_commnet.setImageResource(R.drawable.btn_locate_interact_active);
            btn_location.setImageResource(R.drawable.btn_locate_not_active);
            btn_camera.setImageResource(R.drawable.btn_locate_listing_not_active);
            btn_bag.setImageResource(R.drawable.btn_locate_wishbroad_not_active);
            btn_user.setImageResource(R.drawable.btn_locate_profile_not_active);


//            Glide.with(MainAllActivity.this).load(R.drawable.btn_locate_interact_active).diskCacheStrategy(DiskCacheStrategy.ALL).into(btn_commnet);
//            Glide.with(MainAllActivity.this).load(R.drawable.btn_locate_not_active).diskCacheStrategy(DiskCacheStrategy.ALL).into(btn_location);
//            Glide.with(MainAllActivity.this).load(R.drawable.btn_locate_listing_not_active).diskCacheStrategy(DiskCacheStrategy.ALL).into(btn_camera);
//            Glide.with(MainAllActivity.this).load(R.drawable.btn_locate_wishbroad_not_active).diskCacheStrategy(DiskCacheStrategy.ALL).into(btn_bag);
//            Glide.with(MainAllActivity.this).load(R.drawable.btn_locate_profile_not_active).diskCacheStrategy(DiskCacheStrategy.ALL).into(btn_user);
        }
        else if(i==3) {
            btn_commnet.setImageResource(R.drawable.btn_locate_interact_not_active);
            btn_location.setImageResource(R.drawable.btn_locate_not_active);
            btn_camera.setImageResource(R.drawable.btn_locate_listing_active);
            btn_bag.setImageResource(R.drawable.btn_locate_wishbroad_not_active);
            btn_user.setImageResource(R.drawable.btn_locate_profile_not_active);

//            Glide.with(MainAllActivity.this).load(R.drawable.btn_locate_interact_not_active).diskCacheStrategy(DiskCacheStrategy.ALL).into(btn_commnet);
//            Glide.with(MainAllActivity.this).load(R.drawable.btn_locate_not_active).diskCacheStrategy(DiskCacheStrategy.ALL).into(btn_location);
//            Glide.with(MainAllActivity.this).load(R.drawable.btn_locate_listing_active).diskCacheStrategy(DiskCacheStrategy.ALL).into(btn_camera);
//            Glide.with(MainAllActivity.this).load(R.drawable.btn_locate_wishbroad_not_active).diskCacheStrategy(DiskCacheStrategy.ALL).into(btn_bag);
//            Glide.with(MainAllActivity.this).load(R.drawable.btn_locate_profile_not_active).diskCacheStrategy(DiskCacheStrategy.ALL).into(btn_user);
        }
        else if(i==4) {
            btn_commnet.setImageResource(R.drawable.btn_locate_interact_not_active);
            btn_location.setImageResource(R.drawable.btn_locate_not_active);
            btn_camera.setImageResource(R.drawable.btn_locate_listing_not_active);
            btn_bag.setImageResource(R.drawable.btn_locate_wishbroad_active);
            btn_user.setImageResource(R.drawable.btn_locate_profile_not_active);

//            Glide.with(MainAllActivity.this).load(R.drawable.btn_locate_interact_not_active).diskCacheStrategy(DiskCacheStrategy.ALL).into(btn_commnet);
//            Glide.with(MainAllActivity.this).load(R.drawable.btn_locate_not_active).diskCacheStrategy(DiskCacheStrategy.ALL).into(btn_location);
//            Glide.with(MainAllActivity.this).load(R.drawable.btn_locate_listing_not_active).diskCacheStrategy(DiskCacheStrategy.ALL).into(btn_camera);
//            Glide.with(MainAllActivity.this).load(R.drawable.btn_locate_wishbroad_active).diskCacheStrategy(DiskCacheStrategy.ALL).into(btn_bag);
//            Glide.with(MainAllActivity.this).load(R.drawable.btn_locate_profile_not_active).diskCacheStrategy(DiskCacheStrategy.ALL).into(btn_user);
        }
        else {
            btn_commnet.setImageResource(R.drawable.btn_locate_interact_not_active);
            btn_location.setImageResource(R.drawable.btn_locate_not_active);
            btn_camera.setImageResource(R.drawable.btn_locate_listing_not_active);
            btn_bag.setImageResource(R.drawable.btn_locate_wishbroad_not_active);
            btn_user.setImageResource(R.drawable.btn_locate_profile_active);
//            Glide.with(MainAllActivity.this).load(R.drawable.btn_locate_interact_not_active).diskCacheStrategy(DiskCacheStrategy.ALL).into(btn_commnet);
//            Glide.with(MainAllActivity.this).load(R.drawable.btn_locate_not_active).diskCacheStrategy(DiskCacheStrategy.ALL).into(btn_location);
//            Glide.with(MainAllActivity.this).load(R.drawable.btn_locate_listing_not_active).diskCacheStrategy(DiskCacheStrategy.ALL).into(btn_camera);
//            Glide.with(MainAllActivity.this).load(R.drawable.btn_locate_wishbroad_not_active).diskCacheStrategy(DiskCacheStrategy.ALL).into(btn_bag);
//            Glide.with(MainAllActivity.this).load(R.drawable.btn_locate_profile_active).diskCacheStrategy(DiskCacheStrategy.ALL).into(btn_user);

        }

    }
}
