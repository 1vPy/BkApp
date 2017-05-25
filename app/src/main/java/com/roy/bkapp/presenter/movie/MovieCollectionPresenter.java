package com.roy.bkapp.presenter.movie;

import com.roy.bkapp.db.DbHelperService;
import com.roy.bkapp.http.RequestCallback;
import com.roy.bkapp.model.collection.MovieCollection;
import com.roy.bkapp.presenter.BasePresenter;
import com.roy.bkapp.ui.view.movie.MovieCollectionView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by 1vPy(Roy) on 2017/5/25.
 */

public class MovieCollectionPresenter extends BasePresenter<MovieCollectionView>{

    private DbHelperService mDbHelperService;

    @Inject
    public MovieCollectionPresenter(DbHelperService dbHelperService){
        mDbHelperService = dbHelperService;
    }

    public void selectAllCollection(){
        mDbHelperService.selectAllCollection(new RequestCallback<List<MovieCollection>>() {
            @Override
            public void onSuccess(List<MovieCollection> movieCollectionList) {
                if(isAttached()){
                    getView().movieCollection(movieCollectionList);
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

    public void deleteCollection(String movieId){
        mDbHelperService.deleteCollection(movieId, new RequestCallback<String>() {
            @Override
            public void onSuccess(String s) {
                if(isAttached()){
                    getView().deleteSuccess(s);
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
