package com.example.RSwitch;


import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PostOperate extends AsyncTask<String, Void, String>{


    protected void onPreExecute(){
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String...strings) {
        try {
            String link = "140.114.222.158/index.php?";
            if (strings[0] == "fan") {
                link = link + strings[0] + "=FAN";
            } else if(strings[0]=="light1"){
                link = link + strings[0] + "=LIGHT+1";
            } else {
                link = link + strings[0] + "=LIGHT+2";
            }
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(link);

            HttpResponse resp = client.execute(get);
            HttpEntity respentity = resp.getEntity();
            String response = EntityUtils.toString(respentity, "utf-8");
            if (resp.getStatusLine().getStatusCode() == 200) {
                Pattern state1 = Pattern.compile("<.+?>", Pattern.DOTALL);
                Pattern state2 = Pattern.compile("[^0-9]");
                Matcher match1 = state1.matcher(response);
                String string1 = match1.replaceAll("");
                Matcher match2 = state2.matcher(string1);
                String string2 = match2.replaceAll("");
                return string2;
            }else{
                return "sdd";
            }


        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }
    protected void onPostExecute(String string){
    }

}
