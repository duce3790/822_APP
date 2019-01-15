package com.example.server_connect;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class server_connect extends AppCompatActivity {

    private TextView show;
    private String resultstr;
    private Button Light1, Light2, Fan;
    public PostOperate postoperate;
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
                //show.setText(response);
                show.setText(
                        " Light 1 : " + response.charAt(0) +
                        " Light 2 : " + response.charAt(1) +
                        " Fan : " + response.charAt(2)
                );
                if(Character.getNumericValue(response.charAt(0))==1)
                    light1 = true;
                else
                    light1 = false;
                if(Character.getNumericValue(response.charAt(1))==1)
                    light2 = true;
                else
                    light2 = false;
                fan = Character.getNumericValue(response.charAt(2));

                break;
            default:
                break;
        }
    }
};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_connect);
        show = (TextView)findViewById(R.id.Show);
        Light1 = (Button)findViewById(R.id.light1);
        Light2 = (Button)findViewById(R.id.light2);
        Fan = (Button)findViewById(R.id.fan);
        postoperate = new PostOperate(server_connect.this);;
    }

    public void loginPost(View view){
        sendRequestWithHttpClient();
    }
    public void light1(View view){
        postoperate.execute("light1");
    }
    public void light2(View view){
        postoperate.execute("light2");
    }
    public void fan(View view){
        postoperate.execute("fan");
    }



    private void  sendRequestWithHttpClient(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpCient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet("http://140.114.222.158/");



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
