package com.example.RSwitch;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import java.io.File;

public class ipconfig {

    protected static final int FLAG_EXISTS = 1;
    protected static final int FLAG_SUCCESS = 2;
    protected static final int FLAG_FAILED = 3;

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

}
