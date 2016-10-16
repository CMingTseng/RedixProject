package com.booxtown.custom;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.booxtown.R;

/**
 * Created by thuyetpham94 on 25/08/2016.
 */
public class Custom_invite extends RecyclerView.Adapter<Custom_invite.RecyclerViewHolder> {
    String [] title;
    public Custom_invite(String [] title){
        this.title = title;
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.custom_listview_content_invite, parent, false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.tv.setText(title[position]);
    }

    @Override
    public int getItemCount() {
        return title.length;
    }
    public class RecyclerViewHolder extends RecyclerView.ViewHolder{

        public TextView tv;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.txt_content_listview_invite);
        }
    }

}