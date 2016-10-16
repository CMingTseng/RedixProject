package com.booxtown.custom;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.booxtown.api.ServiceGenerator;
import com.booxtown.model.Notification;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.booxtown.R;

import com.booxtown.controller.NotificationController;
import com.booxtown.controller.ObjectCommon;
import com.booxtown.controller.TransactionController;
import com.booxtown.model.Book;
import com.booxtown.model.Transaction;

/**
 * Created by thuyetpham94 on 27/08/2016.
 */
public class CustomListviewNotificationSwap extends BaseAdapter {
    List<Book> list;
    Context context;
    String trans_id;
    String bookNameMe;
    Transaction trans;
    private static LayoutInflater inflater = null;

    public CustomListviewNotificationSwap(Context context, List<Book> list, String trans_id, String bookNameMe, Transaction trans) {
        // TODO Auto-generated constructor stub
        this.list = list;
        this.context = context;
        this.trans_id = trans_id;
        this.bookNameMe = bookNameMe;
        this.trans = trans;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder {
        TextView tv;
        TextView tv_byAuthor;
        TextView txt_genre;
        ProgressBar seekBar;
        ImageView img_book;
        Button btn_confirm;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        View rowView;
        final Book book = list.get(position);
        rowView = inflater.inflate(R.layout.custom_listview_notification_swap, null);
        holder.tv = (TextView) rowView.findViewById(R.id.txt_notification_swap_title);
        holder.tv_byAuthor = (TextView) rowView.findViewById(R.id.textView27);
        holder.txt_genre = (TextView) rowView.findViewById(R.id.textView29);
        holder.seekBar = (ProgressBar) rowView.findViewById(R.id.seekBar);
        holder.tv.setText(book.getTitle());
        holder.img_book = (ImageView) rowView.findViewById(R.id.imageView11);
        holder.tv_byAuthor.setText(book.getAuthor());
        holder.txt_genre.setText(book.getGenre());
        holder.seekBar.setProgress(Integer.parseInt(book.getCondition()));
        String[] image = book.getPhoto().split(";");
        int index = image[0].indexOf("_+_");
        if (index > 0 && image[0].length() > 3) {
            String img = image[0].substring(index + 3, image[0].length());
            Glide.with(context)
                    .load(ServiceGenerator.API_BASE_URL + "booxtown/rest/getImage?username=" +image[0].substring(0,index) + "&image=" + img + "")
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.blank_image).
                    into(holder.img_book);
        }
        if(trans.getIs_accept()==0 || trans.getIs_cancel()==0|| trans.getIs_reject()==0) {
            holder.btn_confirm = (Button) rowView.findViewById(R.id.btn_notification_swap_listview);
            holder.btn_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_swap_listview_notification);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                    TextView confirm_dialog_noti_swap = (TextView) dialog.findViewById(R.id.confirm_dialog_noti_swap);
                    TextView txt_title_book_you_noti_swap = (TextView) dialog.findViewById(R.id.txt_title_book_you_noti_swap);
                    TextView txt_title_book_buy_noti_swap = (TextView) dialog.findViewById(R.id.txt_title_book_buy_noti_swap);
                    try {
                        confirm_dialog_noti_swap.setText("Confirm swap with " + (book.getUsername().substring(0, 1).toUpperCase() + book.getUsername().substring(1, book.getUsername().length())));
                    }catch (Exception exx){
                        confirm_dialog_noti_swap.setText("Confirm swap with " +book.getUsername());
                    }
                    txt_title_book_you_noti_swap.setText(bookNameMe);
                    txt_title_book_buy_noti_swap.setText(book.getTitle());

                    Button btn_notification_swapdialog_confirm = (Button) dialog.findViewById(R.id.btn_notification_swapdialog_confirm);
                    btn_notification_swapdialog_confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SharedPreferences pref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                            String session_id = pref.getString("session_id", null);
                            transactionChangeStatus trans = new transactionChangeStatus(context, session_id, trans_id, "1", book.getId(), book);
                            trans.execute();
                            dialog.dismiss();
                        }
                    });

                    ImageView imageView = (ImageView) dialog.findViewById(R.id.img_dialog_close_swap_listview_notification);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                }
            });
        }
        return rowView;
    }

    class transactionChangeStatus extends AsyncTask<Void, Void, String> {

        Context context;
        ProgressDialog dialog;
        Book book;
        String session_id, trans_id, status_id;
        String book_id;

        public transactionChangeStatus(Context context, String session_id, String trans_id, String status_id, String book_id, Book book) {
            this.context = context;
            this.session_id = session_id;
            this.trans_id = trans_id;
            this.status_id = status_id;
            this.book_id = book_id;
            this.book = book;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String transactionID = "";
            TransactionController transactionController = new TransactionController();
            transactionID = transactionController.transactionUpdateStatus(session_id, trans_id, status_id, book_id);
            return transactionID;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String transactionID) {
            //Intent intent = new Intent(context, NotificationAcceptActivity.class);
            //intent.putExtra("trans", trans);
            //intent.putExtra("book", book);
            //context.startActivity(intent);

            SharedPreferences pref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            String firstName = pref.getString("firstname", "");

            // send notifi user buy
            List<Hashtable> list = new ArrayList<>();
            Notification notification = new Notification(trans.getUser_sell().toUpperCase() + " accepted for a swap book request", trans.getId()+"","2" );
            Hashtable obj = ObjectCommon.ObjectDymanic(notification);
            obj.put("user_id", book.getUser_id());
            obj.put("messages",firstName.substring(0,1).toUpperCase()+ firstName.substring(1,firstName.length())+ " accepted your swap request");
            list.add(obj);
            NotificationController controller = new NotificationController();
            controller.sendNotification(list);
            // send notifi user seller
            List<Hashtable> listSeller = new ArrayList<>();
            Notification notificationSeller = new Notification("Swap Request", trans.getId()+"","0" );
            Hashtable objSeller = ObjectCommon.ObjectDymanic(notificationSeller);
            objSeller.put("user_id", trans.getUser_seller_id());
            objSeller.put("messages", "You accepted a swap request");
            listSeller.add(objSeller);
            NotificationController controllerSeller = new NotificationController();
            controllerSeller.sendNotification(listSeller);
            // end
            super.onPostExecute(transactionID);
        }
    }
}
