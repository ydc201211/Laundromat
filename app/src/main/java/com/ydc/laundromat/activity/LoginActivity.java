package com.ydc.laundromat.activity;

import android.annotation.TargetApi;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.ydc.laundromat.R;
import com.ydc.laundromat.api.AppClientDao;
import com.ydc.laundromat.api.ResultMessage;
import com.ydc.laundromat.common.LocalStorage;
import com.ydc.laundromat.common.NameValuePair;
import com.ydc.laundromat.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  implements View.OnClickListener{

    private ImageView back_iv;

    private EditText account_et;
    private EditText password_et;

    private Button login_btn;
    private TextView toRegister_btn;
    private AppClientDao appClientDao;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

          /*setStatusBar();*/
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
                case ResultMessage.LOGIN_SUCCESS:
                    Object objs = msg.obj;
                    JSON jsonObject;
                    try {
                        jsonObject = JSON.parseObject(new JSONObject(objs.toString()).getString("results"));

                        User user = JSON.toJavaObject(jsonObject,User.class);
                        saveUser(user);
                        setResult(-1);
                        finish();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case ResultMessage.LOGIN_FAIL:
                    Object objs_1 = msg.obj;
                    String info = objs_1.toString();

                    showLoginInfo(info);
                    break;
            }
        }
    };



    private void initView() {

        back_iv = (ImageView) findViewById(R.id.back_iv);
        account_et = (EditText) findViewById(R.id.login_account_edit);
        password_et = (EditText) findViewById(R.id.login_password);

        login_btn = (Button) findViewById(R.id.login_sign_in_button);
        toRegister_btn = (TextView) findViewById(R.id.login_register_button);
        login_btn.setOnClickListener(this);
        toRegister_btn.setOnClickListener(this);
        back_iv.setOnClickListener(this);
    }

    private void showLoginInfo(String info){
        Toast.makeText(this,info,Toast.LENGTH_SHORT).show();
    }

    private void saveUser(User user) {
        LocalStorage.saveString(this,"user_id",user.getUser_id()+"");
        LocalStorage.saveString(this,"user_account",user.getUser_account());
        LocalStorage.saveString(this,"user_phone",user.getUser_phone());
        LocalStorage.saveString(this,"user_portraitPath",user.getUser_portraitPath());
        LocalStorage.saveString(this,"user_school",user.getUser_school());
        LocalStorage.saveString(this,"user_sex",user.getUser_sex());
    }

    private void doLogin(){

       String account =  account_et.getText().toString().trim();
       String password = password_et.getText().toString().trim();

        if(account.equals("") || password.equals("")){
            Toast.makeText(this,"账号密码不能为空",Toast.LENGTH_SHORT).show();
        }else{
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new NameValuePair("user_account",account));
            param.add(new NameValuePair("user_password",password));
            appClientDao.login(mHandler,param,"/user/login");
        }
    }
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.back_iv:
                intent = new Intent();
                setResult(-1,intent);
                finish();
                break;
            case R.id.login_sign_in_button:
                doLogin();
                break;
            case R.id.login_register_button:

                intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
        }
    }
}





