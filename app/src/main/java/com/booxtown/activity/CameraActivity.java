package com.booxtown.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.sql.Timestamp;
import java.util.List;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.ErrorCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.booxtown.R;
import com.booxtown.adapter.ListBookAdapter;
import com.booxtown.controller.BookController;
import com.booxtown.controller.CheckSession;
import com.booxtown.controller.GPSTracker;
import com.booxtown.fragment.ListingsFragment;
import com.booxtown.model.Book;
import com.squareup.picasso.Picasso;

import test.jinesh.easypermissionslib.EasyPermission;

/**
 * Created by Administrator on 11/01/2017.
 */

public class CameraActivity extends Activity implements Callback,
        OnClickListener,EasyPermission.OnPermissionResult {
    private SurfaceView surfaceView_camera;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private ImageView btn_capture_camera, btn_flash_camera, btn_flip_camera;
    private TextView txt_use_photo_camera, txt_cancel_camera;

    private int cameraId;
    private boolean flashmode = false;
    private int rotation;
    Bitmap loadedImage = null;
    int num_list = 0;
    private boolean flag=false;
    float longitude=0;
    float latitude=0;
    int keyChoose=0;
    GPSTracker gpsTracker;
    EasyPermission easyPermission;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);


        easyPermission = new EasyPermission();
        easyPermission.requestPermission(this, Manifest.permission.CAMERA);

        // camera surface view created
        gpsTracker=new GPSTracker(CameraActivity.this);
        cameraId = CameraInfo.CAMERA_FACING_BACK;
        btn_capture_camera = (ImageView) findViewById(R.id.btn_capture_camera);
        Picasso.with(CameraActivity.this).load(R.drawable.capture_camera2).into(btn_capture_camera);
        btn_flash_camera = (ImageView) findViewById(R.id.btn_flash_camera);
        Picasso.with(CameraActivity.this).load(R.drawable.flash_camera2).into(btn_flash_camera);
        btn_flip_camera = (ImageView) findViewById(R.id.btn_flip_camera);
        Picasso.with(CameraActivity.this).load(R.drawable.flip_camera2).into(btn_flip_camera);
        surfaceView_camera = (SurfaceView) findViewById(R.id.surfaceView_camera);
        txt_use_photo_camera = (TextView) findViewById(R.id.txt_use_photo_camera);
        txt_cancel_camera = (TextView) findViewById(R.id.txt_cancel_camera);

        surfaceHolder = surfaceView_camera.getHolder();
        surfaceHolder.addCallback(this);
        btn_flip_camera.setOnClickListener(this);
        btn_capture_camera.setOnClickListener(this);
        btn_flash_camera.setOnClickListener(this);
        txt_cancel_camera.setOnClickListener(this);
        txt_use_photo_camera.setOnClickListener(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (Camera.getNumberOfCameras() > 1) {
            btn_flip_camera.setVisibility(View.VISIBLE);
        }
        if (!getBaseContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FLASH)) {
            btn_flash_camera.setVisibility(View.GONE);
        }

        SharedPreferences pref =getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String sessionID = pref.getString("session_id", null);
        int is_current_location= pref.getInt("is_current_location",1);
        if(is_current_location==1) {
            longitude = (float) gpsTracker.getLongitude();
            latitude = (float) gpsTracker.getLatitude();
        }else {
            longitude = Float.parseFloat(pref.getString("Longitude",(float)gpsTracker.getLongitude()+""));
            latitude = Float.parseFloat(pref.getString("Latitude",(float) gpsTracker.getLatitude()+""));
        }
        try{
            keyChoose=getIntent().getIntExtra("keyChoose",0);
        }catch (Exception err){

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        easyPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionResult(String permission, boolean isGranted) {
        switch (permission) {
            case Manifest.permission.ACCESS_FINE_LOCATION:
                if (isGranted) {
                    easyPermission.requestPermission(CameraActivity.this,Manifest.permission.CAMERA);
                } else {
                    easyPermission.requestPermission(CameraActivity.this,Manifest.permission.CAMERA);
                }
                break;
            case Manifest.permission.CAMERA:
                if (isGranted) {
                    easyPermission.requestPermission(CameraActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
                } else {
                    easyPermission.requestPermission(CameraActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                break;
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                if (isGranted) {
                    easyPermission.requestPermission(CameraActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE);
                } else {
                    easyPermission.requestPermission(CameraActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE);
                }
                break;
            case Manifest.permission.READ_EXTERNAL_STORAGE:
                if (isGranted) {

                } else {
                    finish();
                }
                break;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!openCamera(CameraInfo.CAMERA_FACING_BACK)) {
            alertCameraDialog();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_flash_camera:
                flashOnButton();
                break;
            case R.id.btn_flip_camera:
                flipCamera();
                break;
            case R.id.btn_capture_camera:
                if(!flag) {
                    takeImage();
                }
                break;
            case R.id.txt_cancel_camera:
                cancelAndRetake();
                break;
            case R.id.txt_use_photo_camera:
                usePhoto();
                break;

            default:
                break;
        }
    }

    private void usePhoto() {
        SharedPreferences pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String session_id = pref.getString("session_id", null);
        listingAsync listingAsync = new listingAsync(session_id,CameraActivity.this,loadedImage);
        listingAsync.execute();
    }

    private void cancelAndRetake() {
        if (txt_cancel_camera.getText().toString().equals("Cancel")) {
            onBackPressed();
            finish();
            releaseCamera();
        } else {
            if (!openCamera(CameraInfo.CAMERA_FACING_BACK)) {
                alertCameraDialog();
            }
            changeDisplay(false);
        }
    }

    private void takeImage() {
        camera.takePicture(null, null, new PictureCallback() {

            private File imageFile;

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                try {
                    // convert byte array into bitmap
                    Bitmap rotatedBitmap = null;
                    loadedImage = BitmapFactory.decodeByteArray(data, 0,
                            data.length);
                    releaseCamera();
                    changeDisplay(true);

                    Matrix rotateMatrix = new Matrix();
                    rotateMatrix.postRotate(rotation);
                    rotatedBitmap = Bitmap.createBitmap(loadedImage, 0, 0,
                            loadedImage.getWidth(), loadedImage.getHeight(),
                            rotateMatrix, false);
                    String state = Environment.getExternalStorageState();
                    File folder = null;
                    if (state.contains(Environment.MEDIA_MOUNTED)) {
                        folder = new File(Environment
                                .getExternalStorageDirectory() + "/Booxtown");
                    } else {
                        folder = new File(Environment
                                .getExternalStorageDirectory() + "/Booxtown");
                    }

                    boolean success = true;
                    if (!folder.exists()) {
                        success = folder.mkdirs();
                    }
                    java.util.Date date = new java.util.Date();
                    String url="";
                    if (success) {

                        imageFile = new File(folder.getAbsolutePath()
                                + File.separator
                                + "Image.jpeg");

                        url=folder.getAbsolutePath()
                                + File.separator
                                + "Image.jpeg";
                        imageFile.createNewFile();
                    } else {
                        Toast.makeText(getBaseContext(), "Image Not saved",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ByteArrayOutputStream ostream = new ByteArrayOutputStream();

                    // save image into gallery
                    rotatedBitmap.compress(CompressFormat.JPEG, 50, ostream);

                    FileOutputStream fout = new FileOutputStream(imageFile);
                    fout.write(ostream.toByteArray());
                    fout.close();

                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("image", url);

                    editor.commit();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void changeDisplay(boolean flag) {
        if (flag) {
            this.flag=true;
            btn_flash_camera.setVisibility(View.GONE);
            btn_flip_camera.setVisibility(View.GONE);
            btn_capture_camera.setVisibility(View.GONE);
            txt_cancel_camera.setText("Retake");
            txt_use_photo_camera.setVisibility(View.VISIBLE);
        } else {
            this.flag=false;
            btn_flash_camera.setVisibility(View.VISIBLE);
            btn_flip_camera.setVisibility(View.VISIBLE);
            btn_capture_camera.setVisibility(View.VISIBLE);
            txt_cancel_camera.setText("Cancel");
            txt_use_photo_camera.setVisibility(View.GONE);

        }
    }

    private boolean openCamera(int id) {
        boolean result = false;
        cameraId = id;
        releaseCamera();
        try {
            camera = Camera.open(cameraId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (camera != null) {
            try {
                setUpCamera(camera);
                camera.setErrorCallback(new ErrorCallback() {

                    @Override
                    public void onError(int error, Camera camera) {

                    }
                });

                Camera.Parameters p = camera.getParameters();
                p.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);

                camera.setParameters(p);
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
                camera.autoFocus(null);
                result = true;


            } catch (IOException e) {
                e.printStackTrace();
                result = false;
                releaseCamera();
            }
        }
        return result;
    }
    @Override
    public void onPause() {
        releaseCamera();;
        super.onPause();
    }
    @Override
    public void onResume(){
        super.onResume();
        releaseCamera();;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if(!flag) {
                camera.autoFocus(null);
            }
        }

        return true;
    }
    private void setUpCamera(Camera c) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degree = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 0;
                break;
            case Surface.ROTATION_90:
                degree = 90;
                break;
            case Surface.ROTATION_180:
                degree = 180;
                break;
            case Surface.ROTATION_270:
                degree = 270;
                break;

            default:
                break;
        }

        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            // frontFacing
            rotation = (info.orientation + degree) % 330;
            rotation = (360 - rotation) % 360;
        } else {
            // Back-facing
            rotation = (info.orientation - degree + 360) % 360;
        }
        c.setDisplayOrientation(rotation);
        Parameters params = c.getParameters();

        showFlashButton(params);

        List<String> focusModes = params.getSupportedFlashModes();
        if (focusModes != null) {
            if (focusModes
                    .contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                params.setFlashMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }
        }

        params.setRotation(rotation);
    }

    private void showFlashButton(Parameters params) {
        boolean showFlash = (getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FLASH) && params.getFlashMode() != null)
                && params.getSupportedFlashModes() != null
                && params.getSupportedFocusModes().size() > 1;

        btn_flash_camera.setVisibility(showFlash ? View.VISIBLE
                : View.INVISIBLE);

    }

    private void releaseCamera() {
        try {
            if (camera != null) {
                camera.setPreviewCallback(null);
                camera.setErrorCallback(null);
                camera.stopPreview();
                camera.release();

            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("error", e.toString());
            camera = null;
        }
    }

    private void alertCameraDialog() {
        AlertDialog.Builder dialog = createAlert(CameraActivity.this,
                "Camera info", "error to open camera");
        dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });

        dialog.show();
    }

    private Builder createAlert(Context context, String title, String message) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(
                new ContextThemeWrapper(context,
                        android.R.style.Theme_Holo_Light_Dialog));
        dialog.setIcon(R.drawable.ic_launcher);
        if (title != null)
            dialog.setTitle(title);
        else
            dialog.setTitle("Information");
        dialog.setMessage(message);
        dialog.setCancelable(false);
        return dialog;

    }

    private void flashOnButton() {
        if (camera != null) {
            try {
                Parameters param = camera.getParameters();
                param.setFlashMode(!flashmode ? Parameters.FLASH_MODE_TORCH
                        : Parameters.FLASH_MODE_OFF);
                camera.setParameters(param);
                flashmode = !flashmode;
            } catch (Exception e) {
                // TODO: handle exception
            }

        }
    }

    private void flipCamera() {
        int id = (cameraId == CameraInfo.CAMERA_FACING_BACK ? CameraInfo.CAMERA_FACING_FRONT
                : CameraInfo.CAMERA_FACING_BACK);
        if (!openCamera(id)) {
            alertCameraDialog();
        }
    }

    class listingAsync extends AsyncTask<String, Void, List<Book>> {

        Context context;
        ProgressDialog dialog;
        String session_id;
        int top, from;
        Bitmap bt;

        public listingAsync(String session_id, Context context, Bitmap bt) {
            this.session_id = session_id;
            this.context = context;
            this.bt=bt;
        }

        @Override
        protected List<Book> doInBackground(String... strings) {
            CheckSession checkSession = new CheckSession();
            boolean check = checkSession.checkSession_id(session_id);
            SharedPreferences pref = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            if (!check) {
                editor.putString("session_id", null);
                editor.putString("active", null);
                editor.commit();
                Intent intent = new Intent(context, SignIn_Activity.class);
                context.startActivity(intent);
                this.cancel(true);
            }
            BookController bookController = new BookController();
            return bookController.getAllBookInApp(0,1000,10,longitude,latitude,"","",pref.getString("session_id", ""),Integer.parseInt(pref.getString("user_id", "0")),10000,0);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            try {
                if(keyChoose==0) {
                    num_list = books.size();
                    Intent intent = new Intent(CameraActivity.this, MainAllActivity.class);
                    intent.putExtra("key", "6");
                    intent.putExtra("num_list", num_list);
                    startActivity(intent);

                }else {
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            } catch (Exception e) {

                Intent intent = new Intent(CameraActivity.this, MainAllActivity.class);
                intent.putExtra("key", "6");
                intent.putExtra("num_list", 0);
                startActivity(intent);
                String sss= e.getMessage();
            }
        }
    }
}