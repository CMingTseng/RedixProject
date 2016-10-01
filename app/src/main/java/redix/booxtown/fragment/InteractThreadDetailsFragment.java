package redix.booxtown.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import redix.booxtown.R;
import redix.booxtown.activity.HomeActivity;
import redix.booxtown.activity.MainAllActivity;
import redix.booxtown.adapter.AdapterInteractThreadDetails;
import redix.booxtown.controller.CommentController;
import redix.booxtown.controller.NotificationController;
import redix.booxtown.controller.ObjectCommon;
import redix.booxtown.controller.ThreadController;
import redix.booxtown.controller.UserController;
import redix.booxtown.custom.MenuBottomCustom;
import redix.booxtown.model.Comment;
import redix.booxtown.model.Notification;
import redix.booxtown.model.Thread;
import redix.booxtown.model.Topic;
import redix.booxtown.model.User;

/**
 * Created by Administrator on 28/08/2016.
 */
public class InteractThreadDetailsFragment extends Fragment
{
    String user_ID="";
    ListView listView;
    private MenuBottomCustom menu_bottom;
    Thread threads;
    TextView txt_author_thread;
    Topic topic;
    TextView txt_title;
    TextView txt_count_thread;
    String type_fragment;
    AdapterInteractThreadDetails adapter;
    List<String> listUser= new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.interact_thread_detail_fragment, container, false);

        txt_title=(TextView) getActivity().findViewById(R.id.txt_title);
        txt_title.setText("Interact");

        ImageView imageView_back=(ImageView) getActivity().findViewById(R.id.img_menu);
        imageView_back.setImageResource(R.drawable.btn_sign_in_back);
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(topic !=null) {
                    if (type_fragment.equals("NotificationFragment")){

                        HomeActivity homeActivity = (HomeActivity) getActivity();
                        homeActivity.callFragment(new NotificationFragment());
                    }else {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("thread", topic);
                        ThreadFragment fragment = new ThreadFragment();
                        fragment.setArguments(bundle);
                        callFragment(fragment);
                    }
                }
                else {
                    MainAllActivity mainAllActivity = (MainAllActivity) getActivity();
                    mainAllActivity.callFragment(new NotificationFragment());
                    txt_title.setText("Notifications");
                }

            }
        });

        //----------------------------------------------
        threads = (Thread) getArguments().getSerializable("thread");
        topic = (Topic) getArguments().getSerializable("interact");
        type_fragment = getArguments().getString("type_fragment");
        //--------------------------------------------------
        TextView txt_title_thread=(TextView) view.findViewById(R.id.txt_title_thread_detail);
        txt_count_thread=(TextView) view.findViewById(R.id.txt_count_thread_detail);
        TextView txt_content_thread=(TextView) view.findViewById(R.id.txt_contern_thread_details);
        txt_author_thread=(TextView) view.findViewById(R.id.txt_author_interact_thread_detail);
        txt_title_thread.setText(threads.getTitle()+"");
        txt_content_thread.setText(threads.getDescription());
        txt_author_thread.setText("Added by "+threads.getUsername());
        txt_count_thread.setText("("+threads.getNum_comment()+")");
        //-----------------------------------------------------------

        listView=(ListView) view.findViewById(R.id.listview_comments);
        listView.setDivider(null);
        listView.setAdapter(adapter);

        commentAsync commentAsync1 = new commentAsync(getContext());
        commentAsync1.execute(threads.getId());

        SharedPreferences pref = getContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor  = pref.edit();
        final String session_id = pref.getString("session_id", null);

        final EditText edit_message = (EditText)view.findViewById(R.id.edit_message);

        final ImageView btn_send_comment_interact = (ImageView)view.findViewById(R.id.btn_send_comment_interact);
            btn_send_comment_interact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    insertComment insertComment1 = new insertComment(getContext());
                    insertComment1.execute(session_id,edit_message.getText().toString(),threads.getId());
                    edit_message.setText("");
                    commentAsync commentAsync1 = new commentAsync(getContext());
                    commentAsync1.execute(threads.getId());
                }
            });


        //---------------------------------------------------------------

        return view;
    }
    public void callFragment(Fragment fragment ){
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //Khi được goi, fragment truyền vào sẽ thay thế vào vị trí FrameLayout trong Activity chính
        transaction.replace(R.id.frame_main_all, fragment);
        transaction.commit();
    }
    class commentAsync extends AsyncTask<String,Void,List<Comment>>{

        Context context;
        ProgressDialog dialog;
        public commentAsync(Context context){
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.setMessage("please waiting...");
            dialog.show();
        }

        @Override
        protected List<Comment> doInBackground(String... strings) {
            CommentController commentController = new CommentController();
            return commentController.getallcomment(strings[0]);
        }

        @Override
        protected void onPostExecute(List<Comment> comments) {
            try {
                if(comments.size() >0){
                    adapter = new AdapterInteractThreadDetails(context,comments);
                    listView.setAdapter(adapter);
                    if(!listUser.contains(threads.getUser_id())) {
                        listUser.add(threads.getUser_id());
                    }
                    for(int i=0; i< comments.size(); i++){
                        if(!listUser.contains(comments.get(i).getUser_id())){
                            listUser.add(comments.get(i).getUser_id());
                        }
                    }
                    dialog.dismiss();
                }else {
                    //Toast.makeText(context,"no data",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }catch (Exception e){
                dialog.dismiss();
            }
        }
    }
    public class ThreadSync extends AsyncTask<Void,Void,Boolean> {
        ProgressDialog dialog;
        Context context;
        String session_id;
        int thread_id;
        public ThreadSync(Context context,String session_id,int thread_id){
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
            ThreadController topicController = new ThreadController();
            return topicController.changeStatusUnreadThread(session_id,thread_id);
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
    class insertComment extends AsyncTask<String,Void,Boolean>{

        Context context;
        ProgressDialog dialog;
        public insertComment(Context context){
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.setMessage("please waiting...");
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            CommentController comment = new CommentController();
            return comment.insertComment(strings[0],strings[1],strings[2],"0");
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            try {
                if(aBoolean == true){
                    Toast.makeText(context,"success",Toast.LENGTH_SHORT).show();
//                    int count= threads.getNum_comment()+1;

                    SharedPreferences pref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                    String session_id = pref.getString("session_id", null);
                    ThreadSync changeStatus = new ThreadSync(context, session_id, Integer.parseInt(threads.getId()));
                    changeStatus.execute();

                    UserID us= new UserID(getContext());
                    us.execute(session_id);

                    dialog.dismiss();
                }else {
                    Toast.makeText(context,"no success",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }catch (Exception e){
                dialog.dismiss();
            }
        }
    }
    class UserID extends AsyncTask<String,Void,String>{
        Context context;
        public UserID(Context context){
            this.context=context;
        }
        ProgressDialog dialog;
        @Override
        protected String doInBackground(String... strings) {
            UserController userController  = new UserController();
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
                    String username = pref.getString("username", null);

                    List<Hashtable> list = new ArrayList<>();
                    for(int i=0; i<listUser.size(); i++) {
                        String s= listUser.get(i);
                        if(!listUser.get(i).equals(user_ID)) {
                            Notification notification = new Notification("Comment from thread ",topic.getId() + "::" + threads.getId() , "10");
                            Hashtable obj = ObjectCommon.ObjectDymanic(notification);
                            obj.put("user_id", listUser.get(i));
                            obj.put("messages", "Comment thread " + threads.getTitle() + " by: " + username);

                            list.add(obj);
                        }
                    }

                    NotificationController controller = new NotificationController();
                    controller.sendNotification(list);

                //}
            }catch (Exception e){
                String ssss= e.getMessage();
                Toast.makeText(context,"no data",Toast.LENGTH_LONG).show();
            }
            dialog.dismiss();
        }
    }
}

