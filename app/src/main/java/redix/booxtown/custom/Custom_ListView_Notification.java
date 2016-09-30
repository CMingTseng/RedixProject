package redix.booxtown.custom;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import redix.booxtown.R;
import redix.booxtown.listener.OnLoadMoreListener;
import redix.booxtown.model.Notification;
import redix.booxtown.model.Topic;

/**
 * Created by thuyetpham94 on 25/08/2016.
 */
public class Custom_ListView_Notification extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Notification> list;
    Context context;
    RecyclerView getLvRecyclerView;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private OnLoadMoreListener mOnLoadMoreListener;

    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    RecyclerView lvRecyclerView;
    public Custom_ListView_Notification(Context context,List<Notification> list,RecyclerView lvRecyclerView) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.list = list;
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
        return list.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.custom_listview_notification, parent, false);
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
            Notification notification = list.get(position);
            if (notification.getIs_read() == 0 ){
                ((RecyclerViewHolder) holder).tv.setTextColor(Color.RED);
                list.get(position).setIs_read(1);
            }else {
                ((RecyclerViewHolder) holder).tv.setTextColor(Color.BLACK);
                list.get(position).setIs_read(0);
            }
            ((RecyclerViewHolder) holder).tv.setText(notification.getTitle_notifi());
            ((RecyclerViewHolder) holder).tv_content.setText(notification.getCreate_date());


        }else if(holder instanceof LoadingViewHolder){
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
        return list.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{

        TextView tv;
        TextView tv_content;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.txt_title_notification);
            tv_content = (TextView)itemView.findViewById(R.id.txt_content_notification);
        }
    }

    public List<Notification> getlist(){
        return list;
    }

}
