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
import com.roy.bkapp.presenter.movie.MovieComeUpPresenter;
import com.roy.bkapp.ui.activity.movie.MovieDetailActivity;
import com.roy.bkapp.ui.adapter.movie.MovieComeUpAdapter;
import com.roy.bkapp.ui.custom.CustomLoadMoreView;
import com.roy.bkapp.ui.fragment.BaseFragment;
import com.roy.bkapp.ui.view.movie.MovieComeUpView;
import com.roy.bkapp.utils.LogUtils;
import com.roy.bkapp.utils.SnackBarUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/5/11.
 */

public class MovieComeUpFragment extends BaseFragment<MovieComeUpView,MovieComeUpPresenter> implements MovieComeUpView
        , BaseQuickAdapter.RequestLoadMoreListener
        , SwipeRefreshLayout.OnRefreshListener
        , View.OnClickListener
        , BaseQuickAdapter.OnItemClickListener{

    private static final String TAG = MovieHotFragment.class.getSimpleName();

    @BindView(R.id.cdl_root)
    CoordinatorLayout cdl_root;

    @BindView(R.id.fab_back_top)
    FloatingActionButton fab_back_top;

    @BindView(R.id.srl_movie)
    SwipeRefreshLayout srl_movie;

    @BindView(R.id.ryv_movie)
    RecyclerView ryv_movie;


    private MovieComeUpAdapter mMovieComeUpAdapter;

    private List<Subjects> mSubjectsList = new ArrayList<>();

    private int pageCount = 0;

    private static final int PAGE_SIZE = 20;

    private int mNowPosition = 0;

    public static MovieComeUpFragment newInstance() {
        Bundle args = new Bundle();
        MovieComeUpFragment fragment = new MovieComeUpFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_comeup, container, false);
        return view;
    }


    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected void initViewAndEvent() {
        ryv_movie.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMovieComeUpAdapter = new MovieComeUpAdapter(R.layout.item_movie_comeup, mSubjectsList);
        ryv_movie.setAdapter(mMovieComeUpAdapter);
        mMovieComeUpAdapter.setEmptyView(R.layout.view_loading, (ViewGroup) ryv_movie.getParent());
        mMovieComeUpAdapter.setLoadMoreView(new CustomLoadMoreView());
        mMovieComeUpAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        initData();
        initListener();
    }

    private void initData() {
        mPresenter.getMovieComeUp(pageCount * PAGE_SIZE, PAGE_SIZE);
    }

    private void initListener() {
        mMovieComeUpAdapter.setOnLoadMoreListener(this, ryv_movie);
        mMovieComeUpAdapter.setOnItemClickListener(this);
        srl_movie.setOnRefreshListener(this);
        fab_back_top.setOnClickListener(this);
        ryv_movie.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
    public void onRefresh() {
        pageCount = 0;
        mPresenter.getMovieComeUp(pageCount * PAGE_SIZE, PAGE_SIZE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_back_top:
                if(mNowPosition < 10){
                    ryv_movie.smoothScrollToPosition(0);
                }else{
                    ryv_movie.scrollToPosition(0);
                }
                fab_back_top.hide(new FloatingActionButton.OnVisibilityChangedListener() {
                    @Override
                    public void onHidden(FloatingActionButton fab) {
                        super.onHidden(fab);
                        SnackBarUtils.LongSnackbar(cdl_root, "已到顶部", SnackBarUtils.Info).show();
                    }
                });
                break;
        }
    }

    @Override
    public void onLoadMoreRequested() {
        pageCount++;
        mPresenter.getMovieComeUp(pageCount * PAGE_SIZE, PAGE_SIZE);
    }

    @Override
    public void movieComeUp(JsonMovieBean jsonMovieBean) {
        LogUtils.log(TAG, jsonMovieBean.getTitle(), LogUtils.DEBUG);
        if (srl_movie.isRefreshing()) {
            srl_movie.setRefreshing(false);
            mSubjectsList.clear();
            mMovieComeUpAdapter.setNewData(mSubjectsList);
            SnackBarUtils.LongSnackbar(cdl_root, "刷新成功", SnackBarUtils.Info).show();
        }
        if (mMovieComeUpAdapter.isLoading()) {
            mMovieComeUpAdapter.loadMoreComplete();
            LogUtils.log(TAG, "total:" + jsonMovieBean.getTotal() + "/current:" + (mSubjectsList.size() + jsonMovieBean.getSubjects().size()), LogUtils.DEBUG);
            if (jsonMovieBean.getTotal() <= (mSubjectsList.size() + jsonMovieBean.getSubjects().size())) {
                mMovieComeUpAdapter.loadMoreEnd();
            }
        }
        mSubjectsList.addAll(jsonMovieBean.getSubjects());
        mMovieComeUpAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String message) {
        if (srl_movie.isRefreshing()) {
            srl_movie.setRefreshing(false);
            SnackBarUtils.LongSnackbar(cdl_root, "刷新失败", SnackBarUtils.Warning).show();
        } else {
            SnackBarUtils.LongSnackbar(cdl_root, message, SnackBarUtils.Warning).show();
        }
        if (mMovieComeUpAdapter.isLoading()) {
            pageCount--;
            mMovieComeUpAdapter.loadMoreFail();
        }
        if (mSubjectsList == null || mSubjectsList.size() <= 0) {
            View error = LayoutInflater.from(getActivity()).inflate(R.layout.view_error, (ViewGroup) ryv_movie.getParent(), false);
            mMovieComeUpAdapter.setEmptyView(error);
            error.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMovieComeUpAdapter.setEmptyView(R.layout.view_loading, (ViewGroup) ryv_movie.getParent());
                    pageCount = 0;
                    mPresenter.getMovieComeUp(pageCount * PAGE_SIZE, PAGE_SIZE);
                }
            });
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        MovieDetailActivity.start(getActivity(),mSubjectsList.get(position).getId());
    }
}
