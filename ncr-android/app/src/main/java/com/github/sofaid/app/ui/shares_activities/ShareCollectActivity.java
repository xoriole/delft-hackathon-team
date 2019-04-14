package com.github.sofaid.app.ui.shares_activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.github.sofaid.app.R;
import com.github.sofaid.app.ethereum.ContractService;
import com.github.sofaid.app.helpers.PreferencesHelper;
import com.github.sofaid.app.secrets.Scheme;
import com.github.sofaid.app.ui.barcode.BarcodeCaptureActivity;
import com.github.sofaid.app.ui.common.BaseActivity;
import com.github.sofaid.app.ui.messages.EnrolledActivity;
import com.github.sofaid.app.ui.shares.ShareView;
import com.github.sofaid.app.ui.shares.ShareUtils;
import com.github.sofaid.app.ui.shares.ShareViewAdapter;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import jnr.ffi.annotations.In;

public class ShareCollectActivity extends BaseActivity implements ShareViewAdapter.ItemClickListener {


    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final int EMOJI_CODE = 0x1F60A;

    private String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }

    ShareViewAdapter adapter;
    Map<Integer, byte[]> share_parts;
    Scheme scheme;
    ConstraintLayout coordinatorLayout;

    @Inject
    PreferencesHelper preferencesHelper;
    @Inject
    ContractService contractService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_shares);
        activityComponent().inject(this);
        coordinatorLayout = findViewById(R.id.relativeLayout3);

        share_parts = new HashMap<>();
        scheme = new Scheme(new SecureRandom("42".getBytes()), 3, 2);

        ArrayList<ShareView> shares = new ArrayList<>();


        shares.add(new ShareView("", getEmojiByUnicode(EMOJI_CODE),
                    1, true));
        shares.add(new ShareView("", getEmojiByUnicode(EMOJI_CODE),
                2, true));

        RecyclerView recyclerView = findViewById(R.id.shares_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ShareViewAdapter(this, shares);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {

        Intent intent = new Intent(ShareCollectActivity.this,
                BarcodeCaptureActivity.class);
        intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
        intent.putExtra(BarcodeCaptureActivity.UseFlash, false);
        startActivityForResult(intent, RC_BARCODE_CAPTURE);

        adapter.getViewHolder(position).shareButton.setAlpha((float) 0.4);
        adapter.getViewHolder(position).shareButton.setText("Recovered");
        adapter.getViewHolder(position).idView.setText("ShareView ");
        adapter.getViewHolder(position).emojiView.setText(getEmojiByUnicode(EMOJI_CODE+position));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    String[] values = barcode.displayValue.split(" ");
                    int position = Integer.parseInt(values[0]);
                    Log.e("", "Size of part "+values[1]);
                    Log.e("", "Size of part "+ ShareUtils.fromHex(values[1]).length);
                    share_parts.put(position, ShareUtils.fromHex(values[1]));

                    if (share_parts.size() > 1){

                        String recovered_secret = new String(scheme.join(share_parts));
                        preferencesHelper.saveMsk(recovered_secret);
                        Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, "Secret: "+recovered_secret,
                                        Snackbar.LENGTH_LONG);
                        snackbar.show();
                        Button button = findViewById(R.id.recover);
                        button.setVisibility(View.VISIBLE);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ShareCollectActivity.this,
                                        EnrolledActivity.class);

                                startActivity(intent);
                            }
                        });
                    }
                } else {
                    Log.d("", "No barcode captured, intent data is null");
                }
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
