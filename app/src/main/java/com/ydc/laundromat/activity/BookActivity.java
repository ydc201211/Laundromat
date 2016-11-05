package com.ydc.laundromat.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
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
import com.ydc.laundromat.model.WM;
import com.ydc.laundromat.util.TitleBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookActivity extends AppCompatActivity implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener{


    private AppClientDao appClientDao;
    private WM wm;
    private  TitleBuilder titleBuilder;

    private TextView book_wm_no;

    private TextView book_wm_name;

    private ImageView icon_book_wm_status;
    private TextView book_wm_status;

    private ImageView icon_book_wm_address;
    private TextView book_wm_address;

    private TextView book_clothe_type,book_wm_price,book_wm_time;


    private TextView book_wm_question;

    private TextView book_wm_btn;

    private RadioGroup rg_tab;
    private RadioButton biaozhun_rb,dawu_rb,kuaisu_rb,dantuo_rb;

    private int select;

    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

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
        appClientDao = new AppClientDao(this);
        Bundle bundle =  getIntent().getExtras();
        wm = (WM) bundle.get("point");
        String titleName = wm.getWm_name() ;
        initTitle(titleName);

        initView();
        initData();
    }
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case ResultMessage.ADD_ORDER_SUCCESS:
                    Object objs = msg.obj;

                    JSON jsonObject;
                    try {
                        jsonObject = JSON.parseObject(new JSONObject(objs.toString()).getString("results"));
                        Order order = JSON.toJavaObject(jsonObject,Order.class);

                        List<NameValuePair> list = new ArrayList<NameValuePair>();
                        list.add(new NameValuePair("wm_id",order.getOrder_wmId()+""));
                        list.add(new NameValuePair("wm_status","1"));

                        appClientDao.editWM(mHandler,list,"/wm/editWM");
                        Intent intent = new Intent(BookActivity.this,PayActivity.class);

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("wm",wm);
                        bundle.putSerializable("order",order);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case ResultMessage.GET_ORDER_STATUS_SUCCESS:
                    Toast.makeText(BookActivity.this,"你已预订机器，请前往付款!",Toast.LENGTH_SHORT).show();
                    break;
                case ResultMessage.GET_ORDER_STATUS_FAIL:
                    setOrder();
                    break;
            }
        }
    };

    private void initData() {
        select = R.id.rb_biaozhun;
        book_wm_no.setText(wm.getWm_no()+"");
        book_wm_name.setText(wm.getWm_name());

        if(wm.getWm_status() == 0){
            book_wm_status.setText("空闲");
            icon_book_wm_status.setImageResource(R.mipmap.icon_available_blue);
            book_wm_btn.setClickable(true);

        }else if(wm.getWm_status() == 1){
            book_wm_status.setText("使用中");
            icon_book_wm_status.setImageResource(R.mipmap.icon_busy_green);
            book_wm_btn.setBackgroundColor(ContextCompat.getColor(this,R.color.gray_3));
            book_wm_btn.setClickable(false);
        }

        icon_book_wm_address.setImageResource(R.mipmap.icon_position_blue);
        book_wm_address.setText(wm.getWm_address());
        book_clothe_type.setText("标准");
        book_wm_price.setText("4.00");
        book_wm_time.setText("32分钟");
        book_wm_question.setText("一般标准清洗程序");
    }

    private void initView() {
        book_wm_no = (TextView) findViewById(R.id.book_wm_no);

        book_wm_name = (TextView) findViewById(R.id.book_wm_name);

        icon_book_wm_status = (ImageView) findViewById(R.id.icon_book_wm_status);
        book_wm_status = (TextView) findViewById(R.id.book_wm_status);

        icon_book_wm_address = (ImageView) findViewById(R.id.icon_book_wm_address);
        book_wm_address = (TextView) findViewById(R.id.book_wm_address);

        book_clothe_type = (TextView) findViewById(R.id.book_clothe_type);
        book_wm_price = (TextView) findViewById(R.id.book_wm_price);
        book_wm_time = (TextView) findViewById(R.id.book_wm_time);

        book_wm_question = (TextView) findViewById(R.id.book_wm_question);

        book_wm_btn = (TextView) findViewById(R.id.book_wm_btn);
        book_wm_btn.setOnClickListener(this);

        rg_tab = (RadioGroup) findViewById(R.id.book_rg_tab);
        rg_tab.setOnCheckedChangeListener(this);

        biaozhun_rb = (RadioButton) findViewById(R.id.rb_biaozhun);
        biaozhun_rb.setBackgroundColor(ContextCompat.getColor(this,R.color.gray_3));
        dawu_rb = (RadioButton) findViewById(R.id.rb_dawu);
        kuaisu_rb = (RadioButton) findViewById(R.id.rb_kuaisu);
        dantuo_rb = (RadioButton) findViewById(R.id.rb_dantuo);

    }

    private void initTitle(String titleName) {
        titleBuilder = new TitleBuilder(this)
                .setTitleText(titleName)
                .setLeftImage(R.drawable.ic_keyboard_backspace).setLeftOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.titlebar_iv_left:
                finish();
                break;
            case R.id.book_wm_btn:
                String user_id = LocalStorage.getString(this,"user_id");
                if (user_id.equals("")) {
                    Toast.makeText(BookActivity.this,"请先登录!",Toast.LENGTH_SHORT).show();
                }else {
                    List<NameValuePair> list = new ArrayList<NameValuePair>();
                    list.add(new NameValuePair("user_id",LocalStorage.getString
                            (BookActivity.this,"user_id")));
                    list.add(new NameValuePair("order_status","待支付"));
                    appClientDao.getOrderByStatusAndUserId(mHandler,list,
                            "/order/getOrderByUserIdAndOrderStatus");

                }
                break;
        }
    }


    public void setOrder(){
        String user_id = LocalStorage.getString(this,"user_id");
        order = new Order();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        order.setOrder_time(str);
        order.setOrder_userId(Integer.parseInt(user_id));
        order.setOrder_wmId(wm.getWm_id());
        order.setOrder_wmName(wm.getWm_name());
        order.setOrder_status("待支付");
        if (select == R.id.rb_biaozhun) {
            order.setOrder_amount("4.00");
            order.setOrder_washingType("标准");
            order.setOrder_realPay("4.00");

        } else if (select == R.id.rb_dawu) {
            order.setOrder_amount("6.00");
            order.setOrder_washingType("大物");
            order.setOrder_realPay("6.00");

        } else if (select == R.id.rb_kuaisu) {
            order.setOrder_amount("3.00");
            order.setOrder_washingType("快速");
            order.setOrder_realPay("3.00");
        } else if (select == R.id.rb_dantuo) {
            order.setOrder_amount("1.00");
            order.setOrder_washingType("单脱");
            order.setOrder_realPay("1.00");
        }

        postOrderToServer();
    }

    private void postOrderToServer() {
        List<NameValuePair> list =new ArrayList<NameValuePair>();
        list.add(new NameValuePair("order_status",order.getOrder_status()));
        list.add(new NameValuePair("order_amount",order.getOrder_amount()));
        list.add(new NameValuePair("order_time",order.getOrder_time()));
        list.add(new NameValuePair("order_userId",order.getOrder_userId()+""));
        list.add(new NameValuePair("order_wmId",order.getOrder_wmId()+""));
        list.add(new NameValuePair("order_wmName",order.getOrder_wmName()));
        list.add(new NameValuePair("order_realPay",order.getOrder_realPay()));
        list.add(new NameValuePair("order_washingType",order.getOrder_washingType()));
        list.add(new NameValuePair("point_no",wm.getWm_parentId()+""));

        appClientDao.addOrder(mHandler,list,"/order/addOrder");

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        // TODO Auto-generated method stub
        switch (i) {
            case R.id.rb_biaozhun:
                select = R.id.rb_biaozhun;
                biaozhun_rb.setBackgroundColor(ContextCompat.getColor(this,R.color.gray_3));
                dawu_rb.setBackgroundColor(ContextCompat.getColor(this,R.color.white));
                kuaisu_rb.setBackgroundColor(ContextCompat.getColor(this,R.color.white));
                dantuo_rb.setBackgroundColor(ContextCompat.getColor(this,R.color.white));
                book_wm_question.setText("一般标准清洗程序");
                book_clothe_type.setText("标准");
                book_wm_price.setText("4.00");
                book_wm_time.setText("32分钟");
                break;
            case R.id.rb_dawu:
                select = R.id.rb_dawu;
                biaozhun_rb.setBackgroundColor(ContextCompat.getColor(this,R.color.white));
                dawu_rb.setBackgroundColor(ContextCompat.getColor(this,R.color.gray_3));
                kuaisu_rb.setBackgroundColor(ContextCompat.getColor(this,R.color.white));
                dantuo_rb.setBackgroundColor(ContextCompat.getColor(this,R.color.white));
                book_wm_question.setText("大物清洗程序，时间较长");
                book_clothe_type.setText("大物");
                book_wm_price.setText("6.00");
                book_wm_time.setText("45分钟");
                break;
            case R.id.rb_kuaisu:
                select = R.id.rb_kuaisu;
                biaozhun_rb.setBackgroundColor(ContextCompat.getColor(this,R.color.white));
                dawu_rb.setBackgroundColor(ContextCompat.getColor(this,R.color.white));
                kuaisu_rb.setBackgroundColor(ContextCompat.getColor(this,R.color.gray_3));
                dantuo_rb.setBackgroundColor(ContextCompat.getColor(this,R.color.white));
                book_wm_question.setText("适合于少量衣物、体积小的衣物清洗，速度较快");
                book_clothe_type.setText("快速");
                book_wm_price.setText("3.00");
                book_wm_time.setText("20分钟");
                break;
            case R.id.rb_dantuo:
                select = R.id.rb_dantuo;
                biaozhun_rb.setBackgroundColor(ContextCompat.getColor(this,R.color.white));
                dawu_rb.setBackgroundColor(ContextCompat.getColor(this,R.color.white));
                kuaisu_rb.setBackgroundColor(ContextCompat.getColor(this,R.color.white));
                dantuo_rb.setBackgroundColor(ContextCompat.getColor(this,R.color.gray_3));
                book_wm_question.setText("适用于一次衣物脱水");
                book_clothe_type.setText("单脱");
                book_wm_price.setText("1.00");
                book_wm_time.setText("8分钟");
            default:
                break;
        }
    }
}
