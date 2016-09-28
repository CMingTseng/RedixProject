package redix.booxtown.activity;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

import redix.booxtown.R;
import redix.booxtown.adapter.AdapterInteractThreadDetails;
import redix.booxtown.adapter.CustomPagerAdapter;
import redix.booxtown.custom.MenuBottomCustom;
import redix.booxtown.fragment.ListingsFragment;
import redix.booxtown.fragment.MainFragment;
import redix.booxtown.fragment.MyProfileFragment;
import redix.booxtown.model.Book;
import redix.booxtown.model.Explore;
import redix.booxtown.model.InteractComment;

/**
 * Created by Administrator on 29/08/2016.
 */
public class ListingsDetailActivity extends Fragment implements View.OnClickListener
{
    private  ListView listView;
    private TextView txt_add_book;
    private TextView txt_my_book;

    private ImageView imSwap;

    private ImageView imFree;

    private ImageView imBuy;
    ArrayList<Explore> listEx= new ArrayList<>();
    GridView grid;
    private MenuBottomCustom bottomListings;

    TextView txt_title_listings_detail;
    TextView txt_author_listings_detail;
    TextView txt_price_listings_detail;
    TextView txt_time_post_listings;
    TextView txt_genre_listing_detail;
    ProgressBar progressBar;
    TextView txt_tag;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_listings_detail,container,false);

        ImageView imageView_back=(ImageView) getActivity().findViewById(R.id.img_menu);
        Picasso.with(getContext()).load(R.drawable.btn_sign_in_back).into(imageView_back);
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFragment(new ListingsFragment());
            }
        });

        init(v);

        //btn_rank
        ImageView btn_rank_one = (ImageView)v.findViewById(R.id.img_rank1_listings);
        Picasso.with(getContext()).load(R.drawable.btn_rank_one).into(btn_rank_one);

        ImageView btn_rank_two = (ImageView)v.findViewById(R.id.img_rank2_listings);
        Picasso.with(getContext()).load(R.drawable.btn_rank_two).into(btn_rank_two);

        ImageView btn_rank_three = (ImageView)v.findViewById(R.id.img_rank3_listings);
        Picasso.with(getContext()).load(R.drawable.btn_rank_three).into(btn_rank_three);
        //end
//        RelativeLayout menu_search = (RelativeLayout)getActivity().findViewById(R.id.custom_search);
//        menu_search.setVisibility(View.GONE);

        TableRow tbTypebook = (TableRow) v.findViewById(R.id.row_type_book);
        EditText editText11 = (EditText) v.findViewById(R.id.editText11);
        String type = getArguments().getString(String.valueOf(R.string.valueListings));
        Log.d("dksdksdslkd",type.toString());

        MainAllActivity activity = (MainAllActivity) getActivity();

        imBuy = (ImageView) v.findViewById(R.id.img_buy_listing);
        imBuy.setOnClickListener(this);
        imFree = (ImageView) v.findViewById(R.id.img_free_listings);
        imSwap = (ImageView) v.findViewById(R.id.img_swap_listing);
        imSwap.setOnClickListener(this);
        activity.gettitle().setText("Listings");
        if (type.equals("1")){
            Book book = (Book)getArguments().getSerializable("item");
            imFree.setVisibility(View.GONE);
            ImageView img_close_dialog_unsubcribe = (ImageView) v.findViewById(R.id.img_close_dialog_unsubcribe);
            Picasso.with(getContext()).load(R.drawable.btn_close_filter).into(img_close_dialog_unsubcribe);
            editText11.setVisibility(View.GONE);
            img_close_dialog_unsubcribe.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)tbTypebook.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            ImageView img_menu_component = (ImageView)getActivity().findViewById(R.id.img_menu_component);
            img_menu_component.setVisibility(View.GONE);
            tbTypebook.setLayoutParams(params);
            txt_title_listings_detail.setText(book.getTitle());
            txt_author_listings_detail.setText(book.getAuthor());
            txt_price_listings_detail.setText("AED "+book.getPrice());
            txt_time_post_listings.setText(book.getCreate_date());
            txt_genre_listing_detail.setText(book.getGenre());
            txt_tag.setText(book.getHash_tag());
           // progressBar.setProgress(Integer.valueOf(book.getCondition()));
        }else {
            Book book = (Book) getArguments().getSerializable("bookedit");
            Log.d("dksdksdslkd",book.toString());

        }
        View view=(View) v.findViewById(R.id.layout_details);
        CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(getActivity());
        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mViewPager.setAdapter(mCustomPagerAdapter);

        CirclePageIndicator indicator = (CirclePageIndicator)v.findViewById(R.id.indicator);
        indicator.setViewPager(mViewPager);

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
        //final AdapterInteractThreadDetails adapter = new AdapterInteractThreadDetails(getActivity(),list);
        RelativeLayout.LayoutParams paramslist = (RelativeLayout.LayoutParams)tbTypebook.getLayoutParams();
        listView=(ListView) v.findViewById(R.id.listView_comment);
        listView.setDivider(null);
        //listView.setAdapter(adapter);
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        return v;

    }

    public void init(View view){
        txt_title_listings_detail = (TextView)view.findViewById(R.id.txt_title_listings_detail);
        txt_author_listings_detail = (TextView)view.findViewById(R.id.txt_author_listings_detail);
        txt_price_listings_detail = (TextView)view.findViewById(R.id.txt_price_listings_detail);
        txt_time_post_listings = (TextView)view.findViewById(R.id.txt_time_post_listings);
        txt_genre_listing_detail = (TextView)view.findViewById(R.id.txt_genre_listing_detail);
        txt_tag = (TextView)view.findViewById(R.id.txt_tag);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_swap_listing:
                swap();
                break;
            case R.id.img_free_listings:
                break;
            case R.id.img_buy_listing:
                buy();
                break;
        }
    }

    public void buy() {

        final Dialog dialog = new Dialog(getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_buy_listing);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();

                    ImageView btn_dialog_notification_swap = (ImageView) dialog.findViewById(R.id.close_buy_listings);
                    Picasso.with(getContext()).load(R.drawable.btn_close_filter).into(btn_dialog_notification_swap);
                    btn_dialog_notification_swap.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            dialog.dismiss();
                        }
                    });


        TextView btn_confirm=(TextView) dialog.findViewById(R.id.btn_confirm_buy_listing);
                    btn_confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            final Dialog dialog1 = new Dialog(getActivity());
                            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog1.setContentView(R.layout.dialog_request_sent_listing);
                            dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog1.show();

                            ImageView btn_close = (ImageView) dialog1.findViewById(R.id.close_sent_request_lising);

                            btn_close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog1.dismiss();
