package com.booxtown.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;

import com.booxtown.activity.WelcomeActivity;
import com.booxtown.api.ServiceGenerator;
import com.booxtown.api.ServiceInterface;
import com.booxtown.fragment.ExploreFragment;
import com.booxtown.model.Contact;
import com.booxtown.model.NumberBook;
import com.booxtown.model.NumberBookResult;
import com.booxtown.model.TimeZone;
import com.google.android.gms.maps.model.LatLng;

import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import com.booxtown.model.Book;
import com.booxtown.model.BookResult;
import com.booxtown.model.CommentBook;
import com.booxtown.model.CommentBookResult;
import com.booxtown.model.Result;
import com.booxtown.model.TransactionResult;
import retrofit2.Call;

/**
 * Created by thuyetpham94 on 11/09/2016.
 */
public class BookController {
    private ServiceInterface service;
    Boolean success;
    Activity mActivity;
    GPSTracker gpsTracker;
    public BookController(){
        service = ServiceGenerator.GetInstance();

    }

    public BookController(Activity mActivity) {
        service = ServiceGenerator.GetInstance();
        this.mActivity = mActivity;
    }

    public String addbook(Book book, String session_id){
        if(book.getPhoto()==null){
            book.setPhoto(" ");
        }
        Hashtable obj = ObjectCommon.ObjectDymanic(book);
        obj.put("session_id",session_id);
        Call<Result> status = service.addbook(obj);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            Result str = status.execute().body();
            if (str.getCode() == 200){
                return str.getDescription();
            }
            String s = "";
        } catch (Exception ex) {
            String s = "";
        }
        return "";
    }
    public boolean insertContact(Contact contact){

        Hashtable obj = ObjectCommon.ObjectDymanic(contact);

        Call<Result> status = service.insertContact(obj);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            Result str = status.execute().body();
            if (str.getCode() == 200){
                return true;
            }

        } catch (Exception ex) {
            return false;
        }
        return false;
    }
    public List<Book> getBookByID(String book_id){
        Call<BookResult> profile = service.getBookByID(book_id);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            BookResult str = profile.execute().body();
            if (str.getCode() == 200){
                return str.getBook();
            }
        } catch (Exception ex) {
        }
        return null;
    }
    public List<Book> getAllBookById(Context context,String session_id){
            Call<BookResult> profile = service.getAllBookByUser(session_id);
            try {
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy =
                            new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
                BookResult str = profile.execute().body();
                if (str.getCode() == 200) {
                    return str.getBook();
                }
            } catch (Exception ex) {
            }
            return null;

    }

    public Boolean updatebook(Book book,String session_id){
        Hashtable table = ObjectCommon.ObjectDymanic(book);
        table.put("session_id",session_id);
        Call<Result> imagebook = service.update(table);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            Result str = imagebook.execute().body();
            if (str.getCode() == 200){
                return true;
            }
        } catch (Exception ex) {
        }
        return false;
    }

    public Boolean deletebook(String book_id){
        Hashtable hashtable  = ObjectCommon.ObjectDymanic(book_id);
        hashtable.put("book_id",book_id);
        Call<Result> delte = service.deletebook(hashtable);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            Result str = delte.execute().body();
            if (str.getCode() == 200){
                success = true;
            }
        } catch (Exception ex) {
            success = false;
        }
        return success;
    }



    public List<Book> getallbook(){
        Call<BookResult> getall = service.getAllBook();

        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            BookResult str = getall.execute().body();
            if (str.getCode() == 200){
                return str.getBook();
            }
        } catch (Exception ex) {
        }
        return null;

    }

    public List<Book> book_gettop(String session_id,long from,long top){
        Call<BookResult> getall = service.book_gettop(session_id,from,top);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            BookResult str = getall.execute().body();
            if (str.getCode() == 200){
                return str.getBook();
            }
        } catch (Exception ex) {
        }
        return null;

    }

    public List<Book> bookTransactionId(String tranhisid){
        Call<TransactionResult> getall = service.getBookTransaction(tranhisid);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            TransactionResult str = getall.execute().body();
            if (str.getCode() == 200){
                return str.getTransaction().getBook();
            }
        } catch (Exception ex) {
        }
        return null;

    }

    public Comparator<Book> distance = new Comparator<Book>() {
        @Override
        public int compare(Book lhs, Book rhs) {
            ExploreFragment exploreFragment = new ExploreFragment();
            LatLng latLng1 = new LatLng(new GPSTracker(mActivity).getLatitude(),new GPSTracker(mActivity).getLongitude());
            LatLng latLng1_2 = new LatLng(lhs.getLocation_latitude(),lhs.getLocation_longitude());
            LatLng latLng2 = new LatLng(rhs.getLocation_latitude(),rhs.getLocation_longitude());
            Double dist1 = exploreFragment.CalculationByDistance(latLng1,latLng1_2);
            Double dist2 = exploreFragment.CalculationByDistance(latLng1,latLng2);
            int i1 = dist1.intValue();
            int i2 = dist1.intValue();
            return i2 - i1;
        }
    };

    public List<CommentBook> getCommnetBook(String book_id){
        Call<CommentBookResult> getall = service.getCommentBook(book_id);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            CommentBookResult str = getall.execute().body();
            if (str.getCode() == 200){
                return str.getComment();
            }
        } catch (Exception ex) {
            String sss= ex.getMessage();
        }
        return null;
    }

    public List<CommentBook> getTopCommnetBook(String book_id,int top,int from){
        Call<CommentBookResult> getall = service.getTopCommentBook(book_id,top,from);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            CommentBookResult str = getall.execute().body();
            if (str.getCode() == 200){
                return str.getComment();
            }
        } catch (Exception ex) {
            String sss= ex.getMessage();
        }
        return null;
    }

    public List<Book> getTopBookByID(int user_id,int to,int from){
        Call<BookResult> profile = service.getTopBookById(user_id,to,from);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            BookResult str = profile.execute().body();
            if (str.getCode() == 200){
                return str.getBook();
            }
        } catch (Exception ex) {
        }
        return null;
    }

    public List<Book> getAllBookInApp(int price_from,int price_to,int distance,float longtitude,float latitude,String genre, String textSearch, String session_id,int user_id, int top, int from){
        Call<BookResult> profile = service.getAllBookInApp( price_from, price_to, distance, longtitude, latitude, genre,  textSearch,  session_id, user_id,  top,  from);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            BookResult str = profile.execute().body();
            if (str.getCode() == 200){
                return str.getBook();
            }
        } catch (Exception ex) {
            String ss= ex.getMessage();
        }
        return null;
    }

    public List<NumberBook> getNumberBook(int  user_id){
        Call<NumberBookResult> profile = service.getNumberBook(user_id);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            NumberBookResult str = profile.execute().body();
            if (str.getCode() == 200){
                return str.getResult();
            }
        } catch (Exception ex) {
        }
        return null;
    }

    //get timezone
    public String GetTimezone(){
        String result="";
        Call<TimeZone> timeZone = service.GetTimeZone();
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            TimeZone str = timeZone.execute().body();

                result = str.getTimezone();


        } catch (Exception ex) {
            String code= ex.getMessage();

        }
        return result;
    }

}
