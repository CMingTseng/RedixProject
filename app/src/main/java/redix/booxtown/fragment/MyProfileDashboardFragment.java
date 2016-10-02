package redix.booxtown.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import redix.booxtown.R;
import redix.booxtown.adapter.AdapterProfileDashboard;
import redix.booxtown.controller.DashBoardController;
import redix.booxtown.controller.Information;
import redix.booxtown.custom.MenuBottomCustom;
import redix.booxtown.model.DashBoard;

public class MyProfileDashboardFragment extends Fragment {

    TextView txt_username;
    ListView lv_myprofile_dashboard;
    int user_id;
    List<DashBoard> dashBoards_new;
    AdapterProfileDashboard adapterProfileDashboard;
    ImageView img_menu_component,img_menu,imv_close_dialog_dashboard_status,imageView26,imageView27;
    TextView title_menu;
    private static RelativeLayout bottomLayout;
    boolean userScrolled = false;
    private static ArrayList<DashBoard> listArrayList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.my_profile_dashboard_fragment, container, false);
        init(view);
        dashBoards_new = new ArrayList<>();
        img_menu_component.setVisibility(View.GONE);
        title_menu.setText("My Profile");
        Picasso.with(getContext()).load(R.drawable.btn_sign_in_back).into(img_menu);
        Picasso.with(getContext()).load(R.drawable.btn_rank_one).into(imv_close_dialog_dashboard_status);
        Picasso.with(getContext()).load(R.drawable.btn_rank_two).into(imageView26);
        Picasso.with(getContext()).load(R.drawable.btn_rank_three).into(imageView27);
        SharedPreferences pref = getContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        final String session_id =  pref.getString("session_id", null);
        final String user_name = pref.getString("username",null);
        user_id = Integer.valueOf(pref.getString("user_id",null));

        txt_username.setText(user_name);

        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callFragment(new MyProfileFragment());
            }
        });
        populatRecyclerView(user_id,session_id);
        implementScrollListener(session_id);

        return view;
    }

    public void init(View view){
        bottomLayout = (RelativeLayout) view
                .findViewById(R.id.loadItemsLayout_listView);
        txt_username = (TextView)view.findViewById(R.id.txt_profile_username);
        imageView27 = (ImageView)view.findViewById(R.id.imageView27);
        imv_close_dialog_dashboard_status = (ImageView)view.findViewById(R.id.imv_close_dialog_dashboard_status);
        imageView26 = (ImageView)view.findViewById(R.id.imageView26);
        img_menu = (ImageView)getActivity().findViewById(R.id.img_menu);
        lv_myprofile_dashboard = (ListView)view.findViewById(R.id.lv_myprofile_dashboard);
        img_menu_component = (ImageView)getActivity().findViewById(R.id.img_menu_component);
        title_menu = (TextView)getActivity().findViewById(R.id.txt_title);
    }

    public void callFragment(Fragment fragment ){
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //Khi được goi, fragment truyền vào sẽ thay thế vào vị trí FrameLayout trong Activity chính
        transaction.replace(R.id.frame_main_all, fragment);
        transaction.commit();
    }

    private void populatRecyclerView(int user_id,String session_id) {
        getDashBoard getDashBoard = new getDashBoard(getContext(),session_id,30,0);
        getDashBoard.execute();
        listArrayList = new ArrayList<DashBoard>();
        adapterProfileDashboard = new AdapterProfileDashboard(getActivity(), listArrayList,user_id);
        // set adapter over recyclerview
        lv_myprofile_dashboard.setAdapter(adapterProfileDashboard);
        adapterProfileDashboard.notifyDataSetChanged();
        lv_myprofile_dashboard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DashBoard dashBoard = listArrayList.get(position);
                if (dashBoard.getIs_accept() == 1 || dashBoard.getIs_reject() == 1){
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("dashboard", dashBoard);
                    DashboardStatusFragment fragment= new DashboardStatusFragment();
                    fragment.setArguments(bundle);
                    callFragment(fragment);
                }else if(dashBoard.getIs_cancel() == 1){
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("dashboard", dashBoard);
                    DashboardDeleteFragment fragment= new DashboardDeleteFragment();
                    fragment.setArguments(bundle);
                    callFragment(fragment);
                }else if(dashBoard.getIs_reject() == 0 && dashBoard.getIs_cancel()==0 && dashBoard.getIs_accept()==0){
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("dashboard", dashBoard);
                    DashboardStopFragment fragment= new DashboardStopFragment();
                    fragment.setArguments(bundle);
                    callFragment(fragment);
                }
            }
        });
    }

    private void implementScrollListener(final String session_id) {

        lv_myprofile_dashboard.setOnScrollListener(new AbsListView.OnScrollListener() {

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
                if (userScrolled
                        && firstVisibleItem + visibleItemCount == totalItemCount) {
                    userScrolled = false;
                    DashBoard dashBoard_lv = listArrayList.get(listArrayList.size()-1);
                    getDashBoard getDashBoard = new getDashBoard(getContext(),session_id,30,dashBoard_lv.getId());
                    getDashBoard.execute();
                }
            }
        });
    }


    class getDashBoard extends AsyncTask<Void,Void,List<DashBoard>>{

        Context context;
        String session_id;
        int top;
        int from;
        public getDashBoard(Context context,String session_id,int top,int from){
            this.context = context;
            this.session_id = session_id;
            this.top = top;
            this.from = from;
        }

        @Override
        protected void onPreExecute() {
            bottomLayout.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<DashBoard> doInBackground(Void... voids) {
            DashBoardController dashBoardController = new DashBoardController();
            return dashBoardController.getDashBoard(session_id,top,from);
        }

        @Override
        protected void onPostExecute(final List<DashBoard> dashBoards) {
            try {
                if(dashBoards.size() > 0){
                    listArrayList.addAll(dashBoards);
                    adapterProfileDashboard.notifyDataSetChanged();
                    bottomLayout.setVisibility(View.GONE);
                }else{
                    bottomLayout.setVisibility(View.GONE);
                }
            }catch (Exception e){
            }
            bottomLayout.setVisibility(View.GONE);
        }
    }
}
