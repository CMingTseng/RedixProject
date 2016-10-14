package redix.booxtown.custom;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import redix.booxtown.R;
import redix.booxtown.model.Genre;

/**
 * Created by thuyetpham94 on 30/08/2016.
 */
public class CustomListviewGenre extends RecyclerView.Adapter<CustomListviewGenre.HolerGenre> {
    ArrayList<Genre> result;
    Context context;
    public CustomListviewGenre(Context context, ArrayList<Genre> prgmNameList) {
        result=prgmNameList;
        this.context=context;
    }

    @Override
    public HolerGenre onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_dialog_genre, null);
        HolerGenre rcv = new HolerGenre(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(HolerGenre holder, final int position) {
        holder.tv.setText(result.get(position).getValue());
        holder.checkBox.setChecked(result.get(position).ischeck());
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.get(position).setIscheck(true);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    class HolerGenre extends RecyclerView.ViewHolder{
        TextView tv;
        CheckBox checkBox;
        public HolerGenre(View itemView) {
            super(itemView);
            tv=(TextView) itemView.findViewById(R.id.txt_custom_listview_genre);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox2);
        }
    }
}
