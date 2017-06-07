package com.roy.bkapp.presenter.movie;

import com.roy.bkapp.http.RequestCallback;
import com.roy.bkapp.http.api.bmob.BmobApiService;
import com.roy.bkapp.model.user.UserInfo;
import com.roy.bkapp.presenter.BasePresenter;
import com.roy.bkapp.ui.view.movie.MovieRatingView;
import com.roy.bkapp.utils.UserPreference;

import javax.inject.Inject;

/**
 * Created by 1vPy(Roy) on 2017/5/27.
 */

public class MovieRatingPresenter extends BasePresenter<MovieRatingView>{

    private BmobApiService mBmobApiService;

    private UserPreference mUserPreference;

    @Inject
    public MovieRatingPresenter(BmobApiService bmobApiService,UserPreference userPreference){
        mBmobApiService = bmobApiService;
        mUserPreference = userPreference;
    }

    public void addComment(String comment,String username,String movieId,float rating){
        mBmobApiService.addComment(comment, username, movieId, rating,new RequestCallback<String>() {
            @Override
            public void onSuccess(String s) {
                if(isAttached()){
                    getView().ratingSuccess(s);
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
}
