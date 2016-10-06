package redix.booxtown.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
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
import redix.booxtown.fragment.NotificationFragment;
import redix.booxtown.model.Book;
import redix.booxtown.model.CommentBook;
import redix.booxtown.model.Notification;

/**
 * Created by Administrator on 29/08/2016.
 */
public class ListingsDetailActivity extends Fragment implements OnMapReadyCallback {
    private ListView listView;
    private ImageView imSwap, imSwap2, imFree, imFree2, imBuy, imBuy2, imageView_back;
    TextView txt_listed_by, txt_tag, txt_title_listings_detail, txt_author_listings_detail, txt_price_listings_detail, txt_time_post_listings, txt_genre_listing_detail;
    CircularImageView icon_user_listing_detail;
    ProgressBar progressBar;
    AdapterCommentBook adapter;
    RatingBar ratingBar_userprofile;
    Book book;
    List<String> listUser = new ArrayList<>();
    //map
    MarkerOptions marker;
    private GoogleMap mMap;

    //end
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_listings_detail, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.fragment_detail);
        mapFragment.getMapAsync(ListingsDetailActivity.this);


        imageView_back = (ImageView) getActivity().findViewById(R.id.img_menu);
        Picasso.with(getContext()).load(R.drawable.btn_sign_in_back).into(imageView_back);

        ImageView img_component = (ImageView) getActivity().findViewById(R.id.img_menu_component);
        img_component.setVisibility(View.GONE);
        init(v);

        try {
            //btn_rank
            ImageView btn_rank_one = (ImageView) v.findViewById(R.id.img_rank1_listings);
            Picasso.with(getContext()).load(R.drawable.btn_rank_one).into(btn_rank_one);

            ImageView btn_rank_two = (ImageView) v.findViewById(R.id.img_rank2_listings);
            Picasso.with(getContext()).load(R.drawable.btn_rank_two).into(btn_rank_two);

            ImageView btn_rank_three = (ImageView) v.findViewById(R.id.img_rank3_listings);
            Picasso.with(getContext()).load(R.drawable.btn_rank_three).into(btn_rank_three);
            //end

            TableRow tbTypebook = (TableRow) v.findViewById(R.id.row_type_book);
            TableRow tbTypebook2 = (TableRow) v.findViewById(R.id.row_type_book2);
            final EditText editText11 = (EditText) v.findViewById(R.id.editText11);
            final String type = getArguments().getString(String.valueOf(R.string.valueListings));

            RelativeLayout layout_comments = (RelativeLayout) v.findViewById(R.id.layout_comment);
            ImageView img_close_dialog_unsubcribe = (ImageView) v.findViewById(R.id.img_close_dialog_unsubcribe);


            imBuy = (ImageView) v.findViewById(R.id.img_buy_listing);
            imFree = (ImageView) v.findViewById(R.id.img_free_listings);
            imSwap = (ImageView) v.findViewById(R.id.img_swap_listing);
            imBuy2 = (ImageView) v.findViewById(R.id.img_buy_listing2);
            imFree2 = (ImageView) v.findViewById(R.id.img_free_listings2);
            imSwap2 = (ImageView) v.findViewById(R.id.img_swap_listing2);
            txt_listed_by = (TextView) v.findViewById(R.id.txt_listed_by);
            icon_user_listing_detail = (CircularImageView) v.findViewById(R.id.icon_user_listing_detail);
            ratingBar_userprofile = (RatingBar) v.findViewById(R.id.ratingBar_userprofile);

            book = (Book) getArguments().getSerializable("item");
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
                    } else if (type.equals("3")) {
                        callFragment(new ListingsFragment());
                    } else {
                        HomeActivity homeActivity = (HomeActivity) getActivity();
                        homeActivity.getTxtTitle().setText("Notifications");
                        homeActivity.callFragment(new NotificationFragment());
                    }
                }
            });

            if (type.equals("1")) {
                View view_search = (View) getActivity().findViewById(R.id.custom_search);
                view_search.setVisibility(View.GONE);
                Picasso.with(getContext()).load(R.drawable.btn_close_filter).into(img_close_dialog_unsubcribe);
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
            setData(book, v, type);
            //-----------------------------------------------------------
            listView = (ListView) v.findViewById(R.id.listView_comment);
            listView.setDivider(null);

            //listView.setAdapter(adapter);
            listView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });

            getComment comment = new getComment(getContext(), book.getId());
            comment.execute();

            SharedPreferences pref = getContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            final String session_id = pref.getString("session_id", null);
            img_close_dialog_unsubcribe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertComment insertComment1 = new insertComment(getContext());
                    insertComment1.execute(session_id, editText11.getText().toString(), book.getId());
                    editText11.setText("");

                    getComment comment = new getComment(getContext(), book.getId());
                    comment.execute();

                }
            });

        }catch (Exception exx){
            String ess= exx.getMessage();
        }
        return v;
    }

    public void setData(final Book book, View v, String type) {
        txt_title_listings_detail.setText(book.getTitle());
        txt_author_listings_detail.setText("by " + book.getAuthor());
        txt_price_listings_detail.setText("AED " + book.getPrice());
        txt_time_post_listings.setText(book.getCreate_date());
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
        if (type.equals("2")) {
            imSwap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    swap(book);
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
                    swap(book);
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
        txt_title_listings_detail = (TextView) view.findViewById(R.id.txt_title_listings_detail);
        txt_author_listings_detail = (TextView) view.findViewById(R.id.txt_author_listings_detail);
        txt_price_listings_detail = (TextView) view.findViewById(R.id.txt_price_listings_detail);
        txt_time_post_listings = (TextView) view.findViewById(R.id.txt_time_post_listings);
        txt_genre_listing_detail = (TextView) view.findViewById(R.id.txt_genre_listing_detail);
        txt_tag = (TextView) view.findViewById(R.id.txt_tag);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
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
            String free = String.valueOf(array[1]);
            String buy = String.valueOf(array[2]);
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
            BookController bookController = new BookController();
            return bookController.getCommnetBook(book_id);
        }

        @Override
        protected void onPostExecute(List<CommentBook> commentBooks) {
            try {
                if (commentBooks.size() > 0) {
                    adapter = new AdapterCommentBook(context, commentBooks);
                    listView.setAdapter(adapter);

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
                    Toast.makeText(context, Information.noti_no_data_listing, Toast.LENGTH_SHORT).show();
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
}
