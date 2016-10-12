package redix.booxtown.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import redix.booxtown.R;
import redix.booxtown.model.DashBoard;

/**
 * Created by thuyetpham94 on 29/08/2016.
 */
public class AdapterProfileDashboard extends RecyclerView.Adapter<AdapterProfileDashboard.RecyclerViewHolder> {

    Context context;
    List<DashBoard> dashBoards;
    int user_id;
    public AdapterProfileDashboard(Context context,List<DashBoard> dashBoards,int user_id) {
        // TODO Auto-generated constructor stub
        this.context=context;
        this.dashBoards = dashBoards;
        this.user_id = user_id;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.custom_listview_profile_dashboard, parent, false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        DashBoard dashBoard = dashBoards.get(position);
        holder.tv.setText(dashBoard.getBook_seller());
        //xử lý offer
        if(dashBoard.getAction().equals("swap")){
//            Bitmap btn1 = BitmapFactory.decodeResource(context.getResources(),R.drawable.myprofile_swap);
//            holder.img_offer.setImageBitmap(btn1);

            Glide.with(context).load(R.drawable.myprofile_swap).into(holder.img_offer);
        }else if (dashBoard.getAction().equals("buy") && dashBoard.getUser_seller_id() == user_id){
//            Bitmap btn1 = BitmapFactory.decodeResource(context.getResources(),R.drawable.myprofile_buy_back);
//            holder.img_offer.setImageBitmap(btn1);

            Glide.with(context).load(R.drawable.myprofile_buy_back).into(holder.img_offer);
        }else if (dashBoard.getAction().equals("buy") && dashBoard.getUser_buyer_id() == user_id){
//            Bitmap btn1 = BitmapFactory.decodeResource(context.getResources(),R.drawable.myprofile_buy_come);
//            holder.img_offer.setImageBitmap(btn1);

            Glide.with(context).load(R.drawable.myprofile_buy_come).into(holder.img_offer);
        }else if (dashBoard.getAction().equals("free") && dashBoard.getUser_seller_id() == user_id){
//            Bitmap btn1 = BitmapFactory.decodeResource(context.getResources(),R.drawable.myprofile_free_back);
//            holder.img_offer.setImageBitmap(btn1);

            Glide.with(context).load(R.drawable.myprofile_free_back).into(holder.img_offer);
        }else if (dashBoard.getAction().equals("free") && dashBoard.getUser_buyer_id() == user_id){
//            Bitmap btn1 = BitmapFactory.decodeResource(context.getResources(),R.drawable.myprofile_free_come);
//            holder.img_offer.setImageBitmap(btn1);

            Glide.with(context).load(R.drawable.myprofile_free_come).into(holder.img_offer);
        }

        //xử lý status
        if (dashBoard.getIs_accept() == 1){
//            Bitmap btn1 = BitmapFactory.decodeResource(context.getResources(),R.drawable.myprofile_tick);
//            holder.img_status.setImageBitmap(btn1);
            holder.ratingBar_dashboard.setVisibility(View.VISIBLE);

            Glide.with(context).load(R.drawable.myprofile_tick).into(holder.img_status);
        }else if(dashBoard.getIs_cancel() == 1|| dashBoard.getIs_reject() == 1){
//            Bitmap btn1 = BitmapFactory.decodeResource(context.getResources(),R.drawable.myprofile_all_not);
//            holder.img_status.setImageBitmap(btn1);
            holder.ratingBar_dashboard.setVisibility(View.VISIBLE);
            Glide.with(context).load(R.drawable.myprofile_all_not).into(holder.img_status);
        }else if(dashBoard.getIs_reject() == 0 && dashBoard.getIs_cancel()==0 && dashBoard.getIs_accept()==0){
//            Bitmap btn1 = BitmapFactory.decodeResource(context.getResources(),R.drawable.myprofile_not);
//            holder.img_status.setImageBitmap(btn1);
            holder.ratingBar_dashboard.setVisibility(View.INVISIBLE);
            Glide.with(context).load(R.drawable.myprofile_not).into(holder.img_status);
        }

        try {
            if(user_id == dashBoard.getUser_buyer_id())
            {
                holder.ratingBar_dashboard.setRating((dashBoard.getUser_promp() + dashBoard.getUser_cour()
                + dashBoard.getUser_quality()) / (float)3);
            }
            else
            {
                holder.ratingBar_dashboard.setRating((dashBoard.getUser_promp_seller() + dashBoard.getUser_cour_seller()
                        + dashBoard.getUser_quality_seller()) / (float)3);
            }
        }catch (Exception e)
        {}



        LayerDrawable stars = (LayerDrawable) holder.ratingBar_dashboard.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.rgb(247,182,0), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(context.getResources().getColor(R.color.color_topic_interact), PorterDuff.Mode.SRC_ATOP);
        DrawableCompat.setTint(DrawableCompat.wrap(stars.getDrawable(1)),context.getResources().getColor(R.color.color_topic_interact));

        if(position %2==0){
            holder.bg_liner.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_user_empty));

        }else{
            holder.bg_liner.setBackgroundColor(ContextCompat.getColor(context, R.color.dot_light_screen1));
        }
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public int getItemCount() {
        return dashBoards.size();
    }
    public class RecyclerViewHolder extends RecyclerView.ViewHolder{
        LinearLayout bg_liner;
        TextView tv;
        ImageView img_offer;
        ImageView img_status;
        RatingBar ratingBar_dashboard;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            bg_liner = (LinearLayout)itemView.findViewById(R.id.bg_liner);
            tv=(TextView) itemView.findViewById(R.id.txt_listview_profiledashboard);
            img_offer = (ImageView)itemView.findViewById(R.id.img_listview_profiledashboard_offer);
            img_status = (ImageView)itemView.findViewById(R.id.img_listview_profiledashboard_status);
            ratingBar_dashboard = (RatingBar)itemView.findViewById(R.id.ratingBar_dashboard);
        }
    }
}