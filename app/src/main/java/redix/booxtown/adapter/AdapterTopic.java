package redix.booxtown.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import redix.booxtown.R;
import redix.booxtown.controller.TopicController;
import redix.booxtown.fragment.ThreadFragment;
import redix.booxtown.fragment.TopicFragment;
import redix.booxtown.listener.OnLoadMoreListener;
import redix.booxtown.model.Interact;
import redix.booxtown.model.Topic;
import redix.booxtown.recyclerclick.RecyclerItemClickListener;

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
            String[] dates = interact.getCreate_date().substring(0, 10).split("-");
            String resultDate = dates[2] +"-"+dates[1] +"-"+dates[0].substring(2,dates[0].length());
            holder.txt_dateUpdate_interact.setText("Last Updated on " + resultDate);

        }catch (Exception exx) {
            holder.txt_dateUpdate_interact.setText("Last Updated on ");

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
