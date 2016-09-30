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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import redix.booxtown.R;
import redix.booxtown.controller.Information;
import redix.booxtown.controller.TopicController;
import redix.booxtown.listener.OnLoadMoreListener;
import redix.booxtown.model.Topic;
import redix.booxtown.recyclerclick.RecyclerItemClickListener;
import redix.booxtown.activity.MenuActivity;
import redix.booxtown.adapter.AdapterTopic;

/**
 * Created by Administrator on 27/08/2016.
 */
public class TopicFragment extends Fragment
{

    List<Topic> listtopic= new ArrayList<>();
    RecyclerView lv_recycler;
    List<Topic> listemp = new ArrayList<>();
    AdapterTopic interact;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.interact_fragment, container, false);

        ImageView imageView_back=(ImageView) getActivity().findViewById(R.id.img_menu);
        Picasso.with(getContext()).load(R.drawable.btn_menu_locate).into(imageView_back);
//        imageView_back.setImageResource(R.drawable.btn_menu_locate);

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref",Context.MODE_PRIVATE);
        String session_id = pref.getString("session_id", null);
        topicSync getalltopic = new topicSync(getContext(),session_id,100,0);
        getalltopic.execute();
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MenuActivity.class);
                startActivity(intent);
            }
        });

        //-----------------------------------------------------------
        lv_recycler=(RecyclerView) view.findViewById(R.id.list_view_interact);
        RecyclerView.LayoutManager  layoutManager = new LinearLayoutManager(getActivity());
        lv_recycler.setLayoutManager(layoutManager);

        return view;
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
                    listtopic.addAll(topics);
                    listemp.addAll(topics);
                    Collections.sort(listemp,Topic.aseid);
                    Collections.sort(listtopic,Topic.aseid);
                    interact = new AdapterTopic(context,listtopic,lv_recycler);
                    lv_recycler.setAdapter(interact);
                    if (listtopic.size()>=10){
                        interact.setOnLoadMoreListener(new OnLoadMoreListener() {
                            @Override
                            public void onLoadMore() {
//                                listtopic.add(null);
                                Log.e("haint", "Load More"+Integer.parseInt(listtopic.get(listtopic.size()-1).getId()));
                                interact.notifyItemInserted(listtopic.size() - 1);

                                //Load more data for reyclerview
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.e("haint", "Load More 2");
                                        listtopic.remove(listtopic.size() - 1);
                                        interact.notifyItemRemoved(listtopic.size());
                                        //Remove loading item
                                        topicSync1 getalltopic = new topicSync1(getContext(),session_id,100,Integer.parseInt(listemp.get(listemp.size()-1).getId()));
                                        getalltopic.execute();
                                        interact.notifyDataSetChanged();
//                                        interact.setLoaded();
                                    }
                                }, 500);
                            }
                        });
                    }
                    lv_recycler.addOnItemTouchListener(
                            new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    Topic item = (Topic) listtopic.get(position);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("thread", item);
                                    ThreadFragment fragment= new ThreadFragment();
                                    fragment.setArguments(bundle);
                                    callFragment(fragment);
                                }
                            })
                    );

                    //end
                }else {
                    Toast.makeText(context,Information.noti_no_data,Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){

            }
            dialog.dismiss();
            super.onPostExecute(topics);
        }
    }

    public class topicSync1 extends AsyncTask<Void,Void,List<Topic>>{
        ProgressDialog dialog;
        Context context;
        String session_id;
        int top, from;
        public topicSync1(Context context,String session_id,int top, int from){
            this.context = context;
            this.session_id=session_id;
            this.top=top;
            this.from=from;
        }

        @Override
        protected void onPreExecute() {
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
                    listtopic.addAll(topics);
                    listemp.addAll(topics);
                    Collections.sort(listemp,Topic.aseid);
                    Collections.sort(listtopic,Topic.aseid);
                }else {
                    Toast.makeText(context,Information.noti_no_data,Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){

            }
            super.onPostExecute(topics);
        }
    }




}
