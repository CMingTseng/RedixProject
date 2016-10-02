package redix.booxtown.controller;

import android.app.Activity;
import android.os.StrictMode;

import com.google.android.gms.maps.model.LatLng;

import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import redix.booxtown.api.ServiceGenerator;
import redix.booxtown.api.ServiceInterface;
import redix.booxtown.fragment.ExploreFragment;
import redix.booxtown.model.Book;
import redix.booxtown.model.BookResult;
import redix.booxtown.model.CommentBook;
import redix.booxtown.model.CommentBookResult;
import redix.booxtown.model.Result;
import redix.booxtown.model.TransactionResult;
import retrofit2.Call;

/**
 * Created by thuyetpham94 on 11/09/2016.
 */
public class BookController {
    private ServiceInterface service;
    Boolean success;
    Activity mActivity;
    public BookController(){
        service = ServiceGenerator.GetInstance();
    }

    public BookController(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public boolean addbook(Book book, String session_id){
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
                return true;
            }
            String s = "";
        } catch (Exception ex) {
            String s = "";
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
    public List<Book> getAllBookById(String session_id){
        Call<BookResult> profile = service.getAllBookByUser(session_id);
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

}
