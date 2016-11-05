package com.ydc.laundromat.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.ydc.laundromat.R;
import com.ydc.laundromat.activity.PayActivity;
import com.ydc.laundromat.api.AppClientDao;
import com.ydc.laundromat.api.ResultMessage;
import com.ydc.laundromat.common.LocalStorage;
import com.ydc.laundromat.common.NameValuePair;
import com.ydc.laundromat.model.Order;
import com.ydc.laundromat.model.WM;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ydc on 2016/9/11.
 */
public class WMAdapter extends ArrayAdapter<WM> {
    private List<WM> wmList;
    private LayoutInflater inflater;
    private int resourceId;
    private Context context;

    private int select;
    private int poi;

    private AppClientDao appClientDao;
    /*// Provide a reference to the type of views that you are using
    // (custom viewholder)
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView wm_name;

        private TextView wm_status;
        private ImageView wm_staus_iv;

        private TextView wm_address;
        private ImageView wm_address_iv;

        private TextView btn;
        private CircleView wm_no;



        public ViewHolder(View v) {
            super(v);
            wm_name = (TextView) v.findViewById(R.id.wm_name);
            wm_status = (TextView) v.findViewById(R.id.wm_status);
            wm_address = (TextView) v.findViewById(R.id.wm_address);
            wm_staus_iv = (ImageView) v.findViewById(R.id.icon_wm_status);
            wm_address_iv = (ImageView) v.findViewById(R.id.icon_wm_address);
            wm_no = (CircleView) v.findViewById(R.id.wm_no);
            btn = (TextView) v.findViewById(R.id.book_btn);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public WMAdapter(Context context,int resourcesId ,List<WM> wmList) {
        this.wmList =wmList;
        this.context = context;
        this.resourcesId = resourcesId;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public WMAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_wm, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.wm_name.setText(wmList.get(position).getWm_name());
        holder.wm_address.setText(wmList.get(position).getWm_address());
        holder.wm_address_iv.setImageResource(R.mipmap.icon_position_green);
        if(wmList.get(position).getWm_status() == 1){
            holder.wm_status.setText("正在使用中");
            holder.wm_status.setTextColor(ContextCompat.getColor(context,R.color.green_1));
            holder.wm_staus_iv.setImageResource(R.mipmap.icon_busy_green);
        }else if(wmList.get(position).getWm_status() == 0){
            holder.wm_status.setText("空闲");
            holder.wm_status.setTextColor(ContextCompat.getColor(context,R.color.blue_1));
            holder.wm_staus_iv.setImageResource(R.mipmap.icon_machine_blue);
        }
        *//*holder.wm_no.setText(wmList.get(position).getWm_no());*//*
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return wmList.size();
    }*/

