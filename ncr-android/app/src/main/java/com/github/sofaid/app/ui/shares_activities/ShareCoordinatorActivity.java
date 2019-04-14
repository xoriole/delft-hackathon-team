package com.github.sofaid.app.ui.shares_activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.sofaid.app.R;
import com.github.sofaid.app.models.db.Share;
import com.github.sofaid.app.secrets.Scheme;
import com.github.sofaid.app.services.core.AccountService;
import com.github.sofaid.app.ui.barcode.BarcodeCaptureActivity;
import com.github.sofaid.app.ui.common.BaseActivity;
import com.github.sofaid.app.ui.shares.ShareView;
import com.github.sofaid.app.ui.shares.ShareViewAdapter;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class ShareCoordinatorActivity extends BaseActivity implements ShareViewAdapter.ItemClickListener {

    private static final int RC_BARCODE_CAPTURE = 9001;

    private String getEmojiByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }

    ShareViewAdapter adapter;
    private static final int EMOJI_CODE = 0x1F60A;

    Map<Integer, byte[]> share_parts;
    Scheme scheme;
    ConstraintLayout coordinatorLayout;
    @Inject
    AccountService accountService;


    private void updateShareLayout(){
        List<Share> shareList = accountService.getAllShares();
        RecyclerView recyclerView = findViewById(R.id.shares_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ShareViewAdapter(this, ShareView.convertFromShareList(shareList));
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_coordinator);
        activityComponent().inject(this);

        updateShareLayout();

        Button createSharesButton = findViewById(R.id.create_shares);
        createSharesButton.setOnClickListener(view -> {
            Intent intent = new Intent(ShareCoordinatorActivity.this,
                    CreateSharesActivity.class);
            startActivity(intent);
        });
        Button button = findViewById(R.id.scan_share);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(ShareCoordinatorActivity.this,
                    BarcodeCaptureActivity.class);
            intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
            intent.putExtra(BarcodeCaptureActivity.UseFlash, false);
            startActivityForResult(intent, RC_BARCODE_CAPTURE);
        });
    }


    public void inputUserName(Share share) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Put the user name for this share");

        // Set up the input
        final EditText input = new EditText(this);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                share.setUserName(input.getText().toString());
                accountService.saveShare(share);
                updateShareLayout();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    String[] values = barcode.displayValue.split(" ");
                    int position = Integer.parseInt(values[0]);

                    Share share = new Share();
                    share.setShareId(position);
                    share.setShare(values[1]);
                    inputUserName(share);
                } else {
                    Log.d("", "No barcode captured, intent data is null");
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onItemClick(View view, int position) {

        Intent intent = new Intent(ShareCoordinatorActivity.this,
                ViewShareActivity.class);
        intent.putExtra(ViewShareActivity.Share, adapter.getItem(position).getShare());
        intent.putExtra(ViewShareActivity.Key, String.valueOf(adapter.getItem(position).getShareId()));
        startActivity(intent);
    }


}
