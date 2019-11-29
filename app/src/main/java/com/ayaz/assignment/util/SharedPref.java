package com.ayaz.assignment.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

import com.ayaz.assignment.constant.Constant;

public class SharedPref {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SharedPref(Context context){
        sharedPreferences = context.getSharedPreferences(Constant.SHARED_PREF, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveData(String boxData){
        editor.putString(Constant.SAVE_BOX_KEY, boxData).commit();
    }

    public String getData(){
        return sharedPreferences.getString(Constant.SAVE_BOX_KEY,"");
    }

    public boolean isFirstTime() {
        boolean flag = sharedPreferences.getBoolean(Constant.FIRST_TIME_KEY, true);

        if(flag)
            editor.putBoolean(Constant.FIRST_TIME_KEY, false).commit();

        return flag;
    }

    public void setDefault(){
        setSize(150);
        setColor(Color.RED);
    }

    public void setColor(int color){
        editor.putInt(Constant.DEFAULT_BOX_COLOR_KEY, color).commit();
    }

    public void setSize(int size){
        editor.putInt(Constant.DEFAULT_BOX_SIZE_KEY, size).commit();
    }

    public int getBoxSize(){
        return sharedPreferences.getInt(Constant.DEFAULT_BOX_SIZE_KEY,-1);
    }

    public int getBoxColor(){
        return sharedPreferences.getInt(Constant.DEFAULT_BOX_COLOR_KEY,-1);
    }
}