//                                    getActivity().finish();
                                }
                            });
                            TextView btn_back=(TextView) dialog1.findViewById(R.id.btn_back_home);
                            btn_back.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog1.dismiss();
                                    callFragment(new MainFragment());
                                }
                            });
                        }
                    });


    }



    public void swap(){
        Intent intent= new Intent(getActivity(), SwapActivity.class);
        startActivity(intent);

    }



    public void callFragment(Fragment fragment ){
        android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_main_all, fragment);
        transaction.commit();
    }


    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_listings_detail);
//        ProgressBar progressBar=(ProgressBar)findViewById(R.id.progressBar);
//        progressBar.setProgress(40);
//        final int type = (int) getIntent().getSerializableExtra("type");
//
//
//        View view_menu_top=(View)findViewById(R.id.menu_top_detail_listings);
//        TextView txtTitle=(TextView) view_menu_top.findViewById(R.id.txt_title);
//        txtTitle.setText("Listings");
//        txtTitle.setGravity(Gravity.CENTER_VERTICAL);
//        ImageView img_component=(ImageView)findViewById(R.id.img_menu_component);
//        img_component.setVisibility(View.INVISIBLE);
//
//        ImageView imageView_back=(ImageView)findViewById(R.id.img_menu);
//        imageView_back.setImageResource(R.drawable.back_interact);
//        imageView_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//
//
//        RelativeLayout layout_user=(RelativeLayout) findViewById(R.id.layout_user);
//        layout_user.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(ListingsDetailActivity.this,UserProfileActivity.class);
//                startActivity(intent);
//            }
//        });
//
//
//        //------------------------------------------------------------
//        View view_bottom=(View) findViewById(R.id.menu_bottom_listing_detail);
//        bottomListings=new MenuBottomCustom(view_bottom,this,3);
//        bottomListings.setDefaut(3);
//        //------------------------------------------------------------
//        View view=(View) findViewById(R.id.layout_details);
//        CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(this);
//        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.pager);
//        mViewPager.setAdapter(mCustomPagerAdapter);
//
//        CirclePageIndicator indicator = (CirclePageIndicator)findViewById(R.id.indicator);
//        indicator.setViewPager(mViewPager);
//
//        ArrayList<InteractComment> list= new ArrayList<>();
//        InteractComment interactComment1= new InteractComment(2.5f,true,false,true,"Gandalf","If you want to buy best books order us1","June 12 at 5:14 pm");
//        InteractComment interactComment2= new InteractComment(3.0f,true,true,true,"Gandalf2","If you want to buy best books order us2","June 12 at 5:14 pm");
//        InteractComment interactComment3= new InteractComment(4.0f,false,false,true,"Gandalf3","If you want to buy best books order us3","June 12 at 5:14 pm");
//        InteractComment interactComment4= new InteractComment(3.5f,true,false,false,"Gandalf4","If you want to buy best books order us4","June 12 at 5:14 pm");
//        InteractComment interactComment5= new InteractComment(5.0f,true,false,false,"Gandalf5","If you want to buy best books order us5","June 12 at 5:14 pm");
//
//        list.add(interactComment1);
//        list.add(interactComment2);
//        list.add(interactComment3);
//        list.add(interactComment4);
//        list.add(interactComment5);
//
//
//        //-----------------------------------------------------------
//        final AdapterInteractThreadDetails adapter = new AdapterInteractThreadDetails(ListingsDetailActivity.this,list);
//        listView=(ListView) view.findViewById(R.id.listView_comment);
//        listView.setDivider(null);
//        listView.setAdapter(adapter);
//        //---------------------------------------------------------------
//        EditText edit=(EditText) view.findViewById(R.id.edit_message);
//        ImageView img_send=(ImageView) view.findViewById(R.id.btn_send_comment_interact);
//        ImageView imgFree=(ImageView) view.findViewById(R.id.img_free_listings);
//        ImageView imgSwap=(ImageView) view.findViewById(R.id.img_swap_listing);
//        ImageView imgBuy=(ImageView) view.findViewById(R.id.img_buy_listing);
//        if(type!=1){
//            edit.setVisibility(View.GONE);
//            img_send.setVisibility(View.GONE);
//            imgFree.setVisibility(View.INVISIBLE);
//        }
//
//        if (type==3){
//            final Dialog dialog = new Dialog(ListingsDetailActivity.this);
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            dialog.setContentView(R.layout.dialog_buy_listing);
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            dialog.show();
//
//            ImageView btn_dialog_notification_swap = (ImageView) dialog.findViewById(R.id.close_buy_listings);
//            btn_dialog_notification_swap.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    dialog.dismiss();
//                }
//            });
//
//            TextView btn_confirm=(TextView) dialog.findViewById(R.id.btn_confirm_buy_listing);
//            btn_confirm.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//                    final Dialog dialog1 = new Dialog(ListingsDetailActivity.this);
//                    dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    dialog1.setContentView(R.layout.dialog_request_sent_listing);
//                    dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                    dialog1.show();
//
//                    ImageView btn_close = (ImageView) dialog1.findViewById(R.id.close_sent_request_lising);
//                    btn_close.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            dialog1.dismiss();
//                            finish();
//                        }
//                    });
//                    TextView btn_back=(TextView) dialog1.findViewById(R.id.btn_back_home);
//                    btn_back.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent inten= new Intent(ListingsDetailActivity.this, MainFragment.class);
//                            startActivity(inten);
//                            finish();
//                        }
//                    });
//                }
//            });
//
//        }
//        if(type==4){
//
//            final Dialog dialog1 = new Dialog(ListingsDetailActivity.this);
//            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            dialog1.setContentView(R.layout.dialog_request_sent_listing);
//            dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            dialog1.show();
//
//            ImageView btn_close = (ImageView) dialog1.findViewById(R.id.close_sent_request_lising);
//            btn_close.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    dialog1.dismiss();
//                    finish();
//                }
//            });
//            TextView btn_back=(TextView) dialog1.findViewById(R.id.btn_back_home);
//            btn_back.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent inten= new Intent(ListingsDetailActivity.this, MainFragment.class);
//                    startActivity(inten);
//                    finish();
//                }
//            });
//
//        }
//
//        imgBuy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(type==2){
//                    final Dialog dialog = new Dialog(ListingsDetailActivity.this);
//                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    dialog.setContentView(R.layout.dialog_buy_listing);
//                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                    dialog.show();
//
//                    ImageView btn_dialog_notification_swap = (ImageView) dialog.findViewById(R.id.close_buy_listings);
//                    btn_dialog_notification_swap.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//                            dialog.dismiss();
//                        }
//                    });
//
//                    TextView btn_confirm=(TextView) dialog.findViewById(R.id.btn_confirm_buy_listing);
//                    btn_confirm.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            dialog.dismiss();
//                            final Dialog dialog1 = new Dialog(ListingsDetailActivity.this);
//                            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                            dialog1.setContentView(R.layout.dialog_request_sent_listing);
//                            dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                            dialog1.show();
//
//                            ImageView btn_close = (ImageView) dialog1.findViewById(R.id.close_sent_request_lising);
//                            btn_close.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    dialog1.dismiss();
//                                    finish();
//                                }
//                            });
//                            TextView btn_back=(TextView) dialog1.findViewById(R.id.btn_back_home);
//                            btn_back.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent inten= new Intent(ListingsDetailActivity.this, MainFragment.class);
//                                    startActivity(inten);
//                                    finish();
//                                }
//                            });
//                        }
//                    });
//
//                }
//            }
//        });
//
//        imgSwap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(type==2){
//                    Intent intent= new Intent(ListingsDetailActivity.this, SwapActivity.class);
//                    startActivity(intent);
//                }
//            }
//        });
    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        bottomListings.setDefaut(3);
//    }

//}

