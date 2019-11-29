package com.ayaz.assignment.listener;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ayaz.assignment.R;
import com.ayaz.assignment.constant.Constant;
import com.ayaz.assignment.util.TransformInfo;

public class MultiTouchListener implements View.OnTouchListener {

    private static final int INVALID_POINTER_ID = -1;
    public boolean isRotateEnabled = true;
    public boolean isTranslateEnabled = true;
    public boolean isScaleEnabled = true;
    public float minimumScale = 0.5f;
    public float maximumScale = 10.0f;
    private int mActivePointerId = INVALID_POINTER_ID;
    private float mPrevX;
    private float mPrevY;
    private ScaleGestureDetector mScaleGestureDetector;
    private static View previousView, CurrentView;
    private static Boolean isFirstTime = true;
    boolean goneFlag = false;
    Context context;

    public MultiTouchListener(Context context) {
        this.context = context;
        mScaleGestureDetector = new ScaleGestureDetector(new ScaleGestureListener());
    }

    //Put this into the class
    final Handler handler = new Handler();
    Runnable mLongPressed = new Runnable() {
        public void run() {
            goneFlag = true;
            //Code for long click

            Log.e("box ::: ","long click");
            showScaleDialog();
        }
    };

    private void showScaleDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_box_scale);
        dialog.show();

        final TextView txt_scale = dialog.findViewById(R.id.txt_x);

        SeekBar sBar_view = dialog.findViewById(R.id.seekBar);
        sBar_view.setProgress((int) Constant.REMOVE_VIEW.getScaleX());
        sBar_view.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(progress == 0)
                    progress = 1;

                Constant.REMOVE_VIEW.setScaleX(progress);
                Constant.REMOVE_VIEW.setScaleY(progress);
                txt_scale.setText(progress+"x");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        Button b_close = dialog.findViewById(R.id.btn_close);
        b_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private static float adjustAngle(float degrees) {
        if (degrees > 180.0f) {
            degrees -= 360.0f;
        } else if (degrees < -180.0f) {
            degrees += 360.0f;
        }

        return degrees;
    }

    public static void move(View view, TransformInfo info) {
        computeRenderOffset(view, info.pivotX, info.pivotY);
        adjustTranslation(view, info.deltaX, info.deltaY);

        // Assume that scaling still maintains aspect ratio.
        float scale = view.getScaleX() * info.deltaScale;
        scale = Math.max(info.minimumScale, Math.min(info.maximumScale, scale));
        view.setScaleX(scale);
        view.setScaleY(scale);

        float rotation = adjustAngle(view.getRotation() + info.deltaAngle);
        view.setRotation(rotation);
    }

    private static void adjustTranslation(View view, float deltaX, float deltaY) {
        float[] deltaVector = {deltaX, deltaY};
        view.getMatrix().mapVectors(deltaVector);
        view.setTranslationX(view.getTranslationX() + deltaVector[0]);
        view.setTranslationY(view.getTranslationY() + deltaVector[1]);
    }

    private static void computeRenderOffset(View view, float pivotX,
                                            float pivotY) {
        if (view.getPivotX() == pivotX && view.getPivotY() == pivotY) {
            return;
        }

        float[] prevPoint = {0.0f, 0.0f};
        view.getMatrix().mapPoints(prevPoint);

        view.setPivotX(pivotX);
        view.setPivotY(pivotY);

        float[] currPoint = {0.0f, 0.0f};
        view.getMatrix().mapPoints(currPoint);

        float offsetX = currPoint[0] - prevPoint[0];
        float offsetY = currPoint[1] - prevPoint[1];

        view.setTranslationX(view.getTranslationX() - offsetX);
        view.setTranslationY(view.getTranslationY() - offsetY);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        Constant.REMOVE_VIEW = view;

        int action = event.getAction();

        switch (action & event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {

                Log.e("action", "button down");

                handler.postDelayed(mLongPressed, 1000);

                mPrevX = event.getX();
                mPrevY = event.getY();

                // Save the ID of this pointer.
                mActivePointerId = event.getPointerId(0);
                break;
            }
            case MotionEvent.ACTION_OUTSIDE:

                break;
            case MotionEvent.ACTION_MOVE: {

                handler.removeCallbacks(mLongPressed);

                // Find the index of the active pointer and fetch its position.
                int pointerIndex = event.findPointerIndex(mActivePointerId);
                if (pointerIndex != -1) {
                    float currX = event.getX(pointerIndex);
                    float currY = event.getY(pointerIndex);

                    // Only move if the ScaleGestureDetector isn't processing a
                    // gesture.
                    if (!mScaleGestureDetector.isInProgress()) {
                        adjustTranslation(view, currX - mPrevX, currY - mPrevY);
                    }
                }

                break;
            }
            case MotionEvent.ACTION_POINTER_DOWN:

                Log.e("action", "button pinter down");
                break;
            case MotionEvent.ACTION_HOVER_EXIT:
                Log.e("action", "button hover exit");
                break;
            case MotionEvent.ACTION_HOVER_ENTER:
                Log.e("action", "button hover enter");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.e("action", "button cancel");
                mActivePointerId = INVALID_POINTER_ID;
                break;

            case MotionEvent.ACTION_UP:
                Log.e("action", "button up");

                handler.removeCallbacks(mLongPressed);
                // view.setBackgroundResource(R.drawable.tv_border);
                mActivePointerId = INVALID_POINTER_ID;
                break;

            case MotionEvent.ACTION_POINTER_UP: {
                Log.e("action", "button pointer up");
                // Extract the index of the pointer that left the touch sensor.
                // view.setBackgroundResource(R.drawable.tv_border);
                int pointerIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                int pointerId = event.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mPrevX = event.getX(newPointerIndex);
                    mPrevY = event.getY(newPointerIndex);
                    mActivePointerId = event.getPointerId(newPointerIndex);
                }

                break;
            }
            case MotionEvent.ACTION_BUTTON_PRESS:
                Log.e("action", "button press");
                break;
            case MotionEvent.ACTION_BUTTON_RELEASE:
                Log.e("action", "button release");
                break;
        }
        mScaleGestureDetector.onTouchEvent(view, event);

        return true;
    }

    private class ScaleGestureListener extends
            ScaleGestureDetector.SimpleOnScaleGestureListener {

        private float mPivotX;
        private float mPivotY;
        private Vector2D mPrevSpanVector = new Vector2D();

        @Override
        public boolean onScaleBegin(View view, ScaleGestureDetector detector) {
            mPivotX = detector.getFocusX();
            mPivotY = detector.getFocusY();
            mPrevSpanVector.set(detector.getCurrentSpanVector());
            return true;
        }

        @Override
        public boolean onScale(View view, ScaleGestureDetector detector) {
            TransformInfo info = new TransformInfo();
            info.deltaScale = isScaleEnabled ? detector.getScaleFactor() : 1.0f;
            info.deltaAngle = isRotateEnabled ? Vector2D.getAngle(
                    mPrevSpanVector, detector.getCurrentSpanVector()) : 0.0f;
            info.deltaX = isTranslateEnabled ? detector.getFocusX() - mPivotX
                    : 0.0f;
            info.deltaY = isTranslateEnabled ? detector.getFocusY() - mPivotY
                    : 0.0f;
            info.pivotX = mPivotX;
            info.pivotY = mPivotY;
            info.minimumScale = minimumScale;
            info.maximumScale = maximumScale;

            move(view, info);
            return false;
        }
    }
}