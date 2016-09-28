package redix.booxtown.api;

import android.graphics.Bitmap;


/**
 * Created by Administrator on 25/06/2016.
 */
public class ServiceAPI {
    private ServiceInterface git;
    private String result="";
    private  Bitmap bmGet=null;
    public ServiceAPI() {
        this.git =  ServiceGenerator.createService(ServiceInterface.class);

    }
    // up ảnh lên server
//    public void uploadFile(Bitmap bm,String fileName){
//
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        bm.createScaledBitmap(bm, 100,100, true);
//        byte[] data2= null;
//        try {
//            bm.compress(Bitmap.CompressFormat.PNG, 100, bos);
//            data2 = bos.toByteArray();
//        }
//        catch (Exception ex){
//
//        }
//        RequestBody reqFile = RequestBody.create(MediaType.parse("application/octet-stream"), data2);
//        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", fileName, reqFile);
//        retrofit2.Call<Result> req = git.postImage(body);
//        req.enqueue(new Callback<Result>() {
//            @Override
//            public void onResponse(Call<Result> call, Response<Result> response) { }
//
//            @Override
//            public void onFailure(Call<Result> call, Throwable t) {
//                t.printStackTrace();
//            }
//        });
//    }

    // get ảnh từ server về
//    public Bitmap getRetrofitImage(Context ct) {
//        retrofit2.Call<okhttp3.ResponseBody> call = git.getImageDetails();
//        try{
//            if (android.os.Build.VERSION.SDK_INT > 9) {
//                StrictMode.ThreadPolicy policy =
//                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
//                StrictMode.setThreadPolicy(policy);
//            }
//            return BitmapFactory.decodeStream(call.execute().body().byteStream());
//        }
//        catch (Exception ex){
//
//        }
//        return  null;
//    }

}
