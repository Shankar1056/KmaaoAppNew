package com.apextechies.kmaaoapp.common;

import android.content.Context;
import android.content.SharedPreferences;

public class ClsGeneral {
	public static Context mContext;



	public static void setPreferences(Context context, String key, String value) {
		mContext = context;
		SharedPreferences.Editor editor = mContext.getSharedPreferences(
				"WED_APP", Context.MODE_PRIVATE).edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static String getPreferences(Context context, String key) {
		mContext = context;
		SharedPreferences prefs = mContext.getSharedPreferences("WED_APP",
				Context.MODE_PRIVATE);
		return  prefs.getString(key, "");
	}




}
