package com.example.server_connect;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

    //private EditText usernameField,passwordField;
    private TextView show;
    private EditText edit;
    public static final int SHOW_RESPONSE = 0;
    private Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case SHOW_RESPONSE:
                String response = (String) msg.obj;
                //show.setText(response);
                edit.setText(response);
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

        //usernameField = (EditText)findViewById(R.id.username);
        //passwordField = (EditText)findViewById(R.id.password);
        //show = (TextView)findViewById(R.id.Show);
        edit = (EditText)findViewById(R.id.Edit);
    }

    public void loginPost(View view){
        //String username = usernameField.getText().toString();
        //String password = passwordField.getText().toString();
        Toast toast = Toast.makeText(server_connect.this, "Yee", Toast.LENGTH_SHORT);
        toast.show();
        sendRequestWithHttpClient();

        //new SigninActivity(this).execute(username,password);
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
                        Pattern state2 = Pattern.compile("/s");
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
