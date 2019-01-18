package com.example.RSwitch;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Light1 = (ImageButton) findViewById(R.id.light1);
        Light2 = (ImageButton) findViewById(R.id.light2);
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
                new PostCmd().execute("http://" + IP, "light2", user, pass);
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
                if (!LoginActivity.logined) {
                    Intent goin = new Intent();
                    goin.setClass(MainActivity.this, com.example.RSwitch.LoginActivity.class);
                    startActivity(goin);
                } else {
                    Toast.makeText(MainActivity.this, "You are Logined", Toast.LENGTH_SHORT).show();
                }
            }
        };

        login.setOnClickListener(listener_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("RSwitch");

        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.hamburger_icon);
        toolbar.setOnMenuItemClickListener(onMenuItemClick);

        ip.setText("  Login as : " +LoginActivity.username );
        status.setText("  Host IP : " + ip_setting.IP);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Intent intent;
                intent = new Intent();
                int id = menuItem.getItemId();
                if (id == R.id.nav_share) {
                } else if (id == R.id.action_logout) {
                    intent.setClass(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.nav_send) {

                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    public class PostCmd extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String TAG = "POSTCMD log";

            try {
                Log.i(TAG, "async");
                String link = strings[0];
                String light = strings[1];
                String username = strings[2];
                String password = strings[3];


                HttpClient httpCient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(link);

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("account", username));
                params.add(new BasicNameValuePair("password", password));
                if(light.equals("light1")){
                    params.add(new BasicNameValuePair("light1", "LIGHT 1"));
                }else{
                    params.add(new BasicNameValuePair("light2", "LIGHT 2"));
                }

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
                    Intent goin = new Intent();
                    goin.setClass(MainActivity.this, com.example.RSwitch.LoginActivity.class);
                    startActivity(goin);
                    finish();
                    break;
            }

            if(!msg.equals("")) {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
            return true;
        }


    };
}
