package com.devshyeon.numberclickgame;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



import java.util.Random;

public class MainActivity extends AppCompatActivity {

    LinearLayout layoutMain;
    TextView tv, tv1,returnstarts;
    ImageView[] btns= new ImageView[12];
    int cnt=0;
    int stage=1;
    ImageView btn_start;
    TimeThread thread;
    int[] arr= new int[12];
    Random rnd= new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Click Click");
        makeRandom();
        btn_start=findViewById(R.id.btn_start);
        returnstarts=findViewById(R.id.returnstarts);
        layoutMain= findViewById(R.id.layout_main);
        tv=findViewById(R.id.tv);
        tv1= findViewById(R.id.tv_msg);
        for(int i=0; i<12; i++){
            btns[i]= findViewById(R.id.btn01+i);
            btns[i].setImageResource(R.drawable.num01+arr[i]);
            btns[i].setTag(arr[i]);
        }

    }

    //중복없는 랜던값 생성작업 메소드
    void makeRandom(){
        for(int i=0; i<12; i++){
            arr[i]= rnd.nextInt(12);
            for(int k=0; k<i; k++){
                if(arr[i]==arr[k]){
                    i--;
                    break;
                }
            }
        }
    }

    //start 버튼 클릭메소드
    public void clickStart(View v){
        ((ImageView)v).setImageResource(R.drawable.ing);
        for(ImageView b : btns) b.setVisibility(View.VISIBLE);
        btn_start.setEnabled(false);
        tv1.setText("숫자를 순서대로 누르세요");
        if (thread == null) {
            thread = new TimeThread();
            thread.start();
        } else { thread.resumeThread();
        }
    }

    //이미지 버튼들 클릭메소드
    public void clickBtn(View v){

        int n= (Integer)v.getTag();

        if( n== cnt){
            v.setVisibility(View.INVISIBLE);
            cnt++;

            if(cnt>=12){
                goNextStage();
                cnt=0;
            }
        }
    }

    //다음 스테이지 생성작업 메소드
    void goNextStage(){
        stage++;

        //GameOver..
        if(stage>3){
            layoutMain.setBackgroundResource(R.drawable.bg4);
            tv1.setVisibility(View.GONE);
            tv.setVisibility(View.VISIBLE);
            returnstarts.setVisibility(View.VISIBLE);
            findViewById(R.id.btn_start).setVisibility(View.GONE);
            if (thread != null) {
                thread.stopThread();
                thread = null;
            }
            return;
        }

        //새로운 랜던값 생성
        makeRandom();

        //stage에 맞게 버튼들에 그림 적용하기
        for(int i=0; i<12; i++){
            if(stage==2){
                btns[i].setImageResource(R.drawable.alpa01+arr[i]);
                tv1.setText("알파벳 순서대로 누르세요");
                layoutMain.setBackgroundResource(R.drawable.bg2);
            }
            else if( stage==3){
                btns[i].setImageResource(R.drawable.cha01+arr[i]);
                tv1.setText("십이지신 순서대로 누르세요");
                layoutMain.setBackgroundResource(R.drawable.bg3);
            }

            btns[i].setTag(arr[i]);
            btns[i].setVisibility(View.VISIBLE);
        }
    }

    class TimeThread extends Thread {
        boolean isRun = true;
        boolean isWait = false;
        int min, sec, millis;

        @Override
        public void run() {
            while (isRun) {
                millis++;
                if (millis >= 100) {
                    millis = 0;
                    sec++;
                    if (sec >= 60) {
                        sec = 0;
                        min++;
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String s = String.format("%02d:%02d:%02d", min, sec, millis);
                        tv.setText(s);
                    }
                });
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (this) {
                    if (isWait) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        void stopThread() {
            isRun = false;
            synchronized (this) {
                this.notify();
            }
        }
        void pauseThread() {
            isWait = true;
        }
        void resumeThread() {
            isWait = false;
            isRun = true;
            synchronized (this) {
                this.notify();
            }

        }
    }

}
