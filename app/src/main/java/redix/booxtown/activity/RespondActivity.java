package redix.booxtown.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import redix.booxtown.R;
import redix.booxtown.adapter.AdapterCommentBook;
import redix.booxtown.adapter.AdapterInteractThreadDetails;
import redix.booxtown.api.ServiceGenerator;
import redix.booxtown.controller.BookController;
import redix.booxtown.controller.CommentController;
import redix.booxtown.controller.Information;
import redix.booxtown.controller.WishboardController;
import redix.booxtown.custom.MenuBottomCustom;
import redix.booxtown.model.CommentBook;
import redix.booxtown.model.Interact;
import redix.booxtown.model.InteractComment;
import redix.booxtown.model.Wishboard;

/**
 * Created by Administrator on 30/08/2016.
 */
public class RespondActivity extends AppCompatActivity implements View.OnClickListener {
    AdapterCommentBook adapter;
    private ListView listView;
    ImageView img_menu_bottom_location,img_menu_bottom_comment,img_menu_bottom_camera,img_menu_bottom_bag,img_menu_bottom_user;

    List<String> listUser = new ArrayList<>();
    Wishboard wishboard;
    CircularImageView photo_author_post;
    TextView txt_author_post,txt_title_book_respond,txt_author_book_post,txt_content_post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respond);
        img_menu_bottom_location = (ImageView) findViewById(R.id.img_menu_bottom_location);
        img_menu_bottom_comment = (ImageView) findViewById(R.id.img_menu_bottom_comment);
        img_menu_bottom_camera = (ImageView) findViewById(R.id.img_menu_bottom_camera);
        img_menu_bottom_bag = (ImageView) findViewById(R.id.img_menu_bottom_bag);
        img_menu_bottom_user = (ImageView) findViewById(R.id.img_menu_bottom_user);
        //--------------------------------------------------
        //rank
        ImageView btn_rank_one = (ImageView) findViewById(R.id.imageView10);
        Picasso.with(getApplicationContext()).load(R.drawable.btn_rank_one).into(btn_rank_one);
        ImageView btn_rank_two = (ImageView) findViewById(R.id.imageView28);
        Picasso.with(getApplicationContext()).load(R.drawable.btn_rank_two).into(btn_rank_two);
        ImageView btn_rank_three = (ImageView) findViewById(R.id.imageView39);
        Picasso.with(getApplicationContext()).load(R.drawable.btn_rank_three).into(btn_rank_three);
        //end
        View view = (View) findViewById(R.id.menu_top_respond);
        TextView txtTitle = (TextView) view.findViewById(R.id.txt_title);
        txtTitle.setText("Respond");
        txtTitle.setGravity(Gravity.CENTER_VERTICAL);
        ImageView img_component = (ImageView) findViewById(R.id.img_menu_component);
        img_component.setVisibility(View.INVISIBLE);
        ImageView imageView_back = (ImageView) findViewById(R.id.img_menu);
        Picasso.with(getApplicationContext()).load(R.drawable.btn_sign_in_back).into(imageView_back);
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        TextView btn_add_book = (TextView) findViewById(R.id.txt_add_book_respond);
        btn_add_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RespondActivity.this,AddbookActivity.class);
                intent.putExtra("type",1);
                startActivity(intent);
            }
        });
        photo_author_post=(CircularImageView) findViewById(R.id.photo_author_post);
        txt_author_post=(TextView) findViewById(R.id.txt_author_post) ;
        txt_title_book_respond=(TextView) findViewById(R.id.txt_title_book_respond) ;
        txt_author_book_post=(TextView) findViewById(R.id.txt_author_book_post) ;
        txt_content_post=(TextView) findViewById(R.id.txt_content_post) ;
        try {
            wishboard = (Wishboard) getIntent().getSerializableExtra("wishboard");
            if(wishboard.getPhoto().length()>3) {
                Picasso.with(RespondActivity.this).load(ServiceGenerator.API_BASE_URL + "booxtown/rest/getImage?username=" + wishboard.getUsername() + "&image=" + wishboard.getPhoto().substring(wishboard.getUsername().length() + 3, wishboard.getPhoto().length())).error(R.mipmap.user_empty).into(photo_author_post);
            }else{
                Picasso.with(RespondActivity.this). load(R.mipmap.user_empty).
                        into(photo_author_post);
            }
            txt_author_post.setText(wishboard.getUsername());
            txt_title_book_respond.setText("Book: "+ wishboard.getTitle());
            txt_author_book_post.setText("Author: "+ wishboard.getAuthor());
            txt_content_post.setText(wishboard.getComment());
        }catch (Exception ex){
        }
        photo_author_post= (CircularImageView) findViewById(R.id.photo_author_post);
        //-----------------------------------------------------------
        listView = (ListView) findViewById(R.id.listView_comment);
        listView.setDivider(null);

        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        listView.setDivider(null);

        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        //---------------------------------------------------------------
        getComment comment = new getComment(RespondActivity.this, wishboard.getId());
        comment.execute();
        //--------------------------------------------------------------
        img_menu_bottom_location.setOnClickListener(this);
        img_menu_bottom_comment.setOnClickListener(this);
        img_menu_bottom_camera.setOnClickListener(this);
        img_menu_bottom_bag.setOnClickListener(this);
        img_menu_bottom_user.setOnClickListener(this);
        //---------------------------------------------------------------
        ImageView img_close_dialog_unsubcribe= (ImageView) findViewById(R.id.img_close_dialog_unsubcribe);
        final EditText message= (EditText) findViewById(R.id.editText11) ;
        SharedPreferences pref = RespondActivity.this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor  = pref.edit();
        final String session_id = pref.getString("session_id", null);
        img_close_dialog_unsubcribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertComment insertComment1 = new insertComment(RespondActivity.this);
                insertComment1.execute(session_id,message.getText().toString(),wishboard.getId());
                message.setText("");
                getComment comment = new getComment(RespondActivity.this,wishboard.getId());
                comment.execute();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_menu_bottom_location:
                Intent intent1 = new Intent(RespondActivity.this, MainAllActivity.class);
                intent1.putExtra("key", "1");
                startActivity(intent1);
                break;
            case R.id.img_menu_bottom_comment:
                Intent intent2 = new Intent(RespondActivity.this, MainAllActivity.class);
                intent2.putExtra("key", "2");
                startActivity(intent2);
                break;
            case R.id.img_menu_bottom_camera:
                Intent intent3 = new Intent(RespondActivity.this, MainAllActivity.class);
                intent3.putExtra("key", "3");
                startActivity(intent3);
                break;
            case R.id.img_menu_bottom_bag:
                Intent intent4 = new Intent(RespondActivity.this, MainAllActivity.class);
                intent4.putExtra("key", "4");
                startActivity(intent4);
                break;
            case R.id.img_menu_bottom_user:
                Intent intent5 = new Intent(RespondActivity.this, MainAllActivity.class);
                intent5.putExtra("key", "5");
                startActivity(intent5);
                break;

        }
    }

    class getComment extends AsyncTask<Void, Void, List<CommentBook>> {

        Context context;
        String book_id;
        int top, from;
        ProgressDialog progressDialog;

        public getComment(Context context, String book_id) {
            this.context = context;
            this.book_id = book_id;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(Information.noti_dialog);
            progressDialog.show();
        }

        @Override
        protected List<CommentBook> doInBackground(Void... voids) {
            WishboardController bookController = new WishboardController();
            return bookController.getCommnetWishboard(book_id,1000,0);
        }

        @Override
        protected void onPostExecute(List<CommentBook> commentBooks) {
            try {
                if (commentBooks.size() > 0) {
                    adapter = new AdapterCommentBook(context, commentBooks);
                    listView.setAdapter(adapter);

                    if (!listUser.contains(wishboard.getUser_id() + "")) {
                        listUser.add(wishboard.getUser_id() + "");
                    }
                    for (int i = 0; i < commentBooks.size(); i++) {
                        if (!listUser.contains(commentBooks.get(i).getUser_id() + "")) {
                            listUser.add(commentBooks.get(i).getUser_id() + "");
                        }
                    }

                    progressDialog.dismiss();
                } else {
                    Toast.makeText(context, Information.noti_no_data_listing, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            } catch (Exception e) {

            }


            progressDialog.dismiss();
        }
    }

    class insertComment extends AsyncTask<String,Void,Boolean>{

        Context context;
        ProgressDialog dialog;
        public insertComment(Context context){
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.setMessage("please waiting...");
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            CommentController comment = new CommentController();
            return comment.insertComment(strings[0],strings[1],"0","0",strings[2]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            try {
                if(aBoolean == true){
                    Toast.makeText(context,"success",Toast.LENGTH_SHORT).show();
//                    int count= threads.getNum_comment()+1;

                    //SharedPreferences pref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                   // String session_id = pref.getString("session_id", null);

                   // UserID us= new UserID(getContext());
                    //us.execute(session_id);

                    dialog.dismiss();
                }else {
                    Toast.makeText(context,"no success",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }catch (Exception e){
                dialog.dismiss();
            }
        }
    }

}
