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

public class RMBActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "RMBActivity";
    EditText editTextren;
    TextView Result;
    double r1 = 0.15026973417;   //美元汇率
    double r2 = 0.126632931655;  //欧元汇率
    double r3 = 0.1143692365182; //英镑汇率
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rmb);
        setViews();
        //读取保存的数据
        SharedPreferences sp = getSharedPreferences("rate1", Activity.MODE_PRIVATE);
        r1 = sp.getFloat("rate1", 0.1f);
        Log.i(TAG, "onCreate：get from sp rate1=" + r1);//记录日志

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
                str = str + String.format("%.2f",Res) + "欧元";//汇率换算结果保留两位小数显示
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
                str = str + String.format("%.2f",Res) + "英镑";//汇率换算结果保留两位小数显示
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
