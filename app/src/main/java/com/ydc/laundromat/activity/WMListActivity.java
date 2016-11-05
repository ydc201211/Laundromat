package com.ydc.laundromat.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.alibaba.fastjson.JSON;
import com.ydc.laundromat.R;
import com.ydc.laundromat.adapter.WMAdapter;
import com.ydc.laundromat.api.AppClientDao;
import com.ydc.laundromat.api.ResultMessage;
import com.ydc.laundromat.common.NameValuePair;
import com.ydc.laundromat.dto.ItemPoint;
import com.ydc.laundromat.model.WM;
import com.ydc.laundromat.util.TitleBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WMListActivity extends AppCompatActivity implements View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener, RadioGroup.OnCheckedChangeListener {
    private List<WM> wmList;

    private AppClientDao appClientDao;
    private String  pointId;

    private TitleBuilder titleBuilder;

    private SwipeRefreshLayout swipeLayout;

    /**
     * ListView
     */
    private ListView listView;

    /**
     * ListView适配器
     */
    private WMAdapter adapter;

    private ItemPoint ip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wmlist);


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
        wmList  = new ArrayList<WM>();

        Bundle bundle =  getIntent().getExtras();
        ip = (ItemPoint) bundle.get("point");
        String titleName = ip.getPointName() + " " + ip.getAddress();

        initTitle(titleName);
        initView();

        initData();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case ResultMessage.GET_WM_SUCCESS: {
                    Object objs = msg.obj;

                    String arrayString = null;
                    try {
                        arrayString = new JSONObject(objs.toString()).getString("results");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    List<WM> list = JSON.parseArray(arrayString,WM.class);
                    Log.i("mmmmmm",list.get(0).getWm_name());
                    for(int i = 0 ;i < list.size();i++){
                        wmList.add(list.get(i));
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        }
    };


    private void initView() {



        swipeLayout = (SwipeRefreshLayout) this.findViewById(R.id.swipe_refresh);
        swipeLayout.setOnRefreshListener(this);

        // 顶部刷新的样式
        swipeLayout.setColorSchemeColors(ContextCompat.getColor(this,android.R.color.holo_red_light),
                ContextCompat.getColor(this,android.R.color.holo_green_light),
                ContextCompat.getColor(this,android.R.color.holo_blue_bright),
                ContextCompat.getColor(this,android.R.color.holo_orange_light));

        listView = (ListView) this.findViewById(R.id.wm_listview);
        adapter = new WMAdapter(this,R.layout.item_wm,wmList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent intent = new Intent( WMListActivity.this,
                        BookActivity.class);

                Bundle n = new Bundle();
                n.putSerializable("point",wmList.get(position));
                intent.putExtras(n);
                startActivity(intent);
            }
        });
    }


    private void initData() {
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new NameValuePair("wm_parentId",ip.getPointId()+""));
        appClientDao.getWMByParentId(mHandler,param,"/wm/getWMsByParentId");

    }

    public void onRefresh() {
        wmList.clear();
        initData();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                swipeLayout.setRefreshing(false);
            }
        }, 2000);
    }

    private void initTitle(String titleName) {
        titleBuilder = new TitleBuilder(this)
                .setTitleText(titleName)
                .setLeftImage(R.drawable.ic_keyboard_backspace).setLeftOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case  R.id.titlebar_iv_left:
                finish();
                break;
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

    }
}
