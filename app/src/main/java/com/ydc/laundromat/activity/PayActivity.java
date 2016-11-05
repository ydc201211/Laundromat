package com.ydc.laundromat.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.ydc.laundromat.R;
import com.ydc.laundromat.api.AppClientDao;
import com.ydc.laundromat.api.ResultMessage;
import com.ydc.laundromat.common.LocalStorage;
import com.ydc.laundromat.common.NameValuePair;
import com.ydc.laundromat.model.Order;
import com.ydc.laundromat.model.User;
import com.ydc.laundromat.model.WM;
import com.ydc.laundromat.service.RegisterCodeTimerService;
import com.ydc.laundromat.util.TitleBuilder;
import com.ydc.laundromat.widget.RegisterCodeTimer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PayActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,
        View.OnClickListener{


    private final static String TGA = "PayActivity";
    private TextView pay_order_no,pay_order_amount,pay_wm_name,
            pay_washingType,pay_order_realPay;

    private Button pay_order_btn;
    private RadioButton pay_rb_alipay,pay_rb_weixin;
    private RadioGroup pay_rg_tab;

    private WM wm;
    private Order order;

    private TextView rightTitle;

    private TitleBuilder titleBuilder;

    private Intent serviceIntent;

    private AppClientDao appClientDao;

    private MsgReceiver msgReceiver;
    private int time = 20000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
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
        /*wm = (WM) bundle.get("wm");*/
        order = (Order) bundle.get("order");
        appClientDao = new AppClientDao(this);
        initTitle();
        initView();

        initData();

        /**
         * 动态注册广播
         */
        msgReceiver = new MsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.communication.RECEIVER");
        registerReceiver(msgReceiver, intentFilter);
       /* RegisterCodeTimerService.setHandler(mHandler);*/


        serviceIntent = new Intent(PayActivity.this, RegisterCodeTimerService.class);


        serviceIntent.putExtra("order_no", order.getOrder_no());
        serviceIntent.putExtra("wm_id", order.getOrder_wmId() + "");
        serviceIntent.putExtra("user_id", order.getOrder_userId() + "");


        startService(serviceIntent);

    }

    private Handler pHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case ResultMessage.EDIT_ORDER_SUCCESS:
                    Intent intent = new Intent(PayActivity.this,FinishActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("order",order);
                    bundle.putSerializable("wm",wm);
                    intent.putExtras(bundle);
                    startActivity(intent);

                    break;
                case ResultMessage.EDIT_ORDER_FAIL:
                    Toast.makeText(PayActivity.this,"修改订单失败",Toast.LENGTH_SHORT).show();
                    break;
                case ResultMessage.GET_WALLET_SUCCESS:
                    Object objs = msg.obj;

                    JSON jsonObject;
                    try {
                        jsonObject = JSON.parseObject(new JSONObject(objs.toString()).getString("results"));
                        User user = JSON.toJavaObject(jsonObject,User.class);
                        if(Float.parseFloat(user.getUser_wallet()) >=
                                Float.parseFloat(pay_order_realPay.getText().toString())){
                            List<NameValuePair> list = new ArrayList<NameValuePair>();
                            list.add(new NameValuePair("user_id", LocalStorage.getString(PayActivity.this,"user_id")));
                            list.add(new NameValuePair("money",pay_order_realPay.getText().toString()));
                            list.add(new NameValuePair("type","subtract"));
                            appClientDao.changeWallet(pHandler,list,"/user/handleWallet");
                        }else{
                            Toast.makeText(PayActivity.this,"余额不足！",Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case ResultMessage.CHANGE_WALLET_SUCCESS:
                    Toast.makeText(PayActivity.this,"支付成功！",Toast.LENGTH_SHORT).show();
                    List<NameValuePair> param = new ArrayList<NameValuePair>();
                    param.add(new NameValuePair("order_no",order.getOrder_no()));
                    param.add(new NameValuePair("order_status","已支付"));
                    appClientDao.editOrder(pHandler,param,"/order/editOrder");
                    break;
            }

        }
    };

    /**
     * 广播接收器
     * @author len
     *
     */
    public class MsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //拿到进度，更新UI
            time = intent.getIntExtra("time", 0);
            int minute=time%(1000*60*60)/(60*1000);
            int second=(time%(1000*60*60))%(60*1000)/1000;
            rightTitle.setText(minute+ "''" + second + "'");

            if(time < 0 ){
                timeoutHandle();
                stopService(serviceIntent);

            }
        }

    }

    @Override
    protected void onDestroy() {

        //注销广播
        unregisterReceiver(msgReceiver);
        super.onDestroy();
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what){
                case RegisterCodeTimer.IN_RUNNING:
                    rightTitle.setText(msg.obj.toString());
                    break;
                case RegisterCodeTimer.END_RUNNING:
                    timeoutHandle();
                    stopService(serviceIntent);
                    break;
            }

        }
    };

    private void timeoutHandle(){
        LocalStorage.saveString(PayActivity.this,"book_status", "0");

        pay_order_btn.setTextColor(ContextCompat.getColor(this,R.color.gray_3));
        pay_order_btn.setClickable(false);
        rightTitle.setText("已超时");

    }
    private void initTitle() {
        titleBuilder = new TitleBuilder(this)
                .setTitleText("付款")
                .setRightText("0''15'")
                .setLeftImage(R.drawable.ic_keyboard_backspace).setLeftOnClickListener(this);

    }
    private void initView() {

        rightTitle = (TextView) findViewById(R.id.titlebar_tv_right);
        pay_order_no = (TextView) findViewById(R.id.pay_order_no);
        pay_order_amount = (TextView) findViewById(R.id.pay_order_amount);
        pay_wm_name = (TextView) findViewById(R.id.pay_wm_name);
        pay_washingType = (TextView) findViewById(R.id.pay_washingType);
        pay_order_realPay= (TextView) findViewById(R.id.pay_order_realPay);

        pay_order_btn = (Button) findViewById(R.id.pay_order_button);
        pay_order_btn.setOnClickListener(this);
        pay_rb_alipay = (RadioButton) findViewById(R.id.pay_rb_alipay);
        pay_rb_weixin = (RadioButton) findViewById(R.id.pay_rb_weixin);
        pay_rg_tab = (RadioGroup) findViewById(R.id.pay_rg_tab);
        pay_rg_tab.setOnCheckedChangeListener(this);


    }

    private void initData() {
        pay_order_no.setText(order.getOrder_no());
        pay_order_amount.setText(order.getOrder_amount());
        pay_wm_name.setText(order.getOrder_wmName());
        pay_washingType.setText(order.getOrder_washingType());
        pay_order_realPay.setText(order.getOrder_amount());
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.titlebar_iv_left:
                Intent intent = new Intent(PayActivity.this,MainActivity.class);
                startActivity(intent);

                break;
            case R.id.pay_order_button:
                doPay();

                break;
        }
    }

    private void doPay() {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new NameValuePair("user_id",LocalStorage.getString(this,"user_id")));
        appClientDao.getWallet(pHandler,list,"/user/getWalletByUserId");
    }
}
