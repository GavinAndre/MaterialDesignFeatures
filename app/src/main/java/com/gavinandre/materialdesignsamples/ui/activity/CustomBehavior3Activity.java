package com.gavinandre.materialdesignsamples.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.gavinandre.materialdesignsamples.R;
import com.gavinandre.materialdesignsamples.tablayout.TabFragmentAdapter;
import com.gavinandre.materialdesignsamples.ui.fragment.MyFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gavinandre on 17-2-13.
 */

public class CustomBehavior3Activity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_behavior_3);
        //initTabLayout();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /*private void initTabLayout() {
        String[] titles = {"搞笑", "科技", "创业"};
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        List<Fragment> fragments = new ArrayList<Fragment>();
        for (int i = 0; i < titles.length; i++) {
            Fragment fragment = new MyFragment();
            Bundle bundle = new Bundle();
            bundle.putString("text", getResources().getString(R.string.large_text));
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        viewPager.setAdapter(new TabFragmentAdapter(fragments, titles, getSupportFragmentManager(), this));
        // 初始化
        TabLayout tablayout = (TabLayout) findViewById(R.id.tablayout);
        // 将ViewPager和TabLayout绑定
        tablayout.setupWithViewPager(viewPager);

    }*/
}
