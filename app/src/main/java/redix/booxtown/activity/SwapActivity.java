package redix.booxtown.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import redix.booxtown.R;
import redix.booxtown.adapter.AdapterExplore;
import redix.booxtown.adapter.AdapterInteractThreadDetails;
import redix.booxtown.adapter.AdapterSwap;
import redix.booxtown.adapter.CustomPagerAdapter;
import redix.booxtown.adapter.ListBookAdapter;
import redix.booxtown.controller.BookController;
import redix.booxtown.custom.MenuBottomCustom;
import redix.booxtown.model.Book;
import redix.booxtown.model.BookSwap;
import redix.booxtown.model.Explore;
import redix.booxtown.model.InteractComment;

/**
 * Created by Administrator on 30/08/2016.
 */
public class SwapActivity extends AppCompatActivity {

    ArrayList<BookSwap> listSwap= new ArrayList<>();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swap_book);
        //------------------------------------------------------------
        View view_menu_top = (View) findViewById(R.id.menu_top_swap);
        TextView txtTitle = (TextView) view_menu_top.findViewById(R.id.txt_title);
        txtTitle.setText("Swap");
        txtTitle.setGravity(Gravity.CENTER_VERTICAL);
        ImageView img_component = (ImageView) findViewById(R.id.img_menu_component);
        img_component.setVisibility(View.INVISIBLE);
        ImageView imageView_back=(ImageView) findViewById(R.id.img_menu);
        imageView_back.setImageResource(R.drawable.btn_sign_in_back);
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //------------------------------------------------------------
//        listSwap.add("Any");
//        listSwap.add("The Last Painting of Sara de Vos");
//        listSwap.add("The Last Painting");
//        listSwap.add("Never a Dull Moment");
//        listSwap.add("A Nation on the Brink");
//        listSwap.add("1634 the Baltic War");
//        listSwap.add("Any");
//        listSwap.add("The Last Painting of Sara de Vos");
//        listSwap.add("The Last Painting");
//        listSwap.add("Never a Dull Moment");
//        listSwap.add("A Nation on the Brink");
//        listSwap.add("1634 the Baltic War");



        TextView btn_Swap=(TextView) findViewById(R.id.btn_swap);
        TextView btn_add_book=(TextView) findViewById(R.id.btn_add_book);

        btn_Swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(SwapActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custompopup_screen78);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                ImageView btnclose = (ImageView) dialog.findViewById(R.id.close_popup);
                TextView btnbacktohome = (TextView) dialog.findViewById(R.id.backhome);

                btnclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.hide();
                    }
                });

                btnbacktohome.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent iten = new Intent(SwapActivity.this,MainAllActivity.class);
                        startActivity(iten);
                    }
                });
            }
        });
        btn_add_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SwapActivity.this, AddbookActivity.class);
                startActivity(intent);
            }
        });

        listingAsync listingAsync = new listingAsync(SwapActivity.this);
        SharedPreferences pref = SwapActivity.this.getSharedPreferences("MyPref",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor  = pref.edit();
        String session_id = pref.getString("session_id", null);
        listingAsync.execute(session_id);
    }

    class listingAsync extends AsyncTask<String,Void,List<Book>> {

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
            if (books == null){
                dialog.dismiss();
            }else {
                BookSwap book;
                for(int i=0; i<books.size(); i++){
                    book= new BookSwap();
                    book.setIscheck(false);
                    book.setValue(books.get(i).getTitle());
                    book.setBook_id(books.get(i).getId());
                    listSwap.add(book);
                }
                AdapterSwap adapter = new AdapterSwap(SwapActivity.this,listSwap);
                listView=(ListView) findViewById(R.id.listView_swap);
                listView.setAdapter(adapter);
                dialog.dismiss();
            }
            super.onPostExecute(books);
        }
    }

}