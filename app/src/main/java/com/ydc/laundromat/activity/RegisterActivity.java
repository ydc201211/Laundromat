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
import android.widget.ImageView;
import android.widget.Toast;

import com.ydc.laundromat.R;
import com.ydc.laundromat.api.AppClientDao;
import com.ydc.laundromat.api.ResultMessage;
import com.ydc.laundromat.common.LocalStorage;
import com.ydc.laundromat.common.NameValuePair;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView back_iv;
    private EditText account_et,
            password_et,sure_password_et,phone_et;
    private Button register_btn;

    private AppClientDao appClientDao;

    private String account,password,sure_password,phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resgister);

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

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        appClientDao = new AppClientDao(this);
        initView();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case ResultMessage.REGISTER_SUCCESS:
                    Toast.makeText(RegisterActivity.this,"成功",Toast.LENGTH_LONG).show();
                    RegisterActivity.this.finish();
                    break;
                case ResultMessage.REGISTER_FAIL:
                    Object objs = msg.obj;

                    String a = objs.toString();
                    Toast.makeText(RegisterActivity.this,a,Toast.LENGTH_LONG).show();

                    break;
            }
        }
    };
    private void initView() {
        back_iv = (ImageView) findViewById(R.id.rg_back_iv);
        account_et = (EditText) findViewById(R.id.rg_account_edit);
        password_et = (EditText) findViewById(R.id.rg_password);
        sure_password_et = (EditText) findViewById(R.id.rg_password_sure);
        phone_et = (EditText) findViewById(R.id.rg_phone_num);

        register_btn = (Button) findViewById(R.id.rg_register_button);
        register_btn.setOnClickListener(this);
        back_iv.setOnClickListener(this);
    }

    private void doRegister() {
        account = account_et.getText().toString().trim();
        password = password_et.getText().toString().trim();
        sure_password = sure_password_et.getText().toString().trim();
        phone = phone_et.getText().toString().trim();
        if(account.equals("") || password.equals("")
                || sure_password.equals("") || phone.equals("")){
            Toast.makeText(this,"请依次输入完成",Toast.LENGTH_SHORT).show();
        }else {
            if (password.equals(sure_password)) {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new NameValuePair("user_account", account));
                params.add(new NameValuePair("user_password", password));
                params.add(new NameValuePair("user_phone", phone));
                params.add(new NameValuePair("user_school", LocalStorage.getString(this, "schoolName")));
                appClientDao.registerUser(mHandler, params, "/user/register");
            } else {
                Toast.makeText(this, "前后密码输入不一致", Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rg_back_iv:
                finish();
                break;
            case R.id.rg_register_button:

                doRegister();
                break;
        }
    }


}
