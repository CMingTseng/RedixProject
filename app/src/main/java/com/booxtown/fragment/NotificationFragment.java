package com.booxtown.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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

import com.booxtown.activity.HomeActivity;
import com.booxtown.activity.ListingsDetailActivity;
import com.booxtown.activity.MenuActivity;
import com.booxtown.activity.NotificationAcceptActivity;
import com.booxtown.activity.NotificationRejectActivity;
import com.booxtown.activity.NotificationSellAccept;
import com.booxtown.activity.NotificationSellActivity;
import com.booxtown.activity.NotificationSellNoAccept;
import com.booxtown.activity.NotificationSellNoReject;
import com.booxtown.activity.NotificationSellReject;
import com.booxtown.activity.NotificationSwapActivity;
import com.booxtown.activity.Notification_Swap_Accept_Like;
import com.booxtown.activity.Notification_Swap_Accept_NoLike;
import com.booxtown.activity.UserProfileActivity;
import com.booxtown.controller.BookController;
import com.booxtown.controller.Information;
import com.booxtown.controller.NotificationController;
import com.booxtown.controller.ThreadController;
import com.booxtown.controller.TopicController;
import com.booxtown.custom.Custom_ListView_Notification;
import com.booxtown.custom.MenuBottomCustom;
import com.booxtown.listener.OnLoadMoreListener;
import com.booxtown.model.Book;
import com.booxtown.model.InteractThread;
import com.booxtown.model.Notification;
import com.booxtown.model.Thread;
import com.booxtown.model.Topic;
import com.booxtown.recyclerclick.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.booxtown.R;

public class NotificationFragment extends Fragment {
    public static String [] prgmNameList={"Unread","Dominic send a swap request","Dominic want to your book","Dominic reject your swap request"};
    private MenuBottomCustom bottomListings;
    public boolean flag=true;
    HomeActivity main;
    String session_id;
    RecyclerView lv_notification;
    List<Notification> listnoNotifications;
    List<Notification> listNotifications;
    Custom_ListView_Notification adapter;
    List<Topic> topic;
    List<Thread> item;
    String[] s;
    Bundle bundle;
    int typeOption=0;
    ArrayList<InteractThread> listInteractThreads= new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        main = (HomeActivity) getActivity();

        SharedPreferences pref = getContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor  = pref.edit();
        session_id = pref.getString("session_id", null);
        listnoNotifications = new ArrayList<>();
        View view = inflater.inflate(R.layout.notification_fragment, container, false);

