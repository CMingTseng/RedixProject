package redix.booxtown.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.my_profile_dashboard_fragment, container, false);
        lv_myprofile_dashboard = (ListView)view.findViewById(R.id.lv_myprofile_dashboard);

        ImageView img_menu_component = (ImageView)getActivity().findViewById(R.id.img_menu_component);
        img_menu_component.setVisibility(View.GONE);

        TextView title_menu = (TextView)getActivity().findViewById(R.id.txt_title);
        title_menu.setText("My Profile");

        ImageView img_menu = (ImageView)getActivity().findViewById(R.id.img_menu);
        Picasso.with(getContext()).load(R.drawable.btn_sign_in_back).into(img_menu);

        ImageView imv_close_dialog_dashboard_status = (ImageView)view.findViewById(R.id.imv_close_dialog_dashboard_status);
        Picasso.with(getContext()).load(R.drawable.btn_rank_one).into(imv_close_dialog_dashboard_status);

        ImageView imageView26 = (ImageView)view.findViewById(R.id.imageView26);
        Picasso.with(getContext()).load(R.drawable.btn_rank_two).into(imageView26);

        ImageView imageView27 = (ImageView)view.findViewById(R.id.imageView27);
        Picasso.with(getContext()).load(R.drawable.btn_rank_three).into(imageView27);

        txt_username = (TextView)view.findViewById(R.id.txt_profile_username);
        String username = (String)getArguments().getString("username");
        txt_username.setText(username);
        user_id = (Integer)getArguments().getInt("user_id");

        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callFragment(new MyProfileFragment());
            }
        });
        return view;
    }

    public void callFragment(Fragment fragment ){
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //Khi được goi, fragment truyền vào sẽ thay thế vào vị trí FrameLayout trong Activity chính
        transaction.replace(R.id.frame_main_all, fragment);
        transaction.commit();
    }

    class getDashBoard extends AsyncTask<Void,Void,List<DashBoard>>{

        Context context;
        String session_id;
        int top;
        int from;
        ProgressDialog dialog;
        public getDashBoard(Context context,String session_id,int top,int from){
            this.context = context;
            this.session_id = session_id;
            this.top = top;
            this.from = from;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage(Information.noti_dialog);
            dialog.setIndeterminate(true);
            dialog.show();
        }

        @Override
        protected List<DashBoard> doInBackground(Void... voids) {
            DashBoardController dashBoardController = new DashBoardController();
            return dashBoardController.getDashBoard(session_id,top,from);
        }

        @Override
        protected void onPostExecute(List<DashBoard> dashBoards) {
            try {
                if(dashBoards.size() > 0){
                    lv_myprofile_dashboard.setAdapter(new AdapterProfileDashboard(getActivity(),dashBoards,user_id));
                    lv_myprofile_dashboard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            if(i==0){
                                callFragment(new DashboardStatusFragment());
                            }else if(i==1) {
                                callFragment(new DashboardStopFragment());
                            }else if(i==2){
                                callFragment(new DashboardDeleteFragment());
                            }
                        }
                    });
                }else{
                    dialog.dismiss();
                }
            }catch (Exception e){
            }
            dialog.dismiss();
        }
    }
}
