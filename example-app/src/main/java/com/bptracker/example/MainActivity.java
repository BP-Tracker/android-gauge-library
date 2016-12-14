package com.bptracker.example;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.bptracker.ui.gauges.GradientCircleGauge;

import java.util.Random;

public class MainActivity extends Activity {

    private GradientCircleGauge mGaugeLeft;
    private GradientCircleGauge mGaugeRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mGaugeRight = (GradientCircleGauge) findViewById(R.id.gcg_gauge_right);
        mGaugeRight.setGaugeLevel(14);

        mGaugeLeft = (GradientCircleGauge) findViewById(R.id.gcg_gauge_left);

        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                mGaugeRight.setGaugeLevel(90);
            }
        }, 3000);


        new Thread(){
            Random random = new Random();

            @Override
            public void run() {
                for(int i = 1; i < 21; i++) {

                    final int num = i;

                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mGaugeLeft.setAdditionalValue(Integer.toString(num));
                                mGaugeLeft.setGaugeLevel(random.nextInt(101));
                            }
                        });
                        Thread.sleep(4000);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
