package redix.booxtown.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import redix.booxtown.R;
import redix.booxtown.activity.ListingCollectionActivity;
import redix.booxtown.activity.ListingsDetailActivity;
import redix.booxtown.activity.SwapActivity;
import redix.booxtown.api.ServiceGenerator;
import redix.booxtown.fragment.MainFragment;
import redix.booxtown.model.Book;
import redix.booxtown.model.Explore;

/**
 * Created by Administrator on 26/08/2016.
 */
public class AdapterExplore extends BaseAdapter implements Filterable {
    private Context mContext;
    private List<Book> listExplore;
    private int type;
    SharedPreferences pref;
    String username;
    private List<Book> originbook;
    private ItemFilter mFilter = new ItemFilter();
    public AdapterExplore(Context c, List<Book> listExplore, int type) {
        mContext = c;
        this.listExplore = listExplore;
        this.originbook = listExplore;
        this.type = type;
        try {
            pref = mContext.getSharedPreferences("MyPref",mContext.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
        }catch (Exception e){

        }

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listExplore.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final Book ex= listExplore.get(position);
        username = pref.getString("username", null);
        String[] image = ex.getPhoto().split(";");
        Hoder hoder = new Hoder();
        convertView = inflater.inflate(R.layout.custom_gridview_explore, null);
        hoder.txt_title_book = (TextView) convertView.findViewById(R.id.txt_title_book_listings);
        hoder.txt_author_book = (TextView) convertView.findViewById(R.id.txt_author_book_listings);
        hoder.txt_pricebook=(TextView) convertView.findViewById(R.id.txt_pricebook);

        hoder.img_book = (ImageView)convertView.findViewById(R.id.img_book);
        hoder.img_swap = (ImageView)convertView.findViewById(R.id.img_explore_swap);
        hoder.img_free = (ImageView)convertView.findViewById(R.id.img_explore_free);
        hoder.img_buy = (ImageView)convertView.findViewById(R.id.img_explore_buy);


        if (image.length!=0){
            int index=image[0].indexOf("_+_");
            if(index>0 && image[0].length() >3 ) {
                String img = image[0].substring(index+3, image[0].length());
                Glide.with(mContext). load(ServiceGenerator.API_BASE_URL+"booxtown/rest/getImage?username=" + ex.getUsername() + "&image=" +  img  + "").diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.blank_image).
                        into(hoder.img_book);
                //Picasso.with(mContext).load(ServiceGenerator.API_BASE_URL+"booxtown/rest/getImage?username=" + ex.getUsername() + "&image=" + img + "").placeholder(R.drawable.blank_image).into(hoder.img_book);
            }
            else{
                Glide.with(mContext). load(ServiceGenerator.API_BASE_URL+"booxtown/rest/getImage?username=" + ex.getUsername() + "&image=" +  image[0]  + "").diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.blank_image).
                        into(hoder.img_book);
                //Picasso.with(mContext).load(ServiceGenerator.API_BASE_URL+"booxtown/rest/getImage?username=" + ex.getUsername() + "&image=" + image[0] + "").placeholder(R.drawable.blank_image).into(hoder.img_book);
            }
        }else {
            Picasso.with(mContext).load(R.drawable.blank_image).into(hoder.img_book);
        }
        //String action[] = ex.getAction().split("");
        final char array[]=ex.getAction().toCharArray();

        if(ex.getPrice() !=0) {
            hoder.txt_pricebook.setVisibility(View.VISIBLE);
            hoder.txt_pricebook.setText("AED "+ex.getPrice());
        }else {
            hoder.txt_pricebook.setVisibility(View.INVISIBLE);
        }
        if(String.valueOf(array[0]).contains("1")){
            Picasso.with(mContext).load(R.drawable.explore_btn_swap_active).into(hoder.img_swap);
            //img_swap.setImageResource((R.drawable.explore_btn_swap_active));
            //Glide.with(mContext).load(R.drawable.explore_btn_swap_active).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_swap);
        }
        else {
            Picasso.with(mContext).load(R.drawable.explore_btn_swap_dis_active).into(hoder.img_swap);

//            Glide.with(mContext).load(R.drawable.explore_btn_swap_dis_active).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_swap);
            //img_swap.setImageResource((R.drawable.explore_btn_swap_dis_active));
        }
        if(String.valueOf(array[1]).contains("1")){
            Picasso.with(mContext).load(R.drawable.explore_btn_free_active).into(hoder.img_free);
//            Glide.with(mContext).load(R.drawable.explore_btn_free_active).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_free);
            //img_free.setImageResource((R.drawable.explore_btn_free_active));
        }
        else {
            Picasso.with(mContext).load(R.drawable.explore_btn_free_dis_active).into(hoder.img_free);
//            Glide.with(mContext).load(R.drawable.explore_btn_free_dis_active).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_free);
            //img_free.setImageResource((R.drawable.explore_btn_free_dis_active));
        }
        if(String.valueOf(array[2]).contains("1")){
            Picasso.with(mContext).load(R.drawable.explore_btn_buy_active).into(hoder.img_buy);
//            Glide.with(mContext).load(R.drawable.listing_btn_buy).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_buy);
            //img_buy.setImageResource((R.drawable.listing_btn_buy));
        }
        else {
            Picasso.with(mContext).load(R.drawable.explore_btn_buy_dis_active).into(hoder.img_buy);
//            Glide.with(mContext).load(R.drawable.explore_btn_buy_dis_active).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_buy);
            //img_buy.setImageResource((R.drawable.explore_btn_buy_dis_active));
        }
        //hoder.img_edit.setVisibility(View.GONE);
//        Glide.with(mContext).load(R.drawable.listing_btn_edit).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_edit);
        //img_edit.setImageResource((R.drawable.listing_btn_edit));

