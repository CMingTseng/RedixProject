package com.booxtown.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.booxtown.activity.AddbookActivity;
import com.booxtown.activity.MainAllActivity;
import com.booxtown.api.ServiceGenerator;
import com.booxtown.controller.BookController;
import com.booxtown.controller.TransactionController;
import com.booxtown.controller.UserController;
import com.booxtown.fragment.MainFragment;
import com.booxtown.model.Notification;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.booxtown.R;

import com.booxtown.activity.ListingsDetailActivity;
import com.booxtown.activity.SwapActivity;
import com.booxtown.controller.NotificationController;
import com.booxtown.controller.ObjectCommon;
import com.booxtown.model.Book;
import com.squareup.picasso.Target;


/**
 * Created by Administrator on 26/08/2016.
 */
public class AdapterExplore extends RecyclerView.Adapter<AdapterExplore.ExploreHoder> implements Filterable {
    private Context mContext;
    private List<Book> listExplore;
    private int type;
    SharedPreferences pref;
    String username;
    private List<Book> originbook;
    private ItemFilter mFilter = new ItemFilter();
    int keyStart=0;

    public List<Book> getListExplore() {
        return listExplore;
    }

    public void setListExplore(List<Book> listExplore) {
        this.listExplore = listExplore;
        notifyDataSetChanged();
    }

