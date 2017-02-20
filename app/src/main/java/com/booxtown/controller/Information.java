package com.booxtown.controller;

import com.booxtown.model.Genre;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thuyetpham94 on 27/09/2016.
 */
public class Information {
    public final static String checkNetwork = "\" Connection Error \" Unable to connect to Server. Please check your network settings and try again";
    public final static String noti_fill_username = "Please enter valid a Username";
    public final static String noti_fill_password = "Please enter a Password";
    public final static String noti_wrong_login = "Email or Password doesn't match. Please try again";
    public final static String noti_check_mail = "Please check email";
    public final static String noti_fill_firstname = "Please enter valid a Firstname";
    public final static String noti_fill_lastname = "Please enter valid a Lastname";
    public final static String noti_fill_phone = "Please enter valid a Phone number";
    public final static String noti_fill_birthday = "Please enter Birthday";
    public final static String noti_validate_email = "You have entered an invalid email address. Please try again.";
    public final static String noti_validate_password = "Please fill password field";
    public final static String noti_match_pass = "Password not match";
    public final static String noti_check_term = "Please agree to Terms and Conditions to continue";
    public final static String noti_login_success = "Signup Success";
    public final static String noti_username_taken = "Username entered already exists. Please try another username";
    public final static String noti_email_taken = "Email entered already exists. Please try another username";
    public final static String noti_quit_app = "Do you want quit application?";
    public final static String noti_input_email = "Please input email";
    public final static String noti_update_success = "Update Successful";
    public final static String noti_no_data = "No Data Found!";
    public final static String noti_send_checkoutmylist = "You successfully responded to the wish";
    public final static String noti_no_comment = "Please enter comment to be posted. ";
    public final static String noti_no_data_listing = "Please enter comment to be posted. ";
    public final static String noti_update_false = "Not update data, please try again";
    public final static String noti_insert_comment = "insert comment success";
    public final static String noti_no_insert_commnet = "insert comment no success";
    public final static String noti_insert_thread = "Thread has been created successfully";
    public final static String noti_no_insert_thread = "Sorry ! Thread could not be created. Please try again.";
    public final static String noti_insert_wishboard = "Wishboard has been created successfully";
    public final static String noti_no_insert_wishboard = "Sorry ! Wishboard could not be created. Please try again.";
    public final static String noti_insert_error = "insert wishboard no success";
    public final static String noti_dialog = "Please wait...";
    public final static String noti_delete_success = "Delete success";
    public final static String noti_delete_fail = "Delete Fail";
    public final static String noti_change_pass = "Change password success";
    public final static String noti_nochange_pass = "Old password not match";
    public final static String noti_update_setting = "Update setting success";
    public final static String noti_update_nosetting = "Update setting no success";
    public final static String noti_update_fail = "Update Failed";
    public final static String noti_tran_done = "Transaction is done";
    public final static String noti_invite_success = "Thank you! An email has been sent to your friend";
    public final static String noti_invite_fail = "Invite friend fail";
    public final static String noti_enter_email = "Please enter valid a email address";
    public final static String noti_check_wishboard = "Please fill a comment field";
    public final static String noti_over_leter = "Sorry ! maximum character limit is 50";
    public final static String noti_checkbox = "At least one action required";
    public final static String noti_not_check_free = "Do not check free";
    public final static String noti_not_check_sell = "You can't choose both Buy and Free action";
    public final static String noti_show_complete = "Please complete this transaction";
    public final static String noti_show_comment_empty = "Hmm, Please do not leave it empty";
    public final static String noti_show_choose_type_addbook_empty = "Please choose a type of listing (Swap/Sell/Free)";
    public final static String noti_show_choose_type_addbook = "Sorry ! You cannot choose ' Free ' listing along with 'Swap' or 'Sell' listings.";
    public final static String noti_show_not_sent_comment = "Comment not sent.";
    public final static String noti_show_sent_comment = "Comment sent successfully.";
    public final static String session_timeout = "please try to login again";

    public final static String add_book_success = "Your book listing will be active in 24 hours";

    public static int minRager = 0;
    public  static int maxRager= 1000;
    public  static double maxSeekbar= 10;
    public  static ArrayList<Genre> lstGenre=new ArrayList<>();
    public  static boolean nearDistance=false;
    public  static boolean priceLowtoHigh=false;
    public  static boolean priceHightoLow=false;
    public  static boolean recently=false;

    public static int FragmentChoose = 0;

    public static String FragmentEmail = "";
    public static String FragmentPhone = "";
    public static String FragmentDateTime = "";
    public static String FragmentBirthday = "";
    public static String FragmentPhoto = "";
    public static String FragmentFirst = "";
    public static String FragmentLast = "";

    // setting

}
