package com.ydc.laundromat.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.ydc.laundromat.R;
import com.ydc.laundromat.util.TitleBuilder;

public class SuggestActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText suggest_et;
    private TitleBuilder titleBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);
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
        initTitle();
        initView();
    }
    private void initTitle() {
        titleBuilder = new TitleBuilder(this)
                .setTitleText("意见反馈")
                .setLeftImage(R.drawable.ic_keyboard_backspace).setLeftOnClickListener(this)
                .setRightText("提交").setRightOnClickListener(this);
    }
    private void initView() {
        suggest_et = (EditText) findViewById(R.id.suggest_et);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.titlebar_iv_left:
                finish();
                break;
            case R.id.titlebar_tv_right:
                doPost();

                break;
        }
    }

    private void doPost() {
        String a= suggest_et.getText().toString().trim();
        if(a.equals("")){
            Toast.makeText(SuggestActivity.this,"不能为空额,亲",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(SuggestActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
