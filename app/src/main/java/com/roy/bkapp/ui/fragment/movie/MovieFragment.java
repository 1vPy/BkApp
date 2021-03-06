package com.roy.bkapp.ui.fragment.movie;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roy.bkapp.R;
import com.roy.bkapp.ui.adapter.DouBaseFragmentAdapter;
import com.roy.bkapp.ui.fragment.RootFragment;
import com.roy.bkapp.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by 1vPy(Roy) on 2017/5/11.
 */

public class MovieFragment extends RootFragment {
    private static final String TAG = MovieFragment.class.getSimpleName();
    @BindView(R.id.tl_movie)
    TabLayout tl_movie;

    @BindView(R.id.vp_movie)
    ViewPager vp_movie;

    private MovieHotFragment mMovieHotFragment;
    private MovieComeUpFragment mMovieComeUpFragment;

    private DouBaseFragmentAdapter mDouBaseFragmentAdapter;

    private List<String> mTitleList = new ArrayList<>();
    private List<Fragment> mFragmentList = new ArrayList<>();

    public static MovieFragment newInstance() {
        Bundle args = new Bundle();
        MovieFragment fragment = new MovieFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie, null);
        return view;
    }

    @Override
    protected void initViewAndEvent() {
        mMovieHotFragment = MovieHotFragment.newInstance();
        mMovieComeUpFragment = MovieComeUpFragment.newInstance();
        mFragmentList.add(mMovieHotFragment);
        mFragmentList.add(mMovieComeUpFragment);
        mTitleList.add("近期热播");
        mTitleList.add("即将上映");
        mDouBaseFragmentAdapter = new DouBaseFragmentAdapter(getChildFragmentManager(), mTitleList, mFragmentList);
        vp_movie.setAdapter(mDouBaseFragmentAdapter);
        vp_movie.setOffscreenPageLimit(3);

        tl_movie.setTabGravity(TabLayout.GRAVITY_FILL);
        tl_movie.setTabMode(TabLayout.MODE_FIXED);
        tl_movie.setupWithViewPager(vp_movie);
        tl_movie.setTabsFromPagerAdapter(mDouBaseFragmentAdapter);
        tl_movie.setTabTextColors(getResources().getColor(android.R.color.holo_blue_dark), getResources().getColor(android.R.color.holo_blue_light));
    }
}
