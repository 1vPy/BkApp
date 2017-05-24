package com.roy.bkapp.ui.fragment.music;

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
import com.roy.bkapp.ui.fragment.movie.MovieFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by 1vPy(Roy) on 2017/5/11.
 */

public class MusicFragment extends RootFragment{
    private static final String TAG = MusicFragment.class.getSimpleName();
    @BindView(R.id.tl_music)
    TabLayout tl_music;
    @BindView(R.id.vp_music)
    ViewPager vp_music;

    private MusicBillFragment mMusicBillFragment;

    private DouBaseFragmentAdapter mDouBaseFragmentAdapter;

    private List<String> mTitleList = new ArrayList<>();
    private List<Fragment> mFragmentList = new ArrayList<>();

    public static MusicFragment newInstance() {
        Bundle args = new Bundle();
        MusicFragment fragment = new MusicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, null);
        return view;
    }

    @Override
    protected void initViewAndEvent() {
        mMusicBillFragment = MusicBillFragment.newInstance();
        //mMusicSheetsFragment = mMusicSheetsFragment.newInstance();
        mFragmentList.add(mMusicBillFragment);
        //mFragmentList.add(mMusicSheetsFragment);
        mTitleList.add("音乐榜");
        //mTitleList.add(getString(R.string.my_music));

        mDouBaseFragmentAdapter = new DouBaseFragmentAdapter(getChildFragmentManager(), mTitleList, mFragmentList);
        vp_music.setAdapter(mDouBaseFragmentAdapter);
        vp_music.setOffscreenPageLimit(3);

        tl_music.setTabGravity(TabLayout.GRAVITY_FILL);
        tl_music.setTabMode(TabLayout.MODE_FIXED);
        tl_music.setupWithViewPager(vp_music);
        tl_music.setTabsFromPagerAdapter(mDouBaseFragmentAdapter);
        tl_music.setTabTextColors(getResources().getColor(android.R.color.holo_blue_dark), getResources().getColor(android.R.color.holo_blue_light));
    }
}
