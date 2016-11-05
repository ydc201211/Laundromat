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
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.ydc.laundromat.R;
import com.ydc.laundromat.api.AppClientDao;
import com.ydc.laundromat.api.ResultMessage;
import com.ydc.laundromat.common.LocalStorage;
import com.ydc.laundromat.common.NameValuePair;
import com.ydc.laundromat.model.User;
import com.ydc.laundromat.util.TitleBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WalletActivity extends AppCompatActivity implements View.OnClickListener {

    private TitleBuilder titleBuilder;
    private TextView account_money_tv;
    private Button add_money_btn;

    private AppClientDao appClientDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

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

        appClientDao =new AppClientDao(this);
        initTitle();
        initView();
        initData();
    }


    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case ResultMessage.CHANGE_WALLET_SUCCESS:
                    Object objs = msg.obj;

                    JSON jsonObject;
                    try {
                        jsonObject = JSON.parseObject(new JSONObject(objs.toString()).getString("results"));
                        User user = JSON.toJavaObject(jsonObject,User.class);
                        account_money_tv.setText("￥"+user.getUser_wallet());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case ResultMessage.CHANGE_WALLET_FAIL:
                    Toast.makeText(WalletActivity.this,"充值失败!",Toast.LENGTH_SHORT).show();
                    break;
                case ResultMessage.GET_WALLET_SUCCESS:
                    Object obj_1 = msg.obj;

                    JSON jsonObject1;
                    try {
                        jsonObject1 = JSON.parseObject(new JSONObject(obj_1.toString()).getString("results"));
                        User user = JSON.toJavaObject(jsonObject1,User.class);
                        account_money_tv.setText("￥"+user.getUser_wallet());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case ResultMessage.GET_WALLET_FAIL:
                    Toast.makeText(WalletActivity.this,"获取钱包失败!",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private void initView() {
        account_money_tv = (TextView) findViewById(R.id.account_money);
        add_money_btn = (Button) findViewById(R.id.add_money_btn);
        add_money_btn.setOnClickListener(this);
    }

    private void initTitle() {
        titleBuilder = new TitleBuilder(this)
                .setTitleText("我的钱包")
                .setLeftImage(R.drawable.ic_keyboard_backspace).setLeftOnClickListener(this);
    }

    private void initData(){
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new NameValuePair("user_id",LocalStorage.getString(this,"user_id")));
        appClientDao.getWallet(mHandler,list,"/user/getWalletByUserId");
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.titlebar_iv_left:
                finish();
                break;
            case R.id.add_money_btn:
                doChangeWallet();
                break;
        }
    }

    private void doChangeWallet() {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new NameValuePair("user_id", LocalStorage.getString(this,"user_id")));
        list.add(new NameValuePair("money","100.00"));
        list.add(new NameValuePair("type","add"));
        appClientDao.changeWallet(mHandler,list,"/user/handleWallet");
    }
}
