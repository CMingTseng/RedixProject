package redix.booxtown.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import redix.booxtown.R;
import redix.booxtown.custom.MenuBottomCustom;

/**
 * Created by thuyetpham94 on 29/08/2016.
 */
public class DashboardStopFragment extends Fragment {
    private MenuBottomCustom menu_bottom;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dashboard_fragment, container, false);
        TextView txt_menu_dashboard_cancel = (TextView)view.findViewById(R.id.txt_menu_dashboard_cancel);
        txt_menu_dashboard_cancel.setVisibility(View.GONE);

        Button btn_menu_dashboard_bottom_rate = (Button) view.findViewById(R.id.btn_menu_dashboard_bottom_rate);
        btn_menu_dashboard_bottom_rate.setBackgroundResource(R.drawable.btn_xam);

        ImageView img_menu_dashboard_bottom_status = (ImageView)view.findViewById(R.id.img_menu_dashboard_bottom_status);
        img_menu_dashboard_bottom_status.setImageResource(R.drawable.icon_stop_profile);

        Button btn_menu_dashboard_bottom_cancel = (Button)view.findViewById(R.id.btn_menu_dashboard_bottom_cancel);
        btn_menu_dashboard_bottom_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_cancel_dashboard);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                Button btn_cancel_dialog_dashboard = (Button)dialog.findViewById(R.id.btn_cancel_dialog_dashboard);
                btn_cancel_dialog_dashboard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callFragment(new DashboardDeleteFragment());
                        dialog.dismiss();
                    }
                });

                ImageView img_close_dialog_cancel_dashboard = (ImageView)dialog.findViewById(R.id.img_close_dialog_cancel_dashboard);
                img_close_dialog_cancel_dashboard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        //menu
        ImageView img_menu = (ImageView)getActivity().findViewById(R.id.img_menu);
        img_menu.setImageResource(R.drawable.back);

        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callFragment(new MyProfileDashboardFragment());
            }
        });

        TextView title_menu = (TextView)getActivity().findViewById(R.id.txt_title);
        title_menu.setText("Dashboard");

        ImageView img_menu_component = (ImageView)getActivity().findViewById(R.id.img_menu_component);
        img_menu_component.setVisibility(View.GONE);
        //end
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
