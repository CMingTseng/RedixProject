package redix.booxtown.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import redix.booxtown.R;
import redix.booxtown.adapter.ListBookAdapter;
import redix.booxtown.controller.BookController;
import redix.booxtown.custom.BorderImage;
import redix.booxtown.custom.CustomListviewNotificationSwap;
import redix.booxtown.custom.MenuBottomCustom;
import redix.booxtown.model.Book;

public class NotificationSwapActivity extends AppCompatActivity implements View.OnClickListener {
    public static String [] prgmNameList={"Home","Notifications","FAQ"};
    ImageView img_menu_bottom_location;
    ImageView img_menu_bottom_comment;
    ImageView img_menu_bottom_camera;
    ImageView img_menu_bottom_bag;
    ImageView img_menu_bottom_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_swap);

        img_menu_bottom_location = (ImageView)findViewById(R.id.img_menu_bottom_location);
        img_menu_bottom_comment = (ImageView)findViewById(R.id.img_menu_bottom_comment);
        img_menu_bottom_camera = (ImageView)findViewById(R.id.img_menu_bottom_camera);
        img_menu_bottom_bag = (ImageView)findViewById(R.id.img_menu_bottom_bag);
        img_menu_bottom_user = (ImageView)findViewById(R.id.img_menu_bottom_user);
        // lấy được list sách swap đẻ đổ vào listview
        String trans_id= getIntent().getStringExtra("trans_id");
        bookAsync bookAsync= new bookAsync(NotificationSwapActivity.this,trans_id);
        bookAsync.execute();

        Button btn_notification_not_like= (Button)findViewById(R.id.btn_notification_not_like);
        btn_notification_not_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(NotificationSwapActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_notification_swap_button);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                Button btn_dialog_notification_swap = (Button)dialog.findViewById(R.id.btn_dialog_notification_swap);
                btn_dialog_notification_swap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(NotificationSwapActivity.this,Notification_Swap_Accept_Like.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });

                ImageView imageView =(ImageView)dialog.findViewById(R.id.imv_clode_dialog_not_like);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        //infor
        ImageView imv_menu_notification_infor1 = (ImageView)findViewById(R.id.imv_menu_notification_infor1);
        imv_menu_notification_infor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotificationSwapActivity.this,UserProfileActivity.class);
                startActivity(intent);
            }
        });
        //end

        //menu
        ImageView img_menu_component = (ImageView)findViewById(R.id.img_menu_component);
        img_menu_component.setVisibility(View.GONE);

        TextView txtTitle=(TextView)findViewById(R.id.txt_title);
        txtTitle.setText("Notifications");

        ImageView img_menu = (ImageView)findViewById(R.id.img_menu);
        img_menu.setImageResource(R.drawable.btn_sign_in_back);

        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //end
        img_menu_bottom_location.setOnClickListener(this);
        img_menu_bottom_comment.setOnClickListener(this);
        img_menu_bottom_camera.setOnClickListener(this);
        img_menu_bottom_bag.setOnClickListener(this);
        img_menu_bottom_user.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_menu_bottom_location:
                Intent intent1 = new Intent(NotificationSwapActivity.this,MainAllActivity.class);
                intent1.putExtra("key","1");
                startActivity(intent1);
                break;
            case R.id.img_menu_bottom_comment:
                Intent intent2 = new Intent(NotificationSwapActivity.this,MainAllActivity.class);
                intent2.putExtra("key","2");
                startActivity(intent2);
                break;
            case R.id.img_menu_bottom_camera:
                Intent intent3 = new Intent(NotificationSwapActivity.this,MainAllActivity.class);
                intent3.putExtra("key","3");
                startActivity(intent3);
                break;
            case R.id.img_menu_bottom_bag:
                Intent intent4 = new Intent(NotificationSwapActivity.this,MainAllActivity.class);
                intent4.putExtra("key","4");
                startActivity(intent4);
                break;
            case R.id.img_menu_bottom_user:
                Intent intent5 = new Intent(NotificationSwapActivity.this,MainAllActivity.class);
                intent5.putExtra("key","5");
                startActivity(intent5);
                break;

        }
    }

    class bookAsync extends AsyncTask<String,Void,List<Book>> {

        Context context;
        ProgressDialog dialog;
        List<Book> listemp;
        String trans_id;
        public bookAsync(Context context, String trans_id){
            this.context = context;
            this.trans_id=trans_id;
        }

        @Override
        protected List<Book> doInBackground(String... strings) {
            listemp = new ArrayList<>();
            BookController bookController = new BookController();
            listemp = bookController.bookTransactionId(trans_id);
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
            if (books == null){
                dialog.dismiss();
            }else {
                ListView listView = (ListView)findViewById(R.id.lv_notification_swap);
                listView.setAdapter(new CustomListviewNotificationSwap(NotificationSwapActivity.this, books, trans_id));
                dialog.dismiss();
            }
            super.onPostExecute(books);
        }
    }
}
