package com.ydc.laundromat.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.ydc.laundromat.activity.LocationApplication;

public class LocalStorage {
	private static SharedPreferences getSharedPreferences(Context context) {
		SharedPreferences sharedPreferences = ((LocationApplication) context.getApplicationContext()).sharedPreferences;
		return sharedPreferences;
	}

	public static void saveString(Context context, String key, String value) {
		SharedPreferences sharedPreferences = getSharedPreferences(context);
		sharedPreferences.edit().putString(key, value).commit();

	}

	public static String getString(Context context, String key) {
		return getSharedPreferences(context).getString(key, "");
	}

}
