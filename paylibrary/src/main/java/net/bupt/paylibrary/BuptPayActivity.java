package net.bupt.paylibrary;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.annotation.Nullable;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;

import net.bupt.paylibrary.di.modules.RequestHelper;
import net.bupt.paylibrary.utils.BuptPayUtils;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuptPayActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "BuptPayActivity";

    private Button zhiButton, wechatButton;
    private TextView showTextView;

    private ImageView imageView;
    Call<JsonElement> call;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bupt_pay);

        showTextView = findViewById(R.id.show);
        imageView = findViewById(R.id.image);
        zhiButton = findViewById(R.id.zhiBtn);
        wechatButton = findViewById(R.id.wechatBtn);
        zhiButton.setOnClickListener(this);
        wechatButton.setOnClickListener(this);

    }

    private void commit() {
        cancle();
        call = RequestHelper.getInstance().getData("122332", "123", "test", "0.01");
        final int dimension = (int) getResources().getDimension(R.dimen.image_size);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.code() != 200) {
                    Log.e(TAG, "code  " + response.code());
                    Toast.makeText(BuptPayActivity.this, "服务器异常，稍后重试", Toast.LENGTH_SHORT).show();
                    return;
                }
                System.out.println(" " + response.body().toString());
                Bitmap result = null;
                try {
                    JSONObject object = new JSONObject(response.body().toString());
                    if (object.getInt("status") != 0) {
                        Toast.makeText(BuptPayActivity.this, "服务器异常，稍后重试", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    result = BuptPayUtils.encodeAsBitmap(object.getString("data"), dimension);
                    if (result == null) {
                        Toast.makeText(BuptPayActivity.this, "数据有误，稍后重试", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    showTextView.setText("请使用微信扫码支付");
                    imageView.setImageBitmap(result);
                } catch (JSONException e) {
                    Toast.makeText(BuptPayActivity.this, "数据有误，稍后重试", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, e.getMessage());
                }
            }


            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                if (!call.isCanceled()) {
                    Toast.makeText(BuptPayActivity.this, "服务器异常，稍后重试", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onFailure  " + t.getMessage());
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancle();
    }

    private void cancle() {
        if (call != null) {
            call.cancel();
            call = null;
        }
    }

    @Override
    public void onClick(View view) {

        int i = view.getId();
        if (i == R.id.zhiBtn) {
            Toast.makeText(this, "支付宝正在建设中。。", Toast.LENGTH_SHORT).show();
        } else if (i == R.id.wechatBtn) {
            commit();
            Toast.makeText(this, "微信", Toast.LENGTH_SHORT).show();
        }
    }
}

