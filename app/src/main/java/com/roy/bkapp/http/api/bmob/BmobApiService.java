package com.roy.bkapp.http.api.bmob;

import android.widget.Toast;

import com.roy.bkapp.BkKit;
import com.roy.bkapp.http.RequestCallback;
import com.roy.bkapp.model.user.ErrorBean;
import com.roy.bkapp.model.user.UploadImg;
import com.roy.bkapp.model.user.UserBean;
import com.roy.bkapp.model.user.UserHeader;
import com.roy.bkapp.model.user.login_config.LoginConfig;
import com.roy.bkapp.model.user_movie.comment.CommentMovie;
import com.roy.bkapp.model.user_movie.comment.CommentResult;
import com.roy.bkapp.model.user_movie.praise.PraiseMovie;
import com.roy.bkapp.model.user_movie.SuccessBean;
import com.roy.bkapp.model.user_movie.praise.PraiseResult;
import com.roy.bkapp.utils.JsonUtils;
import com.roy.bkapp.utils.LogUtils;
import com.roy.bkapp.utils.UserPreference;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
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


    public void loginConfig(String username,RequestCallback<LoginConfig> requestCallback){
        UserBean userBean = new UserBean();
        userBean.setUsername(username);
        String json = JsonUtils.JavaBean2Json(userBean);
        mBmobApi.loginConfig(json)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    if(responseBodyResponse.isSuccessful()){
                        requestCallback.onSuccess(JsonUtils.Json2JavaBean(responseBodyResponse.body().string(),LoginConfig.class));
                    }else {
                        switch (responseBodyResponse.code()) {
                            case 404:
                                requestCallback.onFailure(JsonUtils.Json2JavaBean(responseBodyResponse.errorBody().string(), ErrorBean.class).getError());
                                break;
                            default:
                                requestCallback.onFailure("未知错误");
                                break;
                        }
                    }
                },throwable -> throwable.getLocalizedMessage());
    }

    public void login(String username, String password, RequestCallback<UserBean> rc) {
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

    public void uploadOrUpdateSessionToken(String username,String sessionToken,RequestCallback<String> rc){
        UserBean userBean = new UserBean();
        userBean.setUsername(username);
        String json = JsonUtils.JavaBean2Json(userBean);
        mBmobApi.loginConfig(json)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .concatMap(new Function<Response<ResponseBody>, ObservableSource<Response<ResponseBody>>>() {
                    @Override
                    public ObservableSource<Response<ResponseBody>> apply(@NonNull Response<ResponseBody> responseBodyResponse) throws Exception {
                        if(responseBodyResponse.isSuccessful()){
                            LoginConfig l = JsonUtils.Json2JavaBean(responseBodyResponse.body().string(),LoginConfig.class);
                            if(l.getResults().size()>0){
                                UserBean u = new UserBean();
                                u.setSessionToken(sessionToken);
                                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), JsonUtils.JavaBean2Json(u));
                                return mBmobApi.updateSessionToken(JsonUtils.JavaBean2Json(userBean),requestBody);
                            }else{
                                UserBean u = new UserBean();
                                u.setUsername(username);
                                u.setSessionToken(sessionToken);
                                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), JsonUtils.JavaBean2Json(u));
                                return mBmobApi.uploadSessionToken(requestBody);
                            }
                        }
                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse ->{
                    LogUtils.log(TAG, "code:" + responseBodyResponse.code(), LogUtils.DEBUG);
                    if (responseBodyResponse.isSuccessful()) {
                        rc.onSuccess("ss");
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
        PraiseResult result = new PraiseResult();
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
        PraiseResult result = new PraiseResult();
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
        PraiseResult result = new PraiseResult();
        result.setMovieId(movieId);
        result.setUsername(username);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), JsonUtils.JavaBean2Json(result));
        mBmobApi.addPraise(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    if (responseBodyResponse.isSuccessful()) {
                        requestCallback.onSuccess(JsonUtils.Json2JavaBean(responseBodyResponse.body().string(), SuccessBean.class).getObjectId());
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


    public void commentNum(String movieId, RequestCallback<Integer> requestCallback) {
        CommentResult result = new CommentResult();
        result.setMovieId(movieId);
        String json = JsonUtils.JavaBean2Json(result);
        mBmobApi.commentQuery(json)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    if (responseBodyResponse.isSuccessful()) {
                        requestCallback.onSuccess(JsonUtils.Json2JavaBean(responseBodyResponse.body().string(), CommentMovie.class).getResults().size());
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


    public void commentQuery(String movieId, RequestCallback<CommentMovie> requestCallback) {
        CommentResult result = new CommentResult();
        result.setMovieId(movieId);
        String json = JsonUtils.JavaBean2Json(result);
        mBmobApi.commentQuery(json)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    if (responseBodyResponse.isSuccessful()) {
                        requestCallback.onSuccess(JsonUtils.Json2JavaBean(responseBodyResponse.body().string(), CommentMovie.class));
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


    public void addComment(String comment, String username, String movieId, float rating, RequestCallback<String> requestCallback) {
        CommentResult result = new CommentResult();
        result.setMovieId(movieId);
        result.setUsername(username);
        result.setComment(comment);
        result.setRating(rating);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), JsonUtils.JavaBean2Json(result));
        mBmobApi.addComment(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    if (responseBodyResponse.isSuccessful()) {
                        requestCallback.onSuccess(JsonUtils.Json2JavaBean(responseBodyResponse.body().string(), SuccessBean.class).getObjectId());
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

    public void uploadCrash(String path, RequestCallback<String> requestCallback) {
        LogUtils.log(TAG, path, LogUtils.DEBUG);
        File f = new File(path);
        RequestBody file = RequestBody.create(MediaType.parse("application/octet-stream"), txt2String(f));
        mBmobApi.uploadCrash("Crash.txt", file)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    LogUtils.log(TAG, "上传", LogUtils.DEBUG);
                    if (responseBodyResponse.isSuccessful()) {
                        requestCallback.onSuccess("上传成功");
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
                }, throwable -> {
                    requestCallback.onFailure(throwable.getLocalizedMessage());
                    LogUtils.log(TAG, throwable.getLocalizedMessage(), LogUtils.DEBUG);
                });
    }

    /**
     * 读取txt文件的内容
     *
     * @param file 想要读取的文件对象
     * @return 返回文件内容
     */
    public static String txt2String(File file) {
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                result.append(System.lineSeparator() + s);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public void uploadPic(String s, RequestCallback<String> rc) {
        RequestBody file = RequestBody.create(MediaType.parse("application/octet-stream"), image2byte(s));
        String filename = new File(s).getName();
        mBmobApi.uploadPic(filename, file)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(responseBodyResponse -> {
                    Toast.makeText(BkKit.getContext(), "上传成功", Toast.LENGTH_LONG).show();
                    LogUtils.log(TAG, "上传成功", LogUtils.DEBUG);
                })
                .doOnError(throwable -> LogUtils.log(TAG, "error:" + throwable.getLocalizedMessage(), LogUtils.DEBUG))
                .observeOn(Schedulers.io())
                .concatMap(new Function<Response<ResponseBody>, ObservableSource<Response<ResponseBody>>>() {
                    @Override
                    public ObservableSource<Response<ResponseBody>> apply(@NonNull Response<ResponseBody> responseBodyResponse) throws Exception {
                        UploadImg uploadImg = JsonUtils.Json2JavaBean(responseBodyResponse.body().string(), UploadImg.class);
                        UserPreference.getUserPreference(BkKit.getContext()).saveUserHeader(uploadImg.getUrl());
                        UserBean userBean = new UserBean();
                        UserHeader userHeader = new UserHeader();
                        userHeader.set_Type("File");
                        userHeader.setFilename(uploadImg.getFilename());
                        userHeader.setCdn(uploadImg.getCdn());
                        userHeader.setUrl(uploadImg.getUrl());
                        userBean.setUserHeader(userHeader);
                        LogUtils.log(TAG, "getSessionToken:" + UserPreference.getUserPreference(BkKit.getContext()).readSessionToken() + ",objectId:"+UserPreference.getUserPreference(BkKit.getContext()).readUserInfo().getObjectId()+",json:" + JsonUtils.JavaBean2Json(userBean), LogUtils.DEBUG);
                        RequestBody r = RequestBody.create(MediaType.parse("application/json"), JsonUtils.JavaBean2Json(userBean));
                        return mBmobApi.updateUser(UserPreference.getUserPreference(BkKit.getContext()).readSessionToken(), UserPreference.getUserPreference(BkKit.getContext()).readUserInfo().getObjectId(), r);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                            LogUtils.log(TAG, responseBodyResponse.body().string(), LogUtils.DEBUG);
                            rc.onSuccess(UserPreference.getUserPreference(BkKit.getContext()).readUserHeader());
                        },
                        throwable -> LogUtils.log(TAG, "error:" + throwable.getLocalizedMessage(), LogUtils.DEBUG));

    }


    public byte[] image2byte(String path) {
        byte[] data = null;
        FileInputStream input = null;
        try {
            input = new FileInputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
            output.close();
            input.close();
        } catch (FileNotFoundException ex1) {
            ex1.printStackTrace();
        } catch (IOException ex1) {
            ex1.printStackTrace();
        }
        return data;
    }
}