    public AdapterExplore(Context c, List<Book> listExplore, int type, int key) {
        mContext = c;
        this.listExplore = listExplore;
        this.originbook = listExplore;
        this.type = type;
        this.keyStart=key;
        try {
            pref = mContext.getSharedPreferences("MyPref",mContext.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
        }catch (Exception e){
        }
    }

    @Override
    public ExploreHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_gridview_explore, null);
        ExploreHoder rcv = new ExploreHoder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(ExploreHoder hoders, int position) {
         final ExploreHoder hoder= hoders;
        final Book ex= listExplore.get(position);
        username = pref.getString("username", null);
        final String[] image = ex.getPhoto().split(";");
        if (image.length!=0){
            final int index=image[0].indexOf("_+_");
            if(index>0 && image[0].length() >3 ) {
                final String img = image[0].substring(index+3, image[0].length());
                final String imageLink= ServiceGenerator.API_BASE_URL+"booxtown/rest/getImage?username=" + image[0].substring(0,index) + "&image=" +  img  + "";
                try {

                    Picasso.with(mContext). load(imageLink).placeholder(R.drawable.blank_image).
                            into(hoder.img_book);

                    //GetWithHeight getWithHeight= new GetWithHeight(mContext,imageLink,hoder.img_book);
                    //getWithHeight.execute();

                }catch (Exception exx){
                    String err= exx.getMessage();
                }
            }
            else{
                Picasso.with(mContext). load(ServiceGenerator.API_BASE_URL+"booxtown/rest/getImage?username=" + ex.getUsername() + "&image=" +  image[0]  + "").placeholder(R.drawable.blank_image).
                        into(hoder.img_book);
            }
        }else {
            Picasso.with(mContext).load(R.drawable.blank_image).into(hoder.img_book);
        }
        final char array[]=ex.getAction().toCharArray();

        if(ex.getPrice() !=0) {
            hoder.txt_pricebook.setVisibility(View.VISIBLE);
            try {
                String priceBook=(ex.getPrice()+"");
                int index = priceBook.indexOf(".");
                if (Integer.parseInt(priceBook.substring(index+1,priceBook.length())) != 0) {
                    hoder.txt_pricebook.setText("AED " + ex.getPrice());
                } else {
                    hoder.txt_pricebook.setText("AED " + priceBook.substring(0, index));
                }
            }catch (Exception exx){

            }

        }else {
            hoder.txt_pricebook.setVisibility(View.INVISIBLE);
        }
        if(String.valueOf(array[0]).contains("1")){
            Picasso.with(mContext).load(R.drawable.explore_btn_swap_active).into(hoder.img_swap);
        }
        else {
            Picasso.with(mContext).load(R.drawable.explore_btn_swap_dis_active).into(hoder.img_swap);

        }
        if(String.valueOf(array[2]).contains("1")){
            Picasso.with(mContext).load(R.drawable.explore_btn_free_active).into(hoder.img_free);

        }
        else {
            Picasso.with(mContext).load(R.drawable.explore_btn_free_dis_active).into(hoder.img_free);

        }
        if(String.valueOf(array[1]).contains("1")){
            Picasso.with(mContext).load(R.drawable.explore_btn_buy_active).into(hoder.img_buy);

        }
        else {
            Picasso.with(mContext).load(R.drawable.explore_btn_buy_dis_active).into(hoder.img_buy);

        }

        if(ex.getTitle().toString().length()>0) {
            hoder.txt_title_book.setText(ex.getTitle().toString().substring(0,1).toUpperCase()+ex.getTitle().toString().substring(1,ex.getTitle().toString().length()));
        }
        hoder.txt_author_book.setText(ex.getAuthor().toString());

        hoder.img_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(String.valueOf(array[1]).contains("1")) {
                    SharedPreferences pref = mContext.getSharedPreferences("MyPref", mContext.MODE_PRIVATE);
                    String session_id = pref.getString("session_id", null);
                    checkExits checkExits= new checkExits(mContext,session_id,ex,1);
                    checkExits.execute();

                    //UserID userID = new UserID(mContext, ex.getId(), ex.getUser_id(), 1, ex);
                    //userID.execute();
                }

            }
        });
        hoder.img_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(String.valueOf(array[2]).contains("1")) {
                    SharedPreferences pref = mContext.getSharedPreferences("MyPref", mContext.MODE_PRIVATE);
                    String session_id = pref.getString("session_id", null);
                    checkExits checkExits= new checkExits(mContext,session_id,ex,2);
                    checkExits.execute();
                    //UserID userID = new UserID(mContext, ex.getId(), ex.getUser_id(), 1, ex);
                    //userID.execute();
                }

            }
        });

        hoder.img_swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(String.valueOf(array[0]).contains("1")) {
                    SharedPreferences pref = mContext.getSharedPreferences("MyPref", mContext.MODE_PRIVATE);
                    String session_id = pref.getString("session_id", null);
                    checkExits checkExits= new checkExits(mContext,session_id,ex,3);
                    checkExits.execute();


                }
            }
        });

        hoder.img_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(keyStart==0) {
                        ListingsDetailActivity fragment = new ListingsDetailActivity();
                        Bundle bundle = new Bundle();
                        bundle.putString(String.valueOf(R.string.valueListings), "2");
                        bundle.putSerializable("item", ex);
                        fragment.setArguments(bundle);
                        callFragment(fragment);
                    }else if(keyStart==1){

                        Intent intent= new Intent(mContext.getApplicationContext(), MainAllActivity.class);
                        intent.putExtra(String.valueOf(R.string.valueListings), "2");
                        intent.putExtra("item", ex);
                        intent.putExtra("keyDetail","1");
                        mContext.startActivity(intent);
                        ((Activity)mContext).finish();
                    }
                }catch (Exception exx){

                }
            }
        });
    }


    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getItemCount() {
        return listExplore.size();
    }

    public class ExploreHoder extends RecyclerView.ViewHolder{
        TextView txt_title_book ;
        TextView txt_author_book ;
        TextView txt_pricebook;

        ImageView img_book ;
        ImageView img_swap;
        ImageView img_free;
        ImageView img_buy ;

        public ExploreHoder(View itemView) {
            super(itemView);
            txt_title_book = (TextView) itemView.findViewById(R.id.txt_title_book_listings);
            txt_author_book = (TextView) itemView.findViewById(R.id.txt_author_book_listings);
            txt_pricebook=(TextView) itemView.findViewById(R.id.txt_pricebook);

            img_book = (ImageView)itemView.findViewById(R.id.img_book);
            img_swap = (ImageView)itemView.findViewById(R.id.img_explore_swap);
            img_free = (ImageView)itemView.findViewById(R.id.img_explore_free);
            img_buy = (ImageView)itemView.findViewById(R.id.img_explore_buy);
        }
    }


    public void callFragment(Fragment fragment ){
        try {
            android.support.v4.app.FragmentManager manager = ((FragmentActivity) mContext).getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.frame_main_all, fragment);
            transaction.commit();
        }
        catch (Exception ex){
        }
    }

    private class ItemFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults result = new FilterResults();
            if(constraint != null && constraint.toString().length() > 0)
            {
                ArrayList<Book> filteredItems = new ArrayList<Book>();

                for(int i = 0, l = originbook.size(); i < l; i++)
                {
                    Book country = originbook.get(i);
                    if(country.toString().toLowerCase().contains(constraint))
                        filteredItems.add(country);
                }
                result.count = filteredItems.size();
                result.values = filteredItems;
            }
            else
            {
                synchronized(this)
                {
                    result.values = originbook;
                    result.count = originbook.size();
                }
            }
            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listExplore = (ArrayList<Book>)results.values;
            notifyDataSetChanged();
