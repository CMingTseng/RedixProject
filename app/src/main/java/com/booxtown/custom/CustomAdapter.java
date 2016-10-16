package com.booxtown.custom;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import com.booxtown.R;

/**
 * Created by thuyetpham94 on 24/08/2016.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.RecyclerViewHolder> {
    String [] result;
    int [] imageId;
    Context ctext;
    public CustomAdapter(String [] result,int [] imageId, Context ct) {
        this.result = result;
        this.imageId = imageId;
        this.ctext=ct;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.program_list, parent, false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.tv.setText(result[position]);
        Picasso.with(ctext).load(imageId[position]).into(holder.img);
//        holder.img.setImageResource(imageId[position]);
        //holder.img.setImageResource(imageId[position]);
        if(position == result.length-1){
            holder.txt_menu_cross.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return result.length;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{

        public TextView tv;
        public ImageView img;
        public TextView txt_menu_cross;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.textView1);
            img = (ImageView) itemView.findViewById(R.id.imageView1);
            txt_menu_cross = (TextView)itemView.findViewById(R.id.txt_menu_cross);
        }
    }

}
