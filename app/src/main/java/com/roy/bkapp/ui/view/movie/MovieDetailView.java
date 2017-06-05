package com.roy.bkapp.ui.view.movie;

import com.roy.bkapp.model.movie.details.JsonDetailBean;
import com.roy.bkapp.ui.view.BaseView;

/**
 * 电影详情View接口
 *
 * Created by 1vPy(Roy) on 2017/5/12.
 */

public interface MovieDetailView extends BaseView{
    //电影详情
    void movieDetail(JsonDetailBean jsonDetailBean);

    //收藏成功
    void collectionSuccess(String s);

    //删除收藏成功
    void deleteSuccess(String s);

    //是否已经收藏
    void isCollected(boolean b);

    //是否已经点赞
    void isPraise(boolean b);

    //点赞数量
    void praiseNum(int num);

    //点赞成功
    void praiseSuccess(String s);

    //评论数量
    void commentNum(int num);
}
