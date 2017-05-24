package com.roy.bkapp.presenter.movie;

import com.roy.bkapp.http.RequestCallback;
import com.roy.bkapp.http.api.douban.DoubanApiService;
import com.roy.bkapp.model.movie.JsonMovieBean;
import com.roy.bkapp.presenter.BasePresenter;
import com.roy.bkapp.ui.view.movie.MovieHotView;

import javax.inject.Inject;

/**
 * Created by 1vPy(Roy) on 2017/5/10.
 */

public class MovieHotPresenter extends BasePresenter<MovieHotView> {

    private DoubanApiService mDoubanApiService;

    @Inject
    public MovieHotPresenter(DoubanApiService doubanApiService){
        mDoubanApiService = doubanApiService;
    }

    public void getMovieHot(int start, int count, String city) {
        mDoubanApiService.getHotMovie(start, count, city, new RequestCallback<JsonMovieBean>() {
            @Override
            public void onSuccess(JsonMovieBean jsonMovieBean) {
                if(isAttached()){
                    getView().movieHot(jsonMovieBean);
                }
            }

            @Override
            public void onFailure(String message) {
                if(isAttached()){
                    getView().showError(message);
                }
            }
        });
    }
}
