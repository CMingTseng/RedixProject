package redix.booxtown.api;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import redix.booxtown.model.Book;
import redix.booxtown.model.CommentResult;
import redix.booxtown.model.Notification;
import redix.booxtown.model.NotificationResult;
import redix.booxtown.model.Profile;
import redix.booxtown.model.Result;
import redix.booxtown.model.BookResult;
import redix.booxtown.model.Thread;
import redix.booxtown.model.ThreadResult;
import redix.booxtown.model.TopicResult;
import redix.booxtown.model.User;
import redix.booxtown.model.UserResult;
import redix.booxtown.model.WishboardResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface ServiceInterface {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Multipart
    @POST("/booxtown/rest/uploadimage")
    Call<Result> postImage(@Part MultipartBody.Part image);

    @Multipart
    @POST("/booxtown/rest/uploadimage")
    Call<Result> postImage1(@Part MultipartBody.Part image,@Part MultipartBody.Part image1);
    @Multipart
    @POST("/booxtown/rest/uploadimage")
    Call<Result> postImage2(@Part MultipartBody.Part image,@Part MultipartBody.Part image1,@Part MultipartBody.Part image2);

    @GET("/booxtown/rest/getimage")
    Call<ResponseBody> getImage(@Query("username") String username ,@Query("image") String image);

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Notification
    @POST("/booxtown/rest/user/send_notification")
    Call<Result> sendNotification(@Body Object lst_notification);
    // end
    @POST("/booxtown/rest/user/signup")
    Call<Result> addUser(@Body User user);

    @POST("/booxtown/rest/book/update")
    Call<Result> update(@Body Object book);

    @POST("/booxtown/rest/user/login_firebase")
    Call<Result> login(@Body Object user);

    @GET("/booxtown/user/get_userID")
    Call<Result> getuserID(@Query("session_id") String session_id);

    @GET("/booxtown/rest/user/getprofile")
    Call<UserResult> getprofile(@Query("session_id") String session_id);

    @POST("/booxtown/rest/user/updateprofile")
    Call<Result> updateprofile(@Body Object user);

    @POST("/booxtown/rest/user/changepassword")
    Call<Result> changepassword(@Query("session_id") String session_id, @Query("pwd_old") String pwd_old,
                                @Query("pwd_new") String pwd_new);

    @POST("/booxtown/rest/user/logout")
    Call<Result> logout(@Body Object user);

    @POST("/booxtown/rest/user/forgotpassword")
    Call<Result> forgotpassword(@Body Object email);

    @POST("/booxtown/rest/book/addbook")
    Call<Result> addbook(@Body Object book);

    @POST("/booxtown/rest/book/getinfo")
    Call<BookResult> getinfo(@Query("session_id") String session_id, @Query("book_id") String book_id);

    @GET("/booxtown/rest/book/getallbook")
    Call<BookResult> getAllBook();

    @GET("/booxtown/rest/book/getallbookbyuser")
    Call<BookResult> getAllBookByUser(@Query("session_id") String session_id);

    @POST("/booxtown/rest/book/book_delete")
    Call<Result> deletebook(@Body Object book);


    // topic
    @GET("/booxtown/rest/topic/topic_getall")
    Call<TopicResult> topic_getall();

    @GET("/booxtown/rest/topic/topic_gettop")
    Call<TopicResult> topic_gettop(@Query("session_id") String session_id,@Query("top") int top,@Query("from") int from);

    @POST("/booxtown/rest/topic/topic_addstatus")
    Call<Result> changeStatusTopic(@Body Object topic);

    @POST("/booxtown/rest/topic/topic_removestatus")
    Call<Result> changeStatusUnreadTopic(@Body Object thread);
    // end topic

    // Thread
    @GET("/booxtown/rest/thread/thread_getbytopic")
    Call<ThreadResult> getAllThread(@Query("topic_id") String topic_id);

    @GET("/booxtown/rest/thread/thread_gettop")
    Call<ThreadResult> threadGetTop(@Query("session_id") String session_id,@Query("topic_id") String topic_id,@Query("top") int top,@Query("from") int from);

    @POST("/booxtown/rest/thread/thread_addstatus")
    Call<Result> changeStatusThread(@Body Object thread);

    @POST("/booxtown/rest/thread/thread_removestatus")
    Call<Result> changeStatusUnreadThread(@Body Object thread);

    @POST("/booxtown/rest/thread/thread_insert")
    Call<Result> insertThread(@Body Object thread);

    @GET("/booxtown/rest/comment/comment_getbythread")
    Call<CommentResult> getAllComment(@Query("thread_id") String thread_id);

    @POST("/booxtown/rest/comment/comment_insert")
    Call<Result> inser_comment_thread(@Body Object comment);
    // End Thread

    @GET("/booxtown/rest/post/post_gettop")
    Call<WishboardResult> getAllWishboard(@Query("top") int top, @Query("from") int from, @Query("session_id") String session_id);

    @POST("/booxtown/rest/post/post_insert")
    Call<Result> insertWishboard(@Body Object wishboard);

    @GET("/booxtown/rest/book/book_gettop")
    Call<BookResult> book_gettop(@Query("session_id") String session_id,@Query("from") long from,@Query("top") long top);
    
    // Notification
    @GET("/booxtown/rest/notification/notification_gettop")
    Call<NotificationResult> getTopNotification(@Query("session_id") String session_id, @Query("top") int top, @Query("from") int from);

    @POST("/booxtown/rest/notification/notification_addstatus")
    Call<Result> changeStatusNotification(@Body Object topic);

    @POST("/booxtown/rest/notification/notification_removestatus")
    Call<Result> changeStatusUnreadNotification(@Body Object thread);
    // end Notification

    // Transaction
    @POST("/booxtown/rest/transaction/transaction_insert")
    Call<Result> transactionInsert(@Body Object transaction);
    // end Transaction
}
