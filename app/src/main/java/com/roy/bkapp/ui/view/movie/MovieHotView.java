package com.roy.bkapp.ui.view.movie;

import com.roy.bkapp.model.movie.JsonMovieBean;
import com.roy.bkapp.ui.view.BaseView;

/**
 * 正在热映View接口
 * Created by 1vPy(Roy) on 2017/5/10.
 */

public interface MovieHotView extends BaseView{
    void movieHot(JsonMovieBean jsonMovieBean);
}
