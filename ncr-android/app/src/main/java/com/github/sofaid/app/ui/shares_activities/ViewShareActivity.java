package com.github.sofaid.app.ui.shares_activities;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;
import android.widget.TextView;


import com.github.sofaid.app.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class ViewShareActivity extends AppCompatActivity {

    public static final String Share = "ShareView";
    public static final String Key = "Key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_share);
        // read parameters from the intent used to launch the activity.
        String code = getIntent().getStringExtra(Share);
        String shareId = getIntent().getStringExtra(Key);
        ImageView qrView = findViewById(R.id.shareCode);

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = (int) (size.x/1.6);
            int height = (int) (size.y/1.6);
            BitMatrix bitMatrix = multiFormatWriter.encode(shareId+" "+code,
                    BarcodeFormat.QR_CODE,
                    width,height);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Log.e("GenerationError", "Cannot generate qr code");
        }

        TextView share_key = findViewById(R.id.share_key);
        share_key.setText(shareId);

        TextView shareValue = findViewById(R.id.share_value);
        shareValue.setText(code);
    }

}
