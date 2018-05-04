package net.bupt.paylibrary.center;

import android.text.TextUtils;

import com.google.gson.JsonElement;

import net.bupt.paylibrary.CallBackResponseContent;
import net.bupt.paylibrary.di.modules.RequestHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayCenter {
    private static final String TAG = "PayCenter";

    private volatile static PayCenter instance;
    private static RequestHelper loadInterface;

    private PayCenter() {
        loadInterface = RequestHelper.getInstance();
    }

    public static PayCenter getInstance() {
        //先检查实例是否存在，如果不存在才进入下面的同步块
        if (instance == null) {
            //同步块，线程安全的创建实例
            synchronized (PayCenter.class) {
                //再次检查实例是否存在，如果不存在才真正的创建实例
                if (instance == null) {
                    instance = new PayCenter();
                }
            }
        }
        return instance;
    }

    public Call<JsonElement> validate(String out_trade_no, CallBackResponseContent responseContent) {
        Call<JsonElement> call;
        call = loadInterface.validate(out_trade_no);
        deal(responseContent, call);
        return call;
    }

    public Call<JsonElement> getData(String cardno, String prodcode,
                                     String description, String total,
                                     CallBackResponseContent responseContent) {
        Call<JsonElement> call;
        call = loadInterface.getData(cardno, prodcode, description, total);
        deal(responseContent, call);
        return call;
    }

    private void deal(final CallBackResponseContent responseContent, Call<JsonElement> call) {
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.code() != 200) {
                    responseContent.getFailContent(response.code() + "");
                    return;
                }
                if (TextUtils.isEmpty(response.body().toString())) {
                    responseContent.getFailContent("后台返回数据为空 " + response.body().toString());
                    return;
                }
                responseContent.getResponseContent(response.body().toString());
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                if (!call.isCanceled()) {
                    responseContent.getFailContent(t.getMessage());
                }
            }
        });
    }
}