        ImageView imageView_back=(ImageView) getActivity().findViewById(R.id.img_menu);
        imageView_back.setImageResource(R.drawable.btn_menu_locate);
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MenuActivity.class);
                startActivity(intent);
            }
        });

        //listview content notification
        lv_notification=(RecyclerView) view.findViewById(R.id.lv_content_notification);
        RecyclerView.LayoutManager  layoutManager = new LinearLayoutManager(getActivity());
        lv_notification.setLayoutManager(layoutManager);
        adapter = new Custom_ListView_Notification(getActivity(),listnoNotifications,lv_notification);

        final Gettop_notifi gettop_notifi = new Gettop_notifi(session_id,100,0);
        gettop_notifi.execute();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public class gotoScreen extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            bundle = new Bundle();
            bundle.putSerializable("thread", item.get(0));
            bundle.putSerializable("interact", topic.get(0));
            bundle.putString("type_fragment","NotificationFragment");
            InteractThreadDetailsFragment fragment= new InteractThreadDetailsFragment();
            fragment.setArguments(bundle);
            HomeActivity mainAllActivity = (HomeActivity) getActivity();
            mainAllActivity.callFragment(fragment);
            return null;
        }
    }

    public class Gettopicbyid extends AsyncTask<String,Void,List<Topic>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Topic> doInBackground(String... params) {
            topic = new ArrayList<>();
            TopicController topicController = new TopicController();
            topic = topicController.gettopicbyid(params[0]);
            return topic;
        }

        @Override
        protected void onPostExecute(List<Topic> topics) {
            super.onPostExecute(topics);
        }
    }


    public class Getthreadbyid extends AsyncTask<String,Void,List<Thread>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Thread> doInBackground(String... params) {
            item = new ArrayList<>();
            ThreadController threadController = new ThreadController();
            item =  threadController.getthreadbyid(params[0]);
            return item;
        }

        @Override
        protected void onPostExecute(List<Thread> threads) {
            super.onPostExecute(threads);
        }
    }


    public void callFragment(Fragment fragment ){
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //Khi được goi, fragment truyền vào sẽ thay thế vào vị trí FrameLayout trong Activity chính
        transaction.replace(R.id.frame_main_all, fragment);
        transaction.commit();
    }

    public class Gettop_notifi extends AsyncTask<Void,Void,List<Notification>>{

        String session_id;
        int top;
        int from;

        public Gettop_notifi(String session_id,int top,int from){
            this.session_id = session_id;
            this.top = top;
            this.from = from;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected List<Notification> doInBackground(Void... params) {
            NotificationController notificationController = new NotificationController();

            return notificationController.getALllNotificationTop(session_id,top,from);
        }

        @Override
        protected void onPostExecute(final List<Notification> notifications) {
        try {
            if (notifications.size() > 0) {
                listnoNotifications.addAll(notifications);
                Collections.sort(listnoNotifications, Notification.aseid);
                adapter = new Custom_ListView_Notification(getActivity(), notifications, lv_notification);
                lv_notification.setAdapter(adapter);
                lv_notification.addOnItemTouchListener(
                        new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int i) {
                                Notification notification = (Notification) adapter.getlist().get(i);
                                if (notification.getIs_read() == 0) {
                                    ChangeStatusNotification changeStatusNotification = new ChangeStatusNotification(session_id, Integer.parseInt(notification.getId()));
                                    changeStatusNotification.execute();
                                }
                                if (notification.getId_screen().equals("10")) {
                                    s = notification.getKey_screen().split("::");
                                    Getthreadbyid getthreadbyid = new Getthreadbyid();
                                    getthreadbyid.execute(s[1]);
                                    Gettopicbyid gettopicbyid = new Gettopicbyid();
                                    gettopicbyid.execute(s[0]);
                                    gotoScreen gotoScreen = new gotoScreen();
                                    gotoScreen.execute();

                                } else if (notification.getId_screen().equals("9")) {
                                    Intent intent = new Intent(getActivity(), NotificationSwapActivity.class);
                                    intent.putExtra("trans_id", notification.getKey_screen() + "");
                                    startActivity(intent);
                                } else if (notification.getId_screen().equals("3")) {
                                    Intent intent = new Intent(getActivity(), Notification_Swap_Accept_Like.class);
                                    intent.putExtra("trans_id", notification.getKey_screen() + "");
                                    intent.putExtra("key", "1");
                                    startActivity(intent);
                                } else if (notification.getId_screen().equals("4")) {
                                    Intent intent = new Intent(getActivity(), NotificationSellActivity.class);
                                    intent.putExtra("trans_id", notification.getKey_screen() + "");
                                    intent.putExtra("keyOption", "4");
                                    startActivity(intent);
                                } else if (notification.getId_screen().equals("5")) {

                                    Intent intent = new Intent(getActivity(), NotificationSellNoAccept.class);
                                    intent.putExtra("trans_id", notification.getKey_screen() + "");
                                    intent.putExtra("keyOption", "5");
                                    startActivity(intent);
                                } else if (notification.getId_screen().equals("6")) {
                                    Intent intent = new Intent(getActivity(), NotificationSellReject.class);
                                    intent.putExtra("trans_id", notification.getKey_screen() + "");
                                    intent.putExtra("keyOption", "6");
                                    startActivity(intent);
                                } else if (notification.getId_screen().equals("7")) {
                                    Intent intent = new Intent(getActivity(), NotificationSellAccept.class);
                                    intent.putExtra("trans_id", notification.getKey_screen() + "");
                                    intent.putExtra("keyOption", "7");
                                    startActivity(intent);

                                } else if (notification.getId_screen().equals("8")) {
                                    Intent intent = new Intent(getActivity(), NotificationSellNoReject.class);
                                    intent.putExtra("trans_id", notification.getKey_screen() + "");
                                    intent.putExtra("keyOption", "8");
                                    startActivity(intent);

                                } else if (notification.getId_screen().equals("0")) {
                                    Intent intent = new Intent(getActivity(), NotificationAcceptActivity.class);
                                    intent.putExtra("trans_id", notification.getKey_screen() + "");
                                    startActivity(intent);
                                } else if (notification.getId_screen().equals("1")) {
                                    Intent intent = new Intent(getActivity(), Notification_Swap_Accept_NoLike.class);
                                    intent.putExtra("trans_id", notification.getKey_screen());
                                    startActivity(intent);
                                } else if (notification.getId_screen().equals("2")) {
                                    Intent intent = new Intent(getActivity(), NotificationRejectActivity.class);
                                    intent.putExtra("trans_id", notification.getKey_screen() + "");
                                    startActivity(intent);
                                } else if (notification.getId_screen().equals("11")) {
                                    // comment in book post
                                    getBookByID getBookByID = new getBookByID(getContext(), notification.getKey_screen() + "");
                                    getBookByID.execute();
                                } else if (notification.getId_screen().equals("12")) {
                                    // set cho Cancel transaction in DashBoad
                                    Intent intent = new Intent(getActivity(), Notification_Swap_Accept_Like.class);
                                    intent.putExtra("trans_id", notification.getKey_screen() + "");
                                    intent.putExtra("key", "2");
                                    startActivity(intent);
                                }
                                else if (notification.getId_screen().equals("14")) {
                                    // comment in book post
                                    getBookByID getBookByID = new getBookByID(getContext(), notification.getKey_screen() + "");
                                    getBookByID.execute();
                                }else if (notification.getId_screen().equals("15")) {
                                    Intent intent=new Intent(getActivity(),UserProfileActivity.class);
                                    intent.putExtra("user",notification.getKey_screen() + "");
                                    startActivity(intent);
                                }
                                else if (notification.getId_screen().equals("16")) {
                                    Intent intent = new Intent(getActivity(), NotificationSellActivity.class);
                                    intent.putExtra("trans_id", notification.getKey_screen() + "");
                                    intent.putExtra("keyOption", "16");
                                    startActivity(intent);
                                }
                                else if (notification.getId_screen().equals("17")) {
                                    Intent intent = new Intent(getActivity(), NotificationSellNoAccept.class);
                                    intent.putExtra("trans_id", notification.getKey_screen() + "");
                                    intent.putExtra("keyOption", "17");
                                    startActivity(intent);
                                } else if (notification.getId_screen().equals("18")) {
                                    Intent intent = new Intent(getActivity(), NotificationSellReject.class);
                                    intent.putExtra("trans_id", notification.getKey_screen() + "");
                                    intent.putExtra("keyOption", "18");
                                    startActivity(intent);
                                } else if (notification.getId_screen().equals("19")) {
                                    Intent intent = new Intent(getActivity(), NotificationSellAccept.class);
                                    intent.putExtra("trans_id", notification.getKey_screen() + "");
                                    intent.putExtra("keyOption", "19");
                                    startActivity(intent);

                                } else if (notification.getId_screen().equals("20")) {
                                    Intent intent = new Intent(getActivity(), NotificationSellNoReject.class);
                                    intent.putExtra("trans_id", notification.getKey_screen() + "");
                                    intent.putExtra("keyOption", "20");
                                    startActivity(intent);

                                }

                            }
                        })
                );
                if (listnoNotifications.size() >= 20) {
                    adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                        @Override
                        public void onLoadMore() {
                            Log.e("haint", "Load More");
                            listnoNotifications.add(null);
                            adapter.notifyItemInserted(listnoNotifications.size() - 1);

                            //Load more data for reyclerview
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e("haint", "Load More 2");
                                    listnoNotifications.remove(listnoNotifications.size() - 1);
                                    adapter.notifyItemRemoved(listnoNotifications.size());
                                    //Remove loading item
                                    Getttop_notifi1 getalltopic = new Getttop_notifi1(session_id, 100, Integer.parseInt(listnoNotifications.get(listnoNotifications.size() - 1).getId()));
                                    getalltopic.execute();
//                                    adapter.setLoaded();
                                }
                            }, 2000);
                        }
                    });
                }
            }
        }catch (Exception e){}
        }
    }


    public class ChangeStatusNotification extends AsyncTask<Void,Void,Void>{
        int notificationId;
        String sessionid;

        public ChangeStatusNotification(String sessionid,int notificationId){
            this.sessionid = sessionid;
            this.notificationId = notificationId;
        }


        @Override
        protected Void doInBackground(Void... params) {
            NotificationController notificationController = new NotificationController();
            notificationController.changeStatusNotification(sessionid,notificationId);
            return null;
        }
    }

    public class Getttop_notifi1 extends AsyncTask<Void, Void, List<Notification>> {
        String session_id;
        int top;
        int from;

        public Getttop_notifi1(String session_id,int top,int from){
            this.session_id = session_id;
            this.top = top;
            this.from = from;
        }


        @Override
        protected List<Notification> doInBackground(Void... params) {
            NotificationController notificationController = new NotificationController();

            return notificationController.getALllNotificationTop(session_id,top,from);
        }

        @Override
        protected void onPostExecute(List<Notification> notifications) {
            listnoNotifications.addAll(notifications);
            adapter.notifyDataSetChanged();
            super.onPostExecute(notifications);
        }
    }

    class getBookByID extends AsyncTask<Void, Void, List<Book>> {
        String id;
        Context ctx;
        ProgressDialog dialog;

        public getBookByID(Context ctx,String id) {
            this.id = id;
            this.ctx = ctx;

        }

        @Override
        protected List<Book> doInBackground(Void... params) {
            BookController bookController = new BookController();
            return bookController.getBookByID(id);
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ctx);
            dialog.setMessage(Information.noti_dialog);
            dialog.setIndeterminate(true);
            dialog.show();
        }

        @Override
        protected void onPostExecute(List<Book> list) {
            try {
                if (list.size() > 0) {
                        ListingsDetailActivity fragment = new ListingsDetailActivity();
                        Bundle bundle = new Bundle();
                        bundle.putString(String.valueOf(R.string.valueListings), "4");
                        bundle.putSerializable("item", list.get(0));
                        fragment.setArguments(bundle);
                        HomeActivity mainAllActivity = (HomeActivity) getActivity();
                        mainAllActivity.callFragment(fragment);


                    dialog.dismiss();
                }
            } catch (Exception e) {
                dialog.dismiss();
            }
            dialog.dismiss();

        }
    }

}
