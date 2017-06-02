package com.roy.bkapp.ui.activity.movie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import com.roy.bkapp.R;
import com.roy.bkapp.presenter.movie.MovieRatingPresenter;
import com.roy.bkapp.ui.activity.BaseSwipeBackActivity;
import com.roy.bkapp.ui.view.movie.MovieRatingView;
import com.roy.bkapp.utils.UserPreference;

import butterknife.BindView;

/**
 * Created by 1vPy(Roy) on 2017/5/27.
 */

public class MovieRatingActivity extends BaseSwipeBackActivity<MovieRatingView, MovieRatingPresenter>
        implements MovieRatingView
        , View.OnClickListener {

    @BindView(R.id.rb_movie)
    RatingBar rb_movie;

    @BindView(R.id.et_comment)
    AppCompatEditText et_comment;

    @BindView(R.id.btn_publish)
    AppCompatButton btn_publish;

    private String mId;

    public static void start(Context context, String movieId) {
        Intent intent = new Intent(context, MovieRatingActivity.class);
        intent.putExtra("movieId", movieId);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_rating);
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViewAndEvent() {
        mToolbar.setTitle("发表评论");
        mToolbar.setNavigationOnClickListener(v -> this.finish());
        if (getIntent() != null) {
            mId = getIntent().getStringExtra("movieId");
        }
        btn_publish.setOnClickListener(this);
    }

    @Override
    public void ratingSuccess(String s) {
        Toast.makeText(this, "发表成功", Toast.LENGTH_LONG).show();
        this.finish();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, "发表失败：" + message, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_publish:
                publish();
                break;
        }
    }

    private void publish() {
        if (rb_movie.getProgress() <= 0) {
            Toast.makeText(this, "请完成评分后发表", Toast.LENGTH_LONG).show();
        } else if (et_comment.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "评论不能为空", Toast.LENGTH_LONG).show();
        } else {
            mPresenter.addComment(et_comment.getText().toString().trim(), UserPreference.getUserPreference(this).readUserInfo().getUsername(), mId, rb_movie.getRating());
        }
    }
}
