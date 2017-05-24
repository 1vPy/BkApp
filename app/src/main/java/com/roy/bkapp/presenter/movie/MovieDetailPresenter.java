package com.roy.bkapp.presenter.movie;

import com.roy.bkapp.db.DbHelperService;
import com.roy.bkapp.http.RequestCallback;
import com.roy.bkapp.http.api.douban.DoubanApiService;
import com.roy.bkapp.model.movie.details.JsonDetailBean;
import com.roy.bkapp.presenter.BasePresenter;
import com.roy.bkapp.ui.view.movie.MovieDetailView;

import javax.inject.Inject;

/**
 * Created by 1vPy(Roy) on 2017/5/12.
 */

public class MovieDetailPresenter extends BasePresenter<MovieDetailView>{

    private DoubanApiService mDoubanApiService;

    private DbHelperService mDbHelperService;


    @Inject
    public MovieDetailPresenter(DoubanApiService doubanApiService,DbHelperService dbHelperService){
        mDoubanApiService = doubanApiService;
        mDbHelperService = dbHelperService;
    }

    public void getMovieDetail(String id){
        mDoubanApiService.getMovieDetail(id, new RequestCallback<JsonDetailBean>() {
            @Override
            public void onSuccess(JsonDetailBean jsonDetailBean) {
                if(isAttached()){
                    getView().movieDetail(jsonDetailBean);
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

    public void insertCollection(String id){
        mDbHelperService.insertCollection(id, new RequestCallback<String>() {
            @Override
            public void onSuccess(String s) {
                if(isAttached()){
                    getView().collectionSuccess(s);
                }
            }

            @Override
            public void onFailure(String message) {
                if(isAttached()){
                    getView().collectionFailed(message);
                }
            }
        });
    }

    public void deleteCollection(String id){
        mDbHelperService.deleteCollection(id, new RequestCallback<String>() {
            @Override
            public void onSuccess(String s) {
                if(isAttached()){
                    getView().deleteSuccess(s);
                }
            }

            @Override
            public void onFailure(String message) {
                if(isAttached()){
                    getView().deleteFailed(message);
                }
            }
        });
    }

    public void isCollected(String id){
        mDbHelperService.selectCollection(id,new RequestCallback<Boolean>(){

            @Override
            public void onSuccess(Boolean aBoolean) {
                if(isAttached()){
                    getView().isCollected(aBoolean);
                }
            }

            @Override
            public void onFailure(String message) {
                if(isAttached()){
                    getView().isCollected(false);
                }
            }
        });
    }
}
