package net.bupt.paylibrary.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import net.bupt.paylibrary.BuptPayActivity;

import java.io.UnsupportedEncodingException;

public class BuptPayUtils {

    private static final String TAG = "BuptPayUtils";

    public static void go(Context context) {
        Intent intent = new Intent(context, BuptPayActivity.class);
        context.startActivity(intent);
    }

    /**
     * 生成二维码
     *
     * @param content
     * @param newsize
     * @return
     */
    public static Bitmap encodeAsBitmap(String content, int newsize) {
        Bitmap bitmap = null;
        BitMatrix result = null;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            result = multiFormatWriter.encode(
                    new String(content.getBytes("UTF-8"), "ISO-8859-1"),
                    BarcodeFormat.QR_CODE, newsize, newsize);

            int w = result.getWidth();
            int h = result.getHeight();
            int[] pixels = new int[w * h];
            for (int y = 0; y < h; y++) {
                int offset = y * w;
                for (int x = 0; x < w; x++) {
                    pixels[offset + x] = result.get(x, y) ? Color.BLACK : Color.WHITE;
                }
            }
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, w, 0, 0, w, h);

            Log.d(TAG, "-w-" + w);
            Log.d(TAG, "-h-" + h);
            Log.d(TAG, "-width-" + bitmap.getWidth());
            Log.d(TAG, "-height-" + bitmap.getHeight());

        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
