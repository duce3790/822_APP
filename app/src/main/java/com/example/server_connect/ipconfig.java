package com.example.server_connect;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import java.io.File;

public class ipconfig {

    protected static final int FLAG_EXISTS = 1;    //已存在
    protected static final int FLAG_SUCCESS = 2;   //创建成功
    protected static final int FLAG_FAILED = 3;    //创建失败

    protected static final int REQUEST_EXTERNAL_STORAGE = 1;
    protected static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    protected static void verifyStoragepermit(Activity activity){
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the u
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    protected static int Createipconfig(){
        String path = Environment.getExternalStorageDirectory().getPath();
        String configpath = path + "/RSwitch/ipconfig.txt/";
        File ipconfig = new File(configpath);

        if (!ipconfig.getParentFile().exists()) {
            //父目录不存在 创建父目录
            if (!ipconfig.getParentFile().mkdirs()) {
                return FLAG_FAILED;
            }
        }


        if(ipconfig.exists()){
            return FLAG_EXISTS;
        }
        try{
            if(ipconfig.createNewFile()){
                return FLAG_SUCCESS;
            }else{
                return  FLAG_FAILED;
            }
        }catch (Exception e){
            return FLAG_FAILED;
        }


    }

    /*protected static int Createlyric(){
        String path = Environment.getExternalStorageDirectory().getPath();
        String lyricpath = path + "/animusic/lyric/";
        File lyric = new File(lyricpath);
        if(lyric.exists()){
            return FLAG_EXISTS;
        }

        if(lyric.mkdirs()){
            return FLAG_SUCCESS;
        }else{
            return  FLAG_FAILED;
        }

    }*/
}
