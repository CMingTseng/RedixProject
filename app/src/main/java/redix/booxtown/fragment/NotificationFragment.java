package redix.booxtown.fragment;

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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import redix.booxtown.R;
import redix.booxtown.activity.MainAllActivity;
import redix.booxtown.controller.NotificationController;
import redix.booxtown.listener.OnLoadMoreListener;
import redix.booxtown.model.Notification;
import redix.booxtown.model.Thread;
import redix.booxtown.model.Topic;
import redix.booxtown.recyclerclick.RecyclerItemClickListener;
import redix.booxtown.activity.HomeActivity;
import redix.booxtown.activity.MenuActivity;
import redix.booxtown.activity.NotificationDominicActivity;
import redix.booxtown.activity.NotificationSellActivity;
import redix.booxtown.activity.NotificationSwapActivity;
import redix.booxtown.custom.Custom_ListView_Notification;
import redix.booxtown.custom.MenuBottomCustom;
import redix.booxtown.model.InteractThread;

public class NotificationFragment extends Fragment {
    public static String [] prgmNameList={"Unread","Dominic send a swap request","Dominic want to your book","Dominic reject your swap request"};
    private MenuBottomCustom bottomListings;
    public boolean flag=true;
    HomeActivity main;
    String session_id;
    RecyclerView lv_notification;
    List<Notification> listnoNotifications;
    Custom_ListView_Notification adapter;
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

        Gettop_notifi gettop_notifi = new Gettop_notifi(session_id,100,0);
        gettop_notifi.execute();
        lv_notification.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int i) {
                        Notification notification = (Notification) adapter.getlist().get(i);
                        Toast.makeText(getContext(),"djshjdhsj"+notification.getId(),Toast.LENGTH_LONG).show();
                        if (notification.getKey_screen().equals("BTNotiCommented")){
                            String[] s = notification.getId_screen().split("::");
                            Topic topic = new Topic();
                            Thread item = new Thread();
                            item.setId(s[1]);
                            topic.setId(s[0]);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("thread", item);
                            bundle.putSerializable("interact", topic);
                            bundle.putString("type_fragment","NotificationFragment");
                            InteractThreadDetailsFragment fragment= new InteractThreadDetailsFragment();
                            fragment.setArguments(bundle);
                            HomeActivity mainAllActivity = (HomeActivity) getActivity();
                            mainAllActivity.callFragment(fragment);
                        }
//                        if(i==0){
//                            InteractThread interact1= new InteractThread();
//                            interact1.setInteractThreadTitle("Thread one text");
//                            interact1.setInteractThreadCount("20");
//                            interact1.setStatus(true);
//                            interact1.setInteractThreadContent("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
//                            interact1.setInteractThreadAddBy("Derek Jarma");
//                            listInteractThreads.add(interact1);
//                            InteractThread item = (InteractThread) listInteractThreads.get(0);
//                            InteractThreadDetailsFragment fragment= new InteractThreadDetailsFragment();
//                            Bundle bundle = new Bundle();
//                            bundle.putSerializable("thread", item);
//                            fragment.setArguments(bundle);
//                            main.callFragment(fragment);

//                        }else if(i==1){
//                            Intent intent = new Intent(getActivity(),NotificationSwapActivity.class);
//                            startActivity(intent);
//                        }else if(i==2){
//                            Intent intent1 = new Intent(getActivity(),NotificationSellActivity.class);
//                            startActivity(intent1);
//                        }else if(i==3){
//                            Intent intent2 = new Intent(getActivity(),NotificationDominicActivity.class);
//                            startActivity(intent2);
//                        }
                    }
                })
        );
        return view;
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
        protected void onPostExecute(List<Notification> notifications) {
            if (notifications.size()>0){
                listnoNotifications.addAll(notifications);
                Collections.sort(listnoNotifications,Notification.aseid);
                adapter = new Custom_ListView_Notification(getActivity(),notifications,lv_notification);
                lv_notification.setAdapter(adapter);
                if (listnoNotifications.size()>=20){
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

                                    //Remove loading item
                                    Gettop_notifi getalltopic = new Gettop_notifi(session_id,100,Integer.parseInt(listnoNotifications.get(listnoNotifications.size()-1).getId()));
                                    getalltopic.execute();
                                    adapter.setLoaded();
                                }
                            }, 2000);
                        }
                    });
                }
            }
            super.onPostExecute(notifications);
        }
    }

}
