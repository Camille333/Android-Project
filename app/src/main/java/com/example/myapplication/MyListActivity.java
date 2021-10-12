package com.example.myapplication;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MyListActivity extends ListActivity implements Runnable{
    private static final String TAG="MyList";
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==6){
                    ArrayList<String> list2 = (ArrayList<String>) msg.obj;
                    ListAdapter adapter = new ArrayAdapter<String>(
                            MyListActivity.this,
                            android.R.layout.simple_list_item_1,
                            list2);
                    setListAdapter(adapter);
                }
                super.handleMessage(msg);
            }
        };
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG,"run:run()......");
        List<String> ratelist = new ArrayList<String>();
        //利用Jsoup获取网络数据
        URL url =null;
        Bundle bundle=new Bundle();
        try {
            Document doc = Jsoup.connect("https://www.boc.cn/sourcedb/whpj/ ").get();
            Log.i(TAG, "run:title=" + doc.title());
            Elements h4s = doc.getElementsByTag("h4");
            for (Element h4 : h4s) {
                Log.i(TAG, "run:h4=" + h4.text());
            }
            Elements tables = doc.getElementsByTag("table");
            Element table2 = tables.get(1);
            Elements tds = table2.getElementsByTag("td");
            for (int i = 0; i < tds.size(); i += 8) {
                Element td1 = tds.get(i);
                Element td2 = tds.get(i + 5);//折算价
                String str1 = td1.text();
                String val = td2.text();
                Log.i(TAG, "run: " + str1 + "==> " + val);
                float v = 100f / Float.parseFloat(val);
                // 获取数据并返回 ……
                ratelist.add(str1 + "==> " + val);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //发送消息给主线程
        Message msg = handler.obtainMessage(6, ratelist);
        handler.sendMessage(msg);
        Log.i(TAG, "run:消息已发送");

    }
}