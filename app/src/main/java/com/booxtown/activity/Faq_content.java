package com.booxtown.activity;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.booxtown.R;

import com.booxtown.custom.NewAdapter;

public class Faq_content extends ExpandableListActivity implements View.OnClickListener{
    ImageView img_menu_bottom_location;
    ImageView img_menu_bottom_comment;
    ImageView img_menu_bottom_camera;
    ImageView img_menu_bottom_bag,img_menu;
    ImageView img_menu_bottom_user,img_menu_component,imageView_search_faqcontent;

    EditText editSearch;
    NewAdapter mNewAdapter;
    TextView title_menu;
    ExpandableListView expandableListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq_content);
        init();
        //expland content
        expandableListView = getExpandableListView();
        setGroupData();
        setChildGroupData();
        mNewAdapter = new NewAdapter(getAssets(),groupItem, childItem);
        mNewAdapter
                .setInflater(
                        (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE),
                        this);
        expandableListView.setAdapter(mNewAdapter);
        img_menu_component.setVisibility(View.GONE);
        title_menu.setText("FAQ");
        //picaso
        Picasso.with(getApplicationContext()).load(R.drawable.btn_locate_search).into(imageView_search_faqcontent);
        //end
        Picasso.with(getApplicationContext()).load(R.drawable.btn_sign_in_back).into(img_menu);
        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String s= editSearch.getText().toString().toLowerCase();
                ArrayList<String> Item = new ArrayList<String>();
                for (int k=0;k<groupItem.size();k++){
                    String item = groupItem.get(k).toLowerCase();
                    if(item.contains(s)){
                        Item.add(item);
                    }
                }
                mNewAdapter = new NewAdapter(getAssets(),Item, childItem);
                mNewAdapter
                        .setInflater(
                                (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), Faq_content.this);
                mNewAdapter.notifyDataSetChanged();
                expandableListView.setAdapter(mNewAdapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //bottom
        //--------------------------------------------------------------
        img_menu_bottom_location.setOnClickListener(this);
        img_menu_bottom_comment.setOnClickListener(this);
        img_menu_bottom_camera.setOnClickListener(this);
        img_menu_bottom_bag.setOnClickListener(this);
        img_menu_bottom_user.setOnClickListener(this);
        //---------------------------------------------------------------
    }

    public void init(){
        img_menu_component = (ImageView)findViewById(R.id.img_menu_component);
        title_menu = (TextView)findViewById(R.id.txt_title);
        imageView_search_faqcontent = (ImageView)findViewById(R.id.imageView_search_faqcontent);
        img_menu = (ImageView)findViewById(R.id.img_menu);

        img_menu_bottom_location = (ImageView)findViewById(R.id.img_menu_bottom_location);
        img_menu_bottom_comment = (ImageView)findViewById(R.id.img_menu_bottom_comment);
        img_menu_bottom_camera = (ImageView)findViewById(R.id.img_menu_bottom_camera);
        img_menu_bottom_bag = (ImageView)findViewById(R.id.img_menu_bottom_bag);
        img_menu_bottom_user = (ImageView)findViewById(R.id.img_menu_bottom_user);
        editSearch= (EditText)findViewById(R.id.editSearch);
    }
    public void setGroupData() {
        groupItem.add("TechNology");
        groupItem.add("Mobile");
        groupItem.add("Manufacturer");
        groupItem.add("Extras");
    }

    ArrayList<String> groupItem = new ArrayList<String>();
    ArrayList<Object> childItem = new ArrayList<Object>();

    public void setChildGroupData() {
        /**
         * Add Data For TecthNology
         */
        ArrayList<String> child = new ArrayList<String>();
        child.add("Thanks for the code. Please from now on share code in your question by editing it, " +
                "since it is not readable in comment. Also, please share your main layout");
        childItem.add(child);

        /**
         * Add Data For Mobile
         */
        child = new ArrayList<String>();
        child.add("Thanks for the code. Please from now on share code in your question by editing it," +
                " since it is not readable in comment. Also, please share your main layout");
        childItem.add(child);
        /**
         * Add Data For Manufacture
         */
        child = new ArrayList<String>();
        child.add("Thanks for the code. Please from now on share code in your question by editing it," +
                " since it is not readable in comment. Also, please share your main layout");
        childItem.add(child);
        /**
         * Add Data For Extras
         */
        child = new ArrayList<String>();
        child.add("Thanks for the code. Please from now on share code in your question by editing it," +
                " since it is not readable in comment. Also, please share your main layout");
        childItem.add(child);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_menu_bottom_location:
                Intent intent1 = new Intent(Faq_content.this,MainAllActivity.class);
                intent1.putExtra("key","1");
                startActivity(intent1);
                break;
            case R.id.img_menu_bottom_comment:
                Intent intent2 = new Intent(Faq_content.this,MainAllActivity.class);
                intent2.putExtra("key","2");
                startActivity(intent2);
                break;
            case R.id.img_menu_bottom_camera:
                Intent intent3 = new Intent(Faq_content.this,MainAllActivity.class);
                intent3.putExtra("key","3");
                startActivity(intent3);
                break;
            case R.id.img_menu_bottom_bag:
                Intent intent4 = new Intent(Faq_content.this,MainAllActivity.class);
                intent4.putExtra("key","4");
                startActivity(intent4);
                break;
            case R.id.img_menu_bottom_user:
                Intent intent5 = new Intent(Faq_content.this,MainAllActivity.class);
                intent5.putExtra("key","5");
                startActivity(intent5);
                break;

        }
    }
}
