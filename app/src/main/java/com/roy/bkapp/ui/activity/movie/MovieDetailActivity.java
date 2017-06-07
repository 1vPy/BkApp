package com.roy.bkapp.ui.activity.movie;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.roy.bkapp.BkKit;
import com.roy.bkapp.R;
import com.roy.bkapp.model.collection.MovieCollection;
import com.roy.bkapp.model.movie.Casts;
import com.roy.bkapp.model.movie.DirectorActor;
import com.roy.bkapp.model.movie.Directors;
import com.roy.bkapp.model.movie.details.JsonDetailBean;
import com.roy.bkapp.presenter.movie.MovieDetailPresenter;
import com.roy.bkapp.ui.activity.BaseSwipeBackActivity;
import com.roy.bkapp.ui.activity.common.WebViewActivity;
import com.roy.bkapp.ui.adapter.movie.MovieDetailAdapter;
import com.roy.bkapp.ui.view.movie.MovieDetailView;
import com.roy.bkapp.utils.ImageUtils;
import com.roy.bkapp.utils.LogUtils;
import com.roy.bkapp.utils.SnackBarUtils;
import com.roy.bkapp.utils.UserPreference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * Created by 1vPy(Roy) on 2017/5/12.
 */

public class MovieDetailActivity extends BaseSwipeBackActivity<MovieDetailView, MovieDetailPresenter>
        implements MovieDetailView
        , View.OnClickListener
        , NestedScrollView.OnScrollChangeListener {
    private static final String TAG = MovieDetailActivity.class.getSimpleName();


    @BindView(R.id.cdl_root)
    CoordinatorLayout cdl_root;
    @BindView(R.id.iv_movie_detail_image)
    ImageView iv_movie_detail_image;
    @BindView(R.id.iv_movie)
    ImageView iv_movie;
    @BindView(R.id.tv_rating)
    TextView tv_rating;
    @BindView(R.id.tv_rating_num)
    TextView tv_rating_num;
    @BindView(R.id.tv_year)
    TextView tv_year;
    @BindView(R.id.tv_type)
    TextView tv_type;
    @BindView(R.id.tv_country)
    TextView tv_country;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_original_name)
    TextView tv_original_name;
    @BindView(R.id.tv_summary)
    TextView tv_summary;
    @BindView(R.id.ryv_movie)
    RecyclerView ryv_movie;
    @BindView(R.id.tv_more_details)
    TextView tv_more_details;
    @BindView(R.id.tv_buy_tickets)
    TextView tv_buy_tickets;
    @BindView(R.id.fab_collection)
    FloatingActionButton fab_collection;
    @BindView(R.id.nsv_scroller)
    NestedScrollView nsv_scroller;
    @BindView(R.id.detail_bottom)
    FrameLayout detail_bottom;
    @BindView(R.id.tv_detail_bottom_share)
    TextView tv_detail_bottom_share;
    @BindView(R.id.tv_detail_bottom_comment)
    TextView tv_detail_bottom_comment;
    @BindView(R.id.tv_detail_bottom_praise)
    TextView tv_detail_bottom_praise;


    private RelativeLayout viewError;
    private LinearLayout viewLoading;

    private MovieDetailAdapter mMovieDetailAdapter;

    private String mId;
    private JsonDetailBean mJsonDetailBean;
    private List<DirectorActor> mDirectorActors = new ArrayList<>();
    private List<DirectorActor> mDirectorActorList = new ArrayList<>();
    private boolean isCollected = false;

    private boolean isPraise = false;
    private int praiseNum = 0;
    private boolean isBottomShow = true;

    public static void start(Context context, String id) {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra("movieId", id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mId != null){
            mPresenter.commentNum(mId);
        }
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViewAndEvent() {
        mToolbar.setNavigationOnClickListener(v -> MovieDetailActivity.this.finish());
        ryv_movie.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        nsv_scroller.setOnScrollChangeListener(this);
        mMovieDetailAdapter = new MovieDetailAdapter(R.layout.item_movie_detail, mDirectorActorList);
        ryv_movie.setAdapter(mMovieDetailAdapter);
        if (getIntent() != null) {
            mId = getIntent().getStringExtra("movieId");
        }
        if (mId != null) {
            mPresenter.getMovieDetail(mId);
            mPresenter.isCollected(mId);
            mPresenter.praiseNum(mId);
            if (mPresenter.readUserInfo() != null) {
                tv_detail_bottom_praise.setEnabled(false);
                mPresenter.isPraise(mId, mPresenter.readUserInfo().getUsername());
            }
        }


        ViewGroup parent = (ViewGroup) nsv_scroller.getParent();
        View.inflate(this, R.layout.view_error, parent);
        View.inflate(this, R.layout.view_loading, parent);
        viewError = (RelativeLayout) parent.findViewById(R.id.view_error);
        viewLoading = (LinearLayout) parent.findViewById(R.id.view_loading);


        viewError.setOnClickListener(this);
        fab_collection.setOnClickListener(this);
        tv_detail_bottom_share.setOnClickListener(this);
        tv_detail_bottom_comment.setOnClickListener(this);
        tv_detail_bottom_praise.setOnClickListener(this);
        tv_more_details.setOnClickListener(this);
        tv_buy_tickets.setOnClickListener(this);

        onLoading();

    }

    @Override
    public void finish() {
        if (!isCollected) {
            Intent intent = new Intent();
            intent.putExtra("position", getIntent().getIntExtra("position", -1));
            setResult(RESULT_OK, intent);
        }
        super.finish();
    }

    private void initData(JsonDetailBean jsonDetailBean) {
        mJsonDetailBean = jsonDetailBean;
        if (!TextUtils.isEmpty(jsonDetailBean.getTitle())) {
            mToolbar.setTitle(jsonDetailBean.getTitle());
            tv_name.setText(jsonDetailBean.getTitle());
        }
        ImageUtils.displayImage(this, jsonDetailBean.getImages().getLarge(), iv_movie_detail_image);
        if (jsonDetailBean.getImages() != null) {
            ImageUtils.displayImage(BkKit.getContext(), jsonDetailBean.getImages().getLarge(), iv_movie);
        }
        if (jsonDetailBean.getRating() != null) {
            tv_rating.setText("评分" + jsonDetailBean.getRating().getAverage());
        }
        tv_rating_num.setText(jsonDetailBean.getRatings_count() + "人 评分");
        tv_year.setText(jsonDetailBean.getYear() + " 上映");

        if (jsonDetailBean.getGenres() != null && jsonDetailBean.getGenres().size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String s : jsonDetailBean.getGenres()) {
                stringBuilder.append(s + "/");
            }
            tv_type.setText("类型：" + stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1));
        }
        if (jsonDetailBean.getCountries() != null && jsonDetailBean.getCountries().size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String s : jsonDetailBean.getCountries()) {
                stringBuilder.append(s + "/");
            }
            tv_country.setText("国家：" + stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1));
        }
        tv_original_name.setText(jsonDetailBean.getOriginal_title() + " [原名]");
        tv_summary.setText(jsonDetailBean.getSummary());

        initDirectorActorList(jsonDetailBean);

        onComplete();
    }


    /**
     * 导演、演员合并为一个list
     * @param jsonDetailBean
     */
    private void initDirectorActorList(JsonDetailBean jsonDetailBean) {
        for (Directors directors : jsonDetailBean.getDirectors()) {
            DirectorActor directorActor = new DirectorActor();
            directorActor.setAvatars(directors.getAvatars());
            directorActor.setName(directors.getName());
            directorActor.setId(directors.getId());
            directorActor.setRole(0);
            mDirectorActors.add(directorActor);
        }

        for (Casts casts : jsonDetailBean.getCasts()) {
            DirectorActor directorActor = new DirectorActor();
            directorActor.setAvatars(casts.getAvatars());
            directorActor.setName(casts.getName());
            directorActor.setId(casts.getId());
            directorActor.setRole(1);
            mDirectorActors.add(directorActor);
        }
        LogUtils.log(TAG, "size: " + mDirectorActorList.size(), LogUtils.DEBUG);
        mDirectorActorList.addAll(mDirectorActors);
        mMovieDetailAdapter.notifyDataSetChanged();

    }

    @Override
    public void movieDetail(JsonDetailBean jsonDetailBean) {
        initData(jsonDetailBean);
    }

    @Override
    public void collectionSuccess(String s) {
        fab_collection.setSelected(true);
        isCollected = true;
        SnackBarUtils.LongSnackbar(cdl_root, getString(R.string.collect_success), SnackBarUtils.Info).show();
    }

    @Override
    public void deleteSuccess(String s) {
        fab_collection.setSelected(false);
        isCollected = false;
        SnackBarUtils.LongSnackbar(cdl_root, getString(R.string.collect_delete), SnackBarUtils.Info).show();
    }


    @Override
    public void isCollected(boolean b) {
        isCollected = b;
        if (b) {
            //mPresenter.deleteCollection(mId);
            fab_collection.setSelected(true);
        } else {
            //mPresenter.insertCollection(mId);
            fab_collection.setSelected(false);
        }
    }

    @Override
    public void isPraise(boolean b) {
        isPraise = b;
        if (b) {
            Drawable drawable = getResources().getDrawable(R.drawable.icon_collect_n);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tv_detail_bottom_praise.setCompoundDrawables(null, drawable, null, null);
        }
        tv_detail_bottom_praise.setEnabled(true);
    }

    @Override
    public void praiseNum(int num) {
        praiseNum = num;
        tv_detail_bottom_praise.setText("点赞(" + num + ")");
    }

    @Override
    public void praiseSuccess(String s) {
        isPraise = true;
        Drawable drawable = getResources().getDrawable(R.drawable.icon_collect_n);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tv_detail_bottom_praise.setCompoundDrawables(null, drawable, null, null);
        tv_detail_bottom_praise.setText("点赞(" + (praiseNum + 1) + ")");
        SnackBarUtils.LongSnackbar(cdl_root, getString(R.string.praise_success), SnackBarUtils.Info).show();

    }

    @Override
    public void commentNum(int num) {
        tv_detail_bottom_comment.setText("评论(" + num + ")");
    }

    @Override
    public void showError(String message) {
        onError();
        SnackBarUtils.LongSnackbar(cdl_root, message, SnackBarUtils.Warning).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_more_details:
                openWebView(mJsonDetailBean.getAlt());
                break;
            case R.id.tv_buy_tickets:
                openWebView(mJsonDetailBean.getSchedule_url());
                break;
            case R.id.fab_collection:
                collection();
                break;
            case R.id.view_error:
                mPresenter.getMovieDetail(mId);
                onLoading();
                break;
            case R.id.tv_detail_bottom_praise:
                praise();
                break;
            case R.id.tv_detail_bottom_comment:
                MovieCommentActivity.start(this, mId);
                break;
        }
    }

    private void collection() {
        if (isCollected)
            mPresenter.deleteCollection(mId);
        else {
            MovieCollection collection = new MovieCollection(mJsonDetailBean.getImages().getLarge(), mJsonDetailBean.getTitle(), mJsonDetailBean.getId());
            mPresenter.insertCollection(collection);
        }
    }

    private void praise() {
        if (isPraise) {
            SnackBarUtils.LongSnackbar(cdl_root, getString(R.string.not_praise_again), SnackBarUtils.Info).show();
        } else {
            if (mPresenter.readUserInfo() != null) {
                mPresenter.addPraise(mId, mPresenter.readUserInfo().getUsername());
            } else {
                SnackBarUtils.LongSnackbar(cdl_root, getString(R.string.login_praise), SnackBarUtils.Warning).show();
            }
        }
    }

    private void openWebView(String url) {
        Intent intent = new Intent();
        intent.putExtra("url", url);
        intent.setClass(MovieDetailActivity.this, WebViewActivity.class);
        startActivity(intent);
    }

    //加载时显示
    private void onLoading() {
        viewError.setVisibility(View.INVISIBLE);
        nsv_scroller.setVisibility(View.INVISIBLE);
        viewLoading.setVisibility(View.VISIBLE);
        fab_collection.setEnabled(false);
    }

    //加载出错时显示
    private void onError() {
        viewError.setVisibility(View.VISIBLE);
        nsv_scroller.setVisibility(View.INVISIBLE);
        viewLoading.setVisibility(View.INVISIBLE);
        fab_collection.setEnabled(false);
    }

    //加载完成时显示
    private void onComplete() {
        viewError.setVisibility(View.INVISIBLE);
        nsv_scroller.setVisibility(View.VISIBLE);
        viewLoading.setVisibility(View.INVISIBLE);
        fab_collection.setEnabled(true);
    }


    //控制底部菜单栏的显示与隐藏
    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if (scrollY - oldScrollY > 0 && isBottomShow) {  //下移隐藏
            isBottomShow = false;
            detail_bottom.animate().translationY(detail_bottom.getHeight());
        } else if (scrollY - oldScrollY < 0 && !isBottomShow) {    //上移出现
            isBottomShow = true;
            detail_bottom.animate().translationY(0);
        }
    }

}
