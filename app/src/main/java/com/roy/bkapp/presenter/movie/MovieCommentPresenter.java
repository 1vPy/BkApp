package com.roy.bkapp.presenter.movie;

import com.roy.bkapp.http.RequestCallback;
import com.roy.bkapp.http.api.bmob.BmobApiService;
import com.roy.bkapp.model.user_movie.comment.CommentMovie;
import com.roy.bkapp.presenter.BasePresenter;
import com.roy.bkapp.ui.view.movie.MovieCommentView;

import javax.inject.Inject;

/**
 * Created by 1vPy(Roy) on 2017/5/27.
 */

public class MovieCommentPresenter extends BasePresenter<MovieCommentView> {

    private BmobApiService mBmobApiService;

    @Inject
    public MovieCommentPresenter(BmobApiService bmobApiService){
        mBmobApiService = bmobApiService;
    }

    public void commentQuery(String movieId){
        mBmobApiService.commentQuery(movieId, new RequestCallback<CommentMovie>() {
            @Override
            public void onSuccess(CommentMovie commentMovie) {
                if(isAttached()){
                    getView().movieComment(commentMovie);
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
