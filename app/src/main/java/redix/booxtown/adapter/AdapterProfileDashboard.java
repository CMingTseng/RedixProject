package redix.booxtown.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import redix.booxtown.R;
import redix.booxtown.model.DashBoard;

/**
 * Created by thuyetpham94 on 29/08/2016.
 */
public class AdapterProfileDashboard extends BaseAdapter {

    Context context;
    List<DashBoard> dashBoards;
    int user_id;
    private static LayoutInflater inflater=null;
    public AdapterProfileDashboard(Context context,List<DashBoard> dashBoards,int user_id) {
        // TODO Auto-generated constructor stub
        this.context=context;
        this.dashBoards = dashBoards;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.user_id = user_id;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return dashBoards.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        ImageView img_offer;
        ImageView img_status;
        RatingBar ratingBar_dashboard;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.custom_listview_profile_dashboard, null);
        holder.tv=(TextView) rowView.findViewById(R.id.txt_listview_profiledashboard);
        holder.img_offer = (ImageView)rowView.findViewById(R.id.img_listview_profiledashboard_offer);
        holder.img_status = (ImageView)rowView.findViewById(R.id.img_listview_profiledashboard_status);
        holder.ratingBar_dashboard = (RatingBar)rowView.findViewById(R.id.ratingBar_dashboard);
        DashBoard dashBoard = dashBoards.get(position);
        holder.tv.setText(dashBoard.getBook_seller());

        //xử lý offer
        if(dashBoard.getAction().equals("swap")){
            Glide.with(context).load(R.drawable.myprofile_swap).into(holder.img_offer);
        }else if (dashBoard.getAction().equals("buy") && dashBoard.getUser_seller_id() == user_id){
            Glide.with(context).load(R.drawable.myprofile_buy_back).into(holder.img_offer);
        }else if (dashBoard.getAction().equals("buy") && dashBoard.getUser_buyer_id() == user_id){
            Glide.with(context).load(R.drawable.myprofile_buy_come).into(holder.img_offer);
        }else if (dashBoard.getAction().equals("free") && dashBoard.getUser_seller_id() == user_id){
            Glide.with(context).load(R.drawable.myprofile_free_back).into(holder.img_offer);
        }else if (dashBoard.getAction().equals("free") && dashBoard.getUser_buyer_id() == user_id){
            Glide.with(context).load(R.drawable.myprofile_free_come).into(holder.img_offer);
        }

        //xử lý status
        if (dashBoard.getIs_accept() == 1){
            Glide.with(context).load(R.drawable.myprofile_tick).into(holder.img_status);
        }else if(dashBoard.getIs_cancel() == 1){
            Glide.with(context).load(R.drawable.myprofile_all_not).into(holder.img_status);
        }else if(dashBoard.getIs_reject() == 0 && dashBoard.getIs_cancel()==0 && dashBoard.getIs_accept()==0){
            Glide.with(context).load(R.drawable.myprofile_not).into(holder.img_status);
        }

        holder.ratingBar_dashboard.setRating(dashBoard.getRating());

        if(position %2==0){
            rowView.setBackgroundColor(ContextCompat.getColor(context, R.color.color_myprofile_list_databroad));
        }else{
            rowView.setBackgroundColor(ContextCompat.getColor(context, R.color.color_myprofile_list_databroad));
        }
        return rowView;
    }
}