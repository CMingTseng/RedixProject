package redix.booxtown.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import redix.booxtown.model.GenreValue;

/**
 * Created by thuyetpham94 on 29/09/2016.
 */
public class GetAllGenreAsync extends AsyncTask<Void,Void,List<GenreValue>>{
    public Context context;
    ProgressDialog dialog;
    public static List<String> list;
    public GetAllGenreAsync(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(context);
        dialog.setMessage("Please wait...");
        dialog.setIndeterminate(true);
        dialog.show();
    }

    @Override
    protected List<GenreValue> doInBackground(Void... voids) {
        GenreController genreController = new GenreController();
        return genreController.getAllGenre();
    }

    @Override
    protected void onPostExecute(List<GenreValue> genreValues) {
        list = new ArrayList<>();
        try {
            if(genreValues.size() >0){
//                list.add("All");
                for (int i=0;i<genreValues.size();i++){
                    list.add(genreValues.get(i).getTitle());
                }
            }
        }catch (Exception e){

        }
        dialog.dismiss();
    }
}
