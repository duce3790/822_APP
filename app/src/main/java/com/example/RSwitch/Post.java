package com.example.RSwitch;


import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


public class Post extends AsyncTask{

    public Post(Context context){}

    protected void onPreExecute(){
    }

    @Override
    protected String doInBackground(Object[] arg0) {
        try{
            String username = (String)arg0[0];
            String password = (String)arg0[1];

            String link = "https://www.google.com.tw/";
            String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            data += URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            while((line = reader.readLine())!=null){
                sb.append(line);
                break;
            }
            return sb.toString();

        } catch (Exception e){
            return new String("Expection:" + e.getMessage());
        }
    }

    protected void onPostExecute(String result){
        return;
    }

}
