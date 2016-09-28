package redix.booxtown.fragment;

import android.app.Dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import redix.booxtown.R;
import redix.booxtown.adapter.AdapterThread;
import redix.booxtown.controller.ThreadController;
import redix.booxtown.controller.TopicController;
import redix.booxtown.listener.OnLoadMoreListener;
import redix.booxtown.model.Interact;
import redix.booxtown.model.Thread;
import redix.booxtown.model.Topic;

/**
 * Created by Administrator on 27/08/2016.
 */
public class ThreadFragment extends Fragment
{
    AdapterThread adapterThread;
    List<Thread> listThreads= new ArrayList<>();
    List<Thread> listtemp = new ArrayList<>();
    RecyclerView rv_thread;
    LinearLayoutManager linearLayoutManager;
    Topic topic;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.interact_thread_fragment, container, false);

        ImageView imageView_back=(ImageView) getActivity().findViewById(R.id.img_menu);
        Picasso.with(getContext()).load(R.drawable.btn_sign_in_back).into(imageView_back);
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFragment(new TopicFragment());
            }
        });
        //-----------------------------------------------
        topic = (Topic) getArguments().getSerializable("thread");
        //--------------------------------------------------
        TextView txt_title_thread=(TextView) view.findViewById(R.id.txt_title_thread);
        TextView txt_count_thread=(TextView) view.findViewById(R.id.txt_count_thread);
        txt_title_thread.setText("Topic Name: "+topic.getTitle());
        txt_count_thread.setText("("+topic.getNum_thread()+")");
