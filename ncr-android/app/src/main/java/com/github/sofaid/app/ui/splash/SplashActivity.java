package com.github.sofaid.app.ui.splash;

import android.os.Bundle;

import com.github.sofaid.app.ui.common.BaseActivity;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        startProperActivity();
    }

}
