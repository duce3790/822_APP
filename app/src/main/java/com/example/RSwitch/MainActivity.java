package com.example.RSwitch;

import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private TextView show;
    private String resultstr;
    private ImageButton IP_setting,login, Light1, Light2;

    boolean light1, light2;
    int fan;
    public static final int SHOW_RESPONSE = 0;
    private Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case SHOW_RESPONSE:
                String response = (String) msg.obj;
                String strs = response.substring(response.length()-3);
                //show.setText(response);
                //Toast.makeText(MainActivity.this,"yee", Toast.LENGTH_SHORT ).show();
                if(Character.getNumericValue(strs.charAt(0))==1)
                    light1 = true;
                else
                    light1 = false;
                if(Character.getNumericValue(strs.charAt(1))==1)
                    light2 = true;
                else
                    light2 = false;
                fan = Character.getNumericValue(strs.charAt(2));
                show.setText(
                                "Light 1 : " + strs.charAt(0) + "\n" +
                                "Light 2 : " + strs.charAt(1) + "\n" +
                                "Fan Speed : " + strs.charAt(2)
                );

                break;
            default:
                break;
        }
    }
};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ipconfig.verifyStoragepermit(this);
        switch (ipconfig.Createipconfig()){
            case 1:
                Toast.makeText(this, "ipconfig已存在", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(this, "ipconfig已創建成功", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(this, "ipconfig創建失敗", Toast.LENGTH_SHORT).show();
                break;
        }

        Light1 = (ImageButton)findViewById(R.id.light1);
        Light2 = (ImageButton)findViewById(R.id.light2);
        IP_setting = (ImageButton) findViewById(R.id.ip_setting);
        login = (ImageButton) findViewById(R.id.login);

        View.OnClickListener listener_light1 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "ClickLight1", Toast.LENGTH_SHORT).show();
                new PostCmd().execute("http://140.114.222.158/", "light1");
            }
        };
        Light1.setOnClickListener(listener_light1);

        View.OnClickListener listener_light2 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "ClickLight2", Toast.LENGTH_SHORT).show();
                new PostCmd().execute("http://140.114.222.158/", "light2");
            }
        };
        Light2.setOnClickListener(listener_light2);

        View.OnClickListener listener_IP_setting = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goin = new Intent();//建立intent
                goin.setClass(MainActivity.this, com.example.RSwitch.ip_setting.class);
                startActivity(goin);//啟動
            }
        };

        IP_setting.setOnClickListener(listener_IP_setting);

        View.OnClickListener listener_login = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goin = new Intent();//建立intent
                goin.setClass(MainActivity.this, com.example.RSwitch.LoginActivity.class);
                startActivity(goin);//啟動
            }
        };

        login.setOnClickListener(listener_login);
    }

    public void loginPost(View view){
        sendRequestWithHttpClient("login");
    }
    /*public void light1(View view){
        sendRequestWithHttpClient("light1");
    }
    public void light2(View view){
        sendRequestWithHttpClient("light2");
    }
    public void fan(View view){
        sendRequestWithHttpClient("fan");
    }*/



    private void  sendRequestWithHttpClient(final String str){

        new Thread(new Runnable() {

            @Override

            public void run() {

                HttpClient httpCient = new DefaultHttpClient();
                HttpGet httpGet;
                if(str.equals("light1")){
                    httpGet = new HttpGet("http://140.114.222.158/index.php?light1=LIGHT+1");
                }else if(str.equals("light2")){
                    httpGet = new HttpGet("http://140.114.222.158/index.php?light2=LIGHT+2");
                }else if(str.equals("fan")){
                    httpGet = new HttpGet("http://140.114.222.158/index.php?fan=FAN");
                }else{
                    httpGet = new HttpGet("http://140.114.222.158/");
                }

                try{

                    HttpResponse httpResponse = httpCient.execute(httpGet);

                    if (httpResponse.getStatusLine().getStatusCode() == 200){

                        Pattern state1 = Pattern.compile("<.+?>", Pattern.DOTALL);

                        Pattern state2 = Pattern.compile("[^0-9]");

                        HttpEntity entity = httpResponse.getEntity();

                        String response = EntityUtils.toString(entity,"utf-8");

                        Matcher match1 = state1.matcher(response);

                        String string1 = match1.replaceAll("");

                        Matcher match2 = state2.matcher(string1);

                        String string2 = match2.replaceAll("");

                        Message message = new Message();

                        message.what = SHOW_RESPONSE;

                        message.obj = string2;

                        handler.sendMessage(message);
                    }

                }catch (Exception e){

                    e.printStackTrace();

                }

            }

        }).start();
    }

    public class PostCmd extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String TAG = "POSTCMD log";

            try {
                Log.i(TAG, "async");
                String unit = strings[1].toUpperCase();
                String link = strings[0];


                HttpClient httpCient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(link);

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("light1", unit));

                httpPost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
                Log.i(TAG, params.toString());
                HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost);
                String strResult = EntityUtils.toString(httpResponse.getEntity(),HTTP.UTF_8);
                Log.i(TAG, strResult.toString());

                return null;
            }catch (Exception e){
                return null;
            }

        }
    }
}
