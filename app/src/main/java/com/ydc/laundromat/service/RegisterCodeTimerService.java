package com.ydc.laundromat.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.alibaba.fastjson.JSON;
import com.ydc.laundromat.api.AppClientDao;
import com.ydc.laundromat.api.ResultMessage;
import com.ydc.laundromat.common.NameValuePair;
import com.ydc.laundromat.model.Order;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 注册验证码计时服务
 * 
 * @author zihao
 * 
 */
public class RegisterCodeTimerService extends Service {

	public AppClientDao appClientDao;
	public String order_no;
	public String wm_id;
	public String user_id;

	private int time = 20000;

	private Intent intent = new Intent("com.example.communication.RECEIVER");


	/**
	 * 模拟下载任务，每秒钟更新一次
	 */
	public void startCountDown(){
		new Thread(new Runnable() {

			@Override
			public void run() {
				while(time >= 0){
					time -= 1000;

					intent.putExtra("time", time);
					sendBroadcast(intent);
					if(time < 0){
						List<NameValuePair> list = new ArrayList<NameValuePair>();
						list.add(new NameValuePair("order_no",order_no));
						appClientDao.getOrderByOrderNo(pHandler,list,"/order/getOrderByOrderNo");
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}
		}).start();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		startCountDown();
		return super.onStartCommand(intent, flags, startId);
	}
	/**
	 * 返回一个Binder对象
	 */
	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);

		appClientDao = new AppClientDao();
		order_no = intent.getStringExtra("order_no");
		wm_id = intent.getStringExtra("wm_id");
		user_id = intent.getStringExtra("user_id");

	}

	public void editOrderStatus(Order order){
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new NameValuePair("order_no",order_no));
		list.add(new NameValuePair("order_status","超时未支付"));
		appClientDao.editOrderOnService(list,"/order/editOrder");

		List<NameValuePair> list1 = new ArrayList<NameValuePair>();
		list1.add(new NameValuePair("wm_id",order.getOrder_wmId()+""));
		list1.add(new NameValuePair("wm_status","0"));
		appClientDao.editWM(pHandler,list1,"/wm/editWM");

	}

	private Handler pHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case ResultMessage.GET_ORDER_BY_NO_SUCCESS:
					Object objs = msg.obj;

					JSON jsonObject;
					try {
						jsonObject = JSON.parseObject(new JSONObject(objs.toString()).getString("results"));
						Order order = JSON.toJavaObject(jsonObject,Order.class);
						if(order.getOrder_status().equals("待支付")){

							editOrderStatus(order);
						}


					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;

				case ResultMessage.EDIT_WM_SUCCESS:
					stopSelf();
					break;

			}
		}
	};
}
