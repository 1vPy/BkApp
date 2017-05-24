package com.roy.bkapp.http.api.bmob;

import com.roy.bkapp.http.RequestCallback;
import com.roy.bkapp.model.user.ErrorBean;
import com.roy.bkapp.model.user.UserBean;
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
                .subscribe(new Consumer<Response<ResponseBody>>() {
                    @Override
                    public void accept(@NonNull Response<ResponseBody> responseBodyResponse) throws Exception {
                        LogUtils.log(TAG, "code:" + responseBodyResponse.code(), LogUtils.DEBUG);
                        if(responseBodyResponse.isSuccessful()){
                            rc.onSuccess(JsonUtils.Json2JavaBean(responseBodyResponse.body().string(), UserBean.class));
                        }else{
                            switch (responseBodyResponse.code()) {
                                case 404:
                                    rc.onFailure(JsonUtils.Json2JavaBean(responseBodyResponse.errorBody().string(), ErrorBean.class).getError());
                                    break;
                                default:
                                    rc.onFailure("未知错误");
                                    break;
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        rc.onFailure(throwable.getLocalizedMessage());
                    }
                });
    }

    public void register(UserBean userBean, final RequestCallback<UserBean> rc) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), JsonUtils.JavaBean2Json(userBean));
        mBmobApi.register(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<ResponseBody>>() {
                    @Override
                    public void accept(@NonNull Response<ResponseBody> responseBodyResponse) throws Exception {
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
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        rc.onFailure(throwable.getLocalizedMessage());
                    }
                });
    }
}