    public WMAdapter(Context context, int resourceId,List<WM> list) {
        super(context, 0, list);
        inflater = LayoutInflater.from(context);
        appClientDao = new AppClientDao(context);
        this.context = context;
        this.resourceId = resourceId;
        this.wmList = list;
        select = R.id.wmlist_rb_biaozhun;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        WM wm = getItem(position);
        if (convertView == null) {
            convertView = inflater.inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.wm_name = (TextView) convertView.findViewById(R.id.wm_name);
            viewHolder.wm_status = (TextView) convertView.findViewById(R.id.wm_status);
            viewHolder.wm_address = (TextView) convertView.findViewById(R.id.wm_address);
            viewHolder.wm_staus_iv = (ImageView) convertView.findViewById(R.id.icon_wm_status);
            viewHolder.wm_address_iv = (ImageView) convertView.findViewById(R.id.icon_wm_address);
            viewHolder.wm_no = (TextView) convertView.findViewById(R.id.wm_no);
            viewHolder.btn = (TextView) convertView.findViewById(R.id.book_btn);
            viewHolder.down = (RelativeLayout) convertView.findViewById(R.id.wm_down);
            viewHolder.washing_type_ll = (LinearLayout) convertView.findViewById(R.id.washing_type_ll);
            viewHolder.rg_tab = (RadioGroup) convertView.findViewById(R.id.wmlist_rg_tab);

            viewHolder.biaozhun_rb = (RadioButton) convertView.findViewById(R.id.wmlist_rb_biaozhun);
            viewHolder.dawu_rb = (RadioButton) convertView.findViewById(R.id.wmlist_rb_dawu);
            viewHolder.kuaisu_rb = (RadioButton) convertView.findViewById(R.id.wmlist_rb_kuaisu);
            viewHolder.dantuo_rb = (RadioButton) convertView.findViewById(R.id.wmlist_rb_dantuo);
            viewHolder.clotheType = (TextView) convertView.findViewById(R.id.wmlist_clothe_type);
            viewHolder.price = (TextView) convertView.findViewById(R.id.wmlist_wm_price);
            viewHolder.time = (TextView) convertView.findViewById(R.id.wmlist_wm_time);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.wm_name.setText(wm.getWm_name());
        viewHolder.wm_address.setText(wm.getWm_address());
        viewHolder.wm_address_iv.setImageResource(R.mipmap.icon_position_blue);

        viewHolder.wm_no.setText(wm.getWm_no()+"");


        viewHolder.biaozhun_rb.setChecked(true);
        viewHolder.biaozhun_rb.setBackgroundColor(ContextCompat.getColor(context, R.color.gray_2));

        final LinearLayout washing_type_ll = viewHolder.washing_type_ll;
        final RadioButton biaozhun_rb = viewHolder.biaozhun_rb;
        final RadioButton dawu_rb = viewHolder.dawu_rb;
        final RadioButton kuaisu_rb = viewHolder.kuaisu_rb;
        final RadioButton dantuo_rb = viewHolder.dantuo_rb;
        final TextView clotheType = viewHolder.clotheType;
        final TextView price = viewHolder.price;
        final TextView time = viewHolder.time;

        poi = position;


        if(wm.getWm_status() == 1){
            viewHolder.wm_status.setText("正在使用中");
            viewHolder.wm_status.setTextColor(ContextCompat.getColor(context,R.color.green_1));
            viewHolder.wm_staus_iv.setImageResource(R.mipmap.icon_busy_green);
            viewHolder.btn.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_book_btn_busy));
            viewHolder.btn.setClickable(false);
        }else if(wm.getWm_status() == 0){
            viewHolder.wm_status.setText("空闲");
            viewHolder.wm_status.setTextColor(ContextCompat.getColor(context,R.color.blue_1));
            viewHolder.wm_staus_iv.setImageResource(R.mipmap.icon_machine_blue);

            viewHolder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    poi = position;
                    String user_id = LocalStorage.getString(context,"user_id");

                    if (user_id.equals("")) {
                        Toast.makeText(context,"请先登录!",Toast.LENGTH_SHORT).show();
                    }else{
                        List<NameValuePair> list = new ArrayList<NameValuePair>();
                        list.add(new NameValuePair("user_id",user_id+""));
                        list.add(new NameValuePair("order_status","待支付"));
                        appClientDao.getOrderByStatusAndUserId(mHandler,list,
                                "/order/getOrderByUserIdAndOrderStatus");
                    }
                }
            });
        }


        viewHolder.down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(washing_type_ll.getVisibility() == View.VISIBLE){
                    washing_type_ll.setVisibility(View.GONE);

                }else if(washing_type_ll.getVisibility() == View.GONE){
                    washing_type_ll.setVisibility(View.VISIBLE);

                }
            }
        });

        viewHolder.rg_tab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.wmlist_rb_biaozhun:
                        select = R.id.wmlist_rb_biaozhun;
                        biaozhun_rb.setBackgroundColor(ContextCompat.getColor(context, R.color.gray_2));
                        dawu_rb.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                        kuaisu_rb.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                        dantuo_rb.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                        clotheType.setText("标准");
                        price.setText("4.00");
                        time.setText("32分钟");
                        break;
                    case R.id.wmlist_rb_dawu:
                        select = R.id.wmlist_rb_dawu;
                        biaozhun_rb.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                        dawu_rb.setBackgroundColor(ContextCompat.getColor(context, R.color.gray_2));
                        kuaisu_rb.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                        dantuo_rb.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                        clotheType.setText("大物");
                        price.setText("6.00");
                        time.setText("45分钟");
                        break;
                    case R.id.wmlist_rb_kuaisu:
                        select = R.id.wmlist_rb_kuaisu;
                        biaozhun_rb.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                        dawu_rb.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                        kuaisu_rb.setBackgroundColor(ContextCompat.getColor(context, R.color.gray_2));
                        dantuo_rb.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                        clotheType.setText("快速");
                        price.setText("3.00");
                        time.setText("20分钟");
                        break;
                    case R.id.wmlist_rb_dantuo:
                        select = R.id.wmlist_rb_dantuo;
                        biaozhun_rb.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                        dawu_rb.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                        kuaisu_rb.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                        dantuo_rb.setBackgroundColor(ContextCompat.getColor(context, R.color.gray_2));
                        clotheType.setText("单脱");
                        price.setText("1.00");
                        time.setText("8分钟");
                    default:
                        break;
                }
            }
        });
        return convertView;
    }


    public void setOrder(){
        String user_id = LocalStorage.getString(context,"user_id");
        Order order = new Order();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        order.setOrder_time(str);
        order.setOrder_userId(Integer.parseInt(user_id));

        order.setOrder_wmId(wmList.get(poi).getWm_id());

        order.setOrder_wmName(wmList.get(poi).getWm_name());
        order.setOrder_status("待支付");
        if (select == R.id.wmlist_rb_biaozhun) {
            order.setOrder_amount("4.00");
            order.setOrder_washingType("标准");
            order.setOrder_realPay("4.00");

        } else if (select == R.id.wmlist_rb_dawu) {
            order.setOrder_amount("6.00");
            order.setOrder_washingType("大物");
            order.setOrder_realPay("6.00");

        } else if (select == R.id.wmlist_rb_kuaisu) {
            order.setOrder_amount("3.00");
            order.setOrder_washingType("快速");
            order.setOrder_realPay("3.00");
        } else if (select == R.id.wmlist_rb_dantuo) {
            order.setOrder_amount("1.00");
            order.setOrder_washingType("单脱");
            order.setOrder_realPay("1.00");
        }

        postToServer(order);
    }

    private void postToServer(Order order) {
        List<NameValuePair> list =new ArrayList<NameValuePair>();

        list.add(new NameValuePair("order_status",order.getOrder_status()));
        list.add(new NameValuePair("order_amount",order.getOrder_amount()));
        list.add(new NameValuePair("order_time",order.getOrder_time()));
        list.add(new NameValuePair("order_realPay",order.getOrder_realPay()));
        list.add(new NameValuePair("order_userId",order.getOrder_userId()+""));

        list.add(new NameValuePair("order_wmId",order.getOrder_wmId()+""));
        list.add(new NameValuePair("order_wmName",order.getOrder_wmName()));
        list.add(new NameValuePair("order_washingType",order.getOrder_washingType()));
        list.add(new NameValuePair("point_no",wmList.get(poi).getWm_parentId()+""));

        appClientDao.addOrder(mHandler,list,"/order/addOrder");

    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case ResultMessage.ADD_ORDER_SUCCESS:
                    Object objs = msg.obj;
                    JSON jsonObject;
                    try {
                        jsonObject = JSON.parseObject(new JSONObject(objs.toString()).getString("results"));
                        Order newOrder = JSON.toJavaObject(jsonObject,Order.class);

                        List<NameValuePair> list = new ArrayList<NameValuePair>();
                        list.add(new NameValuePair("wm_id",newOrder.getOrder_wmId()+""));
                        list.add(new NameValuePair("wm_status","1"));
                        appClientDao.editWM(mHandler,list,"/wm/editWM");

                        Intent intent = new Intent(context,PayActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("wm",wmList.get(poi));
                        bundle.putSerializable("order",newOrder);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case ResultMessage.EDIT_WM_SUCCESS:

                    break;
                case ResultMessage.GET_ORDER_STATUS_SUCCESS:

                    Toast.makeText(context,"你已预订机器，请前往付款!",Toast.LENGTH_SHORT).show();
                    break;
                case ResultMessage.GET_ORDER_STATUS_FAIL:
                    setOrder();
                    break;
            }
        }
    };

    public static class ViewHolder {
        private TextView wm_name;

        private TextView wm_status;
        private ImageView wm_staus_iv;

        private TextView wm_address;
        private ImageView wm_address_iv;

        private TextView btn;
        private TextView wm_no;

        private RelativeLayout down;

        private LinearLayout washing_type_ll;

        private RadioGroup rg_tab;
        private RadioButton biaozhun_rb,dawu_rb,kuaisu_rb,dantuo_rb;

        private TextView clotheType,price,time;
    }
}
