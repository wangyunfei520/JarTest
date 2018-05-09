package net.bupt.paylibrary.di.api;


import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * user request interface
 */

public interface RequestApiService {

    @FormUrlEncoded
    @POST("pay/prepay.do")
    Call<JsonElement> getData(@Field("cardno") String cardno,
                              @Field("prodcode") String prodcode,
                              @Field("description") String description,
                              @Field("totalAmount") String totalAmount,
                              @Field("flag") String flag
    );

    @FormUrlEncoded
    @POST("pay/payresult.do")
    Call<JsonElement> validate(@Field("out_trade_no") String out_trade_no,
                               @Field("flag") String flag);
}
