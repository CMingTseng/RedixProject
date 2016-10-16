package com.booxtown.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.booxtown.model.Wishboard;
import com.bumptech.glide.Glide;

import java.util.List;

import com.booxtown.R;
import com.booxtown.activity.RespondActivity;

/**
 * Created by thuyetpham94 on 30/08/2016.
 */
public class AdapterListviewWishboard extends RecyclerView.Adapter<AdapterListviewWishboard.HolderWisboard> {

    //    String [] title;
//    String [] name;
//    String [] date;
    List<Wishboard> list;
    Context context;
    private static LayoutInflater inflater = null;

    public AdapterListviewWishboard(Context context, List<Wishboard> list) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.list = list;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public HolderWisboard onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.custom_listview_wishboard, parent, false);
        return new HolderWisboard(itemView);
    }

    @Override
    public void onBindViewHolder(HolderWisboard holder, final int position) {
        if (list.get(position).getTitle().length() == 0) {
            if (list.get(position).getAuthor().length() == 0) {
                holder.title.setText(list.get(position).getComment());
            } else {
                holder.title.setText(list.get(position).getComment());
                holder.name.setText("by " + list.get(position).getAuthor());
            }
        } else {
            String title = list.get(position).getTitle().substring(0,1).toUpperCase()+ list.get(position).getTitle().substring(1,list.get(position).getTitle().length());
            holder.title.setText(title);
            holder.name.setText("by " + list.get(position).getAuthor());
        }
        try {
            String[] dates = list.get(position).getCreate_date().substring(0, 10).split("-");
            String resultDate = dates[2] + "-" + dates[1] + "-" + dates[0].substring(2, dates[0].length());
            holder.date.setText(resultDate);
        } catch (Exception exx) {
            holder.date.setText(list.get(position).getCreate_date().substring(0, 10));
        }

//        Bitmap btn1 = BitmapFactory.decodeResource(context.getResources(),R.drawable.btn_wishbroad_message);
//        holder.imgv_listview_respond.setImageBitmap(btn1);
        Glide.with(context).load(R.drawable.btn_wishbroad_message).into(holder.imgv_listview_respond);

        holder.imgv_listview_respond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RespondActivity.class);
                intent.putExtra("wishboard", list.get(position));
                context.startActivity(intent);
            }
        });

        if (position % 2 == 0) {
            holder.bg_liner.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_user_empty));
        } else {
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
        return list.size();
    }


    class HolderWisboard extends RecyclerView.ViewHolder{
        TextView title;
        TextView name;
        TextView date;
        RelativeLayout bg_liner;
        ImageView imgv_listview_respond;
        public HolderWisboard(View itemView) {
            super(itemView);
            bg_liner = (RelativeLayout)itemView.findViewById(R.id.relativeLayout_wishboard);
            title=(TextView) itemView.findViewById(R.id.txt_title_lisview_wishboard);
            name = (TextView)itemView.findViewById(R.id.txt_name_custom_listview_wishboard);
            date = (TextView)itemView.findViewById(R.id.txt_date_customlistview_wishboard);
            imgv_listview_respond = (ImageView)itemView.findViewById(R.id.imgv_listview_respond);
        }
    }
}