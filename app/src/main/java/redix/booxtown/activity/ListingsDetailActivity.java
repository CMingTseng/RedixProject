package redix.booxtown.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import redix.booxtown.R;
import redix.booxtown.adapter.AdapterCommentBook;
import redix.booxtown.adapter.CustomPagerAdapter;
import redix.booxtown.adapter.ListBookAdapter;
import redix.booxtown.api.ServiceGenerator;
import redix.booxtown.controller.BookController;
import redix.booxtown.controller.CommentController;
import redix.booxtown.controller.IconMapController;
import redix.booxtown.controller.Information;
import redix.booxtown.controller.NotificationController;
import redix.booxtown.controller.ObjectCommon;
import redix.booxtown.controller.UserController;
import redix.booxtown.fragment.ExploreFragment;
import redix.booxtown.fragment.ListingsFragment;
import redix.booxtown.fragment.MainFragment;
import redix.booxtown.fragment.MyProfileFragment;
import redix.booxtown.fragment.NotificationFragment;
import redix.booxtown.model.Book;
import redix.booxtown.model.CommentBook;
import redix.booxtown.model.Notification;
import redix.booxtown.model.User;

/**
 * Created by Administrator on 29/08/2016.
 */
public class ListingsDetailActivity extends Fragment implements OnMapReadyCallback {
    //private ListView listView;

    private RecyclerView rv_comment;
    LinearLayoutManager linearLayoutManager;
    List<CommentBook> arr_commnet = new ArrayList<>();
    AdapterCommentBook adapter;
    boolean loading = true,
            isLoading = true;
    private int previousTotal = 0;
    private int visibleThreshold = 5;
    RelativeLayout rlv_comment;

    private ImageView imSwap, imSwap2, imFree, imFree2, imBuy, imBuy2, imageView_back,btn_rank_one,btn_rank_two,btn_rank_three;
    TextView txt_listed_by, txt_tag, txt_title_listings_detail, txt_author_listings_detail, txt_price_listings_detail, txt_time_post_listings, txt_genre_listing_detail;
    CircularImageView icon_user_listing_detail;
    ProgressBar progressBar;

