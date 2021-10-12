package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class rateActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "rateActivity";
    float dollarRate;
    float euroRate;
    float wonRate;
    EditText dollar;
    EditText euro;
    EditText won;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        dollar = findViewById(R.id.dollarRate);
        euro = findViewById(R.id.euroRate);
        won = findViewById(R.id.wonRate);
        //获取汇率数据
        Intent intent = getIntent();
        dollarRate = intent.getFloatExtra("dollarRate",6.8f);
        euroRate = intent.getFloatExtra("euroRate",6.8f);
        wonRate = intent.getFloatExtra("wonRate",6.8f);

        dollar.setText(String.valueOf(dollarRate));
        euro.setText(String.valueOf(euroRate));
        won.setText(String.valueOf(wonRate));
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.save){
            Intent intent = getIntent();

            //数据的返回类型为bundle
            dollarRate = Float.valueOf(dollar.getText().toString());
            euroRate = Float.valueOf(euro.getText().toString());
            wonRate = Float.valueOf(won.getText().toString());

            Bundle bundle = new Bundle();
            bundle.putFloat("dollarRate",dollarRate);
            bundle.putFloat("euroRate",euroRate);
            bundle.putFloat("wonRate",wonRate);
            intent.putExtras(bundle);

            //保存修改后的汇率数据到sp中
            SharedPreferences sp= getSharedPreferences("myRate", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor=  sp.edit();
            editor.putFloat("dollarRate",dollarRate);
            editor.putFloat("euroRate",euroRate);
            editor.putFloat("wonRate",wonRate);
            Log.i(TAG,"saved");
            editor.apply();

            //startActivity(intent)将数据传回到主页面
            setResult(3,intent);
            finish();
        }
    }
}