package redix.booxtown.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import redix.booxtown.R;
import redix.booxtown.controller.ThreadController;
import redix.booxtown.controller.TopicController;
import redix.booxtown.listener.OnLoadMoreListener;
import redix.booxtown.model.Thread;
import redix.booxtown.model.Topic;

/**
 * Created by thuyetpham94 on 16/09/2016.
 */
public class AdapterThread extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    private List<Thread> listThread;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private OnLoadMoreListener mOnLoadMoreListener;

    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    RecyclerView lvRecyclerView;
    public AdapterThread(Context context, List<Thread> listThread,RecyclerView lvRecyclerView) {
        this.listThread = listThread;
        this.context = context;
        this.lvRecyclerView = lvRecyclerView;
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) lvRecyclerView.getLayoutManager();
        lvRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });

    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        return listThread.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.custom_interact, parent, false);
            return new RecyclerViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_loading_item, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecyclerViewHolder) {
            final Thread thread= listThread.get(position);
            ((RecyclerViewHolder) holder).txt_title_interact.setText(listThread.get(position).getTitle());
            ((RecyclerViewHolder) holder).txt_dateUpdate_interact.setText("Added by "+ listThread.get(position).getUsername());
            ((RecyclerViewHolder) holder).txt_count_interact.setText("("+listThread.get(position).getNum_comment()+")");
            if(listThread.get(position).getIs_read()==0){
                ((RecyclerViewHolder) holder).txt_count_interact.setTextColor(context.getResources().getColor(R.color.color_text));
            }

//        holder.txt_title_interact.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharedPreferences pref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
//                String session_id = pref.getString("session_id", null);
//                ThreadSync changeStatus = new ThreadSync(context, session_id, Integer.parseInt(thread.getId()));
//                changeStatus.execute();
//            }
//        });
        }else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }


    public void setLoaded() {
        isLoading = false;
    }
    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
        }
    }

    @Override
    public int getItemCount() {
        return listThread == null ? 0:listThread.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{

        public TextView txt_title_interact;
        public TextView txt_count_interact;
        public TextView txt_dateUpdate_interact;
        ImageView imageView;
        RelativeLayout topic_interact;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            topic_interact=(RelativeLayout) itemView.findViewById(R.id.topic_interact);
            txt_title_interact = (TextView) itemView.findViewById(R.id.txt_title_interact);
            txt_count_interact = (TextView) itemView.findViewById(R.id.txt_count_interact);
            txt_dateUpdate_interact = (TextView) itemView.findViewById(R.id.txt_time_update_interact);
            imageView = (ImageView)itemView.findViewById(R.id.imageView_next_interac);
            Picasso.with(context).load(R.drawable.btn_interact_next).into(imageView);
        }
    }


}
