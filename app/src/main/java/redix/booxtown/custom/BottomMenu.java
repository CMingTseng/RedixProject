package redix.booxtown.custom;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import redix.booxtown.R;

/**
 * Created by Administrator on 10/09/2016.
 */
public class BottomMenu {
    Context context;
    private ImageView btn_location;
    private ImageView btn_commnet;
    private ImageView btn_camera;
    private ImageView btn_bag;
    private ImageView btn_user;
    public BottomMenu(Context context){
        this.context=context;
    }

    public void setDefaut(int i){
        //set icon tab
        if(i==0) {
            Picasso.with(context).load(R.drawable.btn_locate_interact_not_active).into(btn_commnet);
            Picasso.with(context).load(R.drawable.btn_locate_not_active).into(btn_location);
            Picasso.with(context).load(R.drawable.btn_locate_listing_not_active).into(btn_camera);
            Picasso.with(context).load(R.drawable.btn_locate_wishbroad_not_active).into(btn_bag);
            Picasso.with(context).load(R.drawable.btn_locate_profile_not_active).into(btn_user);
        }
        else if(i==1) {
            Picasso.with(context).load(R.drawable.btn_locate_interact_not_active).into(btn_commnet);
            Picasso.with(context).load(R.drawable.btn_locate_active).into(btn_location);
            Picasso.with(context).load(R.drawable.btn_locate_listing_not_active).into(btn_camera);
            Picasso.with(context).load(R.drawable.btn_locate_wishbroad_not_active).into(btn_bag);
            Picasso.with(context).load(R.drawable.btn_locate_profile_not_active).into(btn_user);
//            btn_commnet.setImageResource(R.drawable.btn_locate_interact_not_active);
//            btn_location.setImageResource(R.drawable.btn_locate_active);
//            btn_camera.setImageResource(R.drawable.btn_locate_listing_not_active);
//            btn_bag.setImageResource(R.drawable.btn_locate_wishbroad_not_active);
//            btn_user.setImageResource(R.drawable.btn_locate_profile_not_active);
        }
        else if(i==2) {
            Picasso.with(context).load(R.drawable.btn_locate_interact_active).into(btn_commnet);
            Picasso.with(context).load(R.drawable.btn_locate_not_active).into(btn_location);
            Picasso.with(context).load(R.drawable.btn_locate_listing_not_active).into(btn_camera);
            Picasso.with(context).load(R.drawable.btn_locate_wishbroad_not_active).into(btn_bag);
            Picasso.with(context).load(R.drawable.btn_locate_profile_not_active).into(btn_user);

//            btn_commnet.setImageResource(R.drawable.btn_locate_interact_active);
//            btn_location.setImageResource(R.drawable.btn_locate_not_active);
//            btn_camera.setImageResource(R.drawable.btn_locate_listing_not_active);
//            btn_bag.setImageResource(R.drawable.btn_locate_wishbroad_not_active);
//            btn_user.setImageResource(R.drawable.btn_locate_profile_not_active);
        }
        else if(i==3) {
            Picasso.with(context).load(R.drawable.btn_locate_interact_not_active).into(btn_commnet);
            Picasso.with(context).load(R.drawable.btn_locate_not_active).into(btn_location);
            Picasso.with(context).load(R.drawable.btn_locate_listing_active).into(btn_camera);
            Picasso.with(context).load(R.drawable.btn_locate_wishbroad_not_active).into(btn_bag);
            Picasso.with(context).load(R.drawable.btn_locate_profile_not_active).into(btn_user);

//            btn_commnet.setImageResource(R.drawable.btn_locate_interact_not_active);
//            btn_location.setImageResource(R.drawable.btn_locate_not_active);
//            btn_camera.setImageResource(R.drawable.btn_locate_listing_active);
//            btn_bag.setImageResource(R.drawable.btn_locate_wishbroad_not_active);
//            btn_user.setImageResource(R.drawable.btn_locate_profile_not_active);
        }
        else if(i==4) {
            Picasso.with(context).load(R.drawable.btn_locate_interact_not_active).into(btn_commnet);
            Picasso.with(context).load(R.drawable.btn_locate_not_active).into(btn_location);
            Picasso.with(context).load(R.drawable.btn_locate_listing_not_active).into(btn_camera);
            Picasso.with(context).load(R.drawable.btn_locate_wishbroad_active).into(btn_bag);
            Picasso.with(context).load(R.drawable.btn_locate_profile_not_active).into(btn_user);

//            btn_commnet.setImageResource(R.drawable.btn_locate_interact_not_active);
//            btn_location.setImageResource(R.drawable.btn_locate_not_active);
//            btn_camera.setImageResource(R.drawable.btn_locate_listing_not_active);
//            btn_bag.setImageResource(R.drawable.btn_locate_wishbroad_active);
//            btn_user.setImageResource(R.drawable.btn_locate_profile_not_active);
        }
        else {
            Picasso.with(context).load(R.drawable.btn_locate_interact_not_active).into(btn_commnet);
            Picasso.with(context).load(R.drawable.btn_locate_not_active).into(btn_location);
            Picasso.with(context).load(R.drawable.btn_locate_listing_not_active).into(btn_camera);
            Picasso.with(context).load(R.drawable.btn_locate_wishbroad_not_active).into(btn_bag);
            Picasso.with(context).load(R.drawable.btn_locate_profile_active).into(btn_user);

//            btn_commnet.setImageResource(R.drawable.btn_locate_interact_not_active);
//            btn_location.setImageResource(R.drawable.btn_locate_not_active);
//            btn_camera.setImageResource(R.drawable.btn_locate_listing_not_active);
//            btn_bag.setImageResource(R.drawable.btn_locate_wishbroad_not_active);
//            btn_user.setImageResource(R.drawable.btn_locate_profile_active);
        }

    }
}
