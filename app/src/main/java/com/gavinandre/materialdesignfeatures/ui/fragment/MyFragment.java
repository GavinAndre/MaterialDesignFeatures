package com.gavinandre.materialdesignfeatures.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by gavinandre on 17-2-13.
 */

public class MyFragment extends Fragment {
    private String mText;

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if(getArguments()!=null){
            mText = getArguments().getString("text");
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        NestedScrollView scrollableView = new NestedScrollView(getActivity());
        TextView textView = new TextView(getActivity());
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        textView.setText(mText);
        scrollableView.addView(textView);
        return scrollableView;
    }
}
