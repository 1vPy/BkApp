package com.roy.bkapp.ui.view.movie;

import com.roy.bkapp.model.movie.details.JsonDetailBean;
import com.roy.bkapp.ui.view.BaseView;

/**
 * Created by 1vPy(Roy) on 2017/5/12.
 */

public interface MovieDetailView extends BaseView{
    void movieDetail(JsonDetailBean jsonDetailBean);

    void collectionSuccess(String s);

    void collectionFailed(String s);

    void deleteSuccess(String s);

    void deleteFailed(String s);

    void isCollected(boolean b);
}
