package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RateList2Activity extends AppCompatActivity implements Runnable, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    public static final String TAG = "RateList2Activity";
    List<HashMap<String,String>> list = new ArrayList<>();
    ListView listView;
    Handler handler;
    ProgressBar progressBar;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_list2);

        listView = findViewById(R.id.listview);//列表
        //progressBar = findViewById(R.id.progressbar);//进度条

        handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                Log.i(TAG, "handleMessage: 收到消息");
                if(msg.what == 0){
                    //ListAdapter adapter=new SimpleAdapter(RateList2Activity.this,list,R.layout.item_listview,new String[]{"name","value"},new int[]{R.id.item_name,R.id.item_detail});
                    //listView.setAdapter(adapter);
                    adapter = new MyAdapter(RateList2Activity.this, R.layout.item_listview, list);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(RateList2Activity.this);
                    listView.setOnItemLongClickListener(RateList2Activity.this);
                    //progressBar.setVisibility(View.GONE);
                }
                super.handleMessage(msg);
            }
        };
        //开启线程
        Thread thread = new Thread(RateList2Activity.this);
        thread.start();

    }

    @Override
    public void run() {
        try {
            Thread.sleep(3000);//进度条出现时间为3s
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        org.jsoup.nodes.Document document = null;
        try {
            document = Jsoup.connect("https://www.boc.cn/sourcedb/whpj/").get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements elements = document.getElementsByTag("table");
        Element table = elements.get(1);
        Elements trs = table.getElementsByTag("tr");
        trs.remove(0);

        for(Element element : trs){
            Elements td = element.select("td");
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("name", td.get(0).text());
            hashMap.put("value", td.get(4).text());
            list.add(hashMap);
        }
        Message message = handler.obtainMessage(0);
        handler.sendMessage(message);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object item = listView.getItemAtPosition(position);
        HashMap<String,String> hashMap = (HashMap<String, String>)item;
        String name = hashMap.get("name");
        String value = hashMap.get("value");
        Intent intent = new Intent(RateList2Activity.this, ShowActivity.class);
        intent.putExtra("name",name);
        intent.putExtra("value",value);
        startActivity(intent);
    }

    @Override
    //长按时实现删除数据项
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        Log.i(TAG, "长按操作：第" + position + "项");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示").setMessage("请确认是否删除当前数据").setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "对话框事件处理");
                adapter.remove(position);
                adapter.notifyDataSetChanged();
            }
        }).setNegativeButton("否", null);
        builder.create().show();
        Log.i(TAG, "List size=" + list.size());
        return true;//true:当长按时，不执行点击操作
    }
}
