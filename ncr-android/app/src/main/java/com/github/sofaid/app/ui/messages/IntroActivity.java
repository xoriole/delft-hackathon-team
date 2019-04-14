package com.github.sofaid.app.ui.messages;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.github.sofaid.app.R;
import com.github.sofaid.app.ui.dashboard.DashboardActivity;
import com.github.sofaid.app.ui.shares_activities.CreateSharesActivity;
import com.github.sofaid.app.ui.shares_activities.ShareCollectActivity;


public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Button fab = findViewById(R.id.enroll);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(IntroActivity.this,
                    DashboardActivity.class);
            startActivity(intent);
        });

        Button recover = findViewById(R.id.recover);
        recover.setOnClickListener(view -> {
            Intent intent = new Intent(IntroActivity.this,
                    ShareCollectActivity.class);
            startActivity(intent);
        });


    }
}
