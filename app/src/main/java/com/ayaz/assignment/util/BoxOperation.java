package com.ayaz.assignment.util;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.ayaz.assignment.constant.Constant;
import com.ayaz.assignment.customView.CustomView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BoxOperation {

    //extract view and save to sharedPref
    public static void saveBoxes(Context context, FrameLayout frame){

        JSONArray jsonArray = new JSONArray();

        for(int i = 0; i<frame.getChildCount(); i++){
            System.out.println("Saved :::" + i);

            View child = frame.getChildAt(i);
            if(child instanceof CustomView){
                CustomView customView = (CustomView)child;

                int x = (int) customView.getX();
                int y = (int) customView.getY();
                int scale = (int) customView.getScaleX();

                Box box = new Box(i, x , y, scale);
                JSONObject jsonObject = getJSONObj(box);
                jsonArray.put(jsonObject);
            }
        }

        new SharedPref(context).saveData(jsonArray.toString());
    }

    //convert box object to jsonObject
    private static JSONObject getJSONObj(Box box) {

        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put(Constant.INDEX_KEY, box.index);
            jsonObject.put(Constant.X_KEY, box.x);
            jsonObject.put(Constant.Y_KEY, box.y);
            jsonObject.put(Constant.SCALE_KEY, box.scale);

        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static String fetchBoxesString(Context context){

        String boxDataString = new SharedPref(context).getData();
        JSONArray savedJSON = new JSONArray();
        JSONArray createdJSON = new JSONArray();

        try {
            savedJSON = new JSONArray(boxDataString);

            for(int i = 0; i<savedJSON.length(); i++){

                JSONObject jsonObject = savedJSON.getJSONObject(i);

                int x = (int) jsonObject.getDouble(Constant.X_KEY);
                int y = (int) jsonObject.getDouble(Constant.Y_KEY);
                int scale = (int) jsonObject.getDouble(Constant.SCALE_KEY);

                int boxSize = new SharedPref(context).getBoxSize();
                int x1 = x + (boxSize * scale);
                int y1 = y + (boxSize * scale);

                JSONObject boxObj = new JSONObject();
                boxObj.put("x",x);
                boxObj.put("y",y);
                boxObj.put("x1",x1);
                boxObj.put("y1",y1);
                createdJSON.put(boxObj);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return createdJSON.toString();
    }

    //fetch string from sharedPref and return arrayList
    public static ArrayList<Box> fetchBoxes(Context context){

        ArrayList<Box> boxArrayList = new ArrayList<>();
        String boxDataString = new SharedPref(context).getData();

        try {
            JSONArray jsonArray = new JSONArray(boxDataString);

            for(int i = 0;i <jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                int index = jsonObject.getInt(Constant.INDEX_KEY);
                int x = (int) jsonObject.getDouble(Constant.X_KEY);
                int y = (int) jsonObject.getDouble(Constant.Y_KEY);
                int scale = (int) jsonObject.getDouble(Constant.SCALE_KEY);

                Box box = new Box(index, x, y, scale);
                boxArrayList.add(box);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return boxArrayList;
    }
}
