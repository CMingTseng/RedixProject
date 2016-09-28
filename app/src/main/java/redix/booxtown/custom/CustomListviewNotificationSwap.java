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
import android.widget.TextView;

import redix.booxtown.R;
import redix.booxtown.activity.NotificationRejectActivity;

/**
 * Created by thuyetpham94 on 27/08/2016.
 */
public class CustomListviewNotificationSwap extends BaseAdapter {
    String[] result;
    Context context;
    private static LayoutInflater inflater = null;

    public CustomListviewNotificationSwap(Context context, String[] prgmNameList) {
        // TODO Auto-generated constructor stub
        result = prgmNameList;
        this.context = context;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
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
        Button btn_confirm;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.custom_listview_notification_swap, null);
        holder.tv = (TextView) rowView.findViewById(R.id.txt_notification_swap_title);
        holder.tv.setText(result[position]);
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
