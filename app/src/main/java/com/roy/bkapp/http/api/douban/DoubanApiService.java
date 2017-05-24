package com.roy.bkapp.http.api.douban;

import com.roy.bkapp.BkKit;
import com.roy.bkapp.dagger.module.HttpModule_ProvideDoubanApiFactory;
import com.roy.bkapp.http.RequestCallback;
import com.roy.bkapp.model.movie.JsonMovieBean;
import com.roy.bkapp.model.movie.details.JsonDetailBean;
import com.roy.bkapp.model.movie.star.JsonStarBean;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 1vPy(Roy) on 2017/4/10.
 */

public class DoubanApiService {
    private DoubanApi mDoubanApi;

    public DoubanApiService(DoubanApi doubanApi) {
        mDoubanApi = doubanApi;
    }

    public void getHotMovie(int start, int count, String city, final RequestCallback<JsonMovieBean> rc) {
        mDoubanApi.getHotMovie(start, count, city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JsonMovieBean>() {
                    @Override
                    public void accept(JsonMovieBean jsonMovieBean) throws Exception {
                        if (jsonMovieBean != null) {
                            rc.onSuccess(jsonMovieBean);
                        } else {
                            rc.onFailure("没有数据");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        rc.onFailure(throwable.getLocalizedMessage());
                    }
                });

    }


    public void getComingMovie(int start, int count, final RequestCallback<JsonMovieBean> rc) {
        mDoubanApi.getComingMovie(start, count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JsonMovieBean>() {
                    @Override
                    public void accept(JsonMovieBean jsonMovieBean) throws Exception {
                        if (jsonMovieBean != null) {
                            rc.onSuccess(jsonMovieBean);
                        } else {
                            rc.onFailure("没有数据");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        rc.onFailure(throwable.getLocalizedMessage());
                    }
                });

    }

    public void getMovieDetail(String id, final RequestCallback<JsonDetailBean> rc) {
        mDoubanApi.getMovieDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JsonDetailBean>() {
                    @Override
                    public void accept(JsonDetailBean jsonDetailBean) throws Exception {
                        if (jsonDetailBean != null) {
                            rc.onSuccess(jsonDetailBean);
                        } else {
                            rc.onFailure("没有数据");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        rc.onFailure(throwable.getLocalizedMessage());
                    }
                });
    }


    public void getStarDetail(String id, final RequestCallback<JsonStarBean> rc) {
        mDoubanApi.getStarDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JsonStarBean>() {
                    @Override
                    public void accept(JsonStarBean jsonStarBean) throws Exception {
                        if (jsonStarBean != null) {
                            rc.onSuccess(jsonStarBean);
                        } else {
                            rc.onFailure("没有数据");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        rc.onFailure(throwable.getLocalizedMessage());
                    }
                });
    }

    public void searchMovie(String query, final RequestCallback<JsonMovieBean> rc) {
        mDoubanApi.searchMovie(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JsonMovieBean>() {
                    @Override
                    public void accept(JsonMovieBean jsonMovieBean) throws Exception {
                        if (jsonMovieBean != null) {
                            rc.onSuccess(jsonMovieBean);
                        } else {
                            rc.onFailure("没有数据");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        rc.onFailure(throwable.getLocalizedMessage());
                    }
                });
    }

}
