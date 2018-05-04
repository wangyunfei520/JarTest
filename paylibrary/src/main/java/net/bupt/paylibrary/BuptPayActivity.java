package net.bupt.paylibrary;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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

import net.bupt.paylibrary.center.PayCenter;
import net.bupt.paylibrary.utils.BuptPayUtils;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;

public class BuptPayActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "BuptPayActivity";

    private Button zhiButton, wechatButton;
    private TextView showTextView;
    private ImageView imageView;

    private Handler handler;
    private Runnable validateRunnable = null;

    Call<JsonElement> call;

    Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bupt_pay);
        handler = new Handler();

        showTextView = findViewById(R.id.show);
        imageView = findViewById(R.id.image);
        zhiButton = findViewById(R.id.zhiBtn);
        wechatButton = findViewById(R.id.wechatBtn);
        setDrawable(zhiButton, R.drawable.alipay);
        setDrawable(wechatButton, R.drawable.wechat);
        zhiButton.setOnClickListener(this);
        wechatButton.setOnClickListener(this);

    }

    private void setDrawable(Button btn, int id) {
        Drawable drawable = getResources().getDrawable(id);
        drawable.setBounds(-5, 0, 40, 40);
        btn.setCompoundDrawables(drawable, null, null, null);
    }

    private void getData() {
        cancle();
        dialog = BuptPayUtils.createLoadingDialog(this, "正在请求数据");
        dialog.show();
        final int dimension = (int) getResources().getDimension(R.dimen.image_size);
        call = PayCenter.getInstance().getData("122332", "123", "test",
                "0.01", new CallBackResponseContent() {
                    @Override
                    public void getResponseContent(String data) {
                        dialog.dismiss();
                        Bitmap result = null;
                        try {
                            final JSONObject object = new JSONObject(data);
                            if (object.getInt("status") != 0) {
                                service_error();
                                return;
                            }
                            JSONObject objectJSONObject = object.getJSONObject("data");
                            result = BuptPayUtils.encodeAsBitmap(objectJSONObject.getString("code_url"), dimension);
                            if (result == null) {
                                data_error();
                                return;
                            }
                            showTextView.setText("请使用微信扫码支付");
                            imageView.setImageBitmap(result);
                            String sn = objectJSONObject.getString("sn");
                            validate(sn);
                        } catch (JSONException e) {
                            data_error();
                            Log.e(TAG, e.getMessage());
                        }
                    }

                    @Override
                    public void getFailContent(String result) {
                        dialog.dismiss();
                        service_error();
                        Log.e(TAG, "onFailure  " + result);
                    }
                });
    }

    private void validate(final String sn) {
        cancleValide();
        validateRunnable = new Runnable() {
            @Override
            public void run() {
                call = PayCenter.getInstance().validate(sn, new CallBackResponseContent() {
                    @Override
                    public void getResponseContent(String result) {
                        try {
                            JSONObject object = new JSONObject(result);
                            int status = object.getInt("status");
                            if (status != 0) {
                                imageView.setVisibility(View.INVISIBLE);
                                showTextView.setText("支付成功，3秒后返回");
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(3000);
                                            finish();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            } else {
                                handler.postDelayed(validateRunnable, 10 * 1000);
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, e.getMessage());
                            handler.postDelayed(validateRunnable, 10 * 1000);
                        }
                    }

                    @Override
                    public void getFailContent(String result) {
                        handler.postDelayed(validateRunnable, 20 * 1000);
                    }
                });
            }
        };
        handler.postDelayed(validateRunnable, 20 * 1000);
    }

    private void cancleValide() {
        if (validateRunnable != null) {
            handler.removeCallbacks(validateRunnable);
            validateRunnable = null;
        }
    }

    private void data_error() {
        show_toast("数据有误，稍后重试");
    }

    private void service_error() {
        show_toast("服务器异常，稍后重试");
    }

    private void show_toast(String msg) {
        Toast.makeText(BuptPayActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancleValide();
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
            show_toast("支付宝正在建设中。。");
        } else if (i == R.id.wechatBtn) {
            getData();
        }
    }
}

