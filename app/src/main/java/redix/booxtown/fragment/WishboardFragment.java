package redix.booxtown.fragment;

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
import android.support.v7.app.AppCompatActivity;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import redix.booxtown.R;
import redix.booxtown.activity.MenuActivity;
import redix.booxtown.adapter.AdapterCommentBook;
import redix.booxtown.adapter.AdapterExplore;
import redix.booxtown.adapter.AdapterListviewWishboard;
import redix.booxtown.controller.Information;
import redix.booxtown.controller.WishboardController;
import redix.booxtown.custom.CustomSearch;
import redix.booxtown.custom.CustomTabbarExplore;
import redix.booxtown.custom.MenuBottomCustom;
import redix.booxtown.model.Explore;
import redix.booxtown.model.Wishboard;

public class WishboardFragment extends Fragment {

    RecyclerView rv_wishboard;
    LinearLayoutManager linearLayoutManager;
    AdapterListviewWishboard adpater;
    EditText editText_title_wishboard,editText_author_wishboard,editText_comment_wishboard;
    List<Wishboard> array_Wishboard;
    String session_id;
    private int previousTotal = 0,visibleThreshold = 5;
    boolean loading = true,
            isLoading = true;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_wishboard , container, false);

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
        ImageView img_component=(ImageView) getActivity().findViewById(R.id.img_menu_component);
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
                editText_title_wishboard = (EditText)dialog.findViewById(R.id.editText_title_wishboard);
                editText_author_wishboard = (EditText)dialog.findViewById(R.id.editText_author_wishboard);
                editText_comment_wishboard = (EditText)dialog.findViewById(R.id.editText_comment_wishboard);

                Spannable wordtoSpan = new SpannableString("Comments (50 Character)");
                wordtoSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_text_hint)), 10, 23, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                editText_comment_wishboard.setHint(wordtoSpan);
                TextView btn_submit_dialog_post_book_wishbroad = (TextView)dialog.findViewById(R.id.btn_submit_dialog_post_book_wishbroad);
                btn_submit_dialog_post_book_wishbroad.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(editText_comment_wishboard.getText().toString().equals("")){
                            Toast.makeText(getContext(),Information.noti_check_wishboard,Toast.LENGTH_SHORT).show();
                        }else if(editText_comment_wishboard.getText().toString().length() >50){
                            Toast.makeText(getContext(),Information.noti_over_leter,Toast.LENGTH_SHORT).show();
                        }
                        else {
                            insertWishboard insertWishboard = new insertWishboard(getContext());
                            insertWishboard.execute(editText_title_wishboard.getText().toString(), editText_author_wishboard.getText().toString(), editText_comment_wishboard.getText().toString(), session_id);
                            /*getWishboard getWishboard = new getWishboard(getContext());
                            getWishboard.execute("1000000", "0", session_id);*/
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
        populatRecyclerView(session_id);
        implementScrollListener(session_id);
        return view;
    }

    private void populatRecyclerView(String session_id) {
        getWishboard getwishboard = new getWishboard(getContext(),15,0,session_id);
        getwishboard.execute();

    }

    private void implementScrollListener(final String session_id) {
        rv_wishboard.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = rv_wishboard.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold) && isLoading) {
                    // End has been reached
                    Wishboard dashBoard_lv = array_Wishboard.get(array_Wishboard.size()-1);
                    getWishboard getWishboard = new getWishboard(getContext(),15,Integer.valueOf(dashBoard_lv.getId()),session_id);
                    getWishboard.execute();
                    // Do something
                    loading = true;
                }
            }
        });
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
            WishboardController wishboardController = new WishboardController();
            return wishboardController.getWishboardByTop(top,from,session_id);
        }

        @Override
        protected void onPostExecute(List<Wishboard> wishboards) {
            try {
                if (wishboards.size() > 0){
                    array_Wishboard.addAll(wishboards);
                    adpater = new AdapterListviewWishboard(getActivity(), array_Wishboard);
                    rv_wishboard.setAdapter(adpater);
                    adpater.notifyDataSetChanged();
                    isLoading = true;
                    progressDialog.dismiss();
                }else {
                    isLoading =false;
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
