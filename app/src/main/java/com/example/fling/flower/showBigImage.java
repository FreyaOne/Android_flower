package com.example.fling.flower;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import pub.devrel.easypermissions.EasyPermissions;

import static com.example.fling.flower.ImageUtil.saveImageToGallery;

public class showBigImage extends AppCompatActivity{   //点击查看大图
    private ImageView imageView;
    private Object mGestureDetector;
    private Context context;
    private Handler handler =  new Handler(){

        public void handleMessage(Message msg){
            switch(msg.what) {
                case 0:
                    final Bitmap bmp = (Bitmap) msg.obj;
                    imageView.setImageBitmap(bmp);
                    imageView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            AlertDialog builder = new AlertDialog.Builder(showBigImage.this)
                                    .setTitle("另存为")
                                    .setMessage("\n是否保存图片到本地")
                                    .setPositiveButton("确定", null)
                                    .show();
                            builder.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (Build.VERSION.SDK_INT >= 23) {
                                        //读取sd卡的权限
                                        String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};   //申请写的权限
                                        if (EasyPermissions.hasPermissions(showBigImage.this, mPermissionList)) {
                                            //已经同意过
//                                            saveImageToGallery(context,bmp);
                                        } else {
                                            Toast.makeText(showBigImage.this, "需要sd卡读取权限", Toast.LENGTH_SHORT).show();
                                            ActivityCompat.requestPermissions(showBigImage.this,mPermissionList,0x01);
                                        }
                                    }
                                    saveImageToGallery(showBigImage.this, bmp);
                                    saveImage(bmp);
                                    finish();
                                }
                            });
                            return false;
                        }
                    });
                break;
            }

        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化界面，
        // 从这里之后才可以获取到界面信息

        setContentView(R.layout.activity_show_big_image);
        this.imageView = (ImageView)findViewById(R.id.imageView);

        Bundle bundle = getIntent().getExtras();
        final String url = bundle.getString("url");
        Log.d("TAG",url);

        new Thread(new Runnable(){

            public void run(){
                Bitmap bmp = getURLImage(url);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = bmp;
                handler.sendMessage(msg);
            }
        }).start();

    }

    public Bitmap getURLImage(String url){
        Bitmap bmp = null;
        try{
            URL myUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)myUrl.openConnection();
            conn.setConnectTimeout(6000);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.connect();
            InputStream is = conn.getInputStream(); //获取图片的数据流
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmp;
    }


    //保存图片
    private void saveImage(Bitmap bitmap){
        boolean isSaveSuccess = saveImageToGallery(this,bitmap);
        if(isSaveSuccess){
            Toast.makeText(this,"保存图片成功",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"保存图片失败", Toast.LENGTH_LONG).show();
        }
    }

    //授权结果，分发下去
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        //跳转到onPermissionsGranted或者onPermissionsDenied去回调授权结果
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

}
