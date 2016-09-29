package redix.booxtown.custom;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import redix.booxtown.R;
import redix.booxtown.activity.NotificationRejectActivity;
import redix.booxtown.api.ServiceGenerator;
import redix.booxtown.model.Book;

/**
 * Created by thuyetpham94 on 27/08/2016.
 */
public class CustomListviewNotificationSwap extends BaseAdapter {
    List<Book> list;
    Context context;
    private static LayoutInflater inflater = null;

    public CustomListviewNotificationSwap(Context context,List<Book> list) {
        // TODO Auto-generated constructor stub
        this.list = list;
        this.context = context;
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
        Book book= list.get(position);
        rowView = inflater.inflate(R.layout.custom_listview_notification_swap, null);
        holder.tv = (TextView) rowView.findViewById(R.id.txt_notification_swap_title);
        holder.tv_byAuthor=(TextView) rowView.findViewById(R.id.textView27);
        holder.txt_genre=(TextView) rowView.findViewById(R.id.textView29);
        holder.seekBar=(ProgressBar) rowView.findViewById(R.id.seekBar) ;
        holder.tv.setText(book.getTitle());
        holder.img_book=(ImageView) rowView.findViewById(R.id.imageView11);
        holder.tv_byAuthor.setText(book.getAuthor());
        holder.txt_genre.setText(book.getGenre());
        holder.seekBar.setProgress(Integer.parseInt(book.getCondition()));
        String[] image = book.getPhoto().split(";");
        int index=image[0].indexOf("_+_");
        if(index>0 && image[0].length() >3 ) {
            String img = image[0].substring(index+3, image[0].length());
            Glide.with(context). load(ServiceGenerator.API_BASE_URL+"booxtown/rest/getImage?username=" + book.getUsername() + "&image=" +  img  + "").diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.blank_image).
                    into(holder.img_book);
            //Picasso.with(mContext).load(ServiceGenerator.API_BASE_URL+"booxtown/rest/getImage?username=" + ex.getUsername() + "&image=" + img + "").placeholder(R.drawable.blank_image).into(hoder.img_book);
        }

        holder.btn_confirm = (Button) rowView.findViewById(R.id.btn_notification_swap_listview);
        holder.btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_swap_listview_notification);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                Button btn_notification_swapdialog_confirm = (Button)dialog.findViewById(R.id.btn_notification_swapdialog_confirm);
                btn_notification_swapdialog_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(context, NotificationRejectActivity.class);
                        context.startActivity(intent);
                        dialog.dismiss();
                    }
                });

                ImageView imageView =(ImageView)dialog.findViewById(R.id.img_dialog_close_swap_listview_notification);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        return rowView;
    }
}
