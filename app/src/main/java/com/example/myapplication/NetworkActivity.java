package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class NetworkActivity extends AppCompatActivity implements Runnable{
    private static final String TAG = "threadActivity";
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        handler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message msg) {
                //这里完成更新UI的操作
                Log.i(TAG, "发送消息");
                if (msg.what == 5) {
                    String str = (String) msg.obj;
                    Log.i(TAG, "handleMessagestr：str=" + str);
                }
                super.handleMessage(msg);
            }
        };
        //开启线程
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "run:....");

        //在子线程中获取网络数据
        URL url = null;
        try {
            url = new URL("https://www.usd-cny.com/icbc.htm");//需要输入可访问的网址
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            InputStream in = http.getInputStream();
            String html = InputStream2String(in);
            Log.i(TAG, "run：html=" + html);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //获取Message对象，用于返回主线程
        Message msg = handler.obtainMessage(5);
        msg.obj = "Hello from run()";
        handler.sendMessage(msg);
    }
    private String InputStream2String(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "gb2312");
        while (true){
            int rsz = in.read(buffer,0, buffer.length);
            if(rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }



}
