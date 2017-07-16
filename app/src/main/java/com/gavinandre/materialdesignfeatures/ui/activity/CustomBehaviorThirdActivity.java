package com.gavinandre.materialdesignfeatures.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.gavinandre.materialdesignfeatures.R;
import com.gavinandre.materialdesignfeatures.tool.UIHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gavinandre on 17-2-13.
 */

public class CustomBehaviorThirdActivity extends AppCompatActivity {
    private static final String TAG = CustomBehaviorThirdActivity.class.getSimpleName();
    @BindView(R.id.text_view)
    TextView mTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_behavior_3);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_three);
        int NavigationBarHeight = UIHelper.getNavigationBarHeight(CustomBehaviorThirdActivity.this);
        //兼容有NavigationBar的手机
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mTextView.getLayoutParams();
        params.setMargins(0, 0, 0, params.bottomMargin - NavigationBarHeight);
        mTextView.setLayoutParams(params);
    }
}
