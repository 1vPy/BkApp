package com.roy.bkapp.ui.view.movie;

import com.roy.bkapp.model.user_movie.comment.CommentMovie;
import com.roy.bkapp.ui.view.BaseView;

/**
 * 电影评论View接口
 *
 * Created by 1vPy(Roy) on 2017/5/27.
 */

public interface MovieCommentView extends BaseView{
    void movieComment(CommentMovie commentMovie);
}
