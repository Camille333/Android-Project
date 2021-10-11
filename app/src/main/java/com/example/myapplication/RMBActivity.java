package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;

public class RMBActivity extends AppCompatActivity implements Runnable, View.OnClickListener{
    private static final String TAG = "RMBActivity";
    EditText editTextren;
    TextView Result;
    Bundle bundle = new Bundle();
    Handler handler;
    double r1;   //美元汇率
    double r2;  //韩币汇率
    double r3; //欧元汇率
    double[] rr=new double[4];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rmb);

        handler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message msg) {
                //这里完成更新UI的操作
                Log.i(TAG, "发送消息");
                if (msg.what == 5) {
                    //String str = (String) msg.obj;
                    //Log.i(TAG, "handleMessagestr：str=" + str);
                    Bundle bdl = (Bundle) msg.obj;
                    r1 = bdl.getDouble("r1");
                    r2 = bdl.getDouble("r2");
                    r3 = bdl.getDouble("r3");
                    Log.i(TAG, "handleMessage:dollarRate=" + r1);
                    Log.i(TAG, "handleMessage:wonRate=" + r2);
                    Log.i(TAG, "handleMessage:euroRate=" + r3);

                    //提示
                    Toast.makeText(RMBActivity.this, "数据已更新", Toast.LENGTH_SHORT).show();
                }
                super.handleMessage(msg);
            }
        };
        //开启线程
        Thread t = new Thread(this);
        t.start();

        setViews();
        //读取保存的数据
        SharedPreferences sp = getSharedPreferences("rate1", Activity.MODE_PRIVATE);
        r1 = sp.getFloat("rate1", 0.1f);
        Log.i(TAG, "onCreate：get from sp rate1=" + r1);//记录日志

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
        //URL url = null;
        try {
            /*
            url = new URL("https://www.usd-cny.com/icbc.htm");//需要输入可访问的网址
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            InputStream in = http.getInputStream();
            String html = InputStream2String(in);
            Log.i(TAG, "run：html=" + html);
            */

            //利用Jsoup获取html网页数据
            String url = "http://www.usd-cny.com/abc.htm";
            Document doc = Jsoup.connect(url).get();
            Log.i(TAG, "run:" + doc.title());

            //doc.getElementsByTag获取网页中的元素
            //获取h4标题元素
            Elements h4s = doc.getElementsByTag("h4");
            for(Element h4 : h4s){
                Log.i(TAG, "run: h4=" + h4.text());
            }
            //获取table表格元素
            Elements tables = doc.getElementsByTag("table");
            Element table6 = tables.first();

            //获取TD中的数据
            Elements tds = table6.getElementsByTag("td");
            int count = 0;
            for(int i=0; i<tds.size(); i += 25){
                Element td1 = tds.get(i);
                Element td2 = tds.get(i + 4);

                String str1 = td1.text();
                String val = td2.text();
                Log.i(TAG, "run:" + str1 + "==>" + val);

                double r = 100f / Float.parseFloat(val);
                rr[count] = r;
                Log.i(TAG, "run: rate=" + str1 + "==>" + r);
                count = count + 1;
            }
            bundle.putDouble("r1", rr[0]);
            bundle.putDouble("r2", rr[0]);
            bundle.putDouble("r3", rr[0]);
            /*
            r1 = rr[0];
            r2 = rr[1];
            r3 = rr[2];
            */
            //保存数据到sp中
            SharedPreferences sp = getSharedPreferences("test",MODE_MULTI_PROCESS);
            SharedPreferences.Editor editor = sp.edit();
            editor.putFloat("rate1", (float) r1);
            editor.putFloat("rate2", (float) r2);
            editor.putFloat("rate3", (float) r3);
            editor.commit();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        //获取Message对象，用于返回主线程
        Message msg = handler.obtainMessage(5, bundle);
        //msg.obj = "Hello from run()";
        handler.sendMessage(msg);
        Log.i(TAG, "run：消息已发送");
    }

    public void setViews() {
        editTextren = (EditText)findViewById(R.id.editTextren);
        Result = (TextView)findViewById(R.id.Result);
    }
    @Override
    public void onClick(View v) {
        double R1 = 0;
        double Res = 0;
        String a;
        String str = "换算结果为：";
        if(v.getId()==R.id.button){//跳转到汇率设置页面
            Intent intent=new Intent(this, rateActivity.class);
            intent.putExtra("rate1",r1);
            intent.putExtra("rate2",r2);
            intent.putExtra("rate3",r3);
            startActivityForResult(intent,1);
        }
        else if (v.getId()==R.id.button1){
            a=editTextren.getText().toString();
            if("".equals(a)){
                Toast.makeText(RMBActivity.this,"输入有误，请重新输入正确的人民币金额！",Toast.LENGTH_SHORT).show();
            }
            else{
                R1 =Float.valueOf(a);
                Res = R1 * r1;
                str = str + String.format("%.2f",Res) + "美元";//汇率换算结果保留两位小数显示
            }

        }
        else if(v.getId()==R.id.button2){
            a=editTextren.getText().toString();
            if("".equals(a)){
                Toast.makeText(RMBActivity.this,"输入有误，请重新输入正确的人民币金额！",Toast.LENGTH_SHORT).show();
            }
            else{

                R1 =Float.valueOf(a);
                Res = R1 * r2;
                str = str + String.format("%.2f",Res) + "韩币";//汇率换算结果保留两位小数显示
            }
        }
        else if(v.getId()==R.id.button3){
            a=editTextren.getText().toString();
            if("".equals(a)){
                Toast.makeText(RMBActivity.this,"输入有误，请重新输入正确的人民币金额！",Toast.LENGTH_SHORT).show();
            }
            else{
                R1 =Float.valueOf(a);
                Res = R1 * r3;
                str = str + String.format("%.2f",Res) + "欧元";//汇率换算结果保留两位小数显示
            }
        }
        Result.setText(str);
    }

    //这里是设置获取从第二页面中返回的数据，如果我们没有设置这个的话，我们返回该页面，那么数据都会清空
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            r1 = data.getDoubleExtra("rate1", 0.15026973417);
            r2 = data.getDoubleExtra("rate2", 0.126632931655);
            r3 = data.getDoubleExtra("rate3", 0.1143692365182);
        }
        if (requestCode == 1 && resultCode == 3) {
            Bundle bundle = data.getExtras();
            r1 = bundle.getDouble("rate1", 0.15026973417);
            r2 = bundle.getDouble("rate2", 0.126632931655);
            r3 = bundle.getDouble("rate3", 0.1143692365182);
        }
        //修改保存内容
        SharedPreferences sp = getSharedPreferences("rate1", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat("rate1", (float) r1);
        editor.apply();
        Log.i(TAG, "onActivityResult：save to a sp" + r1);
    }
}
