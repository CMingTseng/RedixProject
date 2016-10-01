package redix.booxtown.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import redix.booxtown.R;
import redix.booxtown.adapter.AdapterCommentBook;
import redix.booxtown.adapter.AdapterExplore;
import redix.booxtown.api.ServiceGenerator;
import redix.booxtown.controller.Information;
import redix.booxtown.controller.UserController;
import redix.booxtown.custom.CustomTabbarExplore;
import redix.booxtown.custom.MenuBottomCustom;
import redix.booxtown.model.Book;
import redix.booxtown.model.Explore;
import redix.booxtown.model.User;

/**
 * Created by Administrator on 29/08/2016.
 */
public class UserProfileActivity extends AppCompatActivity
{
    private LinearLayout linear_all;
    private LinearLayout linear_swap;
    private LinearLayout linear_free;
    private LinearLayout linear_cart;
    ArrayList<Book> listEx= new ArrayList<>();
    GridView grid;
    private MenuBottomCustom menu_bottom;

    CircularImageView imv_menu_profile;
    TextView txt_profile_username;
    RatingBar ratingBar_userprofile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //--------------------------------------------------
        View view=(View) findViewById(R.id.menu_top_profile);
        TextView txtTitle=(TextView) view.findViewById(R.id.txt_title);
        txtTitle.setText("User Profile");
        txtTitle.setGravity(Gravity.CENTER_VERTICAL);
        ImageView img_component=(ImageView) findViewById(R.id.img_menu_component);
        img_component.setVisibility(View.INVISIBLE);

        //profile
        imv_menu_profile = (CircularImageView)view.findViewById(R.id.imv_menu_profile);
        txt_profile_username = (TextView)view.findViewById(R.id.txt_profile_username);
        ratingBar_userprofile = (RatingBar)view.findViewById(R.id.ratingBar_userprofile);
        //end

        ImageView img_menu = (ImageView)findViewById(R.id.img_menu);
        Picasso.with(getApplicationContext()).load(R.drawable.btn_sign_in_back).into(img_menu);
//        img_menu.setImageResource(R.drawable.btn_sign_in_back);
        ImageView imv_close_dialog_dashboard_status = (ImageView)findViewById(R.id.imv_close_dialog_dashboard_status);
        Picasso.with(getApplicationContext()).load(R.drawable.btn_rank_one).into(imv_close_dialog_dashboard_status);

        ImageView imageView26 = (ImageView)findViewById(R.id.imageView26);
        Picasso.with(getApplicationContext()).load(R.drawable.btn_rank_two).into(imageView26);

        ImageView imageView27 = (ImageView)findViewById(R.id.imageView27);
        Picasso.with(getApplicationContext()).load(R.drawable.btn_rank_three).into(imageView27);

        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //--------------------------------------------------
        View view_bottom = (View)findViewById(R.id.menu_bottom_profile);
        menu_bottom=new MenuBottomCustom(view_bottom,this,0);
        menu_bottom.setDefaut(0);
        //---------------------------------------------------------------

        final AdapterExplore adapter = new AdapterExplore(UserProfileActivity.this,listEx,0);
        grid=(GridView)findViewById(R.id.grid_view_profile);
        grid.setAdapter(adapter);

        //---------------------------------------------------------------
        View view_tab=(View) findViewById(R.id.tab_bar_profile);
        final CustomTabbarExplore tab_custom=new CustomTabbarExplore(view_tab,this);

        linear_all=(LinearLayout) view_tab.findViewById(R.id.linear_all);
        linear_swap=(LinearLayout) view_tab.findViewById(R.id.linear_swap);
        linear_free=(LinearLayout) view_tab.findViewById(R.id.linear_free);
        linear_cart=(LinearLayout) view_tab.findViewById(R.id.linear_cart);

        linear_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final AdapterExplore adapter = new AdapterExplore(UserProfileActivity.this,filterExplore(1),0);
//                grid=(GridView)findViewById(R.id.grid_view_profile);
//                grid.setAdapter(adapter);
                tab_custom.setDefault(1);
            }
        });

        linear_swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final AdapterExplore adapter = new AdapterExplore(UserProfileActivity.this,filterExplore(2),0);
//                grid=(GridView)findViewById(R.id.grid_view_profile);
//                grid.setAdapter(adapter);

                tab_custom.setDefault(2);
            }
        });

        linear_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final AdapterExplore adapter = new AdapterExplore(UserProfileActivity.this,filterExplore(3),0);
//                grid=(GridView)findViewById(R.id.grid_view_profile);
//                grid.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
                tab_custom.setDefault(3);
            }
        });

        linear_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final AdapterExplore adapter = new AdapterExplore(UserProfileActivity.this,filterExplore(4),0);
//                grid=(GridView)findViewById(R.id.grid_view_profile);
//                grid.setAdapter(adapter);
                tab_custom.setDefault(4);
            }
        });
        Bundle bundle=getIntent().getExtras();
        int user_id=bundle.getInt("user");
        getUser getUser = new getUser(UserProfileActivity.this,user_id);
        getUser.execute();
    }

//    public ArrayList<Explore> filterExplore(int type){
//        ArrayList<Explore> list= new ArrayList<>();
//        if(type==1){
//            list=listEx;
//        }
//        else if(type==2){
//            for (int i=0; i<listEx.size(); i++){
//                if(listEx.get(i).isSwap()){
//                    list.add(listEx.get(i));
//                }
//            }
//        }
//        else if(type==3){
//            for (int i=0; i<listEx.size(); i++){
//                if(listEx.get(i).isFree()){
//                    list.add(listEx.get(i));
//                }
//            }
//        }
//        else{
//            for (int i=0; i<listEx.size(); i++){
//                if(listEx.get(i).isBuy()){
//                    list.add(listEx.get(i));
//                }
//            }
//        }
//
//        return list;
//    }

    @Override
    protected void onRestart() {
        super.onRestart();

        menu_bottom.setDefaut(0);
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
                            .error(R.drawable.blank_image)
                            .into(imv_menu_profile);
                    txt_profile_username.setText(user.get(0).getUsername());
                    ratingBar_userprofile.setRating(user.get(0).getRating());
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
}

