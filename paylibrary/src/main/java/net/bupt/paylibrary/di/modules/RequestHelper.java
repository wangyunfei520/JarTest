package net.bupt.paylibrary.di.modules;

import com.google.gson.JsonElement;


import net.bupt.paylibrary.BuildConfig;
import net.bupt.paylibrary.di.api.RequestApiService;
import net.bupt.paylibrary.request.Requester;


import retrofit2.Call;

/**
 * 网络请求管理类，基于Request对retrofit的二次封装
 */

public class RequestHelper {
    private volatile static RequestHelper instance = null;

    private RequestHelper() {
    }

    public static RequestHelper getInstance() {
        if (instance == null) {
            synchronized (RequestHelper.class) {
                if (instance == null) {
                    instance = new RequestHelper();
                }
            }
        }
        return instance;
    }

    private static RequestApiService requestAPI;

    /**
     * 初始化网络请求
     */
    public void init() {
        Requester.initBuilder(BuildConfig.DEBUG, 30);
        //http://182.18.26.6:8080/PaymentPlatform/
        //http://192.168.10.29:8080/
        requestAPI = Requester.getRequestAPI("http://182.18.26.6:8080/PaymentPlatform/", RequestApiService.class);
    }

    public Call<JsonElement> getData(String flag, String cardno, String prodcode,
                                     String description, String total) {
        return requestAPI.getData(cardno, prodcode, description, total, flag);
    }

    public Call<JsonElement> validate(String out_trade_no, String flag) {
        return requestAPI.validate(out_trade_no, flag);
    }
}
