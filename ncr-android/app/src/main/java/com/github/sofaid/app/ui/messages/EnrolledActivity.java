package com.github.sofaid.app.ui.messages;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.sofaid.app.R;
import com.github.sofaid.app.ui.dashboard.DashboardActivity;


public class EnrolledActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrolled);

        View fab = findViewById(R.id.next_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EnrolledActivity.this,
                        DashboardActivity.class);
                startActivity(intent);
            }
        });


    }
}
