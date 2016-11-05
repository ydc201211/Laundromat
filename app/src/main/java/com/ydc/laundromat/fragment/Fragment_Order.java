package com.ydc.laundromat.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.ydc.laundromat.R;
import com.ydc.laundromat.adapter.OrderAdapter;
import com.ydc.laundromat.api.AppClientDao;
import com.ydc.laundromat.api.ResultMessage;
import com.ydc.laundromat.common.LocalStorage;
import com.ydc.laundromat.common.NameValuePair;
import com.ydc.laundromat.model.Order;
import com.ydc.laundromat.widget.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ydc on 2016/9/3.
 */
public class Fragment_Order extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private View fragment_order;

    protected static final String TAG = "Fragment_Order";
    private ListView listView;
    private OrderAdapter orderAdapter;

    private List<Order> orders;

    private SwipeRefreshLayout swipeLayout;

    private AppClientDao appClientDao;

    private int selectPoi;

    private CustomDialog dialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragment_order = inflater.inflate(R.layout.fragment__order, null);

        orders = new ArrayList<Order>();
        appClientDao =new AppClientDao(getActivity());
        initView();

        initData();
        return fragment_order;
    }


    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case ResultMessage.GET_ORDERS_SUCCESS: {
                    Object objs = msg.obj;

                    String arrayString = null;
                    try {
                        arrayString = new JSONObject(objs.toString()).getString("results");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    List<Order> list = JSON.parseArray(arrayString,Order.class);
                    for(int i = 0; i <list.size();i++){
                        orders.add(list.get(i));
                    }

                    orderAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    private void initData() {
        String user_id = LocalStorage.getString(getActivity(),"user_id");
        if(!user_id.equals("")){
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            list.add(new NameValuePair("order_userId",user_id));
            appClientDao.getOrders(mHandler,list,"/order/getOrdersByUserId");
        }
    }

    private void initView() {

        swipeLayout = (SwipeRefreshLayout) fragment_order.findViewById(R.id.order_swipe_refresh);
        swipeLayout.setOnRefreshListener(this);

        // 顶部刷新的样式
        swipeLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(),android.R.color.holo_red_light),
                ContextCompat.getColor(getActivity(),android.R.color.holo_green_light),
                ContextCompat.getColor(getActivity(),android.R.color.holo_blue_bright),
                ContextCompat.getColor(getActivity(),android.R.color.holo_orange_light));

        listView = (ListView) fragment_order.findViewById(R.id.order_listview);
        orderAdapter = new OrderAdapter(getActivity(),R.layout.item_order ,orders);

        listView.setEmptyView(fragment_order.findViewById(R.id.empty));
        listView.setAdapter(orderAdapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectPoi = i;
                showdialog();
                return false;
            }
        });
    }



    @Override
    public void onDestroy() {
        super.onDestroy();


    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void onRefresh() {
        orders.clear();
        initData();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                swipeLayout.setRefreshing(false);
            }
        }, 2000);

    }

    private void showdialog() {
        dialog = new CustomDialog(getActivity());

        dialog.setOnPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dosomething youself

                List<NameValuePair> list = new ArrayList<NameValuePair>();
                orderAdapter.notifyDataSetChanged();
                dialog.dismiss();
                list.add(new NameValuePair("order_no",orders.get(selectPoi).getOrder_no()));
                orders.remove(selectPoi);
                appClientDao.deleteOrder(mHandler,list,"/order/delateOrder");
            }
        });
        dialog.setOnNegativeListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void hidedialog() {
        dialog.dismiss();
    }
}
