package com.roy.bkapp.presenter.movie;

import com.roy.bkapp.db.DbHelperService;
import com.roy.bkapp.http.RequestCallback;
import com.roy.bkapp.http.api.bmob.BmobApiService;
import com.roy.bkapp.http.api.douban.DoubanApiService;
import com.roy.bkapp.model.collection.MovieCollection;
import com.roy.bkapp.model.movie.details.JsonDetailBean;
import com.roy.bkapp.model.user.UserInfo;
import com.roy.bkapp.presenter.BasePresenter;
import com.roy.bkapp.ui.view.movie.MovieDetailView;
import com.roy.bkapp.utils.UserPreference;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by 1vPy(Roy) on 2017/5/12.
 */

public class MovieDetailPresenter extends BasePresenter<MovieDetailView>{

    private DoubanApiService mDoubanApiService;

    private DbHelperService mDbHelperService;

    private BmobApiService mBmobApiService;

    private UserPreference mUserPreference;


    @Inject
    public MovieDetailPresenter(DoubanApiService doubanApiService,DbHelperService dbHelperService,BmobApiService bmobApiService,UserPreference userPreference){
        mDoubanApiService = doubanApiService;
        mDbHelperService = dbHelperService;
        mBmobApiService = bmobApiService;
        mUserPreference = userPreference;
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

    public void insertCollection(MovieCollection movieCollection){
        mDbHelperService.insertCollection(movieCollection);
    }

    public void deleteCollection(String id){
        mDbHelperService.deleteCollection(id);
    }

    public boolean isCollected(String id){
        return mDbHelperService.selectCollection(id);
    }

    public MovieCollection searchMovieCollection(String movieId){
        return mDbHelperService.searchCollection(movieId);
    }

    public void praiseNum(String movieId){
        mBmobApiService.praiseNum(movieId, new RequestCallback<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                if(isAttached()){
                    getView().praiseNum(integer);
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

    public void isPraise(String movieId,String username){
        mBmobApiService.isPraise(movieId, username, new RequestCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if(isAttached()){
                    getView().isPraise(aBoolean);
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

    public void addPraise(String movieId,String username){
        mBmobApiService.addPraise(movieId, username, new RequestCallback<String>() {
            @Override
            public void onSuccess(String s) {
                if(isAttached()){
                    getView().praiseSuccess(s);
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

    public void commentNum(String movieId){
        mBmobApiService.commentNum(movieId, new RequestCallback<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                if(isAttached()){
                    getView().commentNum(integer);
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

    public UserInfo readUserInfo(){
        return mUserPreference.readUserInfo();
    }

    public boolean readIsSync(){
        return mUserPreference.readIsSync();
    }

    public void syncMovie(MovieCollection movieCollection){
        List<MovieCollection> movieCollections = new ArrayList<>();
        movieCollections.add(movieCollection);
        mBmobApiService.syncCollection(movieCollections, new RequestCallback<String>() {
            @Override
            public void onSuccess(String s) {
                mDbHelperService.toggleSyncMovie(s, true);
                if(isAttached()){
                    getView().syncSuccess("同步收藏成功");
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

    public void deleteCloudMovie(String movieId){
        mBmobApiService.deleteCloudCollection(movieId, new RequestCallback<String>() {
            @Override
            public void onSuccess(String s) {
                if(isAttached()){
                    getView().deleteCloudSuccess(s);
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
