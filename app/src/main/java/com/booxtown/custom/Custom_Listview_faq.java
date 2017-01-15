package com.booxtown.custom;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.booxtown.model.Faq;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import com.booxtown.R;
import com.booxtown.activity.Faq_content;

/**
 * Created by thuyetpham94 on 25/08/2016.
 */
public class Custom_Listview_faq extends RecyclerView.Adapter<Custom_Listview_faq.RecyclerViewHolder>{

    List<String> faqcontent;
    Context context;
    ArrayList<Faq> faqs;
    int faq_id;
    public Custom_Listview_faq(Context context, List<String> faqcontent, ArrayList<Faq> faqs){
        this.faqcontent = faqcontent;
        this.context = context;
        this.faqs = faqs;

    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.custom_listview_faq, parent, false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
        final int index= position;
        holder.tv.setText(faqcontent.get(position));
        holder.layout_faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Faq_content.class);
                intent.putExtra("faq",faqs);
                intent.putExtra("faq_id",faqcontent.get(index));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return faqcontent.size();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder{

        public TextView tv;
        ImageView img_listview_faq_next;
        RelativeLayout layout_faq;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.txt_content_faq);
            img_listview_faq_next = (ImageView)itemView.findViewById(R.id.img_listview_faq_next);
            Picasso.with(context).load(R.drawable.btn_interact_next).into(img_listview_faq_next);
            layout_faq=(RelativeLayout) itemView.findViewById(R.id.layout_faq);
        }
    }
}


