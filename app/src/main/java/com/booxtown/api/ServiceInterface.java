package com.booxtown.api;

import com.booxtown.model.Contact;
import com.booxtown.model.NumberBookResult;
import com.booxtown.model.WishboardResult;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;

import com.booxtown.model.CommentBookResult;
import com.booxtown.model.CommentResult;
import com.booxtown.model.DashBoardResult;
import com.booxtown.model.GenreValueResult;
import com.booxtown.model.NotificationResult;
import com.booxtown.model.Result;
import com.booxtown.model.BookResult;
import com.booxtown.model.SettingResult;
import com.booxtown.model.ThreadResult;
import com.booxtown.model.TopicResult;
import com.booxtown.model.TransactionResult;
import com.booxtown.model.User;
import com.booxtown.model.UserResult;

import retrofit2.Call;
import retrofit2.http.Body;
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

    @POST("/booxtown/rest/user/insertContact")
    Call<Result> insertContact(@Body Object contact);

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
    Call<Result> changepassword(@Body Object user);

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

    @GET("/booxtown/rest/book/book_getBookByBookId")
    Call<BookResult> getBookByID(@Query("book_id") String book_id);

    @GET("/booxtown/rest/transactionhistory/tranhis_getTransactionById")
    Call<TransactionResult> getBookTransaction(@Query("transaction_id") String tranhisid);

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

    @GET("/booxtown/rest/comment/comment_getTopByThread")
    Call<CommentResult> getTopCommentThread(@Query("thread_id") String thread_id,@Query("top") int top,@Query("from") int from);

    @POST("/booxtown/rest/comment/comment_insert")
    Call<Result> inser_comment_thread(@Body Object comment);
    // End Thread

    @GET("/booxtown/rest/post/post_gettop")
    Call<WishboardResult> getWishboardByTop(@Query("top") int top, @Query("from") int from, @Query("session_id") String session_id);

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

    @GET("/booxtown/rest/thread/get_threadbyid")
    Call<ThreadResult> getthreadbyid(@Query("id") String id);

    @GET("/booxtown/rest/topic/get_topicbyid")
    Call<TopicResult> gettopicbyid(@Query("id") String id);
    // end Notification

    // Transaction
    @POST("/booxtown/rest/transaction/transaction_insert")
    Call<Result> transactionInsert(@Body Object transaction);

    @POST("/booxtown/transactionhistory/tranhis_checkTransactionExits")
    Call<Result> CheckExitsTransaction(@Body Object transaction);


    @POST("/booxtown/rest/transaction/transaction_updateStatus")
    Call<Result> transactionUpdateStatus(@Body Object transaction);

    @POST("/booxtown/rest/transactionhistory/tranhis_updateRating")
    Call<Result> updateRating(@Body Object transaction);

    // end Transaction
    @POST("/booxtown/rest/setting/setting_update")
    Call<Result> updateSetting(@Body Object setting);

    @GET("/booxtown/rest/setting/getSettingByUserId")
    Call<SettingResult> getSettingByUserId(@Query("session_id") String session_id);

    @GET("/booxtown/rest/book/getAllGenre")
    Call<GenreValueResult> getAllGenre();

    @GET("/booxtown/rest/comment/comment_getByBookId")
    Call<CommentBookResult> getCommentBook(@Query("book_id") String book_id);

    @GET("/booxtown/rest/comment/comment_getTopByBookId")
    Call<CommentBookResult> getTopCommentBook(@Query("book_id") String book_id,@Query("top") int top,@Query("from") int from);

    @GET("/booxtown/rest/comment/comment_getTopByWishboardId")
    Call<CommentBookResult> getCommentWishboard(@Query("post_id") String post_id,@Query("top") int top, @Query("from") int from);

    @GET("/booxtown/rest/user/getProfileByUserId")
    Call<UserResult> getProfileByUserId(@Query("user_id") int user_id);

    @GET("/booxtown/rest/transactionhistory/tranhis_getTopTransaction")
    Call<DashBoardResult> getDashBoard(@Query("session_id") String session_id,@Query("top") int top,@Query("from") int from);

    @POST("/booxtown/rest/user/inviteFriend")
    Call<Result> inviteFriend(@Body Object email);

    @GET("/booxtown/rest/book/book_gettopbyuserid")
    Call<BookResult> getTopBookById(@Query("user_id") int user_id,@Query("top") int top,@Query("from") int from);

    @GET("/booxtown/book/getNumberOfBook")
    Call<NumberBookResult> getNumberBook(@Query("user_id") int user_id);

    @POST("/booxtown/user/checkSessionTimeout")
    Call<Result> checkSession(@Body Object session_id);
}
