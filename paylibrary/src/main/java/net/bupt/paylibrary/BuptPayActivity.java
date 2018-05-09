package net.bupt.paylibrary;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
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
import net.bupt.paylibrary.entity.PayEntity;
import net.bupt.paylibrary.utils.BuptPayUtils;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;

public class BuptPayActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "BuptPayActivity";
    private String zhi_flag = "2", wechat_flag = "1";

    private Button zhiButton, wechatButton;
    private TextView showTextView;
    private ImageView imageView;

    private Handler handler;
    private Runnable validateRunnable = null;

    Call<JsonElement> call;

    Dialog dialog;
    PayEntity entity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bupt_pay);
        Intent intent = getIntent();
        entity = (PayEntity) intent.getSerializableExtra("data");
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

    private void getData(final String flag) {
        cancle();
        dialog = BuptPayUtils.createLoadingDialog(this, "正在请求数据");
        dialog.show();
        final int dimension = (int) getResources().getDimension(R.dimen.image_size);
        call = PayCenter.getInstance().getData(flag, entity, new CallBackResponseContent() {
            @Override
            public void getResponseContent(String data) {
                Log.v(TAG, data);
                dialog.dismiss();
                Bitmap result = null;
                try {
                    final JSONObject object = new JSONObject(data);
                    if (object.getInt("status") != 0) {
                        show_toast(object.getString("message"));
                        return;
                    }
                    JSONObject objectJSONObject = object.getJSONObject("data");
                    result = BuptPayUtils.encodeAsBitmap(objectJSONObject.getString("code_url"),
                            dimension,
                            flag.equals(zhi_flag) ? BitmapFactory.decodeResource(getResources(), R.drawable.alipay2)
                                    : BitmapFactory.decodeResource(getResources(), R.drawable.wechat2));
                    if (result == null) {
                        data_error();
                        return;
                    }
                    String show = flag.equals(wechat_flag) ? "请使用微信扫码支付" : "请使用支付宝扫码支付";
                    showTextView.setText(show);
                    imageView.setImageBitmap(result);
                    String sn = objectJSONObject.getString("sn");
                    validate(sn, flag);
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

    CountDownTimer timer = null;

    private void validate(final String sn, final String flag) {
        cancleValide();
        validateRunnable = new Runnable() {
            @Override
            public void run() {
                call = PayCenter.getInstance().validate(flag, sn, new CallBackResponseContent() {
                    @Override
                    public void getResponseContent(String result) {
                        try {
                            JSONObject object = new JSONObject(result);
                            int status = object.getInt("status");
                            //status 等于1，表示支付成功
                            if (status != 1) {
                                handler.postDelayed(validateRunnable, 5 * 1000);
                                return;
                            }
                            imageView.setVisibility(View.GONE);
                            timer = new CountDownTimer(5000, 1000) {
                                @Override
                                public void onTick(long l) {
                                    showTextView.setText("支付成功，还剩" + l / 1000 + "秒返回");
                                }

                                @Override
                                public void onFinish() {
                                    BuptPayActivity.this.finish();
                                }
                            };
                            timer.start();
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
        if (timer != null) {
            timer.cancel();
        }
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
            getData(zhi_flag);
        } else if (i == R.id.wechatBtn) {
            getData(wechat_flag);
        }
    }
}

