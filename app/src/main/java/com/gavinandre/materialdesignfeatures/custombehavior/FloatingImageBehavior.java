package com.gavinandre.materialdesignfeatures.custombehavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.gavinandre.materialdesignfeatures.R;


/**
 * Created by gavinandre on 17-3-3.
 */
public class FloatingImageBehavior extends CoordinatorLayout.Behavior<View> {
    private static final String TAG = FloatingImageBehavior.class.getSimpleName();
    // 缩放动画变化的支点
    private static final float ANIM_CHANGE_POINT = 0.2f;
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
    // 依赖视图起始y坐标
    private float mDependencyOriginalY;
    // 依赖视图最终y坐标
    private float mDependencyFinalY;
    // 起始y坐标
    private float mOriginalY;
    // 最终y坐标
    private float mFinalY;
    // Y轴移动插值器
    private DecelerateInterpolator mMoveYInterpolator;
    // X轴移动插值器
    private AccelerateInterpolator mMoveXInterpolator;

    public FloatingImageBehavior(Context context, AttributeSet attributeSet) {
        mMoveXInterpolator = new AccelerateInterpolator();
        mMoveYInterpolator = new DecelerateInterpolator();
        initValue(context);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return isDependent(dependency);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {

        initChildValue(child);

        float progress = 1 - (dependency.getTranslationY() - mDependencyFinalY) /
                (mDependencyOriginalY - mDependencyFinalY);

        float percentY = mMoveYInterpolator.getInterpolation(progress);
        setViewY(child, mOriginalY, mFinalY - mScaleSize, percentY);
        if (progress > ANIM_CHANGE_POINT) {
            float scalePercent = (progress - ANIM_CHANGE_POINT) / (1 - ANIM_CHANGE_POINT);
            float percentX = mMoveXInterpolator.getInterpolation(scalePercent);
            scaleView(child, mOriginalSize, mFinalSize, scalePercent);
            setViewX(child, mOriginalX, mFinalX - mScaleSize, percentX);
        } else {
            scaleView(child, mOriginalSize, mFinalSize, 0);
            setViewX(child, mOriginalX, mFinalX - mScaleSize, 0);
        }
        return true;
    }

    private void initChildValue(View child) {
        if (mOriginalSize == 0) {
            mOriginalSize = child.getWidth();
        }
        if (mOriginalX == 0) {
            mOriginalX = child.getX();
        }
        if (mOriginalY == 0) {
            mOriginalY = child.getY();
        }
        if (mScaleSize == 0) {
            mScaleSize = (mOriginalSize - mFinalSize) * 1.0f / 2;
        }
    }

    private void initValue(Context mContext) {
        mDependencyOriginalY = mContext.getResources().getDimensionPixelOffset(R.dimen.content_height);
        mDependencyFinalY = mContext.getResources().getDimensionPixelOffset(R.dimen.content_offset);
        mFinalY = mContext.getResources().getDimensionPixelSize(R.dimen.floating_image_final_y);
        mFinalX = mContext.getResources().getDimensionPixelSize(R.dimen.floating_image_final_x);
        mFinalSize = mContext.getResources().getDimensionPixelSize(R.dimen.floating_image_final_size);
    }

    private boolean isDependent(View dependency) {
        return dependency != null && dependency.getId() == R.id.scrollView;
    }

    private void setViewX(View view, float originalX, float finalX, float percent) {
        float calcX = (finalX - originalX) * percent + originalX;
        view.setX(calcX);
    }

    private void setViewY(View view, float originalY, float finalY, float percent) {
        float calcY = (finalY - originalY) * percent + originalY;
        view.setY(calcY);
    }

    private void scaleView(View view, float originalSize, float finalSize, float percent) {
        float calcSize = (finalSize - originalSize) * percent + originalSize;
        float caleScale = calcSize / originalSize;
        view.setScaleX(caleScale);
        view.setScaleY(caleScale);
    }
}
