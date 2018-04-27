package net.bupt.paylibrary;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.google.gson.JsonElement;

import net.bupt.paylibrary.di.modules.RequestHelper;
import net.bupt.paylibrary.utils.BuptPayUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuptPayActivity extends AppCompatActivity {

    private static final String TAG = "BuptPayActivity";

    private ImageView imageView,imageView2;
    Call<JsonElement> call;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bupt_pay);
        imageView = findViewById(R.id.show);
        imageView2 = findViewById(R.id.show2);

        call = RequestHelper.getInstance().getCoursebyId();

        final int dimension = (int) getResources().getDimension(R.dimen.image_size);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                System.out.println(" " + response.body().toString());
                Bitmap test = BuptPayUtils.encodeAsBitmap(response.body().toString(), dimension);
                imageView.setImageBitmap(test);
                imageView2.setImageBitmap(test);
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                System.out.println("onFailure  " + t.getMessage());
                Bitmap test = BuptPayUtils.encodeAsBitmap("test", dimension);
                imageView.setImageBitmap(test);
                imageView2.setImageBitmap(test);
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

}

