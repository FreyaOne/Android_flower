package com.example.fling.flower;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtil {

    //保存到本地
    public static boolean saveImageToGallery(Context context, Bitmap bmp){
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator + "dearxy";
        File appDir = new File(storePath);
        if(!appDir.exists()){
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + "jpg";
        File file = new File(appDir,fileName);
        try{
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流方式压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG,60, fos);
            fos.flush();
            fos.close();
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
//            updatePhotoMedia(file,context);
            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,uri));
            if(isSuccess){
                return true;
            }else{
                return false;
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //保存到数据库
    public static void savePhotoMedia(Context context,Bitmap bitmap){
        String uriString = MediaStore.Images.Media.insertImage(context.getContentResolver(),bitmap,null,null);
        File file = new File(getRealPathFromURI(Uri.parse(uriString),context));
        updatePhotoMedia(file,context);
    }
    //更新图库
    private static String getRealPathFromURI(Uri contentUri,Context context){
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri,proj,null,null,null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String fileStr = cursor.getString(column_index);
        cursor.close();
        return fileStr;
    }
    //得到绝对地址
    private static void updatePhotoMedia(File file, Context context){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        context.sendBroadcast(intent);
    }

}