        hoder.txt_title_book.setText(ex.getTitle().toString());
        hoder.txt_author_book.setText(ex.getAuthor().toString());

        hoder.img_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_buy_listing);
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
                        final Dialog dialog1 = new Dialog(mContext);
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
        });

        hoder.img_swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(String.valueOf(array[0]).contains("1")&& type!=0) {
                    Intent intent = new Intent(mContext, SwapActivity.class);
                    mContext.startActivity(intent);
                }
            }
        });

        return convertView;
    }

    public void callFragment(Fragment fragment ){
        android.support.v4.app.FragmentManager manager = ((FragmentActivity) mContext).getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_main_all, fragment);
        transaction.commit();
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
            notifyDataSetInvalidated();
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
        ImageView img_edit ;

    }
}



//package redix.booxtown.adapter;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentTransaction;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//
//import java.util.ArrayList;
//
//import redix.booxtown.R;
//import redix.booxtown.activity.ListingsDetailActivity;
//import redix.booxtown.activity.SwapActivity;
//import redix.booxtown.fragment.MainFragment;
//import redix.booxtown.model.Explore;
//
///**
// * Created by Administrator on 26/08/2016.
// */
//public class AdapterExplore extends BaseAdapter {
//    private Context mContext;
//    private ArrayList<Explore> listExplore;
//    private int type;
//
//    public AdapterExplore(Context c, ArrayList<Explore> list_explores,int type) {
//        mContext = c;
//        this.listExplore = list_explores;
//        this.type=type;
//    }
//
//    @Override
//    public int getCount() {
//        // TODO Auto-generated method stub
//        return listExplore.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        // TODO Auto-generated method stub
//        return 0;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        // TODO Auto-generated method stub
//
//        LayoutInflater inflater = (LayoutInflater) mContext
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        final Explore ex= listExplore.get(position);
//
//            convertView = inflater.inflate(R.layout.custom_gridview_explore, null);
//            TextView txt_title_book = (TextView) convertView.findViewById(R.id.txt_title_book_listings);
//            TextView txt_author_book = (TextView) convertView.findViewById(R.id.txt_author_book_listings);
//            TextView txt_price_book=(TextView) convertView.findViewById(R.id.txt_pricebook);
//
//            ImageView img_book = (ImageView)convertView.findViewById(R.id.img_book);
//            ImageView img_swap = (ImageView)convertView.findViewById(R.id.img_explore_swap);
//            ImageView img_free = (ImageView)convertView.findViewById(R.id.img_explore_free);
//            ImageView img_buy = (ImageView)convertView.findViewById(R.id.img_explore_buy);
//
//
//            if(position%2==0){
//                img_book.setImageResource((R.drawable.img_temp1));
//                txt_title_book.setText("The Las Painting of Sara de Vos");
//                txt_author_book.setText("buy Gandalf");
//            }
//            else
//            {
//                img_book.setImageResource((R.drawable.img_temp2));
//                txt_title_book.setText("Gandalf the first");
//                txt_author_book.setText("buy Ptit");
//            }
//
//            if(ex.isSwap()){
//                //img_swap.setImageResource((R.drawable.explore_btn_swap_active));
//                Glide.with(mContext).load(R.drawable.explore_btn_swap_active).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_swap);
//            }
//            else{
//                Glide.with(mContext).load(R.drawable.explore_btn_swap_dis_active).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_swap);
//                //img_swap.setImageResource((R.drawable.explore_btn_swap_dis_active));
//            }
//
//            if(ex.isFree()){
//                Glide.with(mContext).load(R.drawable.explore_btn_free_active).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_free);
//                //img_free.setImageResource((R.drawable.explore_btn_free_active));
//            }
//            else{
//                Glide.with(mContext).load(R.drawable.explore_btn_free_dis_active).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_free);
//                //img_free.setImageResource((R.drawable.explore_btn_free_dis_active));
//            }
//
//            if(ex.isBuy()){
//                Glide.with(mContext).load(R.drawable.explore_btn_buy_active).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_buy);
//                //img_buy.setImageResource((R.drawable.explore_btn_buy_active));
//                txt_price_book.setVisibility(View.VISIBLE);
//
//            }
//            else{
//                Glide.with(mContext).load(R.drawable.explore_btn_buy_dis_active).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_buy);
//                //img_buy.setImageResource((R.drawable.explore_btn_buy_dis_active));
//            }
//
//            img_buy.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    final Dialog dialog = new Dialog(mContext);
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
//
//                    TextView btn_confirm=(TextView) dialog.findViewById(R.id.btn_confirm_buy_listing);
//                    btn_confirm.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            dialog.dismiss();
//                            final Dialog dialog1 = new Dialog(mContext);
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
////                                    getActivity().finish();
//                                }
//                            });
//                            TextView btn_back=(TextView) dialog1.findViewById(R.id.btn_back_home);
//                            btn_back.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    dialog1.dismiss();
//                                    callFragment(new MainFragment());
//                                }
//                            });
//                        }
//                    });
//                }
//            });
//
//            img_swap.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(ex.isSwap()&& type!=0) {
//                        Intent intent = new Intent(mContext, SwapActivity.class);
//                        mContext.startActivity(intent);
//                    }
//                }
//            });
//
//        return convertView;
//    }
//
//    public void callFragment(Fragment fragment ){
//        android.support.v4.app.FragmentManager manager = ((FragmentActivity) mContext).getSupportFragmentManager();
//        FragmentTransaction transaction = manager.beginTransaction();
//        transaction.replace(R.id.frame_main_all, fragment);
//        transaction.commit();
//    }
//}