    RatingBar ratingBar_userprofile;
    Book book;
    List<String> listUser = new ArrayList<>();
    //map
    MarkerOptions marker;
    private GoogleMap mMap;
    String type;
    int back;
    ImageView img_component,img_close_dialog_unsubcribe;
    String session_id;
    TableRow tbTypebook,tbTypebook2;
    EditText editText11;
    //end
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_listings_detail, container, false);
        init(v);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.fragment_detail);
        mapFragment.getMapAsync(ListingsDetailActivity.this);
        SharedPreferences pref = getContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        session_id = pref.getString("session_id", null);

        imageView_back = (ImageView) getActivity().findViewById(R.id.img_menu);
        Bitmap btm = BitmapFactory.decodeResource(getResources(),R.drawable.btn_sign_in_back);
        imageView_back.setImageBitmap(btm);

        img_component = (ImageView) getActivity().findViewById(R.id.img_menu_component);
        img_component.setVisibility(View.GONE);

        try {
            //btn_rank
            //end

            type = getArguments().getString(String.valueOf(R.string.valueListings));
            back = getArguments().getInt("back");
            book = (Book) getArguments().getSerializable("item");
            RelativeLayout layout_comments = (RelativeLayout) v.findViewById(R.id.layout_comment);

            ratingBar_userprofile.setRating(book.getRating());
            LayerDrawable stars = (LayerDrawable) ratingBar_userprofile.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(Color.rgb(255,224,0), PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(0).setColorFilter(getResources().getColor(R.color.bg_rating), PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(1).setColorFilter(getResources().getColor(R.color.bg_rating), PorterDuff.Mode.SRC_ATOP); // for half filled stars
            DrawableCompat.setTint(DrawableCompat.wrap(stars.getDrawable(1)),getResources().getColor(R.color.bg_rating));


            if (book.getPhoto().length() > 3) {
                Picasso.with(getContext())
                        .load(ServiceGenerator.API_BASE_URL + "booxtown/rest/getImage?username=" + book.getAuthor() + "&image=" + book.getPhoto().substring(book.getUsername().length() + 3, book.getPhoto().length()))
                        .placeholder(R.drawable.blank_image).
                        into(icon_user_listing_detail);
            } else {
                Picasso.with(getContext())
                        .load(R.drawable.blank_image)
                        .into(icon_user_listing_detail);
            }
            txt_listed_by.setText(book.getUsername());
            if (type.equals("4")) {
                HomeActivity activity = (HomeActivity) getActivity();
                activity.getTxtTitle().setText("Listings");
            } else {
                MainAllActivity activity = (MainAllActivity) getActivity();
                activity.gettitle().setText("Listings");
            }
            imageView_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type.equals("1")) {
                        callFragment(new MainFragment());
                    } else if (type.equals("2")) {
                        callFragment(new ExploreFragment());
                        MainAllActivity.setImg_component();
                    } else if (type.equals("3")) {
                        callFragment(new ListingsFragment());
                    }else if (type.equals("5")) {
                        Intent intent=new Intent(getContext(),UserProfileActivity.class);
                        intent.putExtra("user",Integer.parseInt(book.getUser_id()));
                        startActivity(intent);
                        getActivity().finish();
                    }
                    else if (type.equals("5")) {
                        HomeActivity homeActivity = (HomeActivity) getActivity();
                        homeActivity.getTxtTitle().setText("Notifications");
                        homeActivity.callFragment(new NotificationFragment());
                    }
                    else if (type.equals("6")) {
                        callFragment(new MyProfileFragment());
                    }
                    if(back == 2){
                       //
                    }else if(back == 1){
                        //callFragment(new ListingsFragment());
                    }
                }
            });

            if (type.equals("1")) {
                View view_search = (View) getActivity().findViewById(R.id.custom_search);
                view_search.setVisibility(View.GONE);
                Picasso.with(getContext()).load(R.drawable.btn_close_filter).into(img_close_dialog_unsubcribe);
                rlv_comment.setVisibility(View.GONE);
                editText11.setVisibility(View.GONE);
                img_close_dialog_unsubcribe.setVisibility(View.GONE);
                tbTypebook.setVisibility(View.GONE);
                tbTypebook2.setVisibility(View.VISIBLE);
            } else {
                tbTypebook.setVisibility(View.VISIBLE);
                tbTypebook2.setVisibility(View.GONE);

                final float scale = getContext().getResources().getDisplayMetrics().density;
                int pixelsMargin = (int) (-50 * scale + 0.5f);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) layout_comments.getLayoutParams();

                params.topMargin = pixelsMargin;
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layout_comments.setLayoutParams(params);
            }
            getUser getUser = new getUser(getContext(),Integer.valueOf(book.getUser_id()));
            getUser.execute();
            setData(book, v, type);
            //-----------------------------------------------------------

            img_close_dialog_unsubcribe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertComment insertComment1 = new insertComment(getContext());
                    insertComment1.execute(session_id, editText11.getText().toString(), book.getId());
                    editText11.setText("");
                    getComment comment;
                    CommentBook  commentBook;
                    if(arr_commnet.size() == 0){
                        comment = new getComment(getContext(), book.getId(), 15,0);
                    }else {
                        commentBook = arr_commnet.get(arr_commnet.size() - 1);
                        comment = new getComment(getContext(), book.getId(), 15, commentBook.getId());
                    }
                    comment.execute();

                }
            });

        }catch (Exception exx){
            String ess= exx.getMessage();
        }
        populatRecyclerView(book.getId());
        implementScrollListener(book.getId());
        return v;
    }

    public void setData(final Book book, View v, String type) {
        txt_title_listings_detail.setText(book.getTitle());
        txt_author_listings_detail.setText("by " + book.getAuthor());
        txt_price_listings_detail.setText("AED " + book.getPrice());

        String date_post = book.getCreate_date().substring(8,10)+"-"+book.getCreate_date().substring(5,7)+"-"+
                book.getCreate_date().substring(2,4);

        txt_time_post_listings.setText("Posted on "+date_post);
        txt_genre_listing_detail.setText(book.getGenre());
        txt_tag.setText("Hash tag: " + book.getHash_tag());
        View view = (View) v.findViewById(R.id.layout_details);
        String[] image = book.getPhoto().split(";");

        CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(getActivity(), book);
        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mViewPager.setAdapter(mCustomPagerAdapter);

        CirclePageIndicator indicator = (CirclePageIndicator) v.findViewById(R.id.indicator);
        indicator.setViewPager(mViewPager);
        progressBar.setProgress(Integer.valueOf(book.getCondition()));
        char array[] = book.getAction().toCharArray();
        final String swap = String.valueOf(array[0]);
        final String free = String.valueOf(array[1]);
        final String buy = String.valueOf(array[2]);
        String icon = IconMapController.iconExplorer(swap, free, buy);
        if (icon.equals("icon_swap")) {
            imFree.setVisibility(View.GONE);
            imBuy.setVisibility(View.GONE);
            imSwap.setVisibility(View.VISIBLE);

            imFree2.setVisibility(View.GONE);
            imBuy2.setVisibility(View.GONE);
            imSwap2.setVisibility(View.VISIBLE);

        }
        if (icon.equals("icon_free")) {
            imBuy.setVisibility(View.GONE);
            imSwap.setVisibility(View.GONE);
            imFree.setVisibility(View.VISIBLE);

            imBuy2.setVisibility(View.GONE);
            imSwap2.setVisibility(View.GONE);
            imFree2.setVisibility(View.VISIBLE);
        }
        if (icon.equals("icon_buy")) {
            imSwap.setVisibility(View.GONE);
            imFree.setVisibility(View.GONE);
            imBuy.setVisibility(View.VISIBLE);

            imSwap2.setVisibility(View.GONE);
            imFree2.setVisibility(View.GONE);
            imBuy2.setVisibility(View.VISIBLE);
        }
        if (icon.equals("swapfree")) {
            imSwap.setVisibility(View.VISIBLE);
            imFree.setVisibility(View.VISIBLE);
            imBuy.setVisibility(View.GONE);

            imSwap2.setVisibility(View.VISIBLE);
            imFree2.setVisibility(View.VISIBLE);
            imBuy2.setVisibility(View.GONE);
        }
        if (icon.equals("swapbuy")) {
            imSwap.setVisibility(View.VISIBLE);
            imFree.setVisibility(View.GONE);
            imBuy.setVisibility(View.VISIBLE);

            imSwap2.setVisibility(View.VISIBLE);
            imFree2.setVisibility(View.GONE);
            imBuy2.setVisibility(View.VISIBLE);
        }
        if (icon.equals("freebuy")) {
            imFree.setVisibility(View.VISIBLE);
            imSwap.setVisibility(View.GONE);
            imBuy.setVisibility(View.VISIBLE);

            imFree2.setVisibility(View.VISIBLE);
            imSwap2.setVisibility(View.GONE);
            imBuy2.setVisibility(View.VISIBLE);
        }
        if (icon.equals("option")) {
            imFree.setVisibility(View.VISIBLE);
            imSwap.setVisibility(View.VISIBLE);
            imBuy.setVisibility(View.VISIBLE);

            imFree2.setVisibility(View.VISIBLE);
            imSwap2.setVisibility(View.VISIBLE);
            imBuy2.setVisibility(View.VISIBLE);
        }
        if (!type.equals("3")||!type.equals("6")) {
            imSwap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listingAsync listingAsync = new listingAsync(getContext());
                    listingAsync.execute(session_id);

                }
            });
            imBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    buy();
                }
            });

            imSwap2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listingAsync listingAsync = new listingAsync(getContext());
                    listingAsync.execute(session_id);
                }
            });
            imBuy2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    buy();
                }
            });
        }
    }

    public void init(View view) {
        rlv_comment=(RelativeLayout) view.findViewById(R.id.rlv_comment);
        btn_rank_one = (ImageView) view.findViewById(R.id.img_rank1_listings);
        btn_rank_two = (ImageView) view.findViewById(R.id.img_rank2_listings);
        btn_rank_three = (ImageView) view.findViewById(R.id.img_rank3_listings);

        linearLayoutManager = new LinearLayoutManager(getContext());
        rv_comment = (RecyclerView)view.findViewById(R.id.rv_comment);
        rv_comment.setLayoutManager(linearLayoutManager);
        rv_comment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        icon_user_listing_detail = (CircularImageView) view.findViewById(R.id.icon_user_listing_detail);
        ratingBar_userprofile = (RatingBar) view.findViewById(R.id.myRatingBar);

        img_close_dialog_unsubcribe  = (ImageView) view.findViewById(R.id.img_close_dialog_unsubcribe);
        editText11 = (EditText) view.findViewById(R.id.editText11);

        tbTypebook = (TableRow) view.findViewById(R.id.row_type_book);
        tbTypebook2 = (TableRow) view.findViewById(R.id.row_type_book2);
        img_component = (ImageView) getActivity().findViewById(R.id.img_menu_component);
        imageView_back = (ImageView) getActivity().findViewById(R.id.img_menu);

        imBuy = (ImageView) view.findViewById(R.id.img_buy_listing);
        imFree = (ImageView) view.findViewById(R.id.img_free_listings);
        imSwap = (ImageView) view.findViewById(R.id.img_swap_listing);
        imBuy2 = (ImageView) view.findViewById(R.id.img_buy_listing2);
        imFree2 = (ImageView) view.findViewById(R.id.img_free_listings2);
        imSwap2 = (ImageView) view.findViewById(R.id.img_swap_listing2);
        txt_listed_by = (TextView) view.findViewById(R.id.txt_listed_by);


        txt_title_listings_detail = (TextView) view.findViewById(R.id.txt_title_listings_detail);
        txt_author_listings_detail = (TextView) view.findViewById(R.id.txt_author_listings_detail);
        txt_price_listings_detail = (TextView) view.findViewById(R.id.txt_price_listings_detail);
        txt_time_post_listings = (TextView) view.findViewById(R.id.txt_time_post_listings);
        txt_genre_listing_detail = (TextView) view.findViewById(R.id.txt_genre_listing_detail);
        txt_tag = (TextView) view.findViewById(R.id.txt_tag);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
    }

    private void populatRecyclerView(String book_id) {
        getComment getcomment = new getComment(getContext(),book_id,15,0);
        getcomment.execute();
        if (arr_commnet.size() == 0){
            adapter = new AdapterCommentBook(getContext(),arr_commnet);
            rv_comment.setAdapter(adapter);
        }else{
            adapter.notifyDataSetChanged();
        }
    }

    private void implementScrollListener(final String book_id) {
        rv_comment.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = rv_comment.getChildCount();
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
                    CommentBook commentBook= arr_commnet.get(arr_commnet.size()-1);
                    getComment getcomment = new getComment(getContext(),book_id,15,commentBook.getId());
                    getcomment.execute();
                    // Do something
                    loading = true;
                }
            }
        });
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
        TextView btn_confirm = (TextView) dialog.findViewById(R.id.btn_confirm_buy_listing);
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
                TextView btn_back = (TextView) dialog1.findViewById(R.id.btn_yes);
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

    public void swap(Book book) {
        Intent intent = new Intent(getActivity(), SwapActivity.class);
        intent.putExtra("Book", book);
        startActivity(intent);
    }

    public void callFragment(Fragment fragment) {
        android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_main_all, fragment);
        transaction.commit();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setAllGesturesEnabled(true);
            mMap.setTrafficEnabled(true);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(book.getLocation_latitude(), book.getLocation_longitude()), 9));
            addMarker(book);
        } catch (Exception e) {
        }
    }

    public Bitmap resizeMapIcons(String icon, int width, int height) {
        Bitmap imageBitmap;
        Bitmap resizedBitmap;
        try {
            imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(icon, "drawable", getActivity().getPackageName()));
            resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
            return resizedBitmap;
        } catch (Exception e) {
        }
        return null;
    }

    public void addMarker(final Book books) {
        try {
            marker = new MarkerOptions().position(new LatLng(books.getLocation_latitude(), books.getLocation_longitude())).title("Hello Maps");
            // Changing marker icon
            char array[] = books.getAction().toCharArray();
            String swap = String.valueOf(array[0]);
            String buy = String.valueOf(array[1]);
            String free = String.valueOf(array[2]);
            String icon = IconMapController.icon(swap, free, buy);
            if (icon != null) {
                marker.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(icon, 110, 150)));
            }
            mMap.addMarker(marker);
        } catch (Exception e) {

        }
    }

    class getComment extends AsyncTask<Void, Void, List<CommentBook>> {

        Context context;
        String book_id;
        int top, from;
        ProgressDialog progressDialog;

        public getComment(Context context, String book_id,int top,int from) {
            this.context = context;
            this.book_id = book_id;
            this.top = top;
            this.from = from;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(Information.noti_dialog);
            progressDialog.show();
        }

        @Override
        protected List<CommentBook> doInBackground(Void... voids) {
            BookController bookController = new BookController();
            return bookController.getTopCommnetBook(book_id,top,from);
        }

        @Override
        protected void onPostExecute(List<CommentBook> commentBooks) {
            try {
                if (commentBooks.size() > 0) {
                    arr_commnet.addAll(commentBooks);
                    adapter.notifyDataSetChanged();
                    isLoading = true;
                    if (!listUser.contains(book.getUser_id())) {
                        listUser.add(book.getUser_id());
                    }
                    for (int i = 0; i < commentBooks.size(); i++) {
                        if (!listUser.contains(commentBooks.get(i).getUser_id() + "")) {
                            listUser.add(commentBooks.get(i).getUser_id() + "");
                        }
                    }

                    progressDialog.dismiss();
                } else {
                    isLoading = false;
                    progressDialog.dismiss();
                }
            } catch (Exception e) {

            }


            progressDialog.dismiss();
        }
    }

    class insertComment extends AsyncTask<String, Void, Boolean> {

        Context context;
        ProgressDialog dialog;

        public insertComment(Context context) {
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
            return comment.insertComment(strings[0], strings[1], "0", strings[2], "0");
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            try {
                if (aBoolean == true) {
                    Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
//                    int count= threads.getNum_comment()+1;

                    SharedPreferences pref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                    String session_id = pref.getString("session_id", null);

                    UserID us = new UserID(getContext());
                    us.execute(session_id);

                    dialog.dismiss();
                } else {
                    Toast.makeText(context, "no success", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            } catch (Exception e) {
                dialog.dismiss();
            }
        }
    }

    class UserID extends AsyncTask<String, Void, String> {
        Context context;

        public UserID(Context context) {
            this.context = context;
        }

        ProgressDialog dialog;

        @Override
        protected String doInBackground(String... strings) {
            UserController userController = new UserController();
            String user_id = userController.getUserID(strings[0]);
            return user_id;
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
        protected void onPostExecute(String user_ID) {
            try {
                //if(!threads.getUser_id().equals(user_ID)) {
                SharedPreferences pref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                String username = pref.getString("username", null);

                List<Hashtable> list = new ArrayList<>();
                for (int i = 0; i < listUser.size(); i++) {
                    String s = listUser.get(i);
                    if (!listUser.get(i).equals(user_ID)) {
                        Notification notification = new Notification("Comment in book posts", book.getId(), "11");
                        Hashtable obj = ObjectCommon.ObjectDymanic(notification);
                        obj.put("user_id", listUser.get(i));
                        obj.put("messages", "Comment book " + book.getTitle() + " by: " + username);
                        list.add(obj);
                    }
                }

                NotificationController controller = new NotificationController();
                controller.sendNotification(list);
                //}
            } catch (Exception e) {
                Toast.makeText(context, "no data", Toast.LENGTH_LONG).show();
            }
            dialog.dismiss();
        }
    }

    class listingAsync extends AsyncTask<String,Void,List<Book>>{

        Context context;
        ProgressDialog dialog;
        public listingAsync(Context context){
            this.context = context;
        }

        @Override
        protected List<Book> doInBackground(String... strings) {
            BookController bookController = new BookController();
            return bookController.getAllBookById(strings[0]);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            try {
                if (books == null) {
                    dialog.dismiss();
                } else {
                    if(books.size() == 0){
                        final Dialog dialog = new Dialog(getContext());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_check_mybook);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();

                        ImageView img_close_dialog_unsubcribe = (ImageView)dialog.findViewById(R.id.imageView_close_dialog);
                        Picasso.with(getActivity()).load(R.drawable.btn_close_filter).into(img_close_dialog_unsubcribe);
                        img_close_dialog_unsubcribe.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        TextView btn_unsubcribe = (TextView)dialog.findViewById(R.id.tv_addbook_checklist);
                        btn_unsubcribe.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(), AddbookActivity.class);
                                intent.putExtra("type",0);
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        });
                    }else {
                        swap(book);
                    }
                    dialog.dismiss();
                }
            }catch (Exception e){}
        }
    }

    class getUser extends AsyncTask<Void,Void,List<User>>{

        Context context;
        int user_id;
        ProgressDialog progressDialog;
        public getUser(Context context,int user_id){
            this.context = context;
            this.user_id = user_id;
        }
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(Information.noti_dialog);
            progressDialog.show();
        }

        @Override
        protected List<User> doInBackground(Void... voids) {
            UserController userController = new UserController();
            return userController.getByUserId(user_id);
        }

        @Override
        protected void onPostExecute(List<User> user) {
            try {
                if (user.size() > 0){
                    //set rank
                    if(user.get(0).getContributor() == 0){
                        btn_rank_one.setVisibility(View.VISIBLE);
                        Bitmap btn1 = BitmapFactory.decodeResource(getResources(),R.drawable.conbitrutor_one);
                        btn_rank_one.setImageBitmap(btn1);

                    }else{
                        Bitmap btn1 = BitmapFactory.decodeResource(getResources(),R.drawable.conbitrutor_two);
                        btn_rank_one.setImageBitmap(btn1);

                    }
                    if(user.get(0).getGoldenBook() == 0){
                        btn_rank_two.setVisibility(View.GONE);
                    }else if(user.get(0).getGoldenBook() == 1){
                        Bitmap btn1 = BitmapFactory.decodeResource(getResources(),R.drawable.golden_book);
                        btn_rank_two.setImageBitmap(btn1);
                        btn_rank_two.setVisibility(View.VISIBLE);
                    }

                    if(user.get(0).getListBook() == 0){
                        Bitmap btn1 = BitmapFactory.decodeResource(getResources(),R.drawable.newbie);
                        btn_rank_three.setImageBitmap(btn1);
                        btn_rank_three.setVisibility(View.VISIBLE);
                    }else if(user.get(0).getListBook() == 1){
                        Bitmap btn1 = BitmapFactory.decodeResource(getResources(),R.drawable.bookworm);
                        btn_rank_three.setImageBitmap(btn1);
                        btn_rank_three.setVisibility(View.VISIBLE);
                    }else{
                        Bitmap btn1 = BitmapFactory.decodeResource(getResources(),R.drawable.bibliophile);
                        btn_rank_three.setImageBitmap(btn1);
                        btn_rank_three.setVisibility(View.VISIBLE);
                    }
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
}
