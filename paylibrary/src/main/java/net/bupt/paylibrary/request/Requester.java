package net.bupt.paylibrary.request;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求类
 */

public class Requester {
    private static final int CONNECT_TIMEOUT = 30;  //请求超时时间，单位：s
    private static final int READ_TIMEOUT = 30;       //读写超时时间，单位：s

    private static OkHttpClient.Builder builder;

    /**
     * 初始化OkHttpClient.Builder()
     */
    public static void initBuilder(boolean isDebug, int timeout) {

        builder = new OkHttpClient.Builder();
        if (isDebug) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            builder.addInterceptor(loggingInterceptor);
        }
        //设置超时
        if (timeout == 0) {
            builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        } else {
            builder.connectTimeout(timeout, TimeUnit.SECONDS);
        }
        builder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
    }

    /**
     * 构造retrofit请求api
     *
     * @param host    host地址
     * @param service retrofit请求api
     * @param <T>     retrofit请求api
     * @return retrofit请求api
     */
    public static <T> T getRequestAPI(String host, Class<T> service) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(host)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())//此converter放在最后，因为不确定返回的数据是否为json
                .build();
        return retrofit.create(service);
    }
}
