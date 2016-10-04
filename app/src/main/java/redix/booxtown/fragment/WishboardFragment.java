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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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

    ListView lv_wishboard;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_wishboard , container, false);

        ImageView img_menu = (ImageView)getActivity().findViewById(R.id.img_menu);
        Picasso.with(getContext()).load(R.drawable.btn_menu_locate).into(img_menu);
        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),MenuActivity.class);
                startActivity(intent);
            }
        });

        lv_wishboard = (ListView) view.findViewById(R.id.lv_wishboard);
        getWishboard getWishboard = new getWishboard(getContext());
        SharedPreferences pref = getContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor  = pref.edit();
        final String session_id = pref.getString("session_id", null);
        getWishboard.execute("1000000","0",session_id);
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

                TextView btn_submit_dialog_post_book_wishbroad = (TextView)dialog.findViewById(R.id.btn_submit_dialog_post_book_wishbroad);
                btn_submit_dialog_post_book_wishbroad.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextView editText_title_wishboard = (TextView)dialog.findViewById(R.id.editText_title_wishboard);
                        TextView editText_author_wishboard = (TextView)dialog.findViewById(R.id.editText_author_wishboard);
                        TextView editText_comment_wishboard = (TextView)dialog.findViewById(R.id.editText_comment_wishboard);
                        insertWishboard insertWishboard = new insertWishboard(getContext());
                        insertWishboard.execute(editText_title_wishboard.getText().toString(),editText_author_wishboard.getText().toString(),
                                editText_comment_wishboard.getText().toString(),session_id);
                        getWishboard getWishboard = new getWishboard(getContext());
                        getWishboard.execute("1000000","0",session_id);
                        dialog.dismiss();
                    }
                });
            }
        });

        return view;
    }

    class getWishboard extends AsyncTask<String,Void,List<Wishboard>>{
        ProgressDialog progressDialog;
        Context context;
        public getWishboard(Context context){
            this.context = context;
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
            return wishboardController.getAllWishboard(Integer.parseInt(strings[0]),Integer.parseInt(strings[1]),strings[2]);
        }

        @Override
        protected void onPostExecute(List<Wishboard> wishboards) {
            try {
                if (wishboards.size() > 0){
                    lv_wishboard.setAdapter(new AdapterListviewWishboard(getActivity(),wishboards));
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
