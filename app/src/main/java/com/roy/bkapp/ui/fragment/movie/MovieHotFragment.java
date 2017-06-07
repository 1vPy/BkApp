package com.roy.bkapp.ui.fragment.movie;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.roy.bkapp.R;
import com.roy.bkapp.model.movie.JsonMovieBean;
import com.roy.bkapp.model.movie.Subjects;
import com.roy.bkapp.presenter.movie.MovieHotPresenter;
import com.roy.bkapp.ui.activity.movie.MovieDetailActivity;
import com.roy.bkapp.ui.adapter.movie.MovieHotAdapter;
import com.roy.bkapp.ui.custom.CustomLoadMoreView;
import com.roy.bkapp.ui.fragment.BaseFragment;
import com.roy.bkapp.ui.view.movie.MovieHotView;
import com.roy.bkapp.utils.LogUtils;
import com.roy.bkapp.utils.SnackBarUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 正在热映电影Fragment
 *
 * Created by 1vPy(Roy) on 2017/5/11.
 */

public class MovieHotFragment extends BaseFragment<MovieHotView, MovieHotPresenter> implements MovieHotView
        , BaseQuickAdapter.RequestLoadMoreListener
        , SwipeRefreshLayout.OnRefreshListener
        , View.OnClickListener
        , BaseQuickAdapter.OnItemClickListener
        , BaseQuickAdapter.OnItemLongClickListener {
    private static final String TAG = MovieHotFragment.class.getSimpleName();

    @BindView(R.id.cdl_root)
    CoordinatorLayout cdl_root;

    @BindView(R.id.fab_back_top)
    FloatingActionButton fab_back_top;

    @BindView(R.id.srl_movie)
    SwipeRefreshLayout srl_movie;

    @BindView(R.id.ryv_movie)
    RecyclerView ryv_movie;

    private MovieHotAdapter mMovieHotAdapter;

    private List<Subjects> mSubjectsList = new ArrayList<>();

    private int pageCount = 0;

    private static final int PAGE_SIZE = 20;

    private int mNowPosition = 0;

    public static MovieHotFragment newInstance() {
        Bundle args = new Bundle();
        MovieHotFragment fragment = new MovieHotFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_hot, container, false);
        return view;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected void initViewAndEvent() {
        ryv_movie.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMovieHotAdapter = new MovieHotAdapter(R.layout.item_movie_hot, mSubjectsList);
        ryv_movie.setAdapter(mMovieHotAdapter);
        mMovieHotAdapter.setEmptyView(R.layout.view_loading, (ViewGroup) ryv_movie.getParent());//加载时的loading界面
        mMovieHotAdapter.setLoadMoreView(new CustomLoadMoreView());//设置自定义的效果
        mMovieHotAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);//设置item加载东湖
        initData();
        initListener();
    }

    private void initData() {
        mPresenter.getMovieHot(pageCount * PAGE_SIZE, PAGE_SIZE, "武汉");
    }

    private void initListener() {
        mMovieHotAdapter.setOnLoadMoreListener(this, ryv_movie);//设置上拉加载更多监听
        mMovieHotAdapter.setOnItemClickListener(this);
        srl_movie.setOnRefreshListener(this);//下拉刷新监听
        fab_back_top.setOnClickListener(this);

        //RecyclerView滑动监听(当第一个item消失时显示返回顶部按钮，反之显示)
        ryv_movie.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) ryv_movie.getLayoutManager();
                mNowPosition = linearLayoutManager.findFirstVisibleItemPosition();
                if (mNowPosition > 0) {
                    if (!fab_back_top.isShown())
                        fab_back_top.show();
                } else {
                    if (fab_back_top.isShown())
                        fab_back_top.hide();
                }
            }
        });
    }

    @Override
    public void movieHot(JsonMovieBean jsonMovieBean) {
        LogUtils.log(TAG, jsonMovieBean.getTitle(), LogUtils.DEBUG);
        if (srl_movie.isRefreshing()) {
            srl_movie.setRefreshing(false);
            mSubjectsList.clear();
            mMovieHotAdapter.setNewData(mSubjectsList);
            SnackBarUtils.LongSnackbar(cdl_root, getString(R.string.refresh_success), SnackBarUtils.Info).show();
        }
        if (mMovieHotAdapter.isLoading()) {
            mMovieHotAdapter.loadMoreComplete();
            LogUtils.log(TAG, "total:" + jsonMovieBean.getTotal() + "/current:" + (mSubjectsList.size() + jsonMovieBean.getSubjects().size()), LogUtils.DEBUG);
            //判断是否还有更多数据
            if (jsonMovieBean.getTotal() <= (mSubjectsList.size() + jsonMovieBean.getSubjects().size())) {
                mMovieHotAdapter.loadMoreEnd();
            }
        }
        mSubjectsList.addAll(jsonMovieBean.getSubjects());
        mMovieHotAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String message) {
        if (srl_movie.isRefreshing()) {
            srl_movie.setRefreshing(false);
            SnackBarUtils.LongSnackbar(cdl_root, getString(R.string.refresh_failed), SnackBarUtils.Warning).show();
        } else {
            SnackBarUtils.LongSnackbar(cdl_root, message, SnackBarUtils.Warning).show();
        }
        if (mMovieHotAdapter.isLoading()) {
            pageCount--;
            mMovieHotAdapter.loadMoreFail();
        }
        if (mSubjectsList == null || mSubjectsList.size() <= 0) {
            View error = LayoutInflater.from(getActivity()).inflate(R.layout.view_error, (ViewGroup) ryv_movie.getParent(), false);
            mMovieHotAdapter.setEmptyView(error);
            error.setOnClickListener(v -> {
                mMovieHotAdapter.setEmptyView(R.layout.view_loading, (ViewGroup) ryv_movie.getParent());
                pageCount = 0;
                mPresenter.getMovieHot(pageCount * PAGE_SIZE, PAGE_SIZE, "武汉");
            });
        }
    }

    @Override
    public void onLoadMoreRequested() {
        pageCount++;
        mPresenter.getMovieHot(pageCount * PAGE_SIZE, PAGE_SIZE, "武汉");
    }

    @Override
    public void onRefresh() {
        pageCount = 0;
        mPresenter.getMovieHot(pageCount * PAGE_SIZE, PAGE_SIZE, "武汉");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_back_top:
                if (mNowPosition < 10) {
                    ryv_movie.smoothScrollToPosition(0);
                } else {
                    ryv_movie.scrollToPosition(0);
                }
                //返回顶部按钮隐藏监听
                fab_back_top.hide(new FloatingActionButton.OnVisibilityChangedListener() {
                    @Override
                    public void onHidden(FloatingActionButton fab) {
                        super.onHidden(fab);
                        SnackBarUtils.LongSnackbar(cdl_root, getString(R.string.is_top), SnackBarUtils.Info).show();
                    }
                });
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        MovieDetailActivity.start(getActivity(), mSubjectsList.get(position).getId());
    }

    @Override
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
        return false;
    }
}
