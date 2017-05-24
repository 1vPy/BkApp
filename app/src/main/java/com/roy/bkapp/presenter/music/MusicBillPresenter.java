package com.roy.bkapp.presenter.music;

import com.roy.bkapp.http.RequestCallback;
import com.roy.bkapp.http.api.baidu.BaiduApiService;
import com.roy.bkapp.model.music.billcategory.JsonMusicBillBean;
import com.roy.bkapp.presenter.BasePresenter;
import com.roy.bkapp.ui.view.music.MusicBillView;

import javax.inject.Inject;

/**
 * Created by 1vPy(Roy) on 2017/5/11.
 */

public class MusicBillPresenter extends BasePresenter<MusicBillView>{

    private BaiduApiService mBaiduApiService;

    @Inject
    public MusicBillPresenter(BaiduApiService baiduApiService){
        mBaiduApiService = baiduApiService;
    }

    public void getMusicBill(){
        mBaiduApiService.getMusicBillCategory(new RequestCallback<JsonMusicBillBean>() {
            @Override
            public void onSuccess(JsonMusicBillBean jsonMusicBillBean) {
                if(isAttached()){
                    getView().musicBill(jsonMusicBillBean);
                }

            }

            @Override
            public void onFailure(String message) {
                if(isAttached()){
                    getView().showError(message);
                }

            }
        });
    }
}
