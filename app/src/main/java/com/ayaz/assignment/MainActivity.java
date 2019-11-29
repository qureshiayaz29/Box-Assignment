package com.ayaz.assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.ayaz.assignment.customView.CustomView;
import com.ayaz.assignment.listener.MultiTouchListener;
import com.ayaz.assignment.util.Box;
import com.ayaz.assignment.util.BoxOperation;
import com.ayaz.assignment.util.DisplayBoxDialog;
import com.ayaz.assignment.util.ExportBoxDialog;
import com.ayaz.assignment.util.InfoBoxDialog;
import com.ayaz.assignment.util.SharedPref;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FrameLayout frame_Box;
    Button btn_export, btn_view;
    FrameLayout.LayoutParams layoutParams;
    int default_scale = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {

        setDefaults();
        showAppInfo();

        frame_Box = findViewById(R.id.frame);
        btn_export = findViewById(R.id.btn_export);
        btn_view = findViewById(R.id.btn_list);

        int getBoxSize = new SharedPref(MainActivity.this).getBoxSize();
        layoutParams = new FrameLayout.LayoutParams(getBoxSize, getBoxSize);

        frame_Box.setOnTouchListener(frameTouch);
        btn_export.setOnClickListener(exportClicked);
        btn_view.setOnClickListener(boxClicked);
    }

    View.OnClickListener exportClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BoxOperation.saveBoxes(MainActivity.this ,frame_Box);
            new ExportBoxDialog(MainActivity.this);
        }
    };

    View.OnClickListener boxClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BoxOperation.saveBoxes(MainActivity.this ,frame_Box);
            new DisplayBoxDialog(MainActivity.this);
        }
    };

    View.OnTouchListener frameTouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                Log.e("X : "+event.getX(),"Y : "+event.getY());

                float x = event.getX();
                float y = event.getY();
                addBox(x,y,default_scale);
            }
            return true;
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    private void addBox(float x, float y, float scale) {

        final CustomView customView = new CustomView(MainActivity.this);

        final MultiTouchListener touchListener = new MultiTouchListener(MainActivity.this);
        customView.setOnTouchListener(touchListener);

        final FrameLayout.LayoutParams customBoxParams = new FrameLayout.LayoutParams(layoutParams.width,layoutParams.height);
        customBoxParams.leftMargin = (int)x;
        customBoxParams.topMargin = (int)y;

        customView.setScaleX(scale);
        customView.setScaleY(scale);

        frame_Box.addView(customView,customBoxParams);
    }

    private void setDefaults() {
        new SharedPref(MainActivity.this).setDefault();
    }

    private void showAppInfo() {

        //show app info only if it is opened first time
        boolean isFirstTime = new SharedPref(MainActivity.this).isFirstTime();
        if(isFirstTime)
            new InfoBoxDialog(MainActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BoxOperation.saveBoxes(MainActivity.this ,frame_Box);
    }

    @Override
    protected void onStart() {
        super.onStart();

        ArrayList<Box> boxArrayList = BoxOperation.fetchBoxes(MainActivity.this);

        frame_Box.removeAllViews();
        for(Box box : boxArrayList){
            addBox(box.getX(), box.getY(), box.getScale());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(MainActivity.this).inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.info:
                new InfoBoxDialog(MainActivity.this);
                break;
        }
        return true;
    }
}