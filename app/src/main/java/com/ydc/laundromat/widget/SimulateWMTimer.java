package com.ydc.laundromat.widget;

import android.os.CountDownTimer;
import android.os.Handler;

import com.ydc.laundromat.api.AppClientDao;
import com.ydc.laundromat.api.ResultMessage;
import com.ydc.laundromat.common.NameValuePair;
import com.ydc.laundromat.model.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * 注册验证码计时器
 * 
 * @author zihao
 * 
 */
public class SimulateWMTimer extends CountDownTimer {

	private static Handler mHandler;
	public static final int IN_RUNNING = 1001;
	public static final int END_RUNNING = 1002;
	public AppClientDao appClientDao;
	public Order order;
	/**
	 * @param millisInFuture
	 *            // 倒计时的时长
	 * @param countDownInterval
	 *            // 间隔时间
	 * @param handler
	 *            // 通知进度的Handler
	 */
	public SimulateWMTimer(int millisInFuture, long countDownInterval,
							 Handler handler,Order order) {
		super(millisInFuture, countDownInterval);
		appClientDao =new AppClientDao();
		this.order = order;
		mHandler = handler;
	}

	// 结束
	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		if(mHandler!=null){
			editOrderStatus();
		}
	}

	public void editOrderStatus(){
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new NameValuePair("order_no",order.getOrder_no()));
		list.add(new NameValuePair("order_status","已完成"));
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
				case ResultMessage.EDIT_WM_SUCCESS:
					mHandler.obtainMessage(END_RUNNING, "获取验证码").sendToTarget();
					break;

			}
		}
	};

}
