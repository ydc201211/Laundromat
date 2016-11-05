package com.ydc.laundromat.adapter;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.ydc.laundromat.R;
import com.ydc.laundromat.activity.FinishActivity;
import com.ydc.laundromat.api.AppClientDao;
import com.ydc.laundromat.api.ResultMessage;
import com.ydc.laundromat.common.LocalStorage;
import com.ydc.laundromat.common.NameValuePair;
import com.ydc.laundromat.model.Order;
import com.ydc.laundromat.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ydc on 2016/9/13.
 */
public class OrderAdapter extends BaseAdapter {

    private List<Order> orderList;
    private LayoutInflater inflater;

    private Context context;

    private AppClientDao appClientDao;
    private int poi;


    public OrderAdapter(Context context, int resource, List<Order> list) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.orderList = list;

        appClientDao = new AppClientDao(context);
    }

    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Order getItem(int i) {
        return orderList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        Order order = getItem(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_order,null);
            viewHolder = new ViewHolder();
            viewHolder.fg_order_no = (TextView) convertView.findViewById(R.id.fg_order_no);
            viewHolder.fg_order_status = (TextView) convertView.findViewById(R.id.fg_order_status);
            viewHolder.fg_order_realPay = (TextView) convertView.findViewById(R.id.fg_order_realPay);
            viewHolder.fg_order_wm_name = (TextView) convertView.findViewById(R.id.fg_order_wm_name);
            viewHolder.pay_order_btn = (Button) convertView.findViewById(R.id.pay_order_btn);
            viewHolder.cancle_order_btn = (Button) convertView.findViewById(R.id.cancle_order_btn);
            viewHolder.pay_and_cancle_LL = (RelativeLayout) convertView.findViewById(R.id.pay_and_cancle_LL);
            viewHolder.remove_btn = (TextView) convertView.findViewById(R.id.order_remove);
            viewHolder.fg_order_time = (TextView) convertView.findViewById(R.id.fg_order_time);
            viewHolder.statusView = convertView.findViewById(R.id.view_status);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.fg_order_no.setText(order.getOrder_no());
        viewHolder.fg_order_status.setText(order.getOrder_status());
        viewHolder.fg_order_time.setText(order.getOrder_time());
        if(order.getOrder_status().equals("待支付")){
            viewHolder.pay_and_cancle_LL.setVisibility(View.VISIBLE);
            viewHolder.statusView.setBackgroundColor(ContextCompat.getColor(context,R.color.titlebar_blue));
            convertView.setLongClickable(false);

        }else{
            viewHolder.pay_and_cancle_LL.setVisibility(View.GONE);
            viewHolder.statusView.setBackgroundColor(ContextCompat.getColor(context,R.color.gray_3));
            convertView.setLongClickable(true);
        }

        viewHolder.fg_order_wm_name.setText(order.getOrder_wmName());
        viewHolder.fg_order_realPay.setText(order.getOrder_realPay());

        final RelativeLayout pay_and_cancle_LL = viewHolder.pay_and_cancle_LL;
        final View statusView = viewHolder.statusView;
        final TextView statustv = viewHolder.fg_order_status;


        viewHolder.pay_order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                poi = position;
               /* Intent intent = new Intent(context, PayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("order",orderList.get(poi));
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                context.startActivity(intent);*/
                List<NameValuePair> list = new ArrayList<NameValuePair>();
                list.add(new NameValuePair("user_id", LocalStorage.getString(context,"user_id")));
                appClientDao.getWallet(mHandler,list,"/user/getWalletByUserId");

            }
        });

        viewHolder.cancle_order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                poi = position;
                pay_and_cancle_LL.setVisibility(View.GONE);
                statusView.setBackgroundColor(ContextCompat.getColor(context,R.color.gray_3));
                statustv.setText("取消订单");
                Order order = orderList.get(poi);
                Log.i("kkkkkhhhhhhaaaaaa",order.getOrder_no());
                order.setOrder_status("取消订单");
                List<NameValuePair> params =new ArrayList<NameValuePair>();
                params.add(new NameValuePair("order_no",order.getOrder_no()));
                params.add(new NameValuePair("order_status",order.getOrder_status()));
                appClientDao.editOrder(mHandler,params,"/order/editOrder");
            }
        });

        return convertView;
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case ResultMessage.EDIT_ORDER_SUCCESS:
                    Object objs = msg.obj;

                    JSON jsonObject;
                    try {
                        jsonObject = JSON.parseObject(new JSONObject(objs.toString()).getString("results"));
                        Order order = JSON.toJavaObject(jsonObject,Order.class);

                        List<NameValuePair> list = new ArrayList<NameValuePair>();
                        list.add(new NameValuePair("wm_id",order.getOrder_wmId()+""));
                        list.add(new NameValuePair("wm_status","0"));
                        appClientDao.editWM(mHandler,list,"/wm/editWM");



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case ResultMessage.PAY_SUCCESS:
                    Intent intent = new Intent(context,FinishActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("order",orderList.get(poi));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                    break;
                case ResultMessage.PAY_FAIL:
                    Toast.makeText(context,"支付遇到了问题",Toast.LENGTH_SHORT).show();
                    break;
                case ResultMessage.GET_WALLET_SUCCESS:
                    Object obj_1 = msg.obj;

                    JSON jsonObject1;
                    try {
                        jsonObject1 = JSON.parseObject(new JSONObject(obj_1.toString()).getString("results"));
                        User user = JSON.toJavaObject(jsonObject1,User.class);
                        if(Float.parseFloat(user.getUser_wallet()) >=
                                Float.parseFloat(orderList.get(poi).getOrder_realPay())){
                            List<NameValuePair> list = new ArrayList<NameValuePair>();
                            list.add(new NameValuePair("user_id", LocalStorage.getString(context,"user_id")));
                            list.add(new NameValuePair("money",orderList.get(poi).getOrder_realPay()));
                            list.add(new NameValuePair("type","subtract"));
                            appClientDao.changeWallet(mHandler,list,"/user/handleWallet");
                        }else{
                            Toast.makeText(context,"余额不足！",Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case ResultMessage.CHANGE_WALLET_SUCCESS:
                    Toast.makeText(context,"支付成功！",Toast.LENGTH_SHORT).show();

                    List<NameValuePair> param = new ArrayList<NameValuePair>();
                    param.add(new NameValuePair("order_no",orderList.get(poi).getOrder_no()));
                    param.add(new NameValuePair("order_status","已支付"));
                    appClientDao.payOrder(mHandler,param,"/order/editOrder");

                    break;
                case ResultMessage.EDIT_WM_SUCCESS:

                    stopService();
                    break;
            }
        }
    };

    private void stopService(){
        ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        // 获取所有的service
        List<ActivityManager.RunningServiceInfo> list1 = am.getRunningServices(100);

        for (int i = 0; i < list1.size(); i++) {
            System.out.println(list1.get(i).service);
            if (list1.get(i).service.getPackageName().startsWith("com.ydc.laundromat")) {
                // 此方法将service干掉
                context.stopService(new Intent().setComponent(list1.get(i).service));
            }
        }
    }
    public static class ViewHolder {
        private TextView fg_order_no,
                fg_order_status,
                fg_order_wm_name,
                fg_order_realPay,
                fg_order_time;
        private RelativeLayout pay_and_cancle_LL;

        private View statusView;
        private Button pay_order_btn;
        private Button cancle_order_btn;
        private TextView remove_btn;
    }
}
