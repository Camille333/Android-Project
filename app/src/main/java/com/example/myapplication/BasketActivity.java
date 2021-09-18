package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class BasketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        alert("篮球比赛计分器使用说明", "1、可点按各个加分按钮进行加分\n2、重置按钮可重置比分");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void backHome(View view) {
        /*
         * 返回主页
         * */
        this.finish();
        onBackPressed();
    }

    public void teamA_AddScore(View view) {
        /*
         * Team A加score分
         * @Param oldScore 原成绩
         * @Param score  要加的成绩
         * */
        TextView teamAScore = (TextView) findViewById(R.id.teamAScore);
        int oldScore = Integer.parseInt(teamAScore.getText().toString());
        int score = Integer.parseInt(view.getTag().toString());
        teamAScore.setText("" + (score + oldScore));
    }

    public void teamB_AddScore(View view) {
        /*
         * Team B加score分
         * @Param oldScore 原成绩
         * @Param score  要加的成绩
         * */
        TextView teamBScore = (TextView) findViewById(R.id.teamBScore);
        int oldScore = Integer.parseInt((String) teamBScore.getText());
        int score = Integer.parseInt(view.getTag().toString());
        teamBScore.setText("" + (score + oldScore));
    }

    public void reset(View view) {
        /*
         * 重置比分
         * */
        TextView teamAScore = (TextView) findViewById(R.id.teamAScore);
        TextView teamBScore = (TextView) findViewById(R.id.teamBScore);
        teamAScore.setText("0");
        teamBScore.setText("0");

        /*
         * 发送提示信息
         * */
        alert("重置成功！");
    }

    private void alert(String message) {
        /*
         * 提示信息
         * */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage(message);
        builder.show();
    }

    private void alert(String title, String message) {
        /*
         * 提示信息
         * */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void over(View view) {
        /*
         * 结算比赛得分
         * */
        TextView teamAScore = findViewById(R.id.teamAScore);
        TextView teamBScore = findViewById(R.id.teamBScore);

        int teamAscore = Integer.parseInt(teamAScore.getText().toString());
        int teamBscore = Integer.parseInt(teamBScore.getText().toString());

        if(teamAscore == teamBscore)alert("比赛结果","难分伯仲！");
        else if(teamAscore > teamBscore)alert("比赛结果","Team A取得了最后胜利！");
        else alert("比赛结果","Team B取得了最后胜利！");
    }


}
