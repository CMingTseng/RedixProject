package redix.booxtown.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import redix.booxtown.adapter.ListBookAdapter;
import redix.booxtown.api.ServiceGenerator;
import redix.booxtown.controller.BookController;
import redix.booxtown.controller.UserController;
import redix.booxtown.R;
import redix.booxtown.activity.MenuActivity;
import redix.booxtown.adapter.AdapterExplore;
import redix.booxtown.adapter.AdapterListings;
import redix.booxtown.custom.CustomTabbarExplore;
import redix.booxtown.model.Book;
import redix.booxtown.model.Explore;
import redix.booxtown.model.User;

public class MyProfileFragment extends Fragment {
    public final static String user_id=null;
    private LinearLayout linear_all;
    private LinearLayout linear_swap;
    private LinearLayout linear_free;
    private LinearLayout linear_cart;
    List<Book> listEx= new ArrayList<>();
    GridView grid;
    ListBookAdapter adapter;
    ImageView imv_menu_profile;
    TextView txt_profile_phone,txt_profile_birthday,txt_profile_email,txt_profile_username;
    String username;
    TextView tab_all_count,tab_swap_count,tab_free_count,tab_cart_count;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.my_profile_fragment, container, false);

        ImageView img_menu_personal_dashboard = (ImageView)view.findViewById(R.id.img_menu_personal_dashboard);
        img_menu_personal_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyProfileDashboardFragment profile = new MyProfileDashboardFragment();
                Bundle bundle = new Bundle();
                bundle.putString("username",username);
                profile.setArguments(bundle);
                callFragment(profile);

            }
        });

        imv_menu_profile = (ImageView)view.findViewById(R.id.imv_menu_profile);

        ImageView imageView_back=(ImageView) getActivity().findViewById(R.id.img_menu);
        Glide.with(getActivity()).load(R.drawable.btn_menu_locate).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView_back);

        ImageView imv_close_dialog_dashboard_status = (ImageView)view.findViewById(R.id.imv_close_dialog_dashboard_status);
        Picasso.with(getContext()).load(R.drawable.btn_rank_one).into(imv_close_dialog_dashboard_status);

        ImageView imageView26 = (ImageView)view.findViewById(R.id.imageView26);
        Picasso.with(getContext()).load(R.drawable.btn_rank_two).into(imageView26);

        ImageView imageView27 = (ImageView)view.findViewById(R.id.imageView27);
        Picasso.with(getContext()).load(R.drawable.btn_rank_three).into(imageView27);

        Profile profile = new Profile(getContext());
        SharedPreferences pref = getContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);

        String session_id =  pref.getString("session_id", null);
        profile.execute(session_id);

        //profile
        txt_profile_email = (TextView)view.findViewById(R.id.txt_profile_email);
        txt_profile_phone = (TextView)view.findViewById(R.id.txt_profile_phone);
        txt_profile_birthday = (TextView)view.findViewById(R.id.txt_profile_birthday);
        txt_profile_username = (TextView)view.findViewById(R.id.txt_profile_username);
        //end

        //list book
        listingAsync listingAsync = new listingAsync(getContext());
        listingAsync.execute(session_id);
        //end
       // imageView_back.setImageResource(R.drawable.btn_menu_locate);
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MenuActivity.class);
                startActivity(intent);
            }
        });

        grid=(GridView)view.findViewById(R.id.grid_myprofile);
        grid.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        View view_tab=(View)view.findViewById(R.id.tab_myprofile);
        final CustomTabbarExplore tab_custom=new CustomTabbarExplore(view_tab,getActivity());
        linear_all=(LinearLayout) view_tab.findViewById(R.id.linear_all);
        linear_swap=(LinearLayout) view_tab.findViewById(R.id.linear_swap);
        linear_free=(LinearLayout) view_tab.findViewById(R.id.linear_free);
        linear_cart=(LinearLayout) view_tab.findViewById(R.id.linear_cart);

        tab_all_count = (TextView) view_tab.findViewById(R.id.tab_all_count);
        tab_swap_count = (TextView) view_tab.findViewById(R.id.tab_swap_count);
        tab_free_count = (TextView) view_tab.findViewById(R.id.tab_free_count);
        tab_cart_count = (TextView) view_tab.findViewById(R.id.tab_cart_count);

        linear_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ListBookAdapter adapter = new ListBookAdapter(getActivity(),filterBook(1),1);
                grid=(GridView)view.findViewById(R.id.grid_myprofile);
                grid.setAdapter(adapter);
                tab_custom.setDefault(1);
            }
        });

        linear_swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ListBookAdapter adapter = new ListBookAdapter(getActivity(),filterBook(2),1);
                grid=(GridView)view.findViewById(R.id.grid_myprofile);
                grid.setAdapter(adapter);
                tab_custom.setDefault(2);
            }
        });

        linear_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ListBookAdapter adapter = new ListBookAdapter(getActivity(),filterBook(3),1);
                grid=(GridView)view.findViewById(R.id.grid_myprofile);
                grid.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                tab_custom.setDefault(3);
            }
        });

        linear_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ListBookAdapter adapter = new ListBookAdapter(getActivity(),filterBook(4),1);
                grid=(GridView)view.findViewById(R.id.grid_myprofile);
                grid.setAdapter(adapter);
                tab_custom.setDefault(4);
            }
        });

        return view;
    }

    public List<Book> filterBook(int type){
        List<Book> list= new ArrayList<>();
        if(type==1){
            list = listEx;
        }
        else if(type==2){
            for (int i=0; i<listEx.size(); i++){
                if(listEx.get(i).getAction().equals("100")){
                    list.add(listEx.get(i));
                }
            }
        }
        else if(type==3){
            for (int i=0; i<listEx.size(); i++){
                if(listEx.get(i).getAction().equals("010")){
                    list.add(listEx.get(i));
                }
            }
        }
        else{
            for (int i=0; i<listEx.size(); i++){
                if(listEx.get(i).getAction().equals("001")){
                    list.add(listEx.get(i));
                }
            }
        }

        return list;
    }
    public void callFragment(Fragment fragment ){
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //Khi được goi, fragment truyền vào sẽ thay thế vào vị trí FrameLayout trong Activity chính
        transaction.replace(R.id.frame_main_all, fragment);
        transaction.commit();
    }

    class Profile extends AsyncTask<String,Void,List<User>>{
        Context context;
        public Profile(Context context){
            this.context=context;
        }
        ProgressDialog dialog;
        @Override
        protected List<User> doInBackground(String... strings) {
            UserController userController  = new UserController();
            List<User> profile = userController.getprofile(strings[0]);
            return profile;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Please wait...");
            dialog.setIndeterminate(true);
            dialog.show();
        }

        @Override
        protected void onPostExecute(List<User> userResult) {
            try {
                if(userResult.size() == 0){
                    Toast.makeText(context,"No data",Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }else {

                    txt_profile_email.setText(userResult.get(0).getEmail());
                    txt_profile_phone.setText(userResult.get(0).getPhone());
                    txt_profile_birthday.setText(userResult.get(0).getBirthday().substring(0,10));
                    txt_profile_username.setText(userResult.get(0).getUsername());
                    username = userResult.get(0).getUsername();
                    Glide.with(context). load(ServiceGenerator.API_BASE_URL+"booxtown/rest/getImage?username=" + userResult.get(0).getUsername() + "&image=" +  userResult.get(0).getPhoto()  + "").diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.blank_image).
                            into(imv_menu_profile);
                    Picasso.with(context).load("").into(imv_menu_profile);
                    dialog.dismiss();
                }
                super.onPostExecute(userResult);
            }catch (Exception e){
                Toast.makeText(context,"no data",Toast.LENGTH_LONG).show();
            }

        }
    }

    class listingAsync extends AsyncTask<String,Void,List<Book>>{

        Context context;
        ProgressDialog dialog;
        List<Book> listemp;
        public listingAsync(Context context){
            this.context = context;
        }

        @Override
        protected List<Book> doInBackground(String... strings) {
            listemp = new ArrayList<>();
            BookController bookController = new BookController();
            listemp = bookController.getAllBookById(strings[0]);
            return listemp;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.setMessage("Please wait...");
            dialog.setIndeterminate(true);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            try {
                if(books.size() >0){
                    adapter = new ListBookAdapter(getActivity(), books,1);
                    grid.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    listEx = books;
                    tab_all_count.setText(" ("+filterBook(1).size()+")");
                    tab_swap_count.setText(" ("+filterBook(2).size()+")");
                    tab_free_count.setText(" ("+filterBook(3).size()+")");
                    tab_cart_count.setText(" ("+filterBook(4).size()+")");
                }
                super.onPostExecute(books);
            }catch (Exception e){
                Toast.makeText(context, "No Data", Toast.LENGTH_LONG).show();
            }
            dialog.dismiss();
        }
    }

}
