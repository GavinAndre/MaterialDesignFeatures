package com.gavinandre.materialdesignfeatures;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.gavinandre.materialdesignfeatures.ui.activity.CustomBehavior1Activity;
import com.gavinandre.materialdesignfeatures.ui.activity.CustomBehavior2Activity;
import com.gavinandre.materialdesignfeatures.ui.activity.CustomBehavior3Activity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.custom_behavior1)
    TextView mCustomBehavior1;
    @BindView(R.id.custom_behavior2)
    TextView mCustomBehavior2;
    @BindView(R.id.custom_behavior3)
    TextView mCustomBehavior3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.custom_behavior1, R.id.custom_behavior2, R.id.custom_behavior3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.custom_behavior1:
                startActivity(new Intent(MainActivity.this, CustomBehavior1Activity.class));
                break;
            case R.id.custom_behavior2:
                startActivity(new Intent(MainActivity.this, CustomBehavior2Activity.class));
                break;
            case R.id.custom_behavior3:
                startActivity(new Intent(MainActivity.this, CustomBehavior3Activity.class));
                break;
        }
    }
}
