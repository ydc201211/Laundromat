package com.ydc.laundromat.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ydc.laundromat.R;
import com.ydc.laundromat.api.AppClientDao;
import com.ydc.laundromat.api.ResultMessage;
import com.ydc.laundromat.common.LocalStorage;
import com.ydc.laundromat.common.NameValuePair;
import com.ydc.laundromat.util.TitleBuilder;

import java.util.ArrayList;
import java.util.List;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText old_password_et;
    private EditText new_password_et;
    private EditText sure_password_et;
    private Button change_password_btn;

    private String oldPassword;
    private String newPassword;
    private String surePassword;

    private AppClientDao appClientDao;
    private TitleBuilder titleBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

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
        appClientDao =new AppClientDao();
        initView();
        initTitle();
    }
    private void initTitle() {
        titleBuilder = new TitleBuilder(this)
                .setTitleText("修改密码")
                .setLeftImage(R.drawable.ic_keyboard_backspace).setLeftOnClickListener(this);
    }

    private void initView() {
        old_password_et = (EditText) findViewById(R.id.change_password_et);
        new_password_et = (EditText) findViewById(R.id.new_password_et);
        sure_password_et = (EditText) findViewById(R.id.sure_new_password_et);
        change_password_btn = (Button) findViewById(R.id.change_password_button);

        change_password_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.change_password_button:
                doChange();
                break;
            case R.id.titlebar_iv_left:
                finish();
                break;
        }
    }

    private void doChange() {
        oldPassword = old_password_et.getText().toString().trim();
        newPassword = new_password_et.getText().toString().trim();
        surePassword = sure_password_et.getText().toString().trim();

        if (!oldPassword.equals("") && !newPassword.equals("") && !surePassword.equals("")) {
            if (newPassword.equals(surePassword)) {
                List<NameValuePair> list = new ArrayList<NameValuePair>();
                list.add(new NameValuePair("user_id", LocalStorage.getString(this,"user_id")));
                list.add(new NameValuePair("user_account",LocalStorage.getString(this,"user_account")));
                list.add(new NameValuePair("user_password", oldPassword));
                list.add(new NameValuePair("newPassword", newPassword));
                appClientDao.changePassword(mHandler, list, "/user/changePassword");
            } else {
                Toast.makeText(ChangePasswordActivity.this, "新密码与确认密码不一致", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ChangePasswordActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
        }

    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case ResultMessage.CHANGE_PASSWORD_SUCCESS:
                    Toast.makeText(ChangePasswordActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                    break;
                case ResultMessage.CHANGE_PASSWORD_FAIL:
                    Toast.makeText(ChangePasswordActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

}
