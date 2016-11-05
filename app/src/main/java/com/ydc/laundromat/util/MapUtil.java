package com.ydc.laundromat.util;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.DistanceUtil;

/**
 * Created by ydc on 2016/9/10.
 */
public class MapUtil {
    public static LatLng conertToBDCoordinate(LatLng sourceLatLng){
        CoordinateConverter converter  = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.COMMON);
        // sourceLatLng待转换坐标
        converter.coord(sourceLatLng);
        LatLng desLatLng = converter.convert();

        return  desLatLng;
    }

    public static int CalculateDistance(LatLng LatLng_1,LatLng LatLng_2) {
        double a = DistanceUtil.getDistance(LatLng_1,LatLng_2);

        return  (int) a;
    }
}
