package com.roy.bkapp.http.api.bmob;

import com.roy.bkapp.Constants;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/5/17.
 */

public interface BmobApi {
    String API_BASE_URL = "https://api.bmob.cn/";


    /**
     * 用户登录验证
     * @param json
     * @return
     */
    @Headers({"X-Bmob-Application-Id:" + Constants.application_id, "X-Bmob-REST-API-Key:" + Constants.application_key, "Content-Type:application/json"})
    @GET("1/classes/User_SessionToken")
    Observable<Response<ResponseBody>> loginConfig(@Query("where") String json);


    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @return
     */
    @Headers({"X-Bmob-Application-Id:" + Constants.application_id, "X-Bmob-REST-API-Key:" + Constants.application_key, "Content-Type:application/json"})
    @GET("1/login")
    Observable<Response<ResponseBody>> login(@Query("username") String username, @Query("password") String password);

    /**
     * 用户sessionToken上传
     *
     * @param body
     * @return
     */
    @Headers({"X-Bmob-Application-Id:" + Constants.application_id, "X-Bmob-REST-API-Key:" + Constants.application_key, "Content-Type:application/json"})
    @POST("1/classes/User_SessionToken")
    Observable<Response<ResponseBody>> uploadSessionToken(@Body RequestBody body);

    /**
     * 用户sessionToken更新
     *
     * @param json
     * @param body
     * @return
     */
    @Headers({"X-Bmob-Application-Id:" + Constants.application_id, "X-Bmob-REST-API-Key:" + Constants.application_key, "Content-Type:application/json"})
    @PUT("1/classes/User_SessionToken")
    Observable<Response<ResponseBody>> updateSessionToken(@Query("where") String json, @Body RequestBody body);


    /**
     * 用户注册
     *
     * @param body
     * @return
     */
    @Headers({"X-Bmob-Application-Id:" + Constants.application_id, "X-Bmob-REST-API-Key:" + Constants.application_key, "Content-Type:application/json"})
    @POST("1/users")
    Observable<Response<ResponseBody>> register(@Body RequestBody body);

    /**
     * 用户头像上传
     * @param filename
     * @param file
     * @return
     */
    @Headers({"X-Bmob-Application-Id:" + Constants.application_id, "X-Bmob-REST-API-Key:" + Constants.application_key, "Content-Type:image/png"})
    @POST("2/files/{filename}")
    Observable<Response<ResponseBody>> uploadPic(@Path("filename") String filename, @Body RequestBody file);

    /**
     * 更新用户数据
     * @param header
     * @param objectid
     * @param body
     * @return
     */
    @Headers({"X-Bmob-Application-Id:" + Constants.application_id, "X-Bmob-REST-API-Key:" + Constants.application_key, "Content-Type:application/json"})
    @PUT("1/users/{objectid}")
    Observable<Response<ResponseBody>> updateUser(@Header("X-Bmob-Session-Token") String header, @Path("objectid") String objectid, @Body RequestBody body);

    /**
     * 用户点赞查询
     *
     * @param json
     * @return
     */
    @Headers({"X-Bmob-Application-Id:" + Constants.application_id, "X-Bmob-REST-API-Key:" + Constants.application_key, "Content-Type:application/json"})
    @GET("1/classes/Movie_Praise")
    Observable<Response<ResponseBody>> praiseQuery(@Query("where") String json);

    /**
     * 添加用户点赞
     *
     * @param body
     * @return
     */
    @Headers({"X-Bmob-Application-Id:" + Constants.application_id, "X-Bmob-REST-API-Key:" + Constants.application_key, "Content-Type:application/json"})
    @POST("1/classes/Movie_Praise")
    Observable<Response<ResponseBody>> addPraise(@Body RequestBody body);

    /**
     * 评论查询
     *
     * @param json
     * @return
     */
    @Headers({"X-Bmob-Application-Id:" + Constants.application_id, "X-Bmob-REST-API-Key:" + Constants.application_key, "Content-Type:application/json"})
    @GET("1/classes/Movie_Comment")
    Observable<Response<ResponseBody>> commentQuery(@Query("where") String json);

    /**
     * 添加评论
     *
     * @param body
     * @return
     */
    @Headers({"X-Bmob-Application-Id:" + Constants.application_id, "X-Bmob-REST-API-Key:" + Constants.application_key, "Content-Type:application/json"})
    @POST("1/classes/Movie_Comment")
    Observable<Response<ResponseBody>> addComment(@Body RequestBody body);

    /**
     * 上传异常文件
     * @param filename
     * @param file
     * @return
     */
    @Headers({"X-Bmob-Application-Id:" + Constants.application_id, "X-Bmob-REST-API-Key:" + Constants.application_key, "Content-Type:text/plain"})
    @POST("2/files/{filename}")
    Observable<Response<ResponseBody>> uploadCrash(@Path("filename") String filename, @Body RequestBody file);
}
