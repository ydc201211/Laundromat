package com.ydc.laundromat.activity;

import android.annotation.TargetApi;
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

import com.alibaba.fastjson.JSON;
import com.baidu.mapapi.model.LatLng;
import com.ydc.laundromat.R;
import com.ydc.laundromat.adapter.ListViewAdapter;
import com.ydc.laundromat.api.AppClientDao;
import com.ydc.laundromat.api.ResultMessage;
import com.ydc.laundromat.common.LocalStorage;
import com.ydc.laundromat.common.NameValuePair;
import com.ydc.laundromat.dto.ItemPoint;
import com.ydc.laundromat.util.MapUtil;
import com.ydc.laundromat.util.TitleBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PointActivity extends AppCompatActivity implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener{

    private AppClientDao appClientDao;
    private String  schoolId;
    private TitleBuilder titleBuilder;

    private SwipeRefreshLayout swipeLayout;

    /**
     * ListView
     */
    private ListView listView;

    /**
     * ListView适配器
     */
    private ListViewAdapter adapter;

    private List<ItemPoint> pointDetailList;



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);
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
        pointDetailList = new ArrayList<ItemPoint>();
        schoolId = getIntent().getStringExtra("schoolId");
        Log.i("ahsdjhasjd",schoolId);
        initTitle();
        initView();

        initData();

        Log.i("ydc",schoolId);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case ResultMessage.GET_POINT_BY_PARENTID_SUCCESS: {
                    Object objs = msg.obj;

                    String arrayString = null;
                    try {
                        arrayString = new JSONObject(objs.toString()).getString("results");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    List<ItemPoint> list = JSON.parseArray(arrayString,ItemPoint.class);
                    getDistance(list);


                }
            }
        }
    };

    private void getDistance(List<ItemPoint> list) {
        double myLatitude = Double.parseDouble(LocalStorage.getString(this,"myLatitude"));
        double myLongitude = Double.parseDouble(LocalStorage.getString(this,"myLongitude"));

        LatLng myPoint = new LatLng(myLatitude,myLongitude);
        for (int i = 0; i < list.size();i++){
            LatLng latlng = new LatLng(
                    list.get(i).getPointLatitude(),list.get(i).getPointLongitude());

            int a = MapUtil.CalculateDistance(MapUtil.conertToBDCoordinate(myPoint),
                    MapUtil.conertToBDCoordinate(latlng));

            list.get(i).setDistance(a+"");

        }
        for(int j =0;j <list.size();j++){
            pointDetailList.add(list.get(j));
        }

        adapter.notifyDataSetChanged();
    }


    private void initData() {
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new NameValuePair("point_parentId",schoolId));
        appClientDao.getPointByParentId(mHandler,param,"/point/getPointDetail");

    }


    private void initTitle() {
        titleBuilder = new TitleBuilder(this)
                .setTitleText("附近洗衣点")
                .setLeftImage(R.drawable.ic_keyboard_backspace).setLeftOnClickListener(this)
                .setRightImage(R.drawable.icon_location).setRightOnClickListener(this);
    }
    private void initView() {
        swipeLayout = (SwipeRefreshLayout) this.findViewById(R.id.swipe_refresh);
        swipeLayout.setOnRefreshListener(this);

        // 顶部刷新的样式
        swipeLayout.setColorSchemeColors(ContextCompat.getColor(this,android.R.color.holo_red_light),
                ContextCompat.getColor(this,android.R.color.holo_green_light),
                ContextCompat.getColor(this,android.R.color.holo_blue_bright),
                ContextCompat.getColor(this,android.R.color.holo_orange_light));


        /*ItemPoint info = new ItemPoint();
        info.setPointName("coin");
        infoList.add(info);*/
        listView = (ListView) this.findViewById(R.id.listview);
        adapter = new ListViewAdapter(this,R.layout.item_point,pointDetailList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(PointActivity.this,
                        WMListActivity.class);
                Bundle n = new Bundle();
                n.putSerializable("point",pointDetailList.get(position));
                intent.putExtras(n);
                startActivity(intent);
            }
        });
    }

    public void onRefresh() {
        pointDetailList.clear();
        initData();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                swipeLayout.setRefreshing(false);
            }
        }, 2000);

    }
    /***
     * Stop location service
     */
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case  R.id.titlebar_iv_left:
                finish();
                break;
            case R.id.titlebar_iv_right:
                Intent intent =new Intent(PointActivity.this,MapActivity.class);
                startActivity(intent);
                break;
        }
    }
}
