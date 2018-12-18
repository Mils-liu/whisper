package com.mils.whisper.article.collectionlist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.mils.whisper.R;
import com.mils.whisper.base.BaseActivity;
import com.mils.whisper.config.LayoutConfig;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/6/3.
 */

public class CollectionListActivity extends BaseActivity {

    @BindView(R.id.view_statusBar_am)
    public View statusBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articlelist_mycollection);
        setSteepStatusBar(true);
    }

    @Override
    public void initView() {
        initStatusBar(statusBar, LayoutConfig.RELATIVE_LAYOUT);
    }

    @Override
    public void initData() {

    }
}
