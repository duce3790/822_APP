package com.example.RSwitch;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView show;
    private String resultstr;
    private ImageButton Light1, Light2, Fan,id_setting;


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
                    Toast.makeText(MainActivity.this,"yee", Toast.LENGTH_SHORT ).show();
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
        Fan = (ImageButton)findViewById(R.id.fan_speed);
        id_setting = (ImageButton) findViewById(R.id.id_setting);

        View.OnClickListener listener_id_setting = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goin = new Intent();//建立intent
                goin.setClass(MainActivity.this, com.example.RSwitch.id_setting.class);
                startActivity(goin);//啟動
            }
        };
        id_setting = (ImageButton) findViewById(R.id.id_setting);
        id_setting.setOnClickListener(listener_id_setting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void loginPost(View view){
        sendRequestWithHttpClient("login");
    }
    public void light1(View view){
        sendRequestWithHttpClient("light1");
    }
    public void light2(View view){
        sendRequestWithHttpClient("light2");
    }
    public void fan(View view){
        sendRequestWithHttpClient("fan");
    }



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
}
