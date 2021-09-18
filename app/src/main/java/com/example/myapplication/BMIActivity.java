package com.example.myapplication;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;


public class BMIActivity extends Activity implements View.OnClickListener {
    RadioButton rb1;
    RadioButton rb2;
    TextView tvResult;
    EditText txt1;
    EditText txt2;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);
        setViews();
    }
    public void setViews() {
        rb1 = (RadioButton) findViewById(R.id.rb1);
        rb2 = (RadioButton) findViewById(R.id.rb2);//判断男女
        tvResult = (TextView) findViewById(R.id.tvResult);
        txt1 = (EditText)findViewById(R.id.edX);
        txt2 = (EditText)findViewById(R.id.edY);

        btn = (Button)findViewById(R.id.btn);
        btn.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        double x = Double.parseDouble(txt1.getText().toString());//身高
        double y = Double.parseDouble(txt2.getText().toString());//体重
        double res;
        String str = "你的身体质量指数BMI是：";
        if(x<=0 || y<=0) {
            tvResult.setText("输入值异常！请重新输入正确的身高、体重。");
            return ;
        }
        res = y / (x*x);//BMI=体重÷身高²
        String str1 = String.format("%.2f",res);//BMI的计算结果保留两位小数显示
        str = str + str1;
        if(rb1.isChecked())
            res -= 1;//以女性为标准进行比较
        //健康评价标准
        str +=   "体型:";
        if(res < 19)
            str += "过轻";
        else if(res < 24)
            str += "适中";
        else if(res<29)
            str += "超重";
        else if(res<34)
            str += "肥胖";
        else
            str += "严重肥胖";
        tvResult.setText(str);
    }
}
