package com.roy.bkapp.ui.view.movie;

import com.roy.bkapp.model.collection.MovieCollection;
import com.roy.bkapp.ui.view.BaseView;

import java.util.List;

/**
 * 电影收藏View接口
 * Created by 1vPy(Roy) on 2017/5/25.
 */

public interface MovieCollectionView extends BaseView{

    void movieCollection(List<MovieCollection> movieCollectionList);

    void deleteSuccess(String s);
}
