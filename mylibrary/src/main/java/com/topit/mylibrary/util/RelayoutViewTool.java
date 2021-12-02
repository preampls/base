package com.topit.mylibrary.util;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.TextView;

import java.math.BigDecimal;

/**
 * 重新布局工具
 *
 * @author {YueJinbiao}
 */
public class RelayoutViewTool {

    /**
     * 将原视图 宽高，padding，margin 按比例重新布
     *
     * @param view
     * @param scale
     */
    public static void relayoutViewWithScale(View view, float scale) {
        if (view == null) {
            return;
        }
        resetViewLayoutParams(view, scale);

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View child = ((ViewGroup) view).getChildAt(i);
                relayoutViewWithScale(child, scale);
            }
//            View[] where = null;
//            try {
//                Field field = ViewGroup.class.getDeclaredField("mChildren");
//                field.setAccessible(true);
//                where = (View[]) field.get(view);
//            } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
//                e.printStackTrace();
//            }
//            if (where != null) {
//                for (View child : where) {
//                    relayoutViewWithScale(child, scale);
//
//                }
//            }
        }
    }

    private static final int AnimatorTime = 1300;
    private static final float values[] = {0.5f, 1.0f};


    //add animator
    private static void addAnimator(View view) {



        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(

                ObjectAnimator.ofFloat(view, "scaleX", values) // 缩放
                        .setDuration(AnimatorTime),
                ObjectAnimator.ofFloat(view, "scaleY", values) // 缩放
                        .setDuration(AnimatorTime),
//                ObjectAnimator.ofFloat(view, "rotation",0, 360)
//                        .setDuration(AnimatorTime),
                ObjectAnimator.ofFloat(view, "alpha", values)// 透明度动画
                        .setDuration(AnimatorTime)
        );
        animatorSet.setStartDelay(500);
        animatorSet.start();

    }

    private void iniAnimation() {
        // 透明度动画
//        ObjectAnimator.ofFloat(mAlphaImage, "alpha", 1, 0, 1)
//                .setDuration(4000)
//                .start();

//        // 缩放
//        final AnimatorSet animatorSet = new AnimatorSet();
//        mScaleImage.setPivotX(mScaleImage.getWidth()+250);
//        mScaleImage.setPivotY(mScaleImage.getHeight()+250);
//        animatorSet.playTogether(
//                ObjectAnimator.ofFloat(mScaleImage, "scaleX", 1, 0)
//                        .setDuration(2000),
//                ObjectAnimator.ofFloat(mScaleImage, "scaleY", 1, 0)
//                        .setDuration(2000)
//        );
//        animatorSet.start();
//
//        // 平移 translation
//        final AnimatorSet translationAnimatorSet = new AnimatorSet();
//        translationAnimatorSet.playTogether(
//                ObjectAnimator.ofFloat(mTranslationImage, "translationX", 20, 100)
//                        .setDuration(2000),
//                ObjectAnimator.ofFloat(mTranslationImage, "translationY", 20,100)
//                        .setDuration(2000)
//        );
//        translationAnimatorSet.start();
//
//        // 利用 ObjectAnimator 实现旋转动画
//        final AnimatorSet rotateAnimationSet = new AnimatorSet();
//        rotateAnimationSet.playTogether(
//                ObjectAnimator.ofFloat(mRotationImage, "rotation",0, 360)
//                        .setDuration(2000)
//        );
//        rotateAnimationSet.start();
    }

    private static void resetViewLayoutParams(View view, float scale) {
        if (view instanceof TextView) {
            resetTextSize((TextView) view, scale);
        }
        int pLeft = convertFloatToInt(view.getPaddingLeft() * scale);
        int pTop = convertFloatToInt(view.getPaddingTop() * scale);
        int pRight = convertFloatToInt(view.getPaddingRight() * scale);
        int pBottom = convertFloatToInt(view.getPaddingBottom() * scale);
        view.setPadding(pLeft, pTop, pRight, pBottom);
        LayoutParams params = view.getLayoutParams();
        if ("test_relayout".equals(view.getTag())) {
            EvtLog.i("relayout", "test_relayout/" + params);
        }
        if (params == null) {
            return;
        }
        if (params.width > 0) {
            params.width = convertFloatToInt(params.width * scale);
        }
        if (params.height > 0) {
            params.height = convertFloatToInt(params.height * scale);
        }

        if (params instanceof MarginLayoutParams) {
            MarginLayoutParams mParams = (MarginLayoutParams) params;
            if (mParams.leftMargin > 0) {
                mParams.leftMargin = convertFloatToInt(mParams.leftMargin * scale);
            }
            if (mParams.rightMargin > 0) {
                mParams.rightMargin = convertFloatToInt(mParams.rightMargin * scale);
            }
            if (mParams.topMargin > 0) {
                mParams.topMargin = convertFloatToInt(mParams.topMargin * scale);
            }
            if (mParams.bottomMargin > 0) {
                mParams.bottomMargin = convertFloatToInt(mParams.bottomMargin * scale);
            }
        }
//        addAnimator(view);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private static void resetTextSize(TextView textView, float scale) {
        float size = textView.getTextSize(); // lineSpacingExtra
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size * scale);
        float spacingExtra = 0;
        try {
            spacingExtra = textView.getLineSpacingExtra();
        } catch (NoSuchMethodError e) {

        }
        textView.setLineSpacing(spacingExtra * scale, 1f);
    }

    // float 转换int 小数四舍五入
    public static int convertFloatToInt(float sourceNum) {
        BigDecimal bigDecimal = new BigDecimal(sourceNum);
        return bigDecimal.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
    }

    // double 转换int 小数四舍五入
    public static int convertFloatToInt(double sourceNum) {
        BigDecimal bigDecimal = new BigDecimal(sourceNum);
        return bigDecimal.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
    }
}
