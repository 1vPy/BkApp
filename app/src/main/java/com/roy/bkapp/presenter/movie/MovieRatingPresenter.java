package com.roy.bkapp.presenter.movie;

import com.roy.bkapp.http.RequestCallback;
import com.roy.bkapp.http.api.bmob.BmobApiService;
import com.roy.bkapp.presenter.BasePresenter;
import com.roy.bkapp.ui.view.movie.MovieRatingView;

import javax.inject.Inject;

/**
 * Created by 1vPy(Roy) on 2017/5/27.
 */

public class MovieRatingPresenter extends BasePresenter<MovieRatingView>{

    private BmobApiService mBmobApiService;

    @Inject
    public MovieRatingPresenter(BmobApiService bmobApiService){
        mBmobApiService = bmobApiService;
    }

    public void addComment(String comment,String username,String movieId,int rating){
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
}
