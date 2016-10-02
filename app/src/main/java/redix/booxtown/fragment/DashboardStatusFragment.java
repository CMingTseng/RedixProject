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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import redix.booxtown.R;
import redix.booxtown.api.ServiceGenerator;
import redix.booxtown.controller.BookController;
import redix.booxtown.controller.Information;
import redix.booxtown.controller.TransactionController;
import redix.booxtown.controller.UserController;
import redix.booxtown.custom.MenuBottomCustom;
import redix.booxtown.model.Book;
import redix.booxtown.model.DashBoard;
import redix.booxtown.model.User;

public class DashboardStatusFragment extends Fragment {
    DashBoard dashBoard;
    ImageView img_menu;
    TextView txt_menu_dashboard_cancel;
    Button btn_menu_dashboard_bottom_cancel;
    ImageView img_menu_component;
    TextView title_menu;
    Button btn_menu_dashboard_bottom_rate;
    TextView textView_namebook_seller,textView_nameauthor_seller,textView_namebook_buyer,textView_nameauthor_buyer;

    //user profile
    CircularImageView img_menu_dashboard_middle,imageView_username_rating;
    TextView textView_username_dashboard_middle,textView_phone_dashboard_middle,textView_username_rating;
    RatingBar ratingBar_user_dashboard_middle;
    String img_username,username;
    //end

    //dialog rating
    RatingBar rating_promp,rating_cour,rating_quality;
    //end

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dashboard_fragment, container, false);
        init(view);
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
                    if(dashBoard.getUser_promp() != 0 || dashBoard.getUser_cour() != 0 || dashBoard.getUser_quality() !=0){
                        Toast.makeText(getContext(),Information.noti_tran_done,Toast.LENGTH_SHORT).show();
                    }else{
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_dashboard_status);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();

                    //int rating dialog
                    rating_promp = (RatingBar)dialog.findViewById(R.id.rating_promp);
                    rating_cour = (RatingBar)dialog.findViewById(R.id.rating_cour);
                    rating_quality = (RatingBar)dialog.findViewById(R.id.rating_quality);
                    imageView_username_rating = (CircularImageView)dialog.findViewById(R.id.imageView_username_rating);
                    textView_username_rating = (TextView)dialog.findViewById(R.id.textView_username_rating);
                    //end
                    Picasso.with(getContext())
                            .load(img_username)
                            .error(R.drawable.user)
                            .into(imageView_username_rating);
                    textView_username_rating.setText(username);
                    Button btn_rate_dashboard_status = (Button)dialog.findViewById(R.id.btn_rate_dashboard_status);
                    btn_rate_dashboard_status.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ratingAsync ratingAsync = new ratingAsync(getContext(),dashBoard.getId(),rating_promp.getRating(),
                                    rating_cour.getRating(),rating_quality.getRating());
                            ratingAsync.execute();
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
                }
            });

        //end
        if(dashBoard.getAction().equals("swap")){
            getBookByID getBookByID = new getBookByID(getContext(),String.valueOf(dashBoard.getBook_swap_id()));
            getBookByID.execute();
            getUser getUser = new getUser(getContext(),dashBoard.getUser_seller_id());
            getUser.execute();
        }else {
            textView_namebook_buyer.setVisibility(View.GONE);
            textView_nameauthor_buyer.setVisibility(View.GONE);
        }
        getUser getUser = new getUser(getContext(),dashBoard.getUser_seller_id());
        getUser.execute();

        textView_namebook_seller.setText(dashBoard.getBook_seller());
        textView_nameauthor_seller.setText(dashBoard.getAuthor());

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

    class getUser extends AsyncTask<Void,Void,List<User>>{

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

    class ratingAsync extends AsyncTask<Void,Void,Boolean>{
        Context context;
        int trans_id;
        float user_promp,user_cour,user_quality;
        ProgressDialog progressDialog;

        public ratingAsync(Context context,int trans_id,float user_promp,float user_cour,float user_quality){
            this.context = context;
            this.trans_id = trans_id;
            this.user_promp = user_promp;
            this.user_cour = user_cour;
            this.user_quality = user_quality;
        }
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(Information.noti_dialog);
            progressDialog.show();
        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            TransactionController transactionController = new TransactionController();
            return transactionController.updateRating(trans_id,user_promp,user_cour,user_quality);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean == true) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(),Information.noti_update_success, Toast.LENGTH_LONG).show();
            } else {
                progressDialog.dismiss();
                Toast.makeText(getActivity(),Information.noti_update_fail, Toast.LENGTH_LONG).show();
            }
            progressDialog.dismiss();
        }
    }
}
