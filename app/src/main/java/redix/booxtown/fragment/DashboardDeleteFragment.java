package redix.booxtown.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import redix.booxtown.R;
import redix.booxtown.api.ServiceGenerator;
import redix.booxtown.controller.BookController;
import redix.booxtown.controller.Information;
import redix.booxtown.controller.NotificationController;
import redix.booxtown.controller.ObjectCommon;
import redix.booxtown.controller.UserController;
import redix.booxtown.model.Book;
import redix.booxtown.model.DashBoard;
import redix.booxtown.model.Notification;
import redix.booxtown.model.User;

/**
 * Created by thuyetpham94 on 29/08/2016.
 */
public class DashboardDeleteFragment extends Fragment {
    ImageView img_menu_dashboard_bottom_status,img_menu,img_menu_component;
    Button btn_menu_dashboard_bottom_rate,btn_menu_dashboard_bottom_cancel;
    TextView textView_namebook_seller,textView_nameauthor_seller,textView_namebook_buyer,textView_nameauthor_buyer,title_menu;

    //user profile
    CircularImageView img_menu_dashboard_middle,imageView_username_rating;
    TextView textView_username_dashboard_middle,textView_phone_dashboard_middle,textView_with;
    RatingBar ratingBar_user_dashboard_middle;
    String img_username,username;
    ImageView img_free_listings;
    DashBoard dashBoard;
    //end

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboard_fragment,container,false);
        init(view);

        img_menu_dashboard_bottom_status.setImageResource(R.drawable.myprofile_all_not);
        btn_menu_dashboard_bottom_rate.setVisibility(View.GONE);
        btn_menu_dashboard_bottom_cancel.setVisibility(View.GONE);
        dashBoard = (DashBoard)getArguments().getSerializable("dashboard");

        //menu
        img_menu.setImageResource(R.drawable.btn_sign_in_back);
        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               callFragment(new MyProfileDashboardFragment());
            }
        });

        title_menu.setText("Dashboard");
        img_menu_component.setVisibility(View.GONE);
        getUser getUser = new getUser(getContext(),dashBoard.getUser_seller_id());
        getUser.execute();
        if(dashBoard.getAction().equals("swap")){
            getBookByID getBookByID = new getBookByID(getContext(),String.valueOf(dashBoard.getBook_swap_id()));
            getBookByID.execute();
            textView_namebook_buyer.setVisibility(View.VISIBLE);
            textView_nameauthor_buyer.setVisibility(View.VISIBLE);
        }else if(dashBoard.getAction().equals("buy")){
            textView_namebook_buyer.setVisibility(View.GONE);
            textView_nameauthor_buyer.setVisibility(View.GONE);
            Picasso.with(getContext()).load(R.drawable.explore_btn_buy_active).into(img_free_listings);
            textView_with.setVisibility(View.GONE);
        }else if(dashBoard.getAction().equals("free")){
            textView_namebook_buyer.setVisibility(View.GONE);
            textView_nameauthor_buyer.setVisibility(View.GONE);
            Picasso.with(getContext()).load(R.drawable.explore_btn_free_active).into(img_free_listings);
            textView_with.setVisibility(View.GONE);
        }

        textView_namebook_seller.setText(dashBoard.getBook_seller());
        textView_nameauthor_seller.setText(dashBoard.getAuthor());
        return view;
    }

    public void init(View view){
        textView_namebook_seller = (TextView)view.findViewById(R.id.textView_namebook_seller);
        textView_nameauthor_seller = (TextView)view.findViewById(R.id.textView_nameauthor_seller);
        textView_namebook_buyer = (TextView)view.findViewById(R.id.textView_namebook_buyer);
        textView_nameauthor_buyer = (TextView)view.findViewById(R.id.textView_nameauthor_buyer);

        img_menu_component = (ImageView)getActivity().findViewById(R.id.img_menu_component);
        title_menu = (TextView)getActivity().findViewById(R.id.txt_title);
        img_menu = (ImageView)getActivity().findViewById(R.id.img_menu);
        btn_menu_dashboard_bottom_cancel = (Button)view.findViewById(R.id.btn_menu_dashboard_bottom_cancel);
        btn_menu_dashboard_bottom_rate = (Button)view.findViewById(R.id.btn_menu_dashboard_bottom_rate);
        img_menu_dashboard_bottom_status = (ImageView)view.findViewById(R.id.img_menu_dashboard_bottom_status);
        textView_with = (TextView)view.findViewById(R.id.textView_with);
        img_free_listings = (ImageView)view.findViewById(R.id.img_free_listings);
        //user profile
        img_menu_dashboard_middle = (CircularImageView)view.findViewById(R.id.img_menu_dashboard_middle);
        textView_username_dashboard_middle = (TextView)view.findViewById(R.id.textView_username_dashboard_middle);
        textView_phone_dashboard_middle = (TextView)view.findViewById(R.id.textView_phone_dashboard_middle);
        ratingBar_user_dashboard_middle = (RatingBar)view.findViewById(R.id.ratingBar_user_dashboard_middle);
        //end
    }
    public void callFragment(Fragment fragment ){
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //Khi được goi, fragment truyền vào sẽ thay thế vào vị trí FrameLayout trong Activity chính
        transaction.replace(R.id.frame_main_all, fragment);
        transaction.commit();
    }

    class getUser extends AsyncTask<Void,Void,List<User>> {

        Context context;
        int user_id;
        ProgressDialog progressDialog;
        public getUser(Context context,int user_id){
            this.context = context;
            this.user_id = user_id;
        }
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(Information.noti_dialog);
            progressDialog.show();
        }

        @Override
        protected List<User> doInBackground(Void... voids) {
            UserController userController = new UserController();
            return userController.getByUserId(user_id);
        }

        @Override
        protected void onPostExecute(List<User> user) {
            try {
                if (user.size() > 0){
                    Picasso.with(context)
                            .load(ServiceGenerator.API_BASE_URL+"booxtown/rest/getImage?username="+user.get(0).getUsername()+"&image="+user.get(0).getPhoto().substring(user.get(0).getUsername().length()+3,user.get(0).getPhoto().length()))
                            .error(R.drawable.user)
                            .into(img_menu_dashboard_middle);
                    textView_username_dashboard_middle.setText(user.get(0).getUsername());
                    img_username = ServiceGenerator.API_BASE_URL+"booxtown/rest/getImage?username="+user.get(0).getUsername()+"&image="+user.get(0).getPhoto().substring(user.get(0).getUsername().length()+3,user.get(0).getPhoto().length());
                    username = user.get(0).getUsername();

                    ratingBar_user_dashboard_middle.setRating(user.get(0).getRating());
                    textView_phone_dashboard_middle.setText(user.get(0).getPhone());
                    progressDialog.dismiss();
                }else {
                    Toast.makeText(context,Information.noti_no_data,Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }catch (Exception e){

            }
            progressDialog.dismiss();
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
                    textView_namebook_buyer.setText(list.get(0).getTitle()+"");
                    textView_nameauthor_buyer.setText(list.get(0).getAuthor());
                    dialog.dismiss();
                }
            } catch (Exception e) {
                dialog.dismiss();
            }
            dialog.dismiss();

        }
    }

}
