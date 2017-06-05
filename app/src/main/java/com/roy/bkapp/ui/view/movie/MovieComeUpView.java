package com.roy.bkapp.ui.view.movie;

import com.roy.bkapp.model.movie.JsonMovieBean;
import com.roy.bkapp.ui.view.BaseView;

/**即将上映View接口
 *
 * Created by Administrator on 2017/5/11.
 */

public interface MovieComeUpView extends BaseView{
    void movieComeUp(JsonMovieBean jsonMovieBean);
}
