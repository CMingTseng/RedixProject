package redix.booxtown.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
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
import android.widget.RatingBar;
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
import redix.booxtown.model.User;

public class MyProfileDashboardFragment extends Fragment {

    TextView txt_username;
    ListView lv_myprofile_dashboard;
    int user_id;
    List<DashBoard> dashBoards_new;
    AdapterProfileDashboard adapterProfileDashboard;
    ImageView img_menu_component,img_menu,img_rank1,img_rank2,img_rank3;
    TextView title_menu;
    private static RelativeLayout bottomLayout;
    boolean userScrolled = false;
    private static ArrayList<DashBoard> listArrayList;
    RatingBar ratingBar_userprofile;
    User user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.my_profile_dashboard_fragment, container, false);
        init(view);

        user = (User)getArguments().getSerializable("user");
        //set rank
        if(user.getContributor() == 0){
            img_rank1.setVisibility(View.VISIBLE);
            Picasso.with(getContext()).load(R.drawable.conbitrutor_one).into(img_rank1);
        }else{
            Picasso.with(getContext()).load(R.drawable.conbitrutor_two).into(img_rank1);
        }
        if(user.getGoldenBook() == 0){
            img_rank2.setVisibility(View.GONE);
        }else if(user.getGoldenBook() == 1){
            Picasso.with(getContext()).load(R.drawable.golden_book).into(img_rank2);
            img_rank2.setVisibility(View.VISIBLE);
        }

        if(user.getListBook() == 0){
            Picasso.with(getContext()).load(R.drawable.newbie).into(img_rank3);
            img_rank3.setVisibility(View.VISIBLE);
        }else if(user.getListBook() == 1){
            Picasso.with(getContext()).load(R.drawable.bookworm).into(img_rank3);
            img_rank3.setVisibility(View.VISIBLE);
        }else{
            Picasso.with(getContext()).load(R.drawable.bibliophile).into(img_rank3);
            img_rank3.setVisibility(View.VISIBLE);
        }
        //rank
        ratingBar_userprofile.setRating(user.getRating());
        LayerDrawable stars = (LayerDrawable) ratingBar_userprofile.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        //end

        dashBoards_new = new ArrayList<>();
        img_menu_component.setVisibility(View.GONE);
        title_menu.setText("My Profile");
        Picasso.with(getContext()).load(R.drawable.btn_sign_in_back).into(img_menu);
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
        ratingBar_userprofile = (RatingBar)view.findViewById(R.id.ratingBar_userprofile);
        img_rank1 = (ImageView)view.findViewById(R.id.img_rank1);
        img_rank2 = (ImageView)view.findViewById(R.id.img_rank2);
        img_rank3 = (ImageView)view.findViewById(R.id.img_rank3);
        bottomLayout = (RelativeLayout) view
                .findViewById(R.id.loadItemsLayout_listView);
        txt_username = (TextView)view.findViewById(R.id.txt_profile_username);
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
                    bundle.putSerializable("user", user);
                    DashboardStatusFragment fragment= new DashboardStatusFragment();
                    fragment.setArguments(bundle);
                    callFragment(fragment);
                }else if(dashBoard.getIs_cancel() == 1){
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("dashboard", dashBoard);
                    bundle.putSerializable("user", user);
                    DashboardDeleteFragment fragment= new DashboardDeleteFragment();
                    fragment.setArguments(bundle);
                    callFragment(fragment);
                }else if(dashBoard.getIs_reject() == 0 && dashBoard.getIs_cancel()==0 && dashBoard.getIs_accept()==0){
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("dashboard", dashBoard);
                    bundle.putSerializable("user", user);
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
