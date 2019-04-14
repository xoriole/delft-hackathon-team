package com.github.sofaid.app.ui.shares_activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.github.sofaid.app.R;
import com.github.sofaid.app.crypto.CryptoService;
import com.github.sofaid.app.helpers.PreferencesHelper;
import com.github.sofaid.app.secrets.Scheme;
import com.github.sofaid.app.ui.common.BaseActivity;
import com.github.sofaid.app.ui.dashboard.DashboardActivity;
import com.github.sofaid.app.ui.shares.ShareView;
import com.github.sofaid.app.ui.shares.ShareUtils;
import com.github.sofaid.app.ui.shares.ShareViewAdapter;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Map;

import javax.inject.Inject;

public class CreateSharesActivity extends BaseActivity implements ShareViewAdapter.ItemClickListener {

    private String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }

    ShareViewAdapter adapter;
    private static final int EMOJI_CODE = 0x1F60A;

    @Inject
    PreferencesHelper preferencesHelper;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shares);
        activityComponent().inject(this);

        ArrayList<ShareView> shares = new ArrayList<>();
        final Scheme scheme = new Scheme(new SecureRandom(), 3, 2);
        final byte[] secret = preferencesHelper.getMsk().getBytes();
        final Map<Integer, byte[]> parts = scheme.split(secret);

        int emojiCode = EMOJI_CODE+1;
        for (Map.Entry<Integer, byte[]> entry : parts.entrySet()){


            shares.add(new ShareView(ShareUtils.toHex(entry.getValue()),
                    getEmojiByUnicode(emojiCode),
                    entry.getKey(), true));
            emojiCode++;


        }

        RecyclerView recyclerView = findViewById(R.id.shares_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ShareViewAdapter(this, shares);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        Button button = findViewById(R.id.all_in);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(CreateSharesActivity.this,
                    DashboardActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onItemClick(View view, int position) {

        Intent intent = new Intent(CreateSharesActivity.this,
                ViewShareActivity.class);
        intent.putExtra(ViewShareActivity.Share, adapter.getItem(position).getShare());
        intent.putExtra(ViewShareActivity.Key, String.valueOf(adapter.getItem(position).getShareId()));
        startActivity(intent);
        adapter.getViewHolder(position).shareButton.setAlpha((float) 0.4);
        adapter.getViewHolder(position).shareButton.setText("SHARED");

    }
}
