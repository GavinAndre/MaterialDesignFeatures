package com.gavinandre.materialdesignfeatures.custombehavior;

import android.content.Context;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.OverScroller;

import com.gavinandre.materialdesignfeatures.R;

import java.lang.ref.WeakReference;

/**
 * Created by gavinandre on 17-3-3.
 */

public class ContentScrollBehavior extends CoordinatorLayout.Behavior<View> {
    private static final String TAG = ContentScrollBehavior.class.getSimpleName();
    private WeakReference<View> mChildView;
    private OverScroller mOverScroller;
    private Handler mHandler;
    // 依赖视图起始y坐标
    private float mDependencyOriginalY;
    // 依赖视图最终y坐标
    private float mDependencyFinalY;
    private boolean isScrolling = false;

    public ContentScrollBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mOverScroller = new OverScroller(context);
        mHandler = new Handler();
        mDependencyOriginalY = context.getResources().getDimensionPixelOffset(R.dimen.content_height);
        mDependencyFinalY = context.getResources().getDimensionPixelOffset(R.dimen.content_offset);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        if (params != null && params.height == CoordinatorLayout.LayoutParams.MATCH_PARENT) {
            child.layout(0, 0, parent.getWidth(), parent.getHeight());
            child.setTranslationY(mDependencyOriginalY);
            mChildView = new WeakReference<>(child);
            return true;
        }

        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        isScrolling = false;
        mOverScroller.abortAnimation();
        super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View get, int dx, int dy, int[] consumed) {
        Log.e(TAG, "--->invoke onNestedPreScroll");
        Log.i(TAG, "dy---->" + dy);
        //上滑操作dy值大于0,因此小于0直接分发给onNestedScroll
        if (dy < 0) {
            return;
        }

        //上滑后的位置不小于mDependencyFinalY(100dp)
        float transY = child.getTranslationY() - dy;
        Log.i(TAG, "transY:" + transY + "++++child.getTranslationY():" + child.getTranslationY() + "---->dy:" + dy);
        if (transY > 0 && transY >= mDependencyFinalY) {
            child.setTranslationY(transY);
            consumed[1] = dy;
        }
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.e(TAG, "++++invoke onNestedScroll");
        Log.i(TAG, "dyUnconsumed:" + dyUnconsumed);
        //上滑到底部并越界后dyUnconsumed值大于0直接忽略
        if (dyUnconsumed > 0) {
            return;
        }

        //下滑后的位置不大于mDependencyOriginalY(250dp)
        float transY = child.getTranslationY() - dyUnconsumed;
        Log.i(TAG, "------>transY:" + transY + "****** child.getTranslationY():" + child.getTranslationY() + "--->dyUnconsumed" + dxUnconsumed);
        if (transY > 0 && transY <= mDependencyOriginalY) {
            child.setTranslationY(transY);
        }
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY) {
        return onUserStopDragging(velocityY, child);
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target) {
        if (!isScrolling) {
            onUserStopDragging(800, child);
        }
    }

    private boolean onUserStopDragging(float velocity, View child) {
        float translateY = child.getTranslationY();
        float minHeaderTranslate = mDependencyFinalY;
        float maxHeaderTranslate = mDependencyOriginalY;
        float midHeaderTranslate = maxHeaderTranslate - minHeaderTranslate;
        //中间态对比参数
        float y = translateY - maxHeaderTranslate;
        float ym = y + midHeaderTranslate;

        if (translateY == mDependencyFinalY || translateY == mDependencyOriginalY) {
            return false;
        }

        //Log.e(TAG, "onUserStopDragging: translateY " + translateY);
        //Log.i(TAG, "onUserStopDragging: minHeaderTranslate " + minHeaderTranslate);
        //Log.i(TAG, "onUserStopDragging: maxHeaderTranslate " + maxHeaderTranslate);
        //Log.i(TAG, "onUserStopDragging: midHeaderTranslate " + midHeaderTranslate);
        //Log.i(TAG, "onUserStopDragging: maxHeaderTranslate - translateY " + (maxHeaderTranslate - translateY));
        //Log.i(TAG, "onUserStopDragging: y " + y);
        //Log.i(TAG, "onUserStopDragging: ym " + ym);
        //Log.i(TAG, "onUserStopDragging: translateY == minHeaderTranslate " + (translateY == minHeaderTranslate));


        //在这里计算有没有超过中间态
        boolean targetState; // Flag indicates whether to expand the content.
        if (Math.abs(velocity) <= 800) {
            //y范围(-450~0) ym范围(0~450),取绝对值比大小来判断滑动距离有没有超过1/2
            targetState = Math.abs(y) >= Math.abs(ym);
            velocity = 800; // Limit velocity's minimum value.
        } else {
            targetState = velocity > 0;
        }

        //根据targetState判断,超过中间态自动滑动剩余距离,没有则回到原处
        float targetTranslateY = targetState ? minHeaderTranslate : maxHeaderTranslate;

        //Log.e(TAG, "onUserStopDragging: targetState " + targetState);
        //Log.i(TAG, "onUserStopDragging: targetTranslateY " + targetTranslateY);
        //Log.i(TAG, "onUserStopDragging: minHeaderTranslate " + minHeaderTranslate);
        //Log.i(TAG, "onUserStopDragging: maxHeaderTranslate " + maxHeaderTranslate);
        //Log.i(TAG, "onUserStopDragging: translateY " + translateY);
        //Log.i(TAG, "onUserStopDragging: targetTranslateY - translateY " + (targetTranslateY - translateY));

        //根据targetTranslateY的值来减去translateY来计算dy
        mOverScroller.startScroll(0, (int) translateY, 0, (int) (targetTranslateY - translateY), (int) (1000000 / Math.abs(velocity)));
        mHandler.post(flingRunnable);
        isScrolling = true;

        return true;
    }

    private View getChildView() {
        return mChildView.get();
    }

    private Runnable flingRunnable = new Runnable() {
        @Override
        public void run() {
            if (mOverScroller.computeScrollOffset()) {
                getChildView().setTranslationY(mOverScroller.getCurrY());
                mHandler.post(this);
            } else {
                isScrolling = false;
            }
        }
    };
}
