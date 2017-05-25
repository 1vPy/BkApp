package com.roy.bkapp.http.api.douban;

import com.roy.bkapp.http.RequestCallback;
import com.roy.bkapp.model.movie.JsonMovieBean;
import com.roy.bkapp.model.movie.details.JsonDetailBean;
import com.roy.bkapp.model.movie.star.JsonStarBean;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
                .subscribe(jsonMovieBean -> {
                    if (jsonMovieBean != null) {
                        rc.onSuccess(jsonMovieBean);
                    } else {
                        rc.onFailure("没有数据");
                    }
                }, throwable -> rc.onFailure("加载出错：" + throwable.getLocalizedMessage()));

    }


    public void getComingMovie(int start, int count, final RequestCallback<JsonMovieBean> rc) {
        mDoubanApi.getComingMovie(start, count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(jsonMovieBean -> {
                    if (jsonMovieBean != null) {
                        rc.onSuccess(jsonMovieBean);
                    } else {
                        rc.onFailure("没有数据");
                    }
                }, throwable -> rc.onFailure("加载出错：" + throwable.getLocalizedMessage()));

    }

    public void getMovieDetail(String id, final RequestCallback<JsonDetailBean> rc) {
        mDoubanApi.getMovieDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(jsonDetailBean -> {
                    if (jsonDetailBean != null) {
                        rc.onSuccess(jsonDetailBean);
                    } else {
                        rc.onFailure("没有数据");
                    }
                }, throwable -> rc.onFailure("加载出错：" + throwable.getLocalizedMessage()));
    }


    public void getStarDetail(String id, final RequestCallback<JsonStarBean> rc) {
        mDoubanApi.getStarDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(jsonStarBean -> {
                    if (jsonStarBean != null) {
                        rc.onSuccess(jsonStarBean);
                    } else {
                        rc.onFailure("没有数据");
                    }
                }, throwable -> rc.onFailure(throwable.getLocalizedMessage()));
    }

    public void searchMovie(String query, final RequestCallback<JsonMovieBean> rc) {
        mDoubanApi.searchMovie(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(jsonMovieBean -> {
                    if (jsonMovieBean != null) {
                        rc.onSuccess(jsonMovieBean);
                    } else {
                        rc.onFailure("没有数据");
                    }
                }, throwable -> rc.onFailure(throwable.getLocalizedMessage()));
    }

}
