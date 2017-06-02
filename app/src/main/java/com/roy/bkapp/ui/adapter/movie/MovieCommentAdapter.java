package com.roy.bkapp.ui.adapter.movie;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.roy.bkapp.R;
import com.roy.bkapp.model.user_movie.comment.CommentResult;

import java.util.List;

/**
 * Created by 1vPy(Roy) on 2017/5/27.
 */

public class MovieCommentAdapter extends BaseQuickAdapter<CommentResult, BaseViewHolder> {
    public MovieCommentAdapter(int layoutResId, List<CommentResult> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CommentResult item) {
        helper.setText(R.id.tv_username, item.getUsername());
        helper.setText(R.id.tv_comment, "\b\b" + item.getComment());
        helper.setRating(R.id.rb_movie, item.getRating());
    }
}
