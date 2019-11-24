package com.example.music;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    TextView responseText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sendRequest=(Button)findViewById(R.id.send_request);
        responseText=(TextView)findViewById(R.id.response_text);
        sendRequest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.send_request){
            sendRequesWithHttpURLConnection();
        }
    }
    private void sendRequesWithHttpURLConnection(){
        //开启线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                BufferedReader reader=null;
                try{
                    URL url = new URL("https://www.baidu.com");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    //读取数据流
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while((line=reader.readLine())!=null){
                        response.append(line);
                    }
                    //刷新UI
                    showResponse(response.toString());
                }catch(Exception e){
                    e.printStackTrace();
                }finally {
                    if(reader!=null){
                        try{
                            reader.close();
                        }catch(IOException e){
                            e.printStackTrace();
                        }
                    }
                    if(connection!=null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
    private void showResponse(final String response){
        //（1）使用封装后的Handler
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                //进行UI操作
//                responseText.setText(response);
//            }
//        });
        Message msg=Message.obtain();
        msg.obj=response;
        mHandler.sendMessage(msg);


    }
    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            //使用msg
            super.handleMessage(msg);
            String str=(String)msg.obj;
//            Bundle bundle=msg.getData();
//            String str=bundle.toString();
            responseText.setText(str);
        }
    };
}
