package com.example.RSwitch;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ip_setting extends AppCompatActivity {
    public String IP;
    public String[] contents = {""};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_setting);

        String path = Environment.getExternalStorageDirectory().getPath();
        path += "/RSwitch/ipconfig.txt";
        try{
            BufferedReader input = new BufferedReader(new FileReader(path));
            StringBuffer sb = new StringBuffer();
            String strInput = input.readLine();
            while(strInput != null){
                sb.append(strInput).append("\n");
                System.out.println(strInput);
                strInput = input.readLine();
            }

            contents = sb.toString().split("\n");
            input.close();

        }catch (Exception e){
            Log.e("File", e.toString());
        }
        final ListView IPlist = (ListView)findViewById(R.id.IP_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ip_setting.this, android.R.layout.simple_list_item_1,contents);

        IPlist.setAdapter(adapter);

        IPlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                IP = contents[position];
                Toast.makeText(ip_setting.this, "set" + IP + " as connent target", Toast.LENGTH_SHORT).show();
            }

        });

    }


}
