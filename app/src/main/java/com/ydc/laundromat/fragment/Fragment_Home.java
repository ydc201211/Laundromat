package com.ydc.laundromat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.model.LatLng;
import com.ydc.laundromat.R;
import com.ydc.laundromat.activity.LocationApplication;
import com.ydc.laundromat.activity.PointActivity;
import com.ydc.laundromat.api.AppClientDao;
import com.ydc.laundromat.api.ResultMessage;
import com.ydc.laundromat.common.LocalStorage;
import com.ydc.laundromat.model.School;
import com.ydc.laundromat.service.LocationService;
import com.ydc.laundromat.util.MapUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by ydc on 2016/9/3.
 */
public class Fragment_Home extends Fragment implements OnClickListener {
    private View fragment_home;
    private RelativeLayout clothe_RL;

    private LocationService locationService;
    private LatLng myPoint;//我的位置信息

    private int schoolId;

    private AppClientDao appClientDao;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case ResultMessage.GET_SCHOOLS_SUCCESS:
                    Object objs = msg.obj;
                    try {
                        String arrayString = new JSONObject(objs.toString()).getString("results");
                        List<School> resList = JSON.parseArray(arrayString, School.class);

                        schoolId = getSchoolId(resList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                break;

            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragment_home = inflater.inflate(R.layout.fragment__home, null);
        initView();

        return fragment_home;
    }



    private void initView() {
        clothe_RL = (RelativeLayout) fragment_home.findViewById(R.id.cloth);
        clothe_RL.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }

    @Override
    public void onResume() {
        super.onResume();

    }

    /***
     * Stop location service
     */
    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }
    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        // -----------location config ------------
        locationService = ((LocationApplication) getActivity().getApplicationContext()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();


    }
    BDLocationListener mListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            // Receive Location
            if (location == null) {
                return;
            }


            myPoint = new LatLng(location.getLatitude(),location.getLongitude());

            LocalStorage.saveString(getActivity(),"myLatitude",location.getLatitude()+"");
            LocalStorage.saveString(getActivity(),"myLongitude",location.getLongitude()+"");
            Log.i("纬度",location.getLatitude()+"");
            Log.i("经度",location.getLongitude()+"");

            appClientDao = new AppClientDao(getActivity());
            appClientDao.getSchools(mHandler,null,"/school/getSchools");

        }
    };



    private int getSchoolId(List<School> list){
        int minSchoolId=0;
        String minSchoolName = "";
        if(list.size() != 0) {
            LatLng minLatLng = new LatLng(
                    list.get(0).getSchool_latitude(),list.get(0).getSchool_longitude());

            int minDistance = MapUtil.CalculateDistance(MapUtil.conertToBDCoordinate(myPoint),MapUtil.conertToBDCoordinate(minLatLng));
            minSchoolId = list.get(0).getSchool_id();
            for (int i = 1; i < list.size(); i++) {
                LatLng sourceLatLng = new LatLng(
                        list.get(i).getSchool_latitude(),list.get(i).getSchool_longitude());

                int a = MapUtil.CalculateDistance(MapUtil.conertToBDCoordinate(myPoint),MapUtil.conertToBDCoordinate(sourceLatLng));

                if (a < minDistance){
                    minDistance = a;
                    minSchoolId = list.get(i).getSchool_id();

                    minSchoolName = list.get(i).getSchool_name();
                }

            }
            LocalStorage.saveString(getActivity(),"schoolName",minSchoolName);
        }
        return minSchoolId;
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.cloth:
                intent = new Intent(getActivity(), PointActivity.class);
                intent.putExtra("schoolId",schoolId+"");
                startActivity(intent);
                break;
        }
    }
}
