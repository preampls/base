package com.topit.mylibrary;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.topit.mylibrary.util.EvtLog;
import com.topit.mylibrary.util.RelayoutViewTool;
import com.topit.mylibrary.util.StringUtil;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.hardware.camera2.params.BlackLevelPattern.COUNT;


/**
 * @Description:
 * @Author: ls
 * @CreateDate: 2020/6/16 16:45
 * @UpdateUser: ls
 */
public abstract class BaseActivity extends AppCompatActivity {
    private View mView;
    private boolean mIsScale = true;

    public static BaseActivity mContext;
    protected Bundle mSavedInstanceState;
    protected Animation myAnim;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏

        mContext = this;
        mSavedInstanceState = savedInstanceState;


        setContentView(getViewId());
        Intent intent = getIntent();
        parseIntentData(intent, false);

        initView();
        initData();
        if (lacksPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            checkPermission();
        }
        hideBottomUIMenu();
        setTheme(android.R.style.Theme_Light_NoTitleBar);
    }

    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT < 19) { // lower api
            View v = getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    public abstract void initView();

    public abstract void initData();

    protected abstract int getViewId();

    protected void parseIntentData(Intent intent, boolean isFrom) {

    }


    @Override
    public void setContentView(View view) { // 适配xml布局
        if (mIsScale) {
            RelayoutViewTool.relayoutViewWithScale(view, MApplication.mScreenWidthScale);
        }
        super.setContentView(view);
    }


    @Override
    public void setContentView(int layoutResID) {
        mView = View.inflate(this, layoutResID, null);

        setContentView(mView);
    }

    public static BaseActivity getInstance() {
        return mContext;
    }

    public void setScale(boolean isScale) {
        mIsScale = isScale;
    }

    public void jumpToActivity(Class<?> className) {
        Intent intent = new Intent(mContext, className);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    public void jumpToActivity(Class<?> className, String putExtra) {
        Intent intent = new Intent(mContext, className);
        intent.putExtra("putExtra", putExtra);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    public void jumpToActivity(Class<?> className, String putExtra, String putExtra_1) {
        Intent intent = new Intent(mContext, className);
        intent.putExtra("putExtra", putExtra);
        intent.putExtra("putExtra_1", putExtra_1);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }


    /**
     * 判断是否缺少权限
     */
    public static boolean lacksPermission(Context mContexts, String permission) {
        return ContextCompat.checkSelfPermission(mContexts, permission) ==
                PackageManager.PERMISSION_DENIED;
    }


    private String[] strings = {
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    public void checkPermission() {
        boolean isGranted = true;
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //如果没有写sd卡权限
                isGranted = false;
            }
            Log.i("cbs", "isGranted == " + isGranted);
            if (!isGranted) {
                requestPermissions(strings, 102);
            }
        }

    }

    //读写授权后回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //执行代码,这里是已经申请权限成功了,可以不用做处理
//                    initData();

                } else {
                    Toast.makeText(BaseActivity.this, "权限申请失败", Toast.LENGTH_SHORT);
                }
                break;
        }
    }

    /**
     * -------------------------------------------------倒计时------------------------------------------------------
     **/

    public void reTime() {
//        if (isPause) return;
//        if (countDownTimer != null) {
//            countDownTimer.cancel();
//            countDownTimer = null;
//            isPause = true;
//            pauseTimeDown();
//        }

    }


    /**
     * 倒计时显示  倒计时结束跳到屏保页面
     */

//    private Handler handler;
    private int time_sy = 0;
    private static Timer timer;

    private void pauseTimeDown() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isPause) {
                    handler.sendEmptyMessage(COUNT);
                }

            }
        }, 0, 1000);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        int num = 20;

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case COUNT:
                    if (num <= 0) {
                        isPause = false;
                        countDown2();
                    }
                    EvtLog.d("num:= " + num);
                    num--;
                    break;
            }
        }

        ;
    };

    @SuppressLint("HandlerLeak")
    public void countDown2() {
//
//        countDownTimer = new CountDownTimer(time_num * 1000, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
////                button4.setEnabled(false);
////                button4.setText("已发送(" + millisUntilFinished / 1000 + ")");
//                EvtLog.e(millisUntilFinished + "");
//            }
//
//            @Override
//            public void onFinish() {
////                button4.setEnabled(true);
////                button4.setText("重新获取验证码");
////                jumpToActivity(FirstActivity.class);
//            }
//        }.start();


    }

    /**
     * 倒计时显示3
     */

    private static CountDownTimer countDownTimer;
    public static boolean isStartEd = false;//开始过倒计时
    public static final int time_num = 100;
    private boolean isPause = false;//暂停读秒中

    @Override
    public void finish() {
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        super.finish();
    }


    /**
     * 数字动画
     *
     * @param startNum
     * @param endNum
     * @param textView
     */
    public void setNumAnimator(int startNum, int endNum, final TextView textView) {
        ValueAnimator animator = ValueAnimator.ofInt(startNum, endNum);
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                textView.setText(StringUtil.NumC(animation.getAnimatedValue().toString()));
            }
        });
        animator.start();
    }

//    /**
//     * recycleView设置动画
//     *
//     * @param recyclerView
//     */
//    public void initAnim(RecyclerView recyclerView) {
//        //通过加载XML动画设置文件来创建一个Animation对象；
//        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.recycle_item_anim);
//        //得到一个LayoutAnimationController对象；
//        LayoutAnimationController lac = new LayoutAnimationController(animation);
//        //设置控件显示的顺序；
//        lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
//        //设置控件显示间隔时间；
//        lac.setDelay(0.3f);
//        //为ListView设置LayoutAnimationController属性；
//        recyclerView.setLayoutAnimation(lac);
//    }


    /**
     * 软键盘显示/隐藏
     */
    public void hideShowKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); //得到InputMethodManager的实例
        if (imm.isActive()) {//如果开启
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);//关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
        }
    }

    /**
     * 隐藏软键盘(只适用于Activity，不适用于Fragment)
     */
    public void hideSoftKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 隐藏软键盘(可用于Activity，Fragment)
     */
    public void hideSoftKeyboard(Context context, List<View> viewList) {
        if (viewList == null) return;
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        for (View v : viewList) {
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * EditText获取焦点并显示软键盘
     */
    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setEnabled(true);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }
    /**
     * 获取InputMethodManager，隐藏软键盘
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
