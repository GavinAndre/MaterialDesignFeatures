package com.gavinandre.materialdesignfeatures.custombehavior;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.gavinandre.materialdesignfeatures.R;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by zhouwei on 16/12/16.
 */

public class FloatingHeaderTitleBehavior extends CoordinatorLayout.Behavior<View> {
    private static final String TAG = FloatingHeaderTitleBehavior.class.getSimpleName();
    private final Context mContext;
    // 控件原始大小
    private int mOriginalSize;
    // 控件最终大小
    private int mFinalSize;
    // 控件最终缩放的尺寸,设置坐标值需要算上该值
    private float mScaleSize;
    // 原始x坐标
    private float mOriginalX;
    // 最终x坐标
    private float mFinalX;
    // 起始y坐标
    private float mOriginalY;
    // 最终y坐标
    private float mFinalY;
    /**
     * Title 的折叠高度
     */
    private int mTitleCollapsedHeight;
    /**
     * titile 初始化Y轴的位置
     */
    private int mTitleInitY;

    private int mMargin;

    public FloatingHeaderTitleBehavior(Context context, AttributeSet attributeSet) {
        mContext = context;
        mTitleCollapsedHeight = context.getResources().getDimensionPixelOffset(R.dimen.collapsedTitleHeight);
        mTitleInitY = context.getResources().getDimensionPixelOffset(R.dimen.title_init_y);
        mMargin = context.getResources().getDimensionPixelOffset(R.dimen.title_margin_left);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return isDependent(dependency);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {

        float progress = (dependency.getTranslationY() - getHeaderCollspateHeight()) /
                (getHeaderHeight() - getHeaderCollspateHeight());

        Log.i(TAG, "onDependentViewChanged: progress " + progress);



        /*float progress = 1.0f - Math.abs(dependency.getTranslationY() / (dependency.getHeight() - getCollapsedHeight()));

        float translationY = (mTitleInitY - mTitleCollapsedHeight) * progress;

        child.setTranslationY(translationY);

        // background change
        int startColor = mContext.getResources().getColor(R.color.init_bg_color);
        int endColor = mContext.getResources().getColor(R.color.end_bg_color);
        child.setBackgroundColor((Integer) mArgbEvaluator.evaluate(progress, endColor, startColor));
        //set margin
        int margin = (int) (mMargin * progress);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        params.setMargins(margin, 0, margin, 0);
        child.setLayoutParams(params);*/
        return true;
    }

    /*private void _initVariables(CircleImageView child, View dependency) {
        if (mAppBarHeight == 0) {
            mAppBarHeight = dependency.getHeight();
            mAppBarStartY = dependency.getY();
        }
        if (mTotalScrollRange == 0) {
            mTotalScrollRange = ((AppBarLayout) dependency).getTotalScrollRange();
        }
        if (mOriginalSize == 0) {
            mOriginalSize = child.getWidth();
        }
        if (mFinalSize == 0) {
            mFinalSize = mContext.getResources().getDimensionPixelSize(R.dimen.avatar_final_size);
        }
        if (mAppBarWidth == 0) {
            mAppBarWidth = dependency.getWidth();
        }
        if (mOriginalX == 0) {
            mOriginalX = child.getX();
        }
        if (mFinalX == 0) {
            mFinalX = mContext.getResources().getDimensionPixelSize(R.dimen.avatar_final_x);
        }
        if (mOriginalY == 0) {
            mOriginalY = child.getY();
        }
        if (mFinalY == 0) {
            if (mToolBarHeight == 0) {
                mToolBarHeight = mContext.getResources().getDimensionPixelSize(R.dimen.toolbar_height);
            }
            int statusBarHeight = mContext.getResources().getDimensionPixelSize(R.dimen.status_bar_height);
            mFinalY = (mToolBarHeight - mFinalSize) / 2 + mAppBarStartY + statusBarHeight;
        }
        if (mScaleSize == 0) {
            mScaleSize = (mOriginalSize - mFinalSize) * 1.0f / 2;
        }
        if (mFinalViewMarginBottom == 0) {
            mFinalViewMarginBottom = (mToolBarHeight - mFinalSize) / 2;
        }
    }*/

    private boolean isDependent(View dependency) {

        return dependency != null && dependency.getId() == R.id.scrollView;
    }

    private int getCollapsedHeight() {
        return mContext.getResources().getDimensionPixelOffset(R.dimen.collapsedTitleHeight);
    }

    private int getHeaderCollspateHeight() {
        return mContext.getResources().getDimensionPixelOffset(R.dimen.header_offset);
    }

    private int getHeaderHeight() {
        return mContext.getResources().getDimensionPixelOffset(R.dimen.header_height);
    }
}
