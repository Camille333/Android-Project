package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;


public class TimeActivity extends AppCompatActivity {
    TextView mainTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        //获取textView控件
        mainTv = findViewById(R.id.main_tv);
        new TimeThread().start();//启动线程

    }
    //写一个新的线程每隔一秒发送一次消息,这样做会和系统时间相差1秒
    public class TimeThread extends Thread{
        @Override
        public void run() {
            super.run();
            do{
                try {
                    Thread.sleep(1000);   //1000ms = 1s
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }while (true);

        }
    }
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    SimpleDateFormat tt = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss"); //设置显示日期和时间的格式
                    Date date = new Date(System.currentTimeMillis());   //获取当前日期时间
                    mainTv.setText(tt.format(date));    //显示在文本框中
                    break;
            }
            return false;
        }
    });

}