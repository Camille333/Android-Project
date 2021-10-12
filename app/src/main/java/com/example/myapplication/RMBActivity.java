package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Date;


public class RMBActivity extends AppCompatActivity  implements View.OnClickListener,Runnable {

    private static final String TAG = "RMBActivity";
    EditText editText;
    TextView textView;
    float dollarRate;
    float euroRate;
    float wonRate;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rmb);

        editText=findViewById(R.id.input);
        textView=findViewById(R.id.output);

        //获取之前存储在sp中的数据
        SharedPreferences sp = getSharedPreferences("myRate", Activity.MODE_PRIVATE);
        dollarRate = sp.getFloat("dollarRate",6.8f);
        euroRate = sp.getFloat("euroRate",6.8f);
        wonRate = sp.getFloat("wonRate",6.8f);

        handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                Log.i(TAG, "handleMessage: 收到消息");
                if(msg.what == 0){
                    String str = (String) msg.obj;
                    textView.setText(textView.getText().toString() + str);
                }
                super.handleMessage(msg);
            }
        };
        //开启一个线程
        Thread thread = new Thread(RMBActivity.this);
        thread.start();
    }

    @Override
    public void onClick(View v) {
        String input = editText.getText().toString();
        if(input.equals("")){
            Toast.makeText(this,"Please input a number as RMB",Toast.LENGTH_SHORT).show();
        }
        else {
            if(v.getId() == R.id.dollar){
                float money = Float.valueOf(input);
                textView.setText(String.format("%.2f", money*dollarRate) + " Dollar");
            }
            if(v.getId() == R.id.euro){
                float money = Float.valueOf(input);
                textView.setText(String.format("%.2f", money*euroRate) + " EURO");
            }
            if(v.getId() == R.id.won){
                float money = Float.valueOf(input);
                textView.setText(String.format("%.2f", money*wonRate) + " WON");
            }
        }
    }

    public void open(View v) {
        if(v.getId() == R.id.jump){
            Intent intent = new Intent(this,rateActivity.class);
            intent.putExtra("dollarRate",dollarRate);
            intent.putExtra("euroRate",euroRate);
            intent.putExtra("wonRate",wonRate);

            //选择打开的activity是新创建的还是已有的
            startActivityForResult(intent,1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == 2){
            dollarRate = data.getFloatExtra("dollarRate", 6.8f);
            euroRate = data.getFloatExtra("euroRate", 6.8f);
            wonRate = data.getFloatExtra("wonRate", 6.8f);
        }
        if(requestCode == 1 && resultCode == 3){
            Bundle bundle = data.getExtras();
            dollarRate = bundle.getFloat("dollarRate", 6.8f);
            euroRate = bundle.getFloat("euroRate", 6.8f);
            wonRate = bundle.getFloat("wonRate", 6.8f);
        }
    }

    @Override
    public void run() {
        //获取系统当前日期
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd ");
        Date date = new Date(System.currentTimeMillis());
        String dateSS = formatter.format(date);
        //获取之前存储在sp中的时间
        SharedPreferences sp = getSharedPreferences("myRate", Activity.MODE_PRIVATE);
        String dateS = sp.getString("Date","");
        //判断两个日期是否一致，若不一致，则说明是首次运行——>利用Jsoup从网络上获取汇率
        if(!dateS.equals(dateSS)){
            org.jsoup.nodes.Document document = null;
            try {
                document = Jsoup.connect("http://www.usd-cny.com/").get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements elements = document.getElementsByTag("tr");
            elements.remove(0);
            for(Element element : elements){
                Elements td = element.select("td");
                if("美元".equals(td.get(0).text())){
                    dollarRate =100f / Float.valueOf(td.get(4).text());
                    Log.i(TAG,"dollarRate: " + dollarRate );
                }else if("欧元".equals(td.get(0).text())){
                    euroRate = 100f / Float.valueOf(td.get(4).text());
                    Log.i(TAG,"euroRate: " + euroRate);

                }else if("韩币".equals(td.get(0).text())){
                    wonRate = 100f / Float.valueOf(td.get(4).text());
                    Log.i(TAG,"wonRate: " + wonRate);
                }
            }
            //将上述从网页中获取的数据保存到sp中
            SharedPreferences.Editor editor = sp.edit();
            editor.putFloat("dollarRate", dollarRate);
            editor.putFloat("euroRate", euroRate);
            editor.putFloat("wonRate", wonRate);
            editor.putString("Date", formatter.format(date));
            editor.commit();
            //editor.apply();
        }
    }

    //jsoup解析string的html
    private void getDataByJsoup(String html){
        org.jsoup.nodes.Document document = null;
        //document = Jsoup.connect(url).get();
        document = Jsoup.parse(html);
        Elements elements = document.select("tr");
        for(Element element : elements){
            String td = element.select("td").text();
            String[] tds = td.split("\\s+");
            float avg = 0.0f;
            float num = 0.0f;
            for(String ss : tds){
                if(!ss.equals("-") && !ss.equals(tds[0])){
                    avg += Float.valueOf(ss);
                    num ++;
                }
            }
            avg /= num;
            if(num != 0){
                Log.i(TAG,tds[0] + "平均值" + avg);
                Log.i(TAG,"_________________________");
            }
        }
    }

    private String inputStream2String(InputStream inputStream) throws Exception{
        final  int bufferSize = 1024;
        final  char[] buffer = new char[bufferSize];
        final  StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream,"gb2312");
        while (true){
            int rsz = in.read(buffer,0,buffer.length);
            if(rsz < 0){
                break;
            }
            out.append(buffer,0,rsz);
        }
        return out.toString();
    }
}