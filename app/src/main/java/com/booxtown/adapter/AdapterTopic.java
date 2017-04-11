package com.booxtown.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.booxtown.R;

import com.booxtown.model.Topic;

/**
 * Created by Administrator on 27/08/2016.
 */
public class AdapterTopic extends RecyclerView.Adapter<AdapterTopic.HolderTopic> {
    Context context;
    private List<Topic> listTopic;

    public AdapterTopic(Context context, List<Topic> listTopic) {
        this.listTopic = listTopic;
        this.context = context;
    }
    @Override
    public HolderTopic onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_interact, null);
        HolderTopic rcv = new HolderTopic(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(HolderTopic holder, int position) {
        Topic interact = listTopic.get(position);
        Picasso.with(context).load(R.drawable.btn_interact_next).into(holder.imageView);

        holder.txt_title_interact.setText(interact.getTitle());
        holder.txt_count_interact.setText("(" + interact.getNum_thread() + ")");

        if (interact.getIs_read() == 0) {
            holder.txt_count_interact.setTextColor(context.getResources().getColor(R.color.color_text));
        } else {
            holder.txt_count_interact.setTextColor(context.getResources().getColor(R.color.color_topic_interact));
        }
        try {
            SharedPreferences pref = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
            String timeZone = pref.getString("timezone", null);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
            Date dt= dateFormat.parse(interact.getUpdate_date());
            String intMonth = (String) android.text.format.DateFormat.format("MM", dt);
            String year = (String) android.text.format.DateFormat.format("yyyy", dt);
            String day = (String) android.text.format.DateFormat.format("dd", dt);
            String date_post= day+"-"+intMonth+"-"+year;

            holder.txt_dateUpdate_interact.setText(date_post);

        }catch (Exception exx) {


        }
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return listTopic.size();
    }

    class HolderTopic extends RecyclerView.ViewHolder{
        public TextView txt_title_interact;
        public TextView txt_count_interact;
        public TextView txt_dateUpdate_interact;
        ImageView imageView;
        public HolderTopic(View itemView) {
            super(itemView);
            txt_title_interact = (TextView) itemView.findViewById(R.id.txt_title_interact);
            txt_count_interact = (TextView) itemView.findViewById(R.id.txt_count_interact);
            txt_dateUpdate_interact = (TextView) itemView.findViewById(R.id.txt_time_update_interact);
            imageView = (ImageView)itemView.findViewById(R.id.imageView_next_interac);
        }
    }
}
