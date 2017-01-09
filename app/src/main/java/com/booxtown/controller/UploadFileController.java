package com.booxtown.controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;

import com.booxtown.api.ServiceGenerator;
import com.booxtown.api.ServiceInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import com.booxtown.model.Result;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadFileController {

    private ServiceInterface service;

    public boolean success = false;

    public UploadFileController()
    {
        service = ServiceGenerator.GetInstance();
    }

    public boolean uploadFile(ArrayList<Bitmap> bm, List<String> fileName){

        ArrayList<byte[]> data2= new ArrayList<>();
        ArrayList<RequestBody> reqFile= new ArrayList<>();
        for (int i=0; i< bm.size(); i++) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                bm.get(i).compress(Bitmap.CompressFormat.PNG
                        , 80, bos);
                reqFile.add( RequestBody.create(MediaType.parse("application/octet-stream"), bos.toByteArray()));
            } catch (Exception ex) {

            }
        }
        try {
            MultipartBody.Part body, body1, body2;
            Call<Result> req = null;
            if (fileName.size() == 1) {
                body = MultipartBody.Part.createFormData("images", fileName.get(0).trim(), reqFile.get(0));
                req = service.postImage(body);
            }
            if (fileName.size() == 2) {
                body = MultipartBody.Part.createFormData("images", fileName.get(0).trim(), reqFile.get(0));
                body1 = MultipartBody.Part.createFormData("images", fileName.get(1).trim(), reqFile.get(1));
                req = service.postImage1(body, body1);
            }
            if (fileName.size() == 3) {
                body = MultipartBody.Part.createFormData("images", fileName.get(0).trim(), reqFile.get(0));
                body1 = MultipartBody.Part.createFormData("images", fileName.get(1).trim(), reqFile.get(1));
                body2 = MultipartBody.Part.createFormData("images", fileName.get(2).trim(), reqFile.get(2));
                req = service.postImage2(body, body1, body2);
            }

            req.enqueue(new Callback<Result>() {
                @Override
                public void onResponse(Call<Result> call, Response<Result> response) {
                    int s = response.body().getCode();
                    if (s == 200) {
                        success = true;
                    }
                }

                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                    t.printStackTrace();
                    success = false;
                }
            });
        }catch (Exception exx){

        }
        return success;
    }

    public Bitmap getImage(String username,String image) {

        Call<ResponseBody> call = service.getImage(username,image);
        try{
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            return BitmapFactory.decodeStream(call.execute().body().byteStream());
        }
        catch (Exception ex){

        }
        return  null;
    }

    public File saveBitmapToFile(File file){
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE=75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100 , outputStream);

            return file;
        } catch (Exception e) {
            return null;
        }
    }

}
