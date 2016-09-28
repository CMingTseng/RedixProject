package redix.booxtown.custom;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import redix.booxtown.R;
import redix.booxtown.activity.Faq_content;

/**
 * Created by thuyetpham94 on 25/08/2016.
 */
public class Custom_Listview_faq extends RecyclerView.Adapter<Custom_Listview_faq.RecyclerViewHolder>{

    String[] faqcontent;
    Context context;
    public Custom_Listview_faq(Context context,String[] faqcontent){
        this.faqcontent = faqcontent;
        this.context = context;
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.custom_listview_faq, parent, false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.tv.setText(faqcontent[position]);
    }

    @Override
    public int getItemCount() {
        return faqcontent.length;
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder{

        public TextView tv;
        ImageView img_listview_faq_next;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.txt_content_faq);
            img_listview_faq_next = (ImageView)itemView.findViewById(R.id.img_listview_faq_next);
            Picasso.with(context).load(R.drawable.btn_interact_next).into(img_listview_faq_next);
            img_listview_faq_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, Faq_content.class);
                    context.startActivity(intent);
                }
            });
        }
    }
}


