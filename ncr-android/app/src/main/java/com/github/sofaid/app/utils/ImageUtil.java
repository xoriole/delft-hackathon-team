package com.github.sofaid.app.utils;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.EnumMap;
import java.util.Map;

public class ImageUtil {


    // generate qr encoded image from string and return bitmap
    public static Bitmap qrCodeLarge(String string) {
        return qrCode(string, 512, true, false);
    }

    public static Bitmap qrCodeLargeBlack(String string) {
        return qrCode(string, 512, true, false);
    }

    public static Bitmap qrCodeMedium(String string) {
        return qrCode(string, 256, true, true);
    }

    public static Bitmap qrCodeSmall(String string) {
        return qrCode(string, 128, true, true);
    }

    public static Bitmap qrCodeExtraSmall(String string) {
        return qrCode(string, 64, true, true);
    }

    public static Bitmap qrCode(String string, int size, boolean withPadding, boolean isColored) {
        try {

            int frontColor = Color.BLACK;

            if (isColored) {
                int hash = string.hashCode();
                int r = (hash & 0xFF0000) >> 16;
                int g = (hash & 0x00FF00) >> 8;
                int b = hash & 0x0000FF;

                frontColor = Color.rgb(r, g, b);
            }
            int padding = 0;
            if (withPadding) {
                padding = 1;
            }

            //Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();

            Map<EncodeHintType, Object> hintMap = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H); // H = 30% damage
            hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            // adds extra padding to border of QR Code if required else add no White padding to QR Code
            hintMap.put(EncodeHintType.MARGIN, padding);

            QRCodeWriter qrCodeWriter = new QRCodeWriter();

            BitMatrix bitMatrix = qrCodeWriter.encode(string, BarcodeFormat.QR_CODE, size, size, hintMap);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = bitMatrix.get(x, y) ? frontColor : Color.WHITE;
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, size, 0, 0, width, height);
            return bitmap;
        } catch (Exception exception) {
            return null;
        }
    }

}