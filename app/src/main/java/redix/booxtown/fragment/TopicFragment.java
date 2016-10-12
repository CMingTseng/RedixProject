package redix.booxtown.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import java.util.List;

import redix.booxtown.R;
import redix.booxtown.activity.MainAllActivity;
import redix.booxtown.controller.Information;
import redix.booxtown.controller.TopicController;

import redix.booxtown.model.Topic;

import redix.booxtown.activity.MenuActivity;
import redix.booxtown.adapter.AdapterTopic;

/**
 * Created by Administrator on 27/08/2016.
 */
public class TopicFragment extends Fragment
{

    //List<Topic> listtopic= new ArrayList<>();
    RecyclerView rv_recycler;
    LinearLayoutManager linearLayoutManager;
    //List<Topic> listemp = new ArrayList<>();
    AdapterTopic interact;
    ArrayList<Topic> listArrayList = new ArrayList<>();
    boolean loading = true,
            isLoading = true;
    private int previousTotal = 0;
    private int visibleThreshold = 5;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.interact_fragment, container, false);

        TextView title=(TextView) getActivity().findViewById(R.id.txt_title);
        title.setText("Interact");

        ImageView imageView_back=(ImageView) getActivity().findViewById(R.id.img_menu);
        Picasso.with(getContext()).load(R.drawable.btn_menu_locate).into(imageView_back);

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref",Context.MODE_PRIVATE);
        String session_id = pref.getString("session_id", null);
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MenuActivity.class);
                startActivity(intent);
            }
        });
        MainAllActivity.setTxtTitle("Interact");
        //-----------------------------------------------------------
        rv_recycler=(RecyclerView) view.findViewById(R.id.list_view_interact);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rv_recycler.setLayoutManager(linearLayoutManager);

        populatRecyclerView(session_id);
        implementScrollListener(session_id);
        return view;
    }

    private void populatRecyclerView(final String session_id) {
        try {
            topicSync getDashBoard = new topicSync(getContext(), session_id, 20, 0);
            getDashBoard.execute();
            if (listArrayList.size() == 0) {
                interact = new AdapterTopic(getActivity(), listArrayList);
                rv_recycler.setAdapter(interact);
            } else {
                interact.notifyDataSetChanged();
            }

            rv_recycler.addOnItemTouchListener(new redix.booxtown.recyclerclick.RecyclerItemClickListener(getActivity(),
                    new redix.booxtown.recyclerclick.RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Topic topic = listArrayList.get(position);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("thread", topic);
                            ThreadFragment fragment = new ThreadFragment();
                            fragment.setArguments(bundle);
                            callFragment(fragment);
                            if (topic.getIs_read() == 0) {
                                changeStatus changeStatus = new changeStatus(getContext(), session_id, Integer.valueOf(topic.getId()));
                                changeStatus.execute();
                            }
                        }
                    }));
        }catch (Exception e){}
    }

    private void implementScrollListener(final String thread_id) {
        rv_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = recyclerView.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold) && isLoading) {
                    // End has been reached
                    Topic commentBook= listArrayList.get(listArrayList.size()-1);
                    topicSync getcomment = new topicSync(getContext(),thread_id,20,Integer.parseInt(commentBook.getId()));
                    getcomment.execute();
                    // Do something
                    loading = true;
                }
            }
        });
    }


    public void callFragment(Fragment fragment ){
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.frame_main_all, fragment);
        transaction.commit();
    }

    public class topicSync extends AsyncTask<Void,Void,List<Topic>>{
        ProgressDialog dialog;
        Context context;
        String session_id;
        int top, from;
        public topicSync(Context context,String session_id,int top, int from){
            this.context = context;
            this.session_id=session_id;
            this.top=top;
            this.from=from;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.setMessage(Information.noti_dialog);
            dialog.setIndeterminate(true);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected List<Topic> doInBackground(Void... params) {
            TopicController topicController = new TopicController();
            return topicController.getALllTopicTop(session_id,top,from);
        }

        @Override
        protected void onPostExecute(final List<Topic> topics) {
            try{
                if(topics.size() >0){
                    isLoading = true;
                    //set adapter
                    listArrayList.addAll(topics);
                    interact.notifyDataSetChanged();
                    //end
                }else {
                    isLoading = false;
                }
            }catch (Exception e){
            }
            dialog.dismiss();
        }
    }

    public class changeStatus extends AsyncTask<Void,Void,Boolean> {
        ProgressDialog dialog;
        Context context;
        String session_id;
        int thread_id;
        public changeStatus(Context context,String session_id,int thread_id){
            this.context = context;
            this.session_id=session_id;
            this.thread_id=thread_id;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.setMessage("Please wait...");
            dialog.setIndeterminate(true);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            TopicController topicController = new TopicController();
            return topicController.changeStatusTopic(session_id,thread_id);
        }

        @Override
        protected void onPostExecute(final Boolean topics) {
            try{
                if(topics){
                }
            }catch (Exception e){
            }
            dialog.dismiss();
        }
    }
}
