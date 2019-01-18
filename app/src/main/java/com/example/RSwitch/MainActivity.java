package com.example.RSwitch;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.RSwitch.ip_setting.IP;

public class MainActivity extends AppCompatActivity {

    private TextView show,ip,status;
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

        Light1 = (ImageButton)findViewById(R.id.light1);
        Light2 = (ImageButton)findViewById(R.id.light2);
        IP_setting = (ImageButton) findViewById(R.id.ip_setting);
        login = (ImageButton) findViewById(R.id.login);
        ip = (TextView) findViewById(R.id.ip);
        status = (TextView) findViewById(R.id.status);
        final String user = LoginActivity.username;
        final String pass = LoginActivity.password;

        View.OnClickListener listener_light1 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PostCmd().execute("http://" + IP, "light1", user, pass);
            }
        };
        Light1.setOnClickListener(listener_light1);

        View.OnClickListener listener_light2 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PostCmd().execute("http://" + IP , "light2", user, pass);
            }
        };
        Light2.setOnClickListener(listener_light2);

        View.OnClickListener listener_IP_setting = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goin = new Intent();//建立intent
                goin.setClass(MainActivity.this, com.example.RSwitch.ip_setting.class);
                startActivity(goin);//啟動
                onPause();
            }
        };

        IP_setting.setOnClickListener(listener_IP_setting);

        View.OnClickListener listener_login = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!LoginActivity.logined){
                    Intent goin = new Intent();//建立intent
                    goin.setClass(MainActivity.this, com.example.RSwitch.LoginActivity.class);
                    startActivity(goin);//啟動
                }else{
                    Toast.makeText(MainActivity.this, "You are Logined", Toast.LENGTH_SHORT).show();
                }
            }
        };

        login.setOnClickListener(listener_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // App Logo
        //toolbar.setLogo(R.drawable.ic_launcher);
        // Title
        toolbar.setTitle("RSwitch");

        setSupportActionBar(toolbar);

        // Navigation Icon 要設定在 setSupoortActionBar 才有作用
        // 否則會出現 back bottom
        toolbar.setNavigationIcon(R.drawable.hamburger_icon);
        // Menu item click 的監聽事件一樣要設定在 setSupportActionBar 才有作用
        toolbar.setOnMenuItemClickListener(onMenuItemClick);

        ip.setText("  Login as : "+ip_setting.IP);
        status.setText("  Host IP : "+LoginActivity.username);

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


/*
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

                }catch (Exception e){

                    e.printStackTrace();

                }

            }

        }).start();
    }
*/

    public class PostCmd extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String TAG = "POSTCMD log";

            try {
                Log.i(TAG, "async");
                String link = strings[0];
                String light1 = strings[1];
                String username = strings[2];
                String password = strings[3];


                HttpClient httpCient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(link);

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("account", username));
                params.add(new BasicNameValuePair("password", password));
                params.add(new BasicNameValuePair("light1", "LIGHT 1"));
                params.add(new BasicNameValuePair("light2", "LIGHT 2"));
                //params.add(new BasicNameValuePair("username", username));
                //params.add(new BasicNameValuePair("password", password));

                httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                Log.i(TAG, link + params.toString());
                HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost);
                String strResult = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
                Log.i(TAG, strResult);

                return null;
            } catch (Exception e) {
                return null;
            }

        }
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            String msg = "";
            switch (menuItem.getItemId()) {
                case R.id.action_logout:
                    msg += "Log out";
                    LoginActivity.logined = false;
                    Intent goin = new Intent();//建立intent
                    goin.setClass(MainActivity.this, com.example.RSwitch.LoginActivity.class);
                    startActivity(goin);//啟動
                    finish();
                    break;
            }

            if(!msg.equals("")) {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
            return true;
        }


    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 為了讓 Toolbar 的 Menu 有作用，這邊的程式不可以拿掉
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
