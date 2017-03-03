package com.gavinandre.materialdesignfeatures.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gavinandre.materialdesignfeatures.R;
import com.gavinandre.materialdesignfeatures.tool.UIHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gavinandre on 17-2-13.
 */

public class CustomBehaviorThirdActivity extends AppCompatActivity {
    private static final String TAG = CustomBehaviorThirdActivity.class.getSimpleName();
    @BindView(R.id.text_view)
    TextView mTextView;
    private RecyclerView mRecyclerView;

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

        mRecyclerView = (RecyclerView) findViewById(R.id.scrollView);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        MyAdapter adapter = new MyAdapter();
        mRecyclerView.setAdapter(adapter);
        adapter.setData(mockData());
        adapter.notifyDataSetChanged();

    }

    private List<String> mockData(){
        List<String> data = new ArrayList<>();
        for (int i=0;i<100;i++){
            data.add("item:"+i);
        }
        return data;
    }

    public static class MyAdapter extends RecyclerView.Adapter{
        private List<String> mData;

        public void setData(List<String> data) {
            mData = data;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,null));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((MyViewHolder)holder).mTextView.setText(mData.get(position));
        }

        @Override
        public int getItemCount() {
            return mData == null ? 0:mData.size();
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView mTextView;
        public MyViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.item_content_text);
        }
    }

}
