package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class rateActivity extends AppCompatActivity {
    EditText rate1;
    EditText rate2;
    EditText rate3;
    double r1;  //美元汇率
    double r2;  //欧元汇率
    double r3;  //英镑汇率
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        rate1 = (EditText)findViewById(R.id.rate1);
        rate2 = (EditText)findViewById(R.id.rate2);
        rate3 = (EditText)findViewById(R.id.rate3);
        Intent intent =getIntent();
        r1 = intent.getDoubleExtra("rate1",0.15026973417);
        r2 = intent.getDoubleExtra("rate2",0.126632931655);
        r3 = intent.getDoubleExtra("rate3",0.1143692365182);
        rate1.setText(String.valueOf(r1));
        rate2.setText(String.valueOf(r2));
        rate3.setText(String.valueOf(r3));
    }

    public void onClick(View v) {
        if (v.getId() == R.id.save) {   //保存汇率值并跳回到汇率换算页面
            Intent intent = getIntent();
            r1 = Double.valueOf(rate1.getText().toString());
            r2 = Double.valueOf(rate2.getText().toString());
            r3 = Double.valueOf(rate3.getText().toString());
            Bundle bundle=new Bundle();
            bundle.putDouble("rate1", r1);
            bundle.putDouble("rate2", r2);
            bundle.putDouble("rate3", r3);
            intent.putExtras(bundle);

            setResult(3,intent);//设置code及带回的数据
            finish();
        }
    }
}