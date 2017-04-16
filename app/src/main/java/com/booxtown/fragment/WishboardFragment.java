package com.booxtown.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.booxtown.activity.MainAllActivity;
import com.booxtown.activity.MenuActivity;
import com.booxtown.activity.SignIn_Activity;
import com.booxtown.activity.Upgrade;
import com.booxtown.adapter.AdapterListviewWishboard;
import com.booxtown.controller.CheckSession;
import com.booxtown.controller.Information;
import com.booxtown.controller.UserController;
import com.booxtown.controller.WishboardController;
import com.booxtown.model.Comment;
import com.booxtown.model.DayUsed;
import com.booxtown.model.Wishboard;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import com.booxtown.R;

public class WishboardFragment extends Fragment {

    RecyclerView rv_wishboard;
    LinearLayoutManager linearLayoutManager;
    AdapterListviewWishboard adpater;
    EditText editText_title_wishboard,editText_author_wishboard,editText_comment_wishboard;
    List<Wishboard> array_Wishboard;
    List<Wishboard> array_WishboardOld;
    Wishboard wishboard;
    String session_id;
    private int previousTotal = 0,visibleThreshold = 5;
    boolean loading = true,
            isLoading = true;
    ImageView img_component;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_wishboard , container, false);

        TextView title=(TextView) getActivity().findViewById(R.id.txt_title);
        title.setText("Wishboard");

        array_Wishboard = new ArrayList<>();
        ImageView img_menu = (ImageView)getActivity().findViewById(R.id.img_menu);
        Picasso.with(getContext()).load(R.drawable.btn_menu_locate).into(img_menu);
        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),MenuActivity.class);
                startActivity(intent);
            }
        });

        rv_wishboard = (RecyclerView) view.findViewById(R.id.rv_wishboard);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rv_wishboard.setLayoutManager(linearLayoutManager);

        SharedPreferences pref = getContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        session_id = pref.getString("session_id", null);
        img_component=(ImageView) getActivity().findViewById(R.id.img_menu_component);
        img_component.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_post_book_wishbroad);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                ImageView img_close_dialog_post_book_wishbroad = (ImageView)dialog.findViewById(R.id.img_close_dialog_post_book_wishbroad);
                Picasso.with(getContext()).load(R.drawable.btn_wishbroad_close).into(img_close_dialog_post_book_wishbroad);
                img_close_dialog_post_book_wishbroad.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                editText_comment_wishboard = (EditText)dialog.findViewById(R.id.editText_comment_wishboard);

                TextView btn_submit_dialog_post_book_wishbroad = (TextView)dialog.findViewById(R.id.btn_submit_dialog_post_book_wishbroad);
                btn_submit_dialog_post_book_wishbroad.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(editText_comment_wishboard.getText().toString().equals("")){
                            Toast.makeText(getContext(), Information.noti_show_comment_empty,Toast.LENGTH_SHORT).show();
                        }
                        else {
                            insertWishboard insertWishboard = new insertWishboard(getContext());
                            insertWishboard.execute("", "", editText_comment_wishboard.getText().toString(), session_id);
                            array_Wishboard.clear();
                            getWishboard getWishboard = new getWishboard(getContext(),15,0,session_id);
                            getWishboard.execute();
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
        //populatRecyclerView(session_id);
        //implementScrollListener(session_id);
        //MainAllActivity.setTxtTitle("Wishboard");

        GetDayUsed getDayUsed= new GetDayUsed(getContext(),session_id);
        getDayUsed.execute();
        return view;
    }

    private void populatRecyclerView(String session_id) {
        getWishboard getwishboard = new getWishboard(getContext(),15,0,session_id);
        getwishboard.execute();
        if(array_Wishboard.size() == 0){
            adpater = new AdapterListviewWishboard(getActivity(), array_Wishboard);
            rv_wishboard.setAdapter(adpater);
        }else {
            adpater.notifyDataSetChanged();
        }
    }
    private int totalItemCount,lastVisibleItem;
    private void implementScrollListener(final String session_id) {
        rv_wishboard.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                //if (loading) {
                //   if (totalItemCount > previousTotal) {
                //      loading = false;
                //      previousTotal = totalItemCount;
                //  }
                //}
                if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    // End has been reached

                    getWishboard getWishboard = new getWishboard(getContext(),15,Integer.valueOf(wishboard.getId()),session_id);
                    getWishboard.execute();
                    // Do something
                    loading = true;
                }
            }
        });
    }
    class GetDayUsed extends AsyncTask<String, Void,DayUsed> {

        Context context;

        String session_id;


        public GetDayUsed(Context context,String session_id) {
            this.context = context;
            this.session_id = session_id;

        }

        @Override
        protected DayUsed doInBackground(String... strings) {
            try {
                CheckSession checkSession = new CheckSession();
                SharedPreferences pref = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
                boolean check = checkSession.checkSession_id(pref.getString("session_id", null));
                if (!check) {
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("session_id", null);
                    editor.putString("active", null);
                    editor.commit();
                    Intent intent = new Intent(context, SignIn_Activity.class);
                    context.startActivity(intent);
                    this.cancel(true);
                }
            } catch (Exception exx) {
                Intent intent = new Intent(context, SignIn_Activity.class);
                context.startActivity(intent);
                this.cancel(true);
            }
            UserController userController = new UserController(context);
            return userController.GetDayUsed(session_id);
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(DayUsed dayUsed) {
            try {
                if (dayUsed == null) {

                } else {

                    if(dayUsed.getIs_active().equals("1")){
                        if(dayUsed.getDayExpirep()>0){
                            populatRecyclerView(session_id);
                            implementScrollListener(session_id);
                            MainAllActivity.setTxtTitle("Wishboard");
                        }else {
                            img_component.setVisibility(View.INVISIBLE);
                            Toast.makeText(context,"Upgrade your membership",Toast.LENGTH_SHORT).show();
                            Intent intent= new Intent(getActivity(), Upgrade.class);
                            startActivity(intent);
                        }
                    }else {
                        if (Integer.parseInt(dayUsed.getDayUsed()) > 14) {
                            img_component.setVisibility(View.INVISIBLE);
                            Toast.makeText(context,"Upgrade your membership",Toast.LENGTH_SHORT).show();
                            Intent intent= new Intent(getActivity(), Upgrade.class);
                            startActivity(intent);
                        } else if (Integer.parseInt(dayUsed.getDayUsed()) <= 14) {
                            populatRecyclerView(session_id);
                            implementScrollListener(session_id);
                            MainAllActivity.setTxtTitle("Wishboard");
                        }
                    }

                   /* if(Integer.parseInt(dayUsed.getDayUsed())>14 && !dayUsed.getIs_active().equals("1")){
                        img_component.setVisibility(View.INVISIBLE);
                        Toast.makeText(context,"Upgrade your membership",Toast.LENGTH_SHORT).show();
                        Intent intent= new Intent(getActivity(), Upgrade.class);
                        startActivity(intent);
                    }
                    else{

                        populatRecyclerView(session_id);
                        implementScrollListener(session_id);
                        MainAllActivity.setTxtTitle("Wishboard");
                    }*/
                }
            } catch (Exception e) {
            }
        }
    }

    class getWishboard extends AsyncTask<String,Void,List<Wishboard>>{
        ProgressDialog progressDialog;
        Context context;
        int top,from;
        String session_id;
        public getWishboard(Context context,int top,int from,String session_id){
            this.context = context;
            this.top = top;
            this.from =from;
            this.session_id = session_id;
        }
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(Information.noti_dialog);
            progressDialog.show();
        }

        @Override
        protected List<Wishboard> doInBackground(String... strings) {
            CheckSession checkSession = new CheckSession();
            SharedPreferences pref = context.getSharedPreferences("MyPref",context.MODE_PRIVATE);
            boolean check = checkSession.checkSession_id(pref.getString("session_id", null));
            if(!check){
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("session_id",null);
                editor.commit();
                Intent intent = new Intent(context, SignIn_Activity.class);
                context.startActivity(intent);
                this.cancel(true);
            }
            WishboardController wishboardController = new WishboardController();
            return wishboardController.getWishboardByTop(top,from,session_id);
        }

        @Override
        protected void onPostExecute(List<Wishboard> wishboards) {
            try {
                if (wishboards.size() > 0){
                    //array_Wishboard.removeAll(array_WishboardOld);
                    //array_WishboardOld=wishboards;
                    wishboard=wishboards.get(wishboards.size()-1);
                    array_Wishboard.addAll(wishboards);
                    adpater.notifyDataSetChanged();
                    loading = false;
                    progressDialog.dismiss();
                }else {
                    loading =true;
                    progressDialog.dismiss();
                }
            }catch (Exception e){

            }
            progressDialog.dismiss();
        }
    }

    class insertWishboard extends AsyncTask<String,Void,Boolean>{
        ProgressDialog progressDialog;
        Context context;

        public insertWishboard(Context context){
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(Information.noti_dialog);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            CheckSession checkSession = new CheckSession();
            SharedPreferences pref = context.getSharedPreferences("MyPref",context.MODE_PRIVATE);
            boolean check = checkSession.checkSession_id(pref.getString("session_id", null));
            if(!check){
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("session_id",null);
                editor.commit();
                Intent intent = new Intent(context, SignIn_Activity.class);
                context.startActivity(intent);
                this.cancel(true);
            }
            WishboardController wishboardController = new WishboardController();
            return wishboardController.insertWishboard(strings[0],strings[1],strings[2],strings[3]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            try {
                if (aBoolean == true){
                    Toast.makeText(context,Information.noti_insert_wishboard,Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }else {
                    Toast.makeText(context,Information.noti_no_insert_wishboard,Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }catch (Exception e){
                Toast.makeText(context,Information.noti_no_insert_wishboard,Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }
    }

}
