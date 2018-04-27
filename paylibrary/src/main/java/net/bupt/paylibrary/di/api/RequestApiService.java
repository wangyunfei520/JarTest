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
    @POST("course/test1")
    Call<JsonElement> getCoursebyId(@Field("courseId") String courseId, @Field("courseName") String courseName);
}
