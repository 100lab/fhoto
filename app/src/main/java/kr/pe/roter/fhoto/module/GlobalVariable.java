package kr.pe.roter.fhoto.module;

import android.content.Context;
import android.content.SharedPreferences;

public class GlobalVariable {


	public static final String PREF_NAME = "fhoto";

	public static final String KEY_SAVE_COUNT = "save_count";


	public static final int PROPER_IMG_SIZE = 1000; //이미지가 이보다 작게 축소됨. * x 960 들 이미지 때문에 이렇게 함.
	public static final int PROPER_THUMNAIL_SIZE = 80;

	/**
	 * 세이브 횟수를 1 증가시킨다.
	 * @param context
	 */
	public static void addSaveCount(Context context){

		int saveCount = getSaveCount(context);

		saveCount++;
		if(saveCount >= 1000)
			saveCount = 0;
		SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(KEY_SAVE_COUNT, saveCount);
		editor.commit();
	}


	/**
	 * 지점까지 저장한 횟수를 출력한다.
	 * @param context
	 * @return
	 */
	public static int getSaveCount(Context context){
		SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		int value = prefs.getInt(KEY_SAVE_COUNT, 1);
		return value;
	}



}
