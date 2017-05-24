package com.roy.bkapp.ui.fragment.music;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roy.bkapp.R;
import com.roy.bkapp.model.music.billcategory.Content;
import com.roy.bkapp.model.music.billcategory.JsonMusicBillBean;
import com.roy.bkapp.presenter.music.MusicBillPresenter;
import com.roy.bkapp.ui.adapter.music.MusicBillAdapter;
import com.roy.bkapp.ui.fragment.BaseFragment;
import com.roy.bkapp.ui.view.music.MusicBillView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by 1vPy(Roy) on 2017/5/11.
 */

public class MusicBillFragment extends BaseFragment<MusicBillView, MusicBillPresenter> implements MusicBillView {
    @BindView(R.id.ryv_bill)
    RecyclerView ryv_bill;

    private MusicBillAdapter mMusicBillAdapter;
    private List<Content> mContentList = new ArrayList<>();

    public static MusicBillFragment newInstance() {
        Bundle args = new Bundle();
        MusicBillFragment fragment = new MusicBillFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_bill, container, false);
        return view;
    }


    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected void initViewAndEvent() {
        ryv_bill.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mMusicBillAdapter = new MusicBillAdapter(R.layout.item_music_bill, mContentList);
        ryv_bill.setAdapter(mMusicBillAdapter);
        mPresenter.getMusicBill();
    }

    @Override
    public void musicBill(JsonMusicBillBean jsonMusicBillBean) {
        mContentList.addAll(jsonMusicBillBean.getContent());
        mMusicBillAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String message) {

    }
}
