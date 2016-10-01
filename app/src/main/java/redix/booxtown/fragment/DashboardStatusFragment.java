package redix.booxtown.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import redix.booxtown.R;
import redix.booxtown.controller.BookController;
import redix.booxtown.custom.MenuBottomCustom;
import redix.booxtown.model.Book;
import redix.booxtown.model.DashBoard;

public class DashboardStatusFragment extends Fragment {
    DashBoard dashBoard;
    List<Book> list_bookname;
    ImageView img_menu;
    TextView txt_menu_dashboard_cancel;
    Button btn_menu_dashboard_bottom_cancel;
    ImageView img_menu_component;
    TextView title_menu;
    Button btn_menu_dashboard_bottom_rate;
    TextView textView_namebook_seller,textView_nameauthor_seller,textView_namebook_buyer,textView_nameauthor_buyer;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dashboard_fragment, container, false);
        init(view);
        list_bookname = new ArrayList<>();
        dashBoard = (DashBoard)getArguments().getSerializable("dashboard");
        //menu
        btn_menu_dashboard_bottom_cancel.setVisibility(View.GONE);
        txt_menu_dashboard_cancel.setVisibility(View.GONE);
        //menu

        img_menu.setImageResource(R.drawable.btn_sign_in_back);
        title_menu.setText("Dashboard");
        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callFragment(new MyProfileDashboardFragment());
            }
        });

        img_menu_component.setVisibility(View.GONE);
        btn_menu_dashboard_bottom_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_dashboard_status);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                Button btn_rate_dashboard_status = (Button)dialog.findViewById(R.id.btn_rate_dashboard_status);
                btn_rate_dashboard_status.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                ImageView imv_close_dialog_dashboard_status = (ImageView)dialog.findViewById(R.id.imv_close_dialog_dashboard_status);
                imv_close_dialog_dashboard_status.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });
        //end
        getBookByID getBookByID = new getBookByID(getContext(),String.valueOf(dashBoard.getBook_seller_id()));
        getBookByID.execute();
        getBookByID getBookByID1 = new getBookByID(getContext(),String.valueOf(dashBoard.getBook_swap_id()));
        getBookByID1.execute();

        if (list_bookname.size() ==2){
            textView_namebook_seller.setText(list_bookname.get(0).getTitle());
            textView_nameauthor_seller.setText(list_bookname.get(0).getAuthor());

            textView_namebook_buyer.setText(list_bookname.get(1).getTitle());
            textView_nameauthor_buyer.setText(list_bookname.get(1).getAuthor());
        }
        return view;
    }

    public void init(View view){
        btn_menu_dashboard_bottom_rate = (Button)view.findViewById(R.id.btn_menu_dashboard_bottom_rate);
        title_menu = (TextView)getActivity().findViewById(R.id.txt_title);
        img_menu = (ImageView)getActivity().findViewById(R.id.img_menu);
        img_menu_component = (ImageView)getActivity().findViewById(R.id.img_menu_component);
        txt_menu_dashboard_cancel = (TextView)view.findViewById(R.id.txt_menu_dashboard_cancel);
        btn_menu_dashboard_bottom_cancel = (Button)view.findViewById(R.id.btn_menu_dashboard_bottom_cancel);
        textView_namebook_seller = (TextView)view.findViewById(R.id.textView_namebook_seller);
        textView_nameauthor_seller = (TextView)view.findViewById(R.id.textView_nameauthor_seller);
        textView_namebook_buyer = (TextView)view.findViewById(R.id.textView_namebook_buyer);
        textView_nameauthor_buyer = (TextView)view.findViewById(R.id.textView_nameauthor_buyer);
    }

    public void callFragment(Fragment fragment ){
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //Khi được goi, fragment truyền vào sẽ thay thế vào vị trí FrameLayout trong Activity chính
        transaction.replace(R.id.frame_main_all, fragment);
        transaction.commit();
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
            dialog.setMessage("Please wait...");
            dialog.setIndeterminate(true);
            dialog.show();
        }

        @Override
        protected void onPostExecute(List<Book> list) {
            try {
                if (list.size() > 0) {
                    list_bookname.add(list.get(0));
                    dialog.dismiss();
                }
            } catch (Exception e) {
            }
            dialog.dismiss();

        }
    }
}