//        txt_count_thread.setText("("+interact.getInteractCount()+")");
        //------------------------------------------------------------------------------
        TextView btn_add_thread = (TextView) view.findViewById(R.id.btn_add_thread);
        btn_add_thread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_interact_addthread);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                ImageView imv_dialog_interacr_close = (ImageView)dialog.findViewById(R.id.imv_dialog_interacr_close);
                Picasso.with(getContext()).load(R.drawable.btn_close_filter).into(imv_dialog_interacr_close);
                imv_dialog_interacr_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                Button btn_dialog_interact_submit = (Button)dialog.findViewById(R.id.btn_dialog_interact_submit);
                final TextView edit_title_insert_thread = (TextView)dialog.findViewById(R.id.edit_title_insert_thread);
                final TextView edit_description_insert_thread = (TextView)dialog.findViewById(R.id.edit_description_insert_thread);
                final Thread thread = new Thread();
                SharedPreferences pref = getContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor  = pref.edit();
                final String session_id = pref.getString("session_id", null);
                btn_dialog_interact_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //submit dialog add thread
                        insertthreadAsync insertthread = new insertthreadAsync(getContext());
                        insertthread.execute(edit_title_insert_thread.getText().toString(),edit_description_insert_thread.getText().toString(),
                                topic.getId(),session_id);
                        SharedPreferences pref = getActivity().getSharedPreferences("MyPref",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor  = pref.edit();
                        String session_id = pref.getString("session_id", null);
                        threadAsync threadAsync = new threadAsync(getContext(),session_id,100,0);
                        threadAsync.execute(topic.getId());

                        ThreadSync changeStatus = new ThreadSync(getContext(), session_id,Integer.parseInt(topic.getId()),1);
                        changeStatus.execute();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        //end

        //-----------------------------------------------------------
        rv_thread=(RecyclerView) view.findViewById(R.id.list_interact_thread);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rv_thread.setLayoutManager(linearLayoutManager);

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor  = pref.edit();
        final String session_id = pref.getString("session_id", null);
        threadAsync threadAsync = new threadAsync(getContext(),session_id,100,0);
        threadAsync.execute(topic.getId());

        //------------------------------------------------------------------------------

        return view;
    }
    public void callFragment(Fragment fragment ){
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //Khi được goi, fragment truyền vào sẽ thay thế vào vị trí FrameLayout trong Activity chính
        transaction.replace(R.id.frame_main_all, fragment);
        transaction.commit();
    }

    class threadAsync extends AsyncTask<String,Void,List<Thread>>{
        ProgressDialog dialog;
        Context context;
        String session_id;
        int top, from;
        int flag;
        public threadAsync(Context context, String session_id, int top, int from){

            this.context = context;
            this.session_id=session_id;
            this.top=top;
            this.from=from;
            //this.flag= flag;
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
        protected List<Thread> doInBackground(String... strings) {
            ThreadController threadController = new ThreadController();

                return threadController.threadGetTop(session_id, strings[0], top, from);


        }

        @Override
        protected void onPostExecute(final List<Thread> threads) {
            try{
                if(threads.size()>0){
//                    listThreads.addAll(listThreads);
                    listThreads.addAll(threads);
                    listtemp.addAll(threads);
                    Collections.sort(listThreads,Thread.aseid);
                    Collections.sort(listtemp,Thread.aseid);
                    adapterThread = new AdapterThread(context,listThreads,rv_thread);
                    rv_thread.setAdapter(adapterThread);
                    adapterThread.setLoaded();
                    if (listThreads.size()>=10) {
                        adapterThread.setOnLoadMoreListener(new OnLoadMoreListener() {
                            @Override
                            public void onLoadMore() {
                                listThreads.add(null);
                                Log.e("haint", "Load More" + listThreads.size());
                                adapterThread.notifyItemInserted(listThreads.size() - 1);

                                //Load more data for reyclerview
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.e("haint", "Load More 2_"+listtemp.get(listtemp.size()-1).getId());
                                        //Remove loading item
                                        listThreads.remove(listThreads.size() - 1);
                                        adapterThread.notifyItemRemoved(listThreads.size());
                                        threadAsync1 getalltopic = new threadAsync1(getContext(),topic.getId(), session_id, 100, Integer.valueOf(listtemp.get(listtemp.size()-1).getId()));
                                        getalltopic.execute(topic.getId());
                                        adapterThread.notifyDataSetChanged();
                                    }
                                }, 500);
                            }
                        });

                    }
                    rv_thread.addOnItemTouchListener(new redix.booxtown.recyclerclick.RecyclerItemClickListener(getActivity(), new redix.booxtown.recyclerclick.RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            final Thread item = (Thread) listThreads.get(position);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("thread", item);
                            bundle.putSerializable("interact", topic);
                            bundle.putString("type_fragment","ThreadFragment");
                            InteractThreadDetailsFragment fragment= new InteractThreadDetailsFragment();
                            fragment.setArguments(bundle);
                            callFragment(fragment);

                            SharedPreferences pref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                            String session_id = pref.getString("session_id", null);
                            ThreadSync changeStatus = new ThreadSync(context, session_id, Integer.parseInt(item.getId()),0);
                            changeStatus.execute();
                        }
                    }));
                }else{
                    //Toast.makeText(context,"No data",Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){

            }
            dialog.dismiss();
        }
    }


    class threadAsync1 extends AsyncTask<String,Void,List<Thread>>{
        ProgressDialog dialog;
        Context context;
        String session_id;
        String topicid;
        int top, from;
        int flag;
        public threadAsync1(Context context,String topicid, String session_id, int top, int from){

            this.context = context;
            this.session_id=session_id;
            this.top=top;
            this.from=from;
            this.topicid = topicid;
            //this.flag= flag;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Thread> doInBackground(String... strings) {
            ThreadController threadController = new ThreadController();
            return threadController.threadGetTop(session_id, topicid, top, from);


        }

        @Override
        protected void onPostExecute(final List<Thread> threads) {
            try{
                if(threads.size()>0){
                    listThreads.addAll(threads);
                    listtemp.addAll(threads);
                    Collections.sort(listtemp,Thread.aseid);
                    Collections.sort(listThreads,Thread.aseid);
                }else{
                    //Toast.makeText(context,"No data",Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){

            }
        }
    }



    public class ThreadSync extends AsyncTask<Void,Void,Boolean> {
        ProgressDialog dialog;
        Context context;
        String session_id;
        int thread_id;
        int flag;
        public ThreadSync(Context context,String session_id,int thread_id, int flag){
            this.context = context;
            this.session_id=session_id;
            this.thread_id=thread_id;
            this.flag= flag;
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

            if(flag==0) {
                ThreadController topicController = new ThreadController();
                return topicController.changeStatusThread(session_id, thread_id);
            }else if(flag==1){
                TopicController topicController= new TopicController();
                return topicController.changeStatusUnreadTopic(session_id, thread_id);
            }
            return null;
        }

        @Override
        protected void onPostExecute(final Boolean topics) {
            try{
                if(topics){
                    //RecyclerViewHolder holder=  new RecyclerViewHolder(itemView);
                    // holder.txt_count_interact.setTextColor(context.getResources().getColor(R.color.color_topic_interact));
                }

            }catch (Exception e){

            }
            dialog.dismiss();

        }
    }
    class insertthreadAsync extends AsyncTask<String,Void,Boolean>{

        Context context;
        ProgressDialog progressDialog;
        public insertthreadAsync(Context context){
            this.context=context;
        }
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("please waiting....");
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... parrams) {
            ThreadController threadController = new ThreadController();
            return threadController.insertThread(parrams[0],parrams[1],parrams[2],parrams[3]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            try {
                if (aBoolean == true){
                    Toast.makeText(context,"success",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context,"no success",Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){

            }
            progressDialog.dismiss();
        }
    }
}

