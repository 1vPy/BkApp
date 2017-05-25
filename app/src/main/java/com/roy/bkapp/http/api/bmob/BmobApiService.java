package com.roy.bkapp.http.api.bmob;

import com.roy.bkapp.http.RequestCallback;
import com.roy.bkapp.model.user.ErrorBean;
import com.roy.bkapp.model.user.UserBean;
import com.roy.bkapp.model.user_movie.PraiseMovie;
import com.roy.bkapp.model.user_movie.PraiseSuccessBean;
import com.roy.bkapp.model.user_movie.Result;
import com.roy.bkapp.utils.JsonUtils;
import com.roy.bkapp.utils.LogUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/5/17.
 */

public class BmobApiService {
    private static final String TAG = BmobApiService.class.getSimpleName();

    private BmobApi mBmobApi;

    public BmobApiService(BmobApi bmobApi) {
        mBmobApi = bmobApi;
    }

    public void login(String username, String password, final RequestCallback<UserBean> rc) {
        mBmobApi.login(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    LogUtils.log(TAG, "code:" + responseBodyResponse.code(), LogUtils.DEBUG);
                    if (responseBodyResponse.isSuccessful()) {
                        rc.onSuccess(JsonUtils.Json2JavaBean(responseBodyResponse.body().string(), UserBean.class));
                    } else {
                        switch (responseBodyResponse.code()) {
                            case 404:
                                rc.onFailure(JsonUtils.Json2JavaBean(responseBodyResponse.errorBody().string(), ErrorBean.class).getError());
                                break;
                            default:
                                rc.onFailure("未知错误");
                                break;
                        }
                    }
                }, throwable -> rc.onFailure(throwable.getLocalizedMessage()));
    }

    public void register(UserBean userBean, final RequestCallback<UserBean> rc) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), JsonUtils.JavaBean2Json(userBean));
        mBmobApi.register(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    LogUtils.log(TAG, "code:" + responseBodyResponse.code(), LogUtils.DEBUG);
                    if (responseBodyResponse.isSuccessful()) {
                        rc.onSuccess(JsonUtils.Json2JavaBean(responseBodyResponse.body().string(), UserBean.class));
                    } else {
                        switch (responseBodyResponse.code()) {
                            case 404:
                                rc.onFailure(JsonUtils.Json2JavaBean(responseBodyResponse.errorBody().string(), ErrorBean.class).getError());
                                break;
                            default:
                                rc.onFailure("未知错误");
                                break;
                        }
                    }
                }, throwable -> rc.onFailure(throwable.getLocalizedMessage()));
    }

    public void praiseNum(String movieId, RequestCallback<Integer> requestCallback) {
        Result result = new Result();
        result.setMovieId(movieId);
        String json = JsonUtils.JavaBean2Json(result);
        LogUtils.log(TAG, "json:" + json, LogUtils.DEBUG);

        mBmobApi.praiseQuery(json)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    if (responseBodyResponse.isSuccessful()) {
                        requestCallback.onSuccess(JsonUtils.Json2JavaBean(responseBodyResponse.body().string(), PraiseMovie.class).getResults().size());
                    } else {
                        switch (responseBodyResponse.code()) {
                            case 404:
                                requestCallback.onFailure(JsonUtils.Json2JavaBean(responseBodyResponse.errorBody().string(), ErrorBean.class).getError());
                                break;
                            default:
                                requestCallback.onFailure("未知错误");
                                break;
                        }
                    }
                }, throwable -> requestCallback.onFailure(throwable.getLocalizedMessage()));
    }

    public void isPraise(String movieId, String username, RequestCallback<Boolean> requestCallback) {
        Result result = new Result();
        result.setMovieId(movieId);
        result.setUsername(username);
        String json = JsonUtils.JavaBean2Json(result);
        LogUtils.log(TAG, "json:" + json, LogUtils.DEBUG);
        mBmobApi.praiseQuery(json)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    LogUtils.log(TAG, "code:" + responseBodyResponse.code(), LogUtils.DEBUG);
                    if (responseBodyResponse.isSuccessful()) {
                        if (JsonUtils.Json2JavaBean(responseBodyResponse.body().string(), PraiseMovie.class).getResults().size() > 0) {
                            requestCallback.onSuccess(true);
                        } else {
                            requestCallback.onSuccess(false);
                        }
                    } else {
                        switch (responseBodyResponse.code()) {
                            case 404:
                                requestCallback.onFailure(JsonUtils.Json2JavaBean(responseBodyResponse.errorBody().string(), ErrorBean.class).getError());
                                break;
                            default:
                                requestCallback.onFailure("未知错误");
                                break;
                        }
                    }
                }, throwable -> requestCallback.onFailure(throwable.getLocalizedMessage()));
    }

    public void addPraise(String movieId, String username, RequestCallback<String> requestCallback) {
        Result result = new Result();
        result.setMovieId(movieId);
        result.setUsername(username);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), JsonUtils.JavaBean2Json(result));
        mBmobApi.addPraise(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    if (responseBodyResponse.isSuccessful()) {
                        requestCallback.onSuccess(JsonUtils.Json2JavaBean(responseBodyResponse.body().string(), PraiseSuccessBean.class).getObjectId());
                    } else {
                        switch (responseBodyResponse.code()) {
                            case 404:
                                requestCallback.onFailure(JsonUtils.Json2JavaBean(responseBodyResponse.errorBody().string(), ErrorBean.class).getError());
                                break;
                            default:
                                requestCallback.onFailure("未知错误");
                                break;
                        }
                    }
                }, throwable -> requestCallback.onFailure(throwable.getLocalizedMessage()));
    }
}
