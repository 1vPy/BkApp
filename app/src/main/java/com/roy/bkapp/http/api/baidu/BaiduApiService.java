package com.roy.bkapp.http.api.baidu;

import com.roy.bkapp.BkKit;
import com.roy.bkapp.http.RequestCallback;
import com.roy.bkapp.model.music.billcategory.JsonMusicBillBean;
import com.roy.bkapp.model.music.billlist.JsonSongListBean;
import com.roy.bkapp.model.music.musicinfo.JsonMusicInfoBean;
import com.roy.bkapp.model.music.search.JsonSearchBean;
import com.roy.bkapp.utils.AESTools;
import com.roy.bkapp.utils.LogUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 1vPy(Roy) on 2017/4/13.
 */

public class BaiduApiService {
    private static final String TAG = BaiduApiService.class.getSimpleName();

    private BaiduApi mBaiduApi;

    public BaiduApiService(BaiduApi baiduApi) {
        mBaiduApi = baiduApi;
    }

    public void getMusicBillCategory(final RequestCallback<JsonMusicBillBean> rc) {
        mBaiduApi.getMusicBillCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JsonMusicBillBean>() {
                    @Override
                    public void accept(@NonNull JsonMusicBillBean jsonMusicBillBean) throws Exception {
                        if (jsonMusicBillBean != null) {
                            rc.onSuccess(jsonMusicBillBean);
                        } else {
                            rc.onFailure("没有数据");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        rc.onFailure(throwable.getLocalizedMessage());
                    }
                });
    }

    public void getMusicBillList(int type, int offset, int size, final RequestCallback<JsonSongListBean> rc) {
        mBaiduApi.getMusicBillList(type, offset, size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JsonSongListBean>() {
                    @Override
                    public void accept(@NonNull JsonSongListBean jsonSongListBean) throws Exception {
                        if (jsonSongListBean != null) {
                            rc.onSuccess(jsonSongListBean);
                        } else {
                            rc.onFailure("没有数据");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        rc.onFailure(throwable.getLocalizedMessage());
                    }
                });
    }

    public void getMusicSearch(String query, int pageNo, int pageSize, final RequestCallback<JsonSearchBean> rc) {
        mBaiduApi.getMusicSearch(query, pageNo, pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JsonSearchBean>() {
                    @Override
                    public void accept(@NonNull JsonSearchBean jsonSearchBean) throws Exception {
                        if (jsonSearchBean != null) {
                            rc.onSuccess(jsonSearchBean);
                        } else {
                            rc.onFailure("没有数据");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        rc.onFailure(throwable.getLocalizedMessage());
                    }
                });
    }

    public void getMusicInfo(String songid, final RequestCallback<JsonMusicInfoBean> rc) {
        mBaiduApi.getMusicInfo(songInfo(songid))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JsonMusicInfoBean>() {
                    @Override
                    public void accept(@NonNull JsonMusicInfoBean jsonMusicInfoBean) throws Exception {
                        if (jsonMusicInfoBean != null) {
                            rc.onSuccess(jsonMusicInfoBean);
                        } else {
                            rc.onFailure("没有数据");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        rc.onFailure(throwable.getLocalizedMessage());
                    }
                });
    }

    public static String songInfo(String songid) {
        StringBuffer sb = new StringBuffer("http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.6.5.6&format=json");
        String str = "songid=" + songid + "&ts=" + System.currentTimeMillis();
        String e = AESTools.encrpty(str);
        sb.append("&method=").append("baidu.ting.song.getInfos")
                .append("&").append(str)
                .append("&e=").append(e);
        LogUtils.log(TAG, sb.toString(), LogUtils.DEBUG);
        return sb.toString();
    }

}
