package com.example.lab1;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置基本控件
        final TextView tv1=findViewById(R.id.tv1);
        final   Button btn1=findViewById(R.id.btn1);
        final Button btn2=findViewById(R.id.btn2);
        final ImageView imgv1=findViewById(R.id.imgv1);
        final LinearLayout lay=findViewById(R.id.lay1);
        final Button btn3=findViewById(R.id.btn3);
        //imageview初始化
        final Bitmap btm1 = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.drawable.firstphoto2);
        BitmapDrawable bd1=new BitmapDrawable(btm1);
        imgv1.setBackgroundDrawable(bd1);
        //点击事件
         tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv1.setText("哈哈哈");
            }
        });
        btn1.setOnTouchListener(new Button.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //点击按钮未松开时按钮放大
                    blow_up(btn1);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //点击按钮松开后按钮缩小
                    narrow(btn1);
                }
                return false;
            }
        });

        imgv1.setOnTouchListener(new Button.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    imgv1.setBackgroundResource(R.drawable.firstphoto);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    imgv1.setBackgroundResource(R.drawable.firstphoto2);
                }
                return false;
            }
        });
        imgv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    imgv1.setBackgroundResource(R.drawable.firstphoto2);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                lay.setBackgroundColor(Color.parseColor("blue"));
             }
         });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreate(null);
            }
        });
    }
    //放大按钮动画
    private void blow_up(View v) {
        float[] vaules = new float[] { 1.0f, 1.1f, 1.2f, 1.3f, 1.4f, 1.5f};
        AnimatorSet set = new AnimatorSet();
        set.playTogether(ObjectAnimator.ofFloat(v, "scaleX", vaules),
                ObjectAnimator.ofFloat(v, "scaleY", vaules));
        set.setDuration(150);
        set.start();
    }
    //缩小按钮动画
    private void narrow(View v) {
        float[] vaules = new float[] { 1.5f, 1.4f, 1.3f, 1.2f, 1.1f, 1.0f};
        AnimatorSet set = new AnimatorSet();
        set.playTogether(ObjectAnimator.ofFloat(v, "scaleX", vaules),
                ObjectAnimator.ofFloat(v, "scaleY", vaules));
        set.setDuration(150);
        set.start();
    }

}
