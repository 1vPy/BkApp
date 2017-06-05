package com.roy.bkapp.http.api.bmob;

import com.roy.bkapp.Constants;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/5/17.
 */

public interface BmobApi {
    String API_BASE_URL = "https://api.bmob.cn/1/";

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    @Headers({"X-Bmob-Application-Id:" + Constants.application_id, "X-Bmob-REST-API-Key:" + Constants.application_key, "Content-Type:application/json"})
    @GET("login")
    Observable<Response<ResponseBody>> login(@Query("username")String username, @Query("password")String password);

    /**
     * 用户注册
     * @param body
     * @return
     */
    @Headers({"X-Bmob-Application-Id:" + Constants.application_id, "X-Bmob-REST-API-Key:" + Constants.application_key, "Content-Type:application/json"})
    @POST("users")
    Observable<Response<ResponseBody>> register(@Body RequestBody body);

    /**
     * 用户点赞查询
     * @param json
     * @return
     */
    @Headers({"X-Bmob-Application-Id:" + Constants.application_id, "X-Bmob-REST-API-Key:" + Constants.application_key, "Content-Type:application/json"})
    @GET("classes/Movie_Praise")
    Observable<Response<ResponseBody>> praiseQuery(@Query("where") String json);

    /**
     * 添加用户点赞
     * @param body
     * @return
     */
    @Headers({"X-Bmob-Application-Id:" + Constants.application_id, "X-Bmob-REST-API-Key:" + Constants.application_key, "Content-Type:application/json"})
    @POST("classes/Movie_Praise")
    Observable<Response<ResponseBody>> addPraise(@Body RequestBody body);

    /**
     * 评论查询
     * @param json
     * @return
     */
    @Headers({"X-Bmob-Application-Id:" + Constants.application_id, "X-Bmob-REST-API-Key:" + Constants.application_key, "Content-Type:application/json"})
    @GET("classes/Movie_Comment")
    Observable<Response<ResponseBody>> commentQuery(@Query("where") String json);

    /**
     * 添加评论
     * @param body
     * @return
     */
    @Headers({"X-Bmob-Application-Id:" + Constants.application_id, "X-Bmob-REST-API-Key:" + Constants.application_key, "Content-Type:application/json"})
    @POST("classes/Movie_Comment")
    Observable<Response<ResponseBody>> addComment(@Body RequestBody body);
}
