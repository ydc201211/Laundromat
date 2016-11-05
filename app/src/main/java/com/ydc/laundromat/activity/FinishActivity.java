package com.ydc.laundromat.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.ydc.laundromat.R;
import com.ydc.laundromat.model.Order;
import com.ydc.laundromat.service.SimulateService;
import com.ydc.laundromat.util.TitleBuilder;
import com.ydc.laundromat.widget.RegisterCodeTimer;

public class FinishActivity extends AppCompatActivity {

    private TitleBuilder titleBuilder;
    private Button finish_btn;
    private Order order;
    private Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT || Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);

        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.titlebar_blue));
        }
        Bundle bundle = getIntent().getExtras();
        SimulateService.setHandler(mHandler);
        serviceIntent = new Intent(FinishActivity.this, SimulateService.class);
        serviceIntent.putExtras(bundle);
        startService(serviceIntent);
        initTitle();
        initView();



    }

    private void initView() {
        finish_btn = (Button) findViewById(R.id.finish_pay_btn);
        finish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(FinishActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initTitle() {
        titleBuilder = new TitleBuilder(this)
                .setTitleText("支付完成");
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what){
                case RegisterCodeTimer.IN_RUNNING:

                    break;
                case RegisterCodeTimer.END_RUNNING:

                    stopService(serviceIntent);
                    break;
            }

        }
    };
}