//            for(int i = 0, l = listBook.size(); i < l; i++)
//                add(countryList.get(i));
//            notifyDataSetInvalidated();
        }
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public class Hoder{

        TextView txt_title_book ;
        TextView txt_author_book ;
        TextView txt_pricebook;

        ImageView img_book ;
        ImageView img_swap;
        ImageView img_free;
        ImageView img_buy ;
    }

    class transactionInsert extends AsyncTask<Void, Void, String> {

        Context context;
        ProgressDialog dialog;
        List<Book> listemp;
        String session_id, buyUserID, sellUserID, buyBookID, sellBookID, action;

        public transactionInsert(Context context, String session_id, String buyUserID, String sellUserID, String buyBookID, String sellBookID, String action) {
            this.context = context;
            this.session_id = session_id;
            this.buyBookID = buyBookID;
            this.sellUserID = sellUserID;
            this.buyUserID = buyUserID;
            this.sellBookID = sellBookID;
            this.action = action;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String transactionID = "";
            TransactionController transactionController = new TransactionController();
            transactionID = transactionController.transactionInsert(buyUserID, sellUserID, buyBookID, sellBookID, action,session_id);
            return transactionID;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String transactionID) {
            if (transactionID == null) {

            }
            else if(transactionID.equals("isTrial")){
                Toast.makeText(context,"Upgrade your membership",Toast.LENGTH_SHORT).show();
            }
            else {
                if(action.equals("buy")) {
                    SharedPreferences pref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    String firstName = pref.getString("firstname", "");
                    List<Hashtable> list = new ArrayList<>();
                    Notification notification = new Notification("Buying Request", transactionID, "4");
                    Hashtable obj = ObjectCommon.ObjectDymanic(notification);
                    obj.put("user_id", sellUserID);
                    if (firstName.length() > 1) {
                        obj.put("messages", firstName.substring(0, 1).toUpperCase() + firstName.substring(1, firstName.length()) + " wants to buy your book ");
                    }
                    list.add(obj);
                    NotificationController controller = new NotificationController();
                    controller.sendNotification(list);
                }else{
                    SharedPreferences pref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    String firstName = pref.getString("firstname", "");
                    List<Hashtable> list = new ArrayList<>();
                    Notification notification = new Notification("Buying Request", transactionID, "16");
                    Hashtable obj = ObjectCommon.ObjectDymanic(notification);
                    obj.put("user_id", sellUserID);
                    if (firstName.length() > 1) {
                        obj.put("messages", firstName.substring(0, 1).toUpperCase() + firstName.substring(1, firstName.length()) + " wants to get your book ");
                    }
                    list.add(obj);
                    NotificationController controller = new NotificationController();
                    controller.sendNotification(list);
                }

            }
            super.onPostExecute(transactionID);
        }
    }

    class UserID extends AsyncTask<String, Void, String> {
        Context context;
        String bookID;
        String bookUserID;
        int type;
        Book book;
        public UserID(Context context, String bookID, String bookUserID, int type, Book book)
        {
            this.context = context;
            this.bookID=bookID;
            this.bookUserID=bookUserID;
            this.type= type;
            this.book= book;

        }

        @Override
        protected String doInBackground(String... strings) {
            SharedPreferences pref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            String session_id = pref.getString("session_id", null);
            UserController userController = new UserController(mContext);
            String user_id = userController.getUserID(session_id);
            return user_id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(final String user_ID) {
            try {
                SharedPreferences pref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                final String session_id = pref.getString("session_id", null);

                if(!user_ID.equals(bookUserID)) {
                    if(type==1||type==2) {
                        final Dialog dialog = new Dialog(mContext);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_buy_listing);

                        TextView text_title=(TextView) dialog.findViewById(R.id.textView135);
                        if(type==2){
                            text_title.setText("Are you sure you want to get this book");
                        }else {
                            text_title.setText("Are you sure you want to buy this book");
                        }
                        TextView textView_namebook_buy =(TextView)dialog.findViewById(R.id.textView_namebook_buy);
                        textView_namebook_buy.setText("\""+book.getTitle()+"\"");

                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();

                        ImageView btn_dialog_notification_swap = (ImageView) dialog.findViewById(R.id.close_buy_listings);
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
                                if(type==1) {
                                    transactionInsert transactionInsert = new transactionInsert(context, session_id, user_ID, bookUserID, "", bookID, "buy");
                                    transactionInsert.execute();
                                }else if(type==2){
                                    transactionInsert transactionInsert = new transactionInsert(context, session_id, user_ID, bookUserID, "", bookID, "free");
                                    transactionInsert.execute();

                                }

                                final Dialog dialog1 = new Dialog(mContext);
                                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog1.setContentView(R.layout.dialog_request_sent_listing);

                                TextView textView133=(TextView) dialog1.findViewById(R.id.textView133);
                                textView133.setText("Let's wait for "+book.getUsername()+"'s reply");

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
                                TextView btn_back=(TextView) dialog1.findViewById(R.id.btn_yes);
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
                    else{
                        final char array[]=book.getAction().toCharArray();
                        if(String.valueOf(array[0]).contains("1")&& type!=0) {
                            Intent intent = new Intent(mContext, SwapActivity.class);
                            intent.putExtra("Book", book);
                            mContext.startActivity(intent);
                        }

                    }
                }
                else{
                    Toast.makeText(context, "You can't Buy/Swap  your book", Toast.LENGTH_LONG).show();
                }
                //}
            } catch (Exception e) {
                String ssss = e.getMessage();
                // Toast.makeText(context, "no data", Toast.LENGTH_LONG).show();
            }
        }
    }
    class listingAsync extends AsyncTask<String,Void,List<Book>>{

        Context context;
        ProgressDialog dialog;
        Book book;
        public listingAsync(Context context,Book book){
            this.context = context;
            this.book = book;
        }

        @Override
        protected List<Book> doInBackground(String... strings) {
            BookController bookController = new BookController();
            return bookController.getAllBookById(context,strings[0]);
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
                        final Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_check_mybook);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                        ImageView img_close_dialog_unsubcribe = (ImageView)dialog.findViewById(R.id.imageView_close_dialog);
                        Picasso.with(context).load(R.drawable.btn_close_filter).into(img_close_dialog_unsubcribe);
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
                                Intent intent = new Intent(context, AddbookActivity.class);
                                intent.putExtra("type",0);
                                context.startActivity(intent);
                                dialog.dismiss();
                            }
                        });
                    }else {
                        UserID userID= new UserID(mContext,book.getId(), book.getUser_id(), 3, book);
                        userID.execute();
                    }
                    dialog.dismiss();
                }
            }catch (Exception e){}
        }
    }

    class checkExits extends AsyncTask<String,Void,Boolean>{

        Context context;
        ProgressDialog dialog;
        String session_id;
        int type;
        Book book;
        public checkExits(Context context,String session_id, Book book, int type){
            this.context = context;
            this.session_id = session_id;
            this.book= book;
            this.type=type;
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            TransactionController transactionController = new TransactionController();
            return transactionController.CheckExitsTransaction(book.getUser_id(),book.getId(),session_id);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Boolean flag) {
            try {
                    if(flag){
                        if (type==1) {
                            UserID userID = new UserID(mContext, book.getId(), book.getUser_id(), 1, book);
                            userID.execute();
                        }
                        else if (type==2) {
                            UserID userID = new UserID(mContext, book.getId(), book.getUser_id(), 2, book);
                            userID.execute();
                        }
                        else{
                            listingAsync listingAsync = new listingAsync(mContext, book);
                            listingAsync.execute(session_id);
                        }

                    }
                else{
                        Toast.makeText(mContext,"You have already requested this book", Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();

            }catch (Exception e){}
        }
    }

    class GetWithHeight extends AsyncTask<Bitmap,Void,Bitmap>{

        Context context;
       String imageUrl;
        ImageView img_book;
        public GetWithHeight(Context context, String imageUrl,ImageView img_book){
            this.context = context;
            this.imageUrl=imageUrl;
            this.img_book=img_book;
        }

        @Override
        protected Bitmap doInBackground(Bitmap... Bitmap) {
            Bitmap downloadedImage = null;
            try {
                downloadedImage = Picasso.with(context).load(imageUrl).get();
                return downloadedImage;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Bitmap downloadedImage) {
            try {
                int width = downloadedImage.getWidth();
                int height = downloadedImage.getHeight();
                if (width> height) {
                    img_book.setRotation(90f);
                    img_book.setImageBitmap(downloadedImage);
                    /*Picasso.with(mContext).load(imageUrl).rotate(90f).placeholder(R.drawable.blank_image).
                            into(img_book);*/
                } else {
                    img_book.setImageBitmap(downloadedImage);
                    /*Picasso.with(mContext).load(imageUrl).placeholder(R.drawable.blank_image).
                            into(img_book);*/
                }
            }catch (Exception e){}
        }
    }
}