package com.booxtown.custom;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import com.booxtown.R;

/**
 * Created by Administrator on 27/08/2016.
 */
public class CustomTabbarExplore {

    private LinearLayout linear_all;
    private LinearLayout linear_swap;
    private LinearLayout linear_free;
    private LinearLayout linear_cart;

    private TextView txt_all;
    private TextView txt_all_count;
    private ImageView img_swap;
    private TextView  txt_swap_count;
    private ImageView img_free;
    private TextView txt_free_count;
    private ImageView imge_cart;
    private TextView txt_cart_count;

    View view;
    Context ct;
    public CustomTabbarExplore(View view, Context ct){
        this.ct = ct;
        this.view= view;

        linear_all=(LinearLayout) view.findViewById(R.id.linear_all);
        linear_swap=(LinearLayout) view.findViewById(R.id.linear_swap);
        linear_free=(LinearLayout) view.findViewById(R.id.linear_free);
        linear_cart=(LinearLayout) view.findViewById(R.id.linear_cart);

        txt_all=(TextView) view.findViewById(R.id.tab_all);
        txt_all_count=(TextView) view.findViewById(R.id.tab_all_count);

        img_swap=(ImageView) view.findViewById(R.id.tab_swap);
        txt_swap_count=(TextView) view.findViewById(R.id.tab_swap_count);

        img_free=(ImageView) view.findViewById(R.id.tab_free);
        txt_free_count=(TextView) view.findViewById(R.id.tab_free_count);

        imge_cart=(ImageView) view.findViewById(R.id.tab_cart);
        txt_cart_count=(TextView) view.findViewById(R.id.tab_cart_count);
        setDefault(1);
    }


    public void setDefault(int type){
        if(type==1){
            linear_all.setBackgroundColor(view.getResources().getColor(R.color.dot_light_screen1));
            txt_all.setTextColor(view.getResources().getColor(R.color.color_text));
            txt_all_count.setTextColor(view.getResources().getColor(R.color.color_text));

            linear_swap.setBackgroundColor(view.getResources().getColor(R.color.color_text));
            Picasso.with(ct).load(R.drawable.explore_btn_swap_not_active).into(img_swap);
//            img_swap.setImageResource(R.drawable.explore_btn_swap_not_active);
            txt_swap_count.setTextColor(view.getResources().getColor(R.color.color_text_hint));

            linear_free.setBackgroundColor(view.getResources().getColor(R.color.color_text));
            Picasso.with(ct).load(R.drawable.explore_btn_free_not_active).into(img_free);
//            img_free.setImageResource(R.drawable.explore_btn_free_not_active);
            txt_free_count.setTextColor(view.getResources().getColor(R.color.color_text_hint));

            linear_cart.setBackgroundColor(view.getResources().getColor(R.color.color_text));
            Picasso.with(ct).load(R.drawable.explore_btn_buy_not_active).into(imge_cart);
//            imge_cart.setImageResource(R.drawable.explore_btn_buy_not_active);
            txt_cart_count.setTextColor(view.getResources().getColor(R.color.color_text_hint));

        }
        else if(type==2){
            linear_all.setBackgroundColor(view.getResources().getColor(R.color.color_text));
            txt_all.setTextColor(view.getResources().getColor(R.color.dot_light_screen1));
            txt_all_count.setTextColor(view.getResources().getColor(R.color.color_text_hint));

            linear_swap.setBackgroundColor(view.getResources().getColor(R.color.dot_light_screen1));
            Picasso.with(ct).load(R.drawable.explore_btn_swap_active).into(img_swap);
//            img_swap.setImageResource(R.drawable.explore_btn_swap_active);
            txt_swap_count.setTextColor(view.getResources().getColor(R.color.color_text));

            linear_free.setBackgroundColor(view.getResources().getColor(R.color.color_text));
            Picasso.with(ct).load(R.drawable.explore_btn_free_not_active).into(img_free);
//            img_free.setImageResource(R.drawable.explore_btn_free_not_active);
            txt_free_count.setTextColor(view.getResources().getColor(R.color.color_text_hint));

            linear_cart.setBackgroundColor(view.getResources().getColor(R.color.color_text));
            Picasso.with(ct).load(R.drawable.explore_btn_buy_not_active).into(imge_cart);
//            imge_cart.setImageResource(R.drawable.explore_btn_buy_not_active);
            txt_cart_count.setTextColor(view.getResources().getColor(R.color.color_text_hint));
        }
        else if(type==3){
            linear_all.setBackgroundColor(view.getResources().getColor(R.color.color_text));
            txt_all.setTextColor(view.getResources().getColor(R.color.dot_light_screen1));
            txt_all_count.setTextColor(view.getResources().getColor(R.color.color_text_hint));

            linear_swap.setBackgroundColor(view.getResources().getColor(R.color.color_text));
            Picasso.with(ct).load(R.drawable.explore_btn_swap_not_active).into(img_swap);
//            img_swap.setImageResource(R.drawable.explore_btn_swap_not_active);
            txt_swap_count.setTextColor(view.getResources().getColor(R.color.color_text_hint));

            linear_free.setBackgroundColor(view.getResources().getColor(R.color.dot_light_screen1));
            Picasso.with(ct).load(R.drawable.explore_btn_free_active).into(img_free);
//            img_free.setImageResource(R.drawable.explore_btn_free_active);
            txt_free_count.setTextColor(view.getResources().getColor(R.color.color_text));

            linear_cart.setBackgroundColor(view.getResources().getColor(R.color.color_text));
            Picasso.with(ct).load(R.drawable.explore_btn_buy_not_active).into(imge_cart);
//            imge_cart.setImageResource(R.drawable.explore_btn_buy_not_active);
            txt_cart_count.setTextColor(view.getResources().getColor(R.color.color_text_hint));
        }
        else {
            linear_all.setBackgroundColor(view.getResources().getColor(R.color.color_text));
            txt_all.setTextColor(view.getResources().getColor(R.color.dot_light_screen1));
            txt_all_count.setTextColor(view.getResources().getColor(R.color.color_text_hint));

            linear_swap.setBackgroundColor(view.getResources().getColor(R.color.color_text));
            Picasso.with(ct).load(R.drawable.explore_btn_swap_not_active).into(img_swap);
            txt_swap_count.setTextColor(view.getResources().getColor(R.color.color_text_hint));

            linear_free.setBackgroundColor(view.getResources().getColor(R.color.color_text));
            Picasso.with(ct).load(R.drawable.explore_btn_free_not_active).into(img_free);
            txt_free_count.setTextColor(view.getResources().getColor(R.color.color_text_hint));

            linear_cart.setBackgroundColor(view.getResources().getColor(R.color.dot_light_screen1));
            Picasso.with(ct).load(R.drawable.explore_btn_buy_active).into(imge_cart);
            txt_cart_count.setTextColor(view.getResources().getColor(R.color.color_text));
        }
    }

}
