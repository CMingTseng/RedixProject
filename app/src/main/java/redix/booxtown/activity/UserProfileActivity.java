package redix.booxtown.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import redix.booxtown.controller.BookController;
import redix.booxtown.controller.Information;
import redix.booxtown.controller.UserController;
import redix.booxtown.custom.CustomTabbarExplore;
import redix.booxtown.custom.MenuBottomCustom;
import redix.booxtown.fragment.MainFragment;
import redix.booxtown.model.Book;
import redix.booxtown.model.Explore;
import redix.booxtown.model.User;

/**
 * Created by Administrator on 29/08/2016.
 */
public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener
{
    private LinearLayout linear_all,linear_swap,linear_free,linear_cart;
    List<Book> listEx = new ArrayList<>();
    GridView grid;
    CircularImageView imv_menu_profile;
    RatingBar ratingBar_userprofile;
    AdapterExplore adapter;
    //-----------------------------
    private ImageView btn_location,btn_user,btn_commnet,btn_camera,btn_bag;
    ImageView img_rank1,img_rank2,img_rank3;
    CustomTabbarExplore tab_custom;
    RecyclerView rView;
    GridLayoutManager gridLayoutManager;
    //-----------------------------
    public TextView tab_all_count,tab_swap_count,tab_free_count,tab_cart_count,txt_profile_username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        init();
        View view=(View) findViewById(R.id.menu_top_profile);
        TextView txtTitle=(TextView) view.findViewById(R.id.txt_title);
        txtTitle.setText("User Profile");
        txtTitle.setGravity(Gravity.CENTER_VERTICAL);
        ImageView img_component=(ImageView) findViewById(R.id.img_menu_component);
        img_component.setVisibility(View.INVISIBLE);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        rView = (RecyclerView)view.findViewById(R.id.recycler_view);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(gridLayoutManager);

        ImageView img_menu = (ImageView)findViewById(R.id.img_menu);
        Picasso.with(getApplicationContext()).load(R.drawable.btn_sign_in_back).into(img_menu);

        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //--------------------------------------------------


        linear_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AdapterExplore adapter = new AdapterExplore(UserProfileActivity.this,filterExplore(1),0,1);
                grid=(GridView)findViewById(R.id.grid_view_profile);
                rView.setAdapter(adapter);
                tab_custom.setDefault(1);
            }
        });

        linear_swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AdapterExplore adapter = new AdapterExplore(UserProfileActivity.this,filterExplore(2),0,1);
                grid=(GridView)findViewById(R.id.grid_view_profile);
                rView.setAdapter(adapter);
                tab_custom.setDefault(2);
            }
        });

        linear_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AdapterExplore adapter = new AdapterExplore(UserProfileActivity.this,filterExplore(3),0,1);
                grid=(GridView)findViewById(R.id.grid_view_profile);
                rView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                tab_custom.setDefault(3);
            }
        });

        linear_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AdapterExplore adapter = new AdapterExplore(UserProfileActivity.this,filterExplore(4),0,1);
                grid=(GridView)findViewById(R.id.grid_view_profile);
                rView.setAdapter(adapter);
                tab_custom.setDefault(4);
            }
        });
        Bundle bundle=getIntent().getExtras();
        try {
            int user_id = bundle.getInt("user");
            getUser getUser = new getUser(UserProfileActivity.this,user_id);
            getUser.execute();
            getTopBook getTopBook = new getTopBook(UserProfileActivity.this,user_id,100,0);
            getTopBook.execute();
        }catch (Exception ex){

        }
    }
    public void init(){
        //rank
        img_rank1 = (ImageView)findViewById(R.id.img_rank1);
        img_rank2 = (ImageView)findViewById(R.id.img_rank2);
        img_rank3 = (ImageView)findViewById(R.id.img_rank3);
        //end
        //bottom menu
        btn_location = (ImageView) findViewById(R.id.img_menu_bottom_location);
        btn_commnet = (ImageView) findViewById(R.id.img_menu_bottom_comment);
        btn_camera = (ImageView) findViewById(R.id.img_menu_bottom_camera);
        btn_bag = (ImageView) findViewById(R.id.img_menu_bottom_bag);
        btn_user = (ImageView) findViewById(R.id.img_menu_bottom_user);
        //--------------------------------------------------
        //profile
        imv_menu_profile = (CircularImageView)findViewById(R.id.imv_menu_profile);
        txt_profile_username = (TextView)findViewById(R.id.txt_profile_username);
        ratingBar_userprofile = (RatingBar)findViewById(R.id.ratingBar_userprofile);
        //end

        //---------------------------------------------------------------
        View view_tab=(View) findViewById(R.id.tab_bar_profile);
        tab_custom=new CustomTabbarExplore(view_tab,this);

        linear_all=(LinearLayout) view_tab.findViewById(R.id.linear_all);
        linear_swap=(LinearLayout) view_tab.findViewById(R.id.linear_swap);
        linear_free=(LinearLayout) view_tab.findViewById(R.id.linear_free);
        linear_cart=(LinearLayout) view_tab.findViewById(R.id.linear_cart);
        tab_all_count = (TextView) view_tab.findViewById(R.id.tab_all_count) ;
        tab_cart_count = (TextView) view_tab.findViewById(R.id.tab_cart_count) ;
        tab_free_count = (TextView) view_tab.findViewById(R.id.tab_free_count) ;
        tab_swap_count = (TextView) view_tab.findViewById(R.id.tab_swap_count) ;
    }

    public List<Book> filterExplore(int type){
        List<Book> list= new ArrayList<>();
        if(type==1){
            list = listEx;
        }
        else if(type==2){
            for (int i=0; i<listEx.size(); i++){
                if(listEx.get(i).getAction().substring(0,1).equals("1")){
                    list.add(listEx.get(i));
                }
            }
        }
        else if(type==3){
            for (int i=0; i<listEx.size(); i++){
                if(listEx.get(i).getAction().substring(1,2).equals("1")){
                    list.add(listEx.get(i));
                }
            }
        }
        else{
            for (int i=0; i<listEx.size(); i++){
                if(listEx.get(i).getAction().substring(2,3).equals("1")){
                    list.add(listEx.get(i));
                }
            }
        }

        return list;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_menu_bottom_location:
                MainAllActivity mainAllActivity = (MainAllActivity)getApplicationContext();
                mainAllActivity.callFragment(new MainFragment());
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
                    txt_profile_username.setText(user.get(0).getUsername());
                    ratingBar_userprofile.setRating(user.get(0).getRating());
                    //set rank
                    if(user.get(0).getContributor() == 0){
                        img_rank1.setVisibility(View.VISIBLE);
                        Picasso.with(context).load(R.drawable.conbitrutor_one).into(img_rank1);
                    }else{
                        Picasso.with(context).load(R.drawable.conbitrutor_two).into(img_rank1);
                    }
                    if(user.get(0).getGoldenBook() == 0){
                        img_rank2.setVisibility(View.GONE);
                    }else if(user.get(0).getGoldenBook() == 1){
                        Picasso.with(context).load(R.drawable.golden_book).into(img_rank2);
                        img_rank2.setVisibility(View.VISIBLE);
                    }

                    if(user.get(0).getListBook() == 0){
                        Picasso.with(context).load(R.drawable.newbie).into(img_rank3);
                        img_rank3.setVisibility(View.VISIBLE);
                    }else if(user.get(0).getListBook() == 1){
                        Picasso.with(context).load(R.drawable.bookworm).into(img_rank3);
                        img_rank3.setVisibility(View.VISIBLE);
                    }else{
                        Picasso.with(context).load(R.drawable.bibliophile).into(img_rank3);
                        img_rank3.setVisibility(View.VISIBLE);
                    }

                    Picasso.with(context)
                            .load(ServiceGenerator.API_BASE_URL+"booxtown/rest/getImage?username="+user.get(0).getUsername()+"&image="+user.get(0).getPhoto().substring(user.get(0).getUsername().length()+3,user.get(0).getPhoto().length()))
                            .error(R.drawable.blank_image)
                            .into(imv_menu_profile);

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

    class getTopBook extends AsyncTask<Void,Void,List<Book>>{
        Context context;
        int user_id;
        ProgressDialog progressDialog;
        int top,from;
        public getTopBook(Context context,int user_id,int top,int from){
            this.context = context;
            this.user_id = user_id;
            this.top = top;
            this.from = from;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(Information.noti_dialog);
            progressDialog.show();
        }

        @Override
        protected List<Book> doInBackground(Void... voids) {
            BookController bookController = new BookController();
            return  bookController.getTopBookByID(user_id,top,from);
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            try {
                if (books.size() >0){
                    for(int i=0; i<books.size(); i++){
                        books.get(i).setUser_id(user_id+"");
                    }
                    listEx = books;
                    adapter = new AdapterExplore(UserProfileActivity.this,books,0,1);
                    grid=(GridView)findViewById(R.id.grid_view_profile);
                    rView.setAdapter(adapter);
                    tab_all_count.setText("(" + filterExplore(1).size() + ")");
                    tab_swap_count.setText("(" + filterExplore(2).size() + ")");
                    tab_free_count.setText("(" + filterExplore(3).size() + ")");
                    tab_cart_count.setText("(" + filterExplore(4).size() + ")");
                }
                progressDialog.dismiss();
            }catch (Exception e){

            }
            progressDialog.dismiss();
        }
    }


}

