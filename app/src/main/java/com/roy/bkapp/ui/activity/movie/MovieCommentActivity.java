package com.roy.bkapp.ui.activity.movie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.roy.bkapp.R;
import com.roy.bkapp.model.user_movie.comment.CommentMovie;
import com.roy.bkapp.model.user_movie.comment.CommentResult;
import com.roy.bkapp.presenter.movie.MovieCommentPresenter;
import com.roy.bkapp.ui.activity.BaseSwipeBackActivity;
import com.roy.bkapp.ui.adapter.movie.MovieCommentAdapter;
import com.roy.bkapp.ui.view.movie.MovieCommentView;
import com.roy.bkapp.utils.LogUtils;
import com.roy.bkapp.utils.ToastUtils;
import com.roy.bkapp.utils.UserPreference;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by 1vPy(Roy) on 2017/5/27.
 */

public class MovieCommentActivity extends BaseSwipeBackActivity<MovieCommentView, MovieCommentPresenter>
        implements MovieCommentView
        , View.OnClickListener {
    public static final String TAG = MovieCommentActivity.class.getSimpleName();

    @BindView(R.id.ryv_comment)
    RecyclerView ryv_comment;

    @BindView(R.id.iv_rating)
    ImageView iv_rating;

    private String mId;

    private MovieCommentAdapter mMovieCommentAdapter;

    private List<CommentResult> mCommentResultList = new ArrayList<>();

    public static void start(Context context, String movieId) {
        Intent intent = new Intent(context, MovieCommentActivity.class);
        intent.putExtra("movieId", movieId);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_comment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mId != null) {
            mPresenter.commentQuery(mId);
        }
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViewAndEvent() {
        mToolbar.setTitle(R.string.movie_comment);
        mToolbar.setNavigationOnClickListener(v -> this.finish());
        if (getIntent() != null) {
            mId = getIntent().getStringExtra("movieId");
        }
        mMovieCommentAdapter = new MovieCommentAdapter(R.layout.item_movie_comment, mCommentResultList);
        ryv_comment.setLayoutManager(new LinearLayoutManager(this));
        ryv_comment.setAdapter(mMovieCommentAdapter);
/*        if (mId != null) {
            mPresenter.commentQuery(mId);
        }*/

        iv_rating.setOnClickListener(this);
    }

    @Override
    public void movieComment(CommentMovie commentMovie) {
        mCommentResultList.clear();
        mCommentResultList.addAll(commentMovie.getResults());
        mMovieCommentAdapter.notifyDataSetChanged();
    }


    @Override
    public void showError(String message) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_rating:
                if(UserPreference.getUserPreference(this).readUserInfo()!= null){
                    MovieRatingActivity.start(this,mId);
                }else{
                    ToastUtils.showToast(R.string.login_comment);
                }
                break;
        }
    }
}
