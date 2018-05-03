package net.bupt.paylibrary;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonElement;

import net.bupt.paylibrary.di.modules.RequestHelper;
import net.bupt.paylibrary.utils.BuptPayUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuptPayActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "BuptPayActivity";

    private Button zhiButton, wechatButton;

    private ImageView imageView;
    Call<JsonElement> call;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bupt_pay);

        imageView = findViewById(R.id.image);
        zhiButton = findViewById(R.id.zhiBtn);
        wechatButton = findViewById(R.id.wechatBtn);
        zhiButton.setOnClickListener(this);
        wechatButton.setOnClickListener(this);

        call = RequestHelper.getInstance().getCoursebyId();

        final int dimension = (int) getResources().getDimension(R.dimen.image_size);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                System.out.println(" " + response.body().toString());
                Bitmap test = BuptPayUtils.encodeAsBitmap(response.body().toString(), dimension);
                imageView.setImageBitmap(test);
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                System.out.println("onFailure  " + t.getMessage());
                Bitmap test = BuptPayUtils.encodeAsBitmap("test", dimension);
                imageView.setImageBitmap(test);
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (call != null && call.isExecuted()) {
            call.cancel();
        }
    }

    @Override
    public void onClick(View view) {

        int i = view.getId();
        if (i == R.id.zhiBtn) {
            Toast.makeText(this, "支付宝", Toast.LENGTH_SHORT).show();
        } else if (i == R.id.wechatBtn) {
            Toast.makeText(this, "微信", Toast.LENGTH_SHORT).show();
        }
    }
}

