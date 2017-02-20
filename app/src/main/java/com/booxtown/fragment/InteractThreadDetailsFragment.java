package com.booxtown.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.booxtown.R;
import com.booxtown.activity.HomeActivity;
import com.booxtown.activity.SignIn_Activity;
import com.booxtown.adapter.AdapterInteractThreadDetails;
import com.booxtown.controller.CheckSession;
import com.booxtown.controller.CommentController;
import com.booxtown.controller.Information;
import com.booxtown.controller.NotificationController;
import com.booxtown.controller.ObjectCommon;
import com.booxtown.controller.ThreadController;
import com.booxtown.controller.UserController;
import com.booxtown.model.Comment;
import com.booxtown.model.Notification;
import com.booxtown.model.Result;
import com.booxtown.model.Thread;
import com.booxtown.model.Topic;

/**
 * Created by Administrator on 28/08/2016.
 */
public class InteractThreadDetailsFragment extends Fragment {
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    List<Comment> arr_commet = new ArrayList<>();
    List<String> arr_commetID = new ArrayList<>();
    boolean loading = true,
            isLoading = true;
    private int previousTotal = 0;
    private int visibleThreshold = 4;

    Thread threads;
    Topic topic;
    TextView txt_title, txt_count_thread, txt_author_thread, txt_title_thread, txt_content_thread;
    String type_fragment, session_id;
    AdapterInteractThreadDetails adapter;
    List<String> listUser = new ArrayList<>();
    EditText edit_message;
    boolean flag = true;
    Comment commentBook;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.interact_thread_detail_fragment, container, false);
        init(view);
        txt_title.setText("Interact");
        ImageView imageView_back = (ImageView) getActivity().findViewById(R.id.img_menu);
        imageView_back.setImageResource(R.drawable.btn_sign_in_back);
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (topic != null) {
                   /* if (getFragmentManager().getBackStackEntryCount() > 0) {
                        getFragmentManager().popBackStack();
                        return;
                    } else {*/
                    if (type_fragment.equals("NotificationFragment")) {
                        HomeActivity homeActivity = (HomeActivity) getActivity();
                        homeActivity.getTxtTitle().setText("Notifications");
                        homeActivity.callFragment(new NotificationFragment());
                    } else {
                        FragmentManager fm = getFragmentManager();
                        if (fm.getBackStackEntryCount() > 0) {
                            fm.popBackStack();
                        }
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("thread", topic);
                        ThreadFragment fragment = new ThreadFragment();
                        fragment.setArguments(bundle);
                        callFragment(fragment);
                    }

                } else {
                    HomeActivity homeActivity = (HomeActivity) getActivity();
                    homeActivity.getTxtTitle().setText("Notifications");
                    homeActivity.callFragment(new NotificationFragment());
                }
            }

        });
        //----------------------------------------------
        threads = (Thread) getArguments().getSerializable("thread");
        topic = (Topic) getArguments().getSerializable("interact");
        type_fragment = getArguments().getString("type_fragment");
        //--------------------------------------------------

        txt_author_thread = (TextView) view.findViewById(R.id.txt_author_interact_thread_detail);
        txt_title_thread.setText(threads.getTitle().substring(0, 1).toUpperCase() + threads.getTitle().substring(1, threads.getTitle().length()) + "");
        txt_content_thread.setText(threads.getDescription());
        txt_author_thread.setText("Added by " + threads.getUsername());
        txt_count_thread.setText("(" + threads.getNum_comment() + ")");
        //-----------------------------------------------------------

        SharedPreferences pref = getContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        session_id = pref.getString("session_id", null);
        edit_message = (EditText) view.findViewById(R.id.edit_message);
        final ImageView btn_send_comment_interact = (ImageView) view.findViewById(R.id.btn_send_comment_interact);
        btn_send_comment_interact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edit_message.getText().toString().trim().equals("")) {
                    insertComment insertComment1 = new insertComment(getContext());
                    insertComment1.execute(session_id, edit_message.getText().toString(), threads.getId());
                    edit_message.setText("");
                    arr_commet.clear();

                    commentAsync getcomment = new commentAsync(getContext(), threads.getId(), 15, 0);
                    getcomment.execute();
                } else {
                    Toast.makeText(getContext(), Information.noti_show_comment_empty, Toast.LENGTH_SHORT).show();
                }
            }
        });
        //---------------------------------------------------------------
        populatRecyclerView(threads.getId());
        implementScrollListener(threads.getId());
        return view;
    }

    public void callFragment(Fragment fragment) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //Khi được goi, fragment truyền vào sẽ thay thế vào vị trí FrameLayout trong Activity chính
        transaction.replace(R.id.frame_main_all, fragment);
        transaction.commit();
    }

    public void replaceFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        String fragmentTag = backStateName;

        FragmentManager manager = getActivity().getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null) { //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.frame_main_all, fragment, fragmentTag);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    public void init(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_comment_thread);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        txt_title = (TextView) getActivity().findViewById(R.id.txt_title);
        txt_title_thread = (TextView) view.findViewById(R.id.txt_title_thread_detail);
        txt_count_thread = (TextView) view.findViewById(R.id.txt_count_thread_detail);
        txt_content_thread = (TextView) view.findViewById(R.id.txt_contern_thread_details);
    }

    private void populatRecyclerView(String thread_id) {
        commentAsync getcomment = new commentAsync(getContext(), thread_id, 15, 0);
        getcomment.execute();
        if (arr_commet.size() == 0) {
            adapter = new AdapterInteractThreadDetails(getContext(), arr_commet);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private int totalItemCount, lastVisibleItem;

    private void implementScrollListener(final String thread_id) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = recyclerView.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                //if (loading) {
                //   if (totalItemCount > previousTotal) {
                //      loading = false;
                //      previousTotal = totalItemCount;
                //  }
                //}
                if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    // End has been reached
                    commentAsync getcomment = new commentAsync(getContext(), thread_id, 15, Integer.parseInt(commentBook.getId()));
                    getcomment.execute();
                    // Do something
                    loading = true;
                }
            }
        });
    }

    class commentAsync extends AsyncTask<String, Void, List<Comment>> {

        Context context;
        ProgressDialog dialog;
        String thread_id;
        int top, from;

        public commentAsync(Context context, String thread_id, int top, int from) {
            this.context = context;
            this.thread_id = thread_id;
            this.top = top;
            this.from = from;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.setMessage(Information.noti_dialog);
            dialog.show();
        }

        @Override
        protected List<Comment> doInBackground(String... strings) {
            CheckSession checkSession = new CheckSession();
            SharedPreferences pref = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
            boolean check = checkSession.checkSession_id(pref.getString("session_id", null));
            if (!check) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("session_id", null);
                editor.commit();
                Intent intent = new Intent(context, SignIn_Activity.class);
                context.startActivity(intent);
                this.cancel(true);
            }
            CommentController commentController = new CommentController();
            return commentController.getTopComment(thread_id, top, from);
        }

        @Override
        protected void onPostExecute(List<Comment> comments) {
            try {
                if (comments.size() > 0) {
                    /*for(int i=0; i<comments.size(); i++){
                        if(!arr_commetID.contains(comments.get(i).getId())){
                            arr_commet.add(comments.get(i));
                            arr_commetID.add(comments.get(i).getId());
                        }
                    }*/
                    commentBook = comments.get(comments.size() - 1);
                    arr_commet.addAll(comments);
                    adapter.notifyDataSetChanged();
                    if (!listUser.contains(threads.getUser_id())) {
                        listUser.add(threads.getUser_id());
                    }
                    /*for (int i = 0; i < comments.size(); i++) {
                        if (!listUser.contains(comments.get(i).getUser_id())) {
                            listUser.add(comments.get(i).getUser_id());
                        }
                    }*/
                    dialog.dismiss();
                    loading = false;
                } else {
                    isLoading = true;
                    dialog.dismiss();
                }
            } catch (Exception e) {
                dialog.dismiss();
            }
        }
    }

    public class ThreadSync extends AsyncTask<Void, Void, Boolean> {
        ProgressDialog dialog;
        Context context;
        String session_id;
        int thread_id;

        public ThreadSync(Context context, String session_id, int thread_id) {
            this.context = context;
            this.session_id = session_id;
            this.thread_id = thread_id;

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
            CheckSession checkSession = new CheckSession();
            SharedPreferences pref = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
            boolean check = checkSession.checkSession_id(pref.getString("session_id", null));
            if (!check) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("session_id", null);
                editor.commit();
                Intent intent = new Intent(context, SignIn_Activity.class);
                context.startActivity(intent);
                this.cancel(true);
            }
            ThreadController topicController = new ThreadController();
            return topicController.changeStatusUnreadThread(session_id, thread_id);
        }

        @Override
        protected void onPostExecute(final Boolean topics) {
            try {
                if (topics) {
                    //RecyclerViewHolder holder=  new RecyclerViewHolder(itemView);
                    // holder.txt_count_interact.setTextColor(context.getResources().getColor(R.color.color_topic_interact));
                }

            } catch (Exception e) {

            }
            dialog.dismiss();

        }
    }

    class insertComment extends AsyncTask<String, Void, Result> {

        Context context;
        ProgressDialog dialog;

        public insertComment(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.setMessage("please waiting...");
            dialog.show();
        }

        @Override
        protected Result doInBackground(String... strings) {
            CheckSession checkSession = new CheckSession();
            SharedPreferences pref = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
            boolean check = checkSession.checkSession_id(pref.getString("session_id", null));
            if (!check) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("session_id", null);
                editor.commit();
                Intent intent = new Intent(context, SignIn_Activity.class);
                context.startActivity(intent);
                this.cancel(true);
            }

            CommentController comment = new CommentController();
            return comment.insertComment(strings[0], strings[1], strings[2], "0", "0");

        }

        @Override
        protected void onPostExecute(Result result) {
            try {
                if (result.getCode() == 200) {
                    txt_count_thread.setText("(" + result.getSession_id() + ")");
                    Toast.makeText(context, Information.noti_show_sent_comment, Toast.LENGTH_SHORT).show();
//                    int count= threads.getNum_comment()+1;
                    SharedPreferences pref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                    String session_id = pref.getString("session_id", null);
                    ThreadSync changeStatus = new ThreadSync(context, session_id, Integer.parseInt(threads.getId()));
                    changeStatus.execute();
                    UserID us = new UserID(getContext());
                    us.execute(session_id);

                    dialog.dismiss();
                } else {
                    Toast.makeText(context, Information.noti_show_not_sent_comment, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            } catch (Exception e) {
                Toast.makeText(context, Information.noti_show_not_sent_comment, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }
    }

    class UserID extends AsyncTask<String, Void, String> {
        Context context;

        public UserID(Context context) {
            this.context = context;
        }

        ProgressDialog dialog;

        @Override
        protected String doInBackground(String... strings) {
            CheckSession checkSession = new CheckSession();
            SharedPreferences pref = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
            boolean check = checkSession.checkSession_id(pref.getString("session_id", null));
            if (!check) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("session_id", null);
                editor.commit();
                Intent intent = new Intent(context, SignIn_Activity.class);
                context.startActivity(intent);
                this.cancel(true);
            }
            UserController userController = new UserController(context);
            String user_id = userController.getUserID(strings[0]);
            return user_id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Please wait...");
            dialog.setIndeterminate(true);
            dialog.show();
        }

        @Override
        protected void onPostExecute(String user_ID) {
            try {
                //if(!threads.getUser_id().equals(user_ID)) {
                SharedPreferences pref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                String firstName = pref.getString("firstname", "");

                List<Hashtable> list = new ArrayList<>();
                for (int i = 0; i < listUser.size(); i++) {
                    String s = listUser.get(i);
                    if (!listUser.get(i).equals(user_ID)) {
                        Notification notification = new Notification("Thread Commented", topic.getId() + "::" + threads.getId(), "10");
                        Hashtable obj = ObjectCommon.ObjectDymanic(notification);
                        obj.put("user_id", listUser.get(i));
                        obj.put("messages", firstName + " commmented on " + threads.getTitle());

                        list.add(obj);
                    }
                }

                NotificationController controller = new NotificationController();
                controller.sendNotification(list);

                //}
            } catch (Exception e) {
                String ssss = e.getMessage();
                //Toast.makeText(context,"no data",Toast.LENGTH_LONG).show();
            }
            dialog.dismiss();
        }
    }


}

