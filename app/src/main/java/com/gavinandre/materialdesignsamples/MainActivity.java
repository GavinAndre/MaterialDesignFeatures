package com.gavinandre.materialdesignsamples;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.gavinandre.materialdesignsamples.ui.Activity.CustomBehavior2Activity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.custom_behavior1)
    TextView mCustomBehavior1;
    @BindView(R.id.custom_behavior2)
    TextView mCustomBehavior2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.custom_behavior1, R.id.custom_behavior2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.custom_behavior1:

                break;
            case R.id.custom_behavior2:
                Intent intent = new Intent(MainActivity.this, CustomBehavior2Activity.class);
                startActivity(intent);
                break;
        }
    }
}
