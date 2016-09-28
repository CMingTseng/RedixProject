package redix.booxtown.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import redix.booxtown.R;
import redix.booxtown.adapter.AdapterInteractThreadDetails;
import redix.booxtown.custom.MenuBottomCustom;
import redix.booxtown.model.Interact;
import redix.booxtown.model.InteractComment;

/**
 * Created by Administrator on 30/08/2016.
 */
public class RespondActivity extends AppCompatActivity implements View.OnClickListener
{

    ImageView img_menu_bottom_location;
    ImageView img_menu_bottom_comment;
    ImageView img_menu_bottom_camera;
    ImageView img_menu_bottom_bag;
    ImageView img_menu_bottom_user;
    ArrayList<Interact> listInteract= new ArrayList<>();
    ListView listView;
    private MenuBottomCustom menu_bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respond);
        img_menu_bottom_location = (ImageView)findViewById(R.id.img_menu_bottom_location);
        img_menu_bottom_comment = (ImageView)findViewById(R.id.img_menu_bottom_comment);
        img_menu_bottom_camera = (ImageView)findViewById(R.id.img_menu_bottom_camera);
        img_menu_bottom_bag = (ImageView)findViewById(R.id.img_menu_bottom_bag);
        img_menu_bottom_user = (ImageView)findViewById(R.id.img_menu_bottom_user);
        //--------------------------------------------------
        //rank
        ImageView btn_rank_one = (ImageView)findViewById(R.id.imageView10);
        Picasso.with(getApplicationContext()).load(R.drawable.btn_rank_one).into(btn_rank_one);

        ImageView btn_rank_two = (ImageView)findViewById(R.id.imageView28);
        Picasso.with(getApplicationContext()).load(R.drawable.btn_rank_two).into(btn_rank_two);

        ImageView btn_rank_three = (ImageView)findViewById(R.id.imageView39);
        Picasso.with(getApplicationContext()).load(R.drawable.btn_rank_three).into(btn_rank_three);
        //end
        View view=(View) findViewById(R.id.menu_top_respond);
        TextView txtTitle=(TextView) view.findViewById(R.id.txt_title);
        txtTitle.setText("Respond");
        txtTitle.setGravity(Gravity.CENTER_VERTICAL);
        ImageView img_component=(ImageView) findViewById(R.id.img_menu_component);
        img_component.setVisibility(View.INVISIBLE);
        ImageView imageView_back=(ImageView) findViewById(R.id.img_menu);
        Picasso.with(getApplicationContext()).load(R.drawable.btn_sign_in_back).into(imageView_back);
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //--------------------------------------------------
//        View view_bottom = (View)findViewById(R.id.menu_bottom_respond);
//        menu_bottom=new MenuBottomCustom(view_bottom,this,4);
//        menu_bottom.setDefaut(4);
        //---------------------------------------------------------------
        TextView btn_add_book=(TextView) findViewById(R.id.txt_add_book_respond);
        btn_add_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RespondActivity.this,AddbookActivity.class);
                startActivity(intent);
            }
        });

        ArrayList<InteractComment> list= new ArrayList<>();
        InteractComment interactComment1= new InteractComment(2.5f,true,false,true,"Gandalf","If you want to buy best books order us1","June 12 at 5:14 pm");
        InteractComment interactComment2= new InteractComment(3.0f,true,true,true,"Gandalf2","If you want to buy best books order us2","June 12 at 5:14 pm");
        InteractComment interactComment3= new InteractComment(4.0f,false,false,true,"Gandalf3","If you want to buy best books order us3","June 12 at 5:14 pm");
        InteractComment interactComment4= new InteractComment(3.5f,true,false,false,"Gandalf4","If you want to buy best books order us4","June 12 at 5:14 pm");
        InteractComment interactComment5= new InteractComment(5.0f,true,false,false,"Gandalf5","If you want to buy best books order us5","June 12 at 5:14 pm");

        list.add(interactComment1);
        list.add(interactComment2);
        list.add(interactComment3);
        list.add(interactComment4);
        list.add(interactComment5);


        //-----------------------------------------------------------
        View view_comment= (View) findViewById(R.id.gridview_comments_respond);
        ListView listView= (ListView) view_comment.findViewById(R.id.listView_comment);
        //final AdapterInteractThreadDetails adapter = new AdapterInteractThreadDetails(RespondActivity.this,list);

        listView.setDivider(null);
        //listView.setAdapter(adapter);
        //Log.d("adapterRespone",String.valueOf(adapter.getCount()));

        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        //---------------------------------------------------------------
//--------------------------------------------------------------
        img_menu_bottom_location.setOnClickListener(this);
        img_menu_bottom_comment.setOnClickListener(this);
        img_menu_bottom_camera.setOnClickListener(this);
        img_menu_bottom_bag.setOnClickListener(this);
        img_menu_bottom_user.setOnClickListener(this);
        //---------------------------------------------------------------
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_menu_bottom_location:
                Intent intent1 = new Intent(RespondActivity.this,MainAllActivity.class);
                intent1.putExtra("key","1");
                startActivity(intent1);
                break;
            case R.id.img_menu_bottom_comment:
                Intent intent2 = new Intent(RespondActivity.this,MainAllActivity.class);
                intent2.putExtra("key","2");
                startActivity(intent2);
                break;
            case R.id.img_menu_bottom_camera:
                Intent intent3 = new Intent(RespondActivity.this,MainAllActivity.class);
                intent3.putExtra("key","3");
                startActivity(intent3);
                break;
            case R.id.img_menu_bottom_bag:
                Intent intent4 = new Intent(RespondActivity.this,MainAllActivity.class);
                intent4.putExtra("key","4");
                startActivity(intent4);
                break;
            case R.id.img_menu_bottom_user:
                Intent intent5 = new Intent(RespondActivity.this,MainAllActivity.class);
                intent5.putExtra("key","5");
                startActivity(intent5);
                break;

        }
    }
}
