package redix.booxtown.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import redix.booxtown.R;
import redix.booxtown.activity.Faq_content;

/**
 * Created by thuyetpham94 on 25/08/2016.
 */
public class AdapterInvite extends RecyclerView.Adapter<AdapterInvite.RecyclerViewHolder>{

    List<String> invitecontent;
    Context context;
    public AdapterInvite(Context context, List<String> invitecontent){
        this.invitecontent = invitecontent;
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
        holder.tv.setText(invitecontent.get(position));
    }

    @Override
    public int getItemCount() {
        return invitecontent.size();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder{

        public TextView tv;
        ImageView img_listview_faq_next;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.txt_content_faq);
        }
    }
}


