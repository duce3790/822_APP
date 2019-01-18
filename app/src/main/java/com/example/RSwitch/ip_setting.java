package com.example.RSwitch;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class ip_setting extends AppCompatActivity {
    public static String IP;
    public ArrayList<String> contents = new ArrayList<String>();
    private String path = Environment.getExternalStorageDirectory().getPath() + "/RSwitch/ipconfig.txt";;
    private ListView IPlist;

    private Button Add, Edit, Del;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_setting);
        ipconfig.verifyStoragepermit(this);
        ipconfig.Createipconfig();
        new DoIOoperater().execute("read");

        IPlist = (ListView)findViewById(R.id.IP_list);
        IPlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                IP = contents.get(position);
                Toast.makeText(ip_setting.this, "set " + IP + " as connent target", Toast.LENGTH_SHORT).show();
                if(IP!=null){
                    Intent goin = new Intent();//建立intent
                    if(LoginActivity.logined){
                        goin.setClass(ip_setting.this, MainActivity.class);
                    }else{
                        goin.setClass(ip_setting.this, LoginActivity.class);
                    }
                    startActivity(goin);//啟動
                    finish();
                }
            }

        });


        Add = (Button)findViewById(R.id.add);
        Edit = (Button)findViewById(R.id.edit);
        Del = (Button)findViewById(R.id.delete);

        View.OnClickListener listener_add = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText editText = new EditText(ip_setting.this);
                new AlertDialog.Builder(ip_setting.this)
                        .setTitle(getString(R.string.title))
                        .setIcon(android.R.drawable.ic_input_add)
                        .setView(editText).setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String input = editText.getText().toString();
                        if (input.equals("")){
                            Toast.makeText(ip_setting.this, getString(R.string.inValid), Toast.LENGTH_LONG).show();
                        }
                        else{
                            new DoIOoperater().execute("add", input);
                        }
                    }
                }).setNegativeButton(getString(R.string.Cancel), null).show();
            }
        };

        Add.setOnClickListener(listener_add);

    }



    private class DoIOoperater extends AsyncTask<String, Void, ArrayList>{

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            if(strings[0].equals("read")){
                File file = new File(path);
                try{
                    InputStream inputStream = new FileInputStream(file);
                    if(inputStream != null){
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        String line;
                        while ((line = bufferedReader.readLine() )!= null){
                            contents.add(line);
                        }
                        inputStream.close();
                    }
                    return contents;
                }catch (Exception e){
                    Log.e("File", e.toString());
                    return null;
                }
            }else if(strings[0].equals("add")){
                try{
                    FileWriter fw = new FileWriter(path, true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write(strings[1]);
                    bw.newLine();
                    bw.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList strings) {

            //執行後 完成背景任務

            super.onPostExecute(strings);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ip_setting.this, android.R.layout.simple_list_item_1,strings);
            IPlist.setAdapter(adapter);
        }
    }
}


