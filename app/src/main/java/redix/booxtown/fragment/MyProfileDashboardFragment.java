package redix.booxtown.fragment;

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

import redix.booxtown.R;
import redix.booxtown.adapter.AdapterProfileDashboard;
import redix.booxtown.custom.MenuBottomCustom;

public class MyProfileDashboardFragment extends Fragment {

    public static int [] imgoffer={R.drawable.myprofile_buy_come,R.drawable.myprofile_free_back,R.drawable.myprofile_swap,R.drawable.myprofile_swap};
    public static int [] imgstatus={R.drawable.myprofile_tick,R.drawable.myprofile_not,R.drawable.myprofile_all_not,R.drawable.myprofile_not};
    public static String [] txtbook={"Nearest distance","Price low to high","Price high to low","Price high to low"};
    TextView txt_username;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.my_profile_dashboard_fragment, container, false);
        ListView lv_myprofile_dashboard = (ListView)view.findViewById(R.id.lv_myprofile_dashboard);
        lv_myprofile_dashboard.setAdapter(new AdapterProfileDashboard(getActivity(),txtbook,imgoffer,imgstatus));

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
}
