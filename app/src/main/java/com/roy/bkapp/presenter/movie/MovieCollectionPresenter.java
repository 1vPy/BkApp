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

    public List<MovieCollection> selectAllCollection(){
        return mDbHelperService.selectAllCollection();
    }

    public void deleteCollection(String movieId){
        mDbHelperService.deleteCollection(movieId);
    }

}
