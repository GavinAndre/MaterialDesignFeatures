package com.gavinandre.materialdesignfeatures.custombehavior;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.OverScroller;

import com.gavinandre.materialdesignfeatures.R;

import java.lang.ref.WeakReference;

/**
 * Created by gavinandre on 17-2-13.
 */
public class ToolBarScrollBehavior extends CoordinatorLayout.Behavior<View> {

    private static final String TAG = ToolBarScrollBehavior.class.getSimpleName();

    private WeakReference<View> mDependencyView;

    private WeakReference<TabLayout> mTabLayout;

    private OverScroller mOverScroller;

    private Handler mHandler;

    private boolean isScrolling = false;

    private Context mContext;

    private ArgbEvaluator evaluator;

    public ToolBarScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mOverScroller = new OverScroller(context);
        mHandler = new Handler();
        evaluator = new ArgbEvaluator();
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        if (dependency != null && dependency.getId() == R.id.toolbar) {
            mDependencyView = new WeakReference<>(dependency);
            mTabLayout = new WeakReference<>((TabLayout) ((LinearLayout) child).getChildAt(0));
            return true;
        }
        return false;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        final float progress = Math.abs(dependency.getTranslationY() / (dependency.getHeight()));

        child.setTranslationY(dependency.getHeight() + dependency.getTranslationY());

        final int colorPrimary = getColor(R.color.colorPrimary);
        final int evaluate1 = (Integer) evaluator.evaluate(progress, Color.WHITE, colorPrimary);
        final int evaluate2 = (Integer) evaluator.evaluate(progress, colorPrimary, Color.WHITE);

        getTabLayoutView().setBackgroundColor(evaluate1);
        getTabLayoutView().setTabTextColors(evaluate2, evaluate2);
        getTabLayoutView().setSelectedTabIndicatorColor(evaluate2);

        return true;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        if (params != null && params.height == CoordinatorLayout.LayoutParams.MATCH_PARENT) {
            child.layout(0, 0, parent.getWidth(), parent.getHeight());
            return true;
        }
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(CoordinatorLayout coordinatorLayout, View child,
                                       View directTargetChild, View target, int nestedScrollAxes) {
        isScrolling = false;
        mOverScroller.abortAnimation();
        super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        // 在这个方法里面只处理向上滑动
        if (dy < 0) {
            return;
        }
        View dependencyView = getDependencyView();
        float transY = dependencyView.getTranslationY() - dy;
        if (transY < 0 && -transY < getToolbarSpreadHeight()) {
            dependencyView.setTranslationY(transY);
            consumed[1] = dy;
        }
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        // 在这个方法里只处理向下滑动
        if (dyUnconsumed > 0) {
            return;
        }
        View dependencyView = getDependencyView();
        float transY = dependencyView.getTranslationY() - dyUnconsumed;
        if (transY < 0) {
            dependencyView.setTranslationY(transY);
        }
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, View child, View target,
                                    float velocityX, float velocityY) {
        return onUserStopDragging(velocityY);
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target) {
        if (!isScrolling) {
            onUserStopDragging(800);
        }
    }

    private boolean onUserStopDragging(float velocity) {
        View dependentView = getDependencyView();
        float translateY = dependentView.getTranslationY();
        float minHeaderTranslate = -(dependentView.getY() + getToolbarSpreadHeight());
        Log.e(TAG, "onUserStopDragging: translateY " + translateY);
        Log.i(TAG, "onUserStopDragging: dependentView.getY() " + dependentView.getY());
        Log.i(TAG, "onUserStopDragging: getToolbarSpreadHeight() " + getToolbarSpreadHeight());
        Log.i(TAG, "onUserStopDragging: minHeaderTranslate" + minHeaderTranslate);
        Log.i(TAG, "onUserStopDragging: translateY - minHeaderTranslate" + (translateY - minHeaderTranslate));

        if (translateY == 0 || translateY == -getToolbarSpreadHeight()) {
            return false;
        }
        boolean targetState; // Flag indicates whether to expand the content.
        if (Math.abs(velocity) <= 800) {
            if (Math.abs(translateY) < Math.abs(translateY - minHeaderTranslate)) {
                targetState = false;
            } else {
                targetState = true;
            }
            velocity = 800; // Limit velocity's minimum value.
        } else {
            if (velocity > 0) {
                targetState = true;
            } else {
                targetState = false;
            }
        }

        float targetTranslateY = targetState ? minHeaderTranslate : -dependentView.getY();
        mOverScroller.startScroll(0, (int) translateY, 0, (int) (targetTranslateY), (int) (1000000 / Math.abs(velocity)));
        mHandler.post(flingRunnable);
        isScrolling = true;

        return true;
    }


    private Runnable flingRunnable = new Runnable() {
        @Override
        public void run() {
            if (mOverScroller.computeScrollOffset()) {
                getDependencyView().setTranslationY(mOverScroller.getCurrY());
                mHandler.post(this);
            } else {
                isScrolling = false;
            }
        }
    };

    private int getToolbarSpreadHeight() {
        return mContext.getResources().getDimensionPixelOffset(R.dimen.actionbar_size_offset);
    }

    private View getDependencyView() {
        return mDependencyView.get();
    }

    private TabLayout getTabLayoutView() {
        return mTabLayout.get();
    }

    private int getColor(@ColorRes int id) {
        return ContextCompat.getColor(mContext, id);
    }

}
