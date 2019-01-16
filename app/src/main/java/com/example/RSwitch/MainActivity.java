package com.example.RSwitch;

import android.content.Intent;
import android.media.Image;
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
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private TextView show;
    private String resultstr;
    private ImageButton id_setting,login;

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

        View.OnClickListener listener_login = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goin = new Intent();//建立intent
                goin.setClass(MainActivity.this, com.example.RSwitch.LoginActivity.class);
                startActivity(goin);//啟動
            }
        };
        login = (ImageButton) findViewById(R.id.login);
        login.setOnClickListener(listener_login);
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
