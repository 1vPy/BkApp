package com.roy.bkapp.http.api.bmob;

import android.text.TextUtils;
import android.widget.Toast;

import com.roy.bkapp.BkKit;
import com.roy.bkapp.http.RequestCallback;
import com.roy.bkapp.model.collection.MovieCloudCollection;
import com.roy.bkapp.model.collection.MovieCloudResult;
import com.roy.bkapp.model.collection.MovieCollection;
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
import com.roy.bkapp.utils.ToastUtils;
import com.roy.bkapp.utils.UserPreference;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
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

    private UserPreference mUserPreference;

    public BmobApiService(BmobApi bmobApi, UserPreference userPreference) {
        mBmobApi = bmobApi;
        mUserPreference = userPreference;
    }


    public void loginConfig(String username, RequestCallback<LoginConfig> requestCallback) {
        UserBean userBean = new UserBean();
        userBean.setUsername(username);
        String json = JsonUtils.JavaBean2Json(userBean);
        mBmobApi.loginConfig(json)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    if (responseBodyResponse.isSuccessful()) {
                        requestCallback.onSuccess(JsonUtils.Json2JavaBean(responseBodyResponse.body().string(), LoginConfig.class));
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
                }, throwable -> throwable.getLocalizedMessage());
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

    public void uploadOrUpdateSessionToken(String username, String sessionToken, RequestCallback<String> rc) {
        UserBean userBean = new UserBean();
        userBean.setUsername(username);
        String json = JsonUtils.JavaBean2Json(userBean);
        mBmobApi.loginConfig(json)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .concatMap(new Function<Response<ResponseBody>, ObservableSource<Response<ResponseBody>>>() {
                    @Override
                    public ObservableSource<Response<ResponseBody>> apply(@NonNull Response<ResponseBody> responseBodyResponse) throws Exception {
                        if (responseBodyResponse.isSuccessful()) {
                            LoginConfig l = JsonUtils.Json2JavaBean(responseBodyResponse.body().string(), LoginConfig.class);
                            if (l.getResults().size() > 0) {
                                UserBean u = new UserBean();
                                u.setSessionToken(sessionToken);
                                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), JsonUtils.JavaBean2Json(u));
                                return mBmobApi.updateSessionToken(JsonUtils.JavaBean2Json(userBean), requestBody);
                            } else {
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
                .subscribe(responseBodyResponse -> {
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

    public void uploadPicRegister(File file, UserBean userBean, RequestCallback<UserBean> requestCallback) {
        LogUtils.log(TAG, file.getAbsolutePath(), LogUtils.DEBUG);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), image2byte(file.getAbsolutePath()));
        mBmobApi.uploadPic(file.getName(), requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> requestCallback.onFailure("头像设置失败"))
                .observeOn(Schedulers.io())
                .concatMap(new Function<Response<ResponseBody>, ObservableSource<Response<ResponseBody>>>() {
                    @Override
                    public ObservableSource<Response<ResponseBody>> apply(@NonNull Response<ResponseBody> responseBodyResponse) throws Exception {
                        if (responseBodyResponse.isSuccessful()) {
                            UploadImg uploadImg = JsonUtils.Json2JavaBean(responseBodyResponse.body().string(), UploadImg.class);
                            UserHeader userHeader = new UserHeader();
                            userHeader.set_Type("File");
                            userHeader.setCdn(uploadImg.getCdn());
                            userHeader.setFilename(uploadImg.getFilename());
                            userHeader.setUrl(uploadImg.getUrl());
                            userBean.setUserHeader(userHeader);
                        }
                        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), JsonUtils.JavaBean2Json(userBean));
                        return mBmobApi.register(requestBody);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    if (responseBodyResponse.isSuccessful()) {
                        requestCallback.onSuccess(JsonUtils.Json2JavaBean(responseBodyResponse.body().string(), UserBean.class));
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

    public void modifyAvatar(String s, RequestCallback<String> rc) {
        RequestBody file = RequestBody.create(MediaType.parse("application/octet-stream"), image2byte(s));
        String filename = new File(s).getName();
        mBmobApi.uploadPic(filename, file)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(responseBodyResponse -> {
                    ToastUtils.showToast("上传成功");
                    LogUtils.log(TAG, "上传成功", LogUtils.DEBUG);
                })
                .doOnError(throwable -> LogUtils.log(TAG, "error:" + throwable.getLocalizedMessage(), LogUtils.DEBUG))
                .observeOn(Schedulers.io())
                .concatMap(new Function<Response<ResponseBody>, ObservableSource<Response<ResponseBody>>>() {
                    @Override
                    public ObservableSource<Response<ResponseBody>> apply(@NonNull Response<ResponseBody> responseBodyResponse) throws Exception {
                        UploadImg uploadImg = JsonUtils.Json2JavaBean(responseBodyResponse.body().string(), UploadImg.class);
                        mUserPreference.saveUserHeader(uploadImg.getUrl());
                        UserBean userBean = new UserBean();
                        UserHeader userHeader = new UserHeader();
                        userHeader.set_Type("File");
                        userHeader.setFilename(uploadImg.getFilename());
                        userHeader.setCdn(uploadImg.getCdn());
                        userHeader.setUrl(uploadImg.getUrl());
                        userBean.setUserHeader(userHeader);
                        LogUtils.log(TAG, "getSessionToken:" + mUserPreference.readSessionToken() + ",objectId:" + mUserPreference.readUserInfo().getObjectId() + ",json:" + JsonUtils.JavaBean2Json(userBean), LogUtils.DEBUG);
                        RequestBody r = RequestBody.create(MediaType.parse("application/json"), JsonUtils.JavaBean2Json(userBean));
                        return mBmobApi.updateUser(mUserPreference.readSessionToken(), mUserPreference.readUserInfo().getObjectId(), r);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                            LogUtils.log(TAG, responseBodyResponse.body().string(), LogUtils.DEBUG);
                            rc.onSuccess(mUserPreference.readUserHeader());
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

    public void syncCollection(List<MovieCollection> collectionList, RequestCallback<String> requestCallback) {
        for (MovieCollection collection : collectionList) {
            MovieCloudResult movieCloudResult = new MovieCloudResult();
            movieCloudResult.setUsername(mUserPreference.readUserInfo().getUsername());
            movieCloudResult.setMovieId(collection.getMovieId());
            LogUtils.log(TAG,"json:"+JsonUtils.JavaBean2Json(movieCloudResult),LogUtils.DEBUG);
            mBmobApi.searchCloudCollection(JsonUtils.JavaBean2Json(movieCloudResult))
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .concatMap(new Function<Response<ResponseBody>, ObservableSource<Response<ResponseBody>>>() {
                        @Override
                        public ObservableSource<Response<ResponseBody>> apply(@NonNull Response<ResponseBody> responseBodyResponse) throws Exception {
                            if (responseBodyResponse.isSuccessful()) {
                                if (JsonUtils.Json2JavaBean(responseBodyResponse.body().string(), MovieCloudCollection.class).getResults().size() > 0) {
                                    return null;
                                }
                            }
                            movieCloudResult.setMovieName(collection.getMovieName());
                            movieCloudResult.setImageUrl(collection.getImageUrl());
                            RequestBody r = RequestBody.create(MediaType.parse("application/json"), JsonUtils.JavaBean2Json(movieCloudResult));
                            return mBmobApi.syncCollection(r);
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(responseBodyResponse -> {
                        if (responseBodyResponse.isSuccessful()) {
                            requestCallback.onSuccess(collection.getMovieId());
                        } else {
                            requestCallback.onFailure("Failed");
                        }
                    }, throwable -> {
                        LogUtils.log(TAG,throwable.getLocalizedMessage(),LogUtils.DEBUG);
                        if(TextUtils.equals("The mapper returned a null ObservableSource",throwable.getLocalizedMessage())){
                            requestCallback.onSuccess(collection.getMovieId());
                        }else{
                            requestCallback.onFailure("Failed");
                        }
                    });
        }

    }

    public void deleteCloudCollection(String movieId ,RequestCallback<String> requestCallback){
            MovieCloudResult movieCloudResult = new MovieCloudResult();
            movieCloudResult.setUsername(mUserPreference.readUserInfo().getUsername());
            movieCloudResult.setMovieId(movieId);
            LogUtils.log(TAG,"json:"+JsonUtils.JavaBean2Json(movieCloudResult),LogUtils.DEBUG);
            mBmobApi.searchCloudCollection(JsonUtils.JavaBean2Json(movieCloudResult))
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .concatMap(new Function<Response<ResponseBody>, ObservableSource<Response<ResponseBody>>>() {
                        @Override
                        public ObservableSource<Response<ResponseBody>> apply(@NonNull Response<ResponseBody> responseBodyResponse) throws Exception {
                            LogUtils.log(TAG,"1",LogUtils.DEBUG);
                            if (responseBodyResponse.isSuccessful()) {
                                LogUtils.log(TAG,"2",LogUtils.DEBUG);
                                MovieCloudCollection movieCloudCollection = JsonUtils.Json2JavaBean(responseBodyResponse.body().string(), MovieCloudCollection.class);
                                if (movieCloudCollection.getResults().size() <= 0) {
                                    LogUtils.log(TAG,"3",LogUtils.DEBUG);
                                    return null;
                                }
                                return mBmobApi.deleteCloudCollection(movieCloudCollection.getResults().get(0).getObjectId());
                            }
                            return null;
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(responseBodyResponse -> requestCallback.onSuccess("云端删除成功"),throwable -> {
                        LogUtils.log(TAG,throwable.getLocalizedMessage(),LogUtils.DEBUG);
                        if(TextUtils.equals("The mapper returned a null ObservableSource",throwable.getLocalizedMessage())){
                            requestCallback.onSuccess("云端删除成功");
                        }else{
                            requestCallback.onFailure("云端删除失败");
                        }
                    });
    }

    public void searchCloudCollectionNum(RequestCallback<Integer> requestCallback){
        MovieCloudResult movieCloudResult = new MovieCloudResult();
        movieCloudResult.setUsername(mUserPreference.readUserInfo().getUsername());
        LogUtils.log(TAG,"json:"+JsonUtils.JavaBean2Json(movieCloudResult),LogUtils.DEBUG);
        mBmobApi.searchCloudCollection(JsonUtils.JavaBean2Json(movieCloudResult))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    if (responseBodyResponse.isSuccessful()) {
                            requestCallback.onSuccess(JsonUtils.Json2JavaBean(responseBodyResponse.body().string(), MovieCloudCollection.class).getResults().size());
                    }
                },throwable -> requestCallback.onFailure(throwable.getLocalizedMessage()));
    }
}
