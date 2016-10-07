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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import java.util.List;

import redix.booxtown.R;
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
    ListView lv_recycler;
    //List<Topic> listemp = new ArrayList<>();
    AdapterTopic interact;
    ArrayList<Topic> listArrayList = new ArrayList<>();
    boolean userScrolled = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.interact_fragment, container, false);

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

        //-----------------------------------------------------------
        lv_recycler=(ListView) view.findViewById(R.id.list_view_interact);
        populatRecyclerView(session_id);
        implementScrollListener(session_id);
        return view;
    }

    private void populatRecyclerView(final String session_id) {
        topicSync getDashBoard = new topicSync(getContext(),session_id,6,0);
        getDashBoard.execute();
        listArrayList = new ArrayList<Topic>();
        interact = new AdapterTopic(getActivity(), listArrayList);
        // set adapter over recyclerview
        lv_recycler.setAdapter(interact);
        interact.notifyDataSetChanged();
        lv_recycler.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Topic topic = listArrayList.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("thread", topic);
                ThreadFragment fragment= new ThreadFragment();
                fragment.setArguments(bundle);
                callFragment(fragment);
                if(topic.getIs_read() == 0) {
                    changeStatus changeStatus = new changeStatus(getContext(), session_id, Integer.valueOf(topic.getId()));
                    changeStatus.execute();
                }
            }
        });
    }

    private void implementScrollListener(final String session_id) {

        lv_recycler.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView arg0, int scrollState) {
                // If scroll state is touch scroll then set userScrolled
                // true
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    userScrolled = true;

                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // Now check if userScrolled is true and also check if
                // the item is end then update list view and set
                // userScrolled to false
                int a = view.getLastVisiblePosition();
                if (userScrolled
                        && firstVisibleItem + visibleItemCount == totalItemCount) {
                    userScrolled = false;
                    Topic dashBoard_lv = listArrayList.get(listArrayList.size()-1);
                    topicSync getDashBoard = new topicSync(getContext(),session_id,6,Integer.valueOf(dashBoard_lv.getId()));
                    getDashBoard.execute();
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
                    //set adapter
                    listArrayList.addAll(topics);
                    interact.notifyDataSetChanged();
                    //end
                }else {
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
