package com.ydc.laundromat.widget;

import android.os.CountDownTimer;
import android.os.Handler;

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
 * 注册验证码计时器
 * 
 * @author zihao
 * 
 */
public class RegisterCodeTimer extends CountDownTimer {
	private static Handler mHandler;
	public static final int IN_RUNNING = 1001;
	public static final int END_RUNNING = 1002;
	public AppClientDao appClientDao;
	public String order_no;
	public String wm_id;
	public String user_id;
	/**
	 * @param millisInFuture
	 *            // 倒计时的时长
	 * @param countDownInterval
	 *            // 间隔时间
	 * @param handler
	 *            // 通知进度的Handler
	 */
	public RegisterCodeTimer(int millisInFuture, long countDownInterval,
			Handler handler,String order_no,String wm_id,String user_id) {
		super(millisInFuture, countDownInterval);
		appClientDao =new AppClientDao();
		this.order_no =order_no;
		this.wm_id = wm_id;
		this.user_id = user_id;
		mHandler = handler;
	}

	// 结束
	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		if (mHandler != null) {

			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new NameValuePair("order_no",order_no));
			appClientDao.getOrderByOrderNo(pHandler,list,"/order/getOrderByOrderNo");

		}
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

	@Override
	public void onTick(long millisUntilFinished) {
		// TODO Auto-generated method stub
		if (mHandler != null){
			int time = (int) millisUntilFinished;

			int minute=time%(1000*60*60)/(60*1000);
			int second=(time%(1000*60*60))%(60*1000)/1000;
			mHandler.obtainMessage(IN_RUNNING,minute+ "''" + second + "'").sendToTarget();
		}
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
					mHandler.obtainMessage(END_RUNNING, "获取验证码").sendToTarget();
					break;

			}
		}
	};

}
