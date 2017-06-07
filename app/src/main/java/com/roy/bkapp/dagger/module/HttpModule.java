package com.roy.bkapp.dagger.module;

import com.roy.bkapp.BkKit;
import com.roy.bkapp.http.api.baidu.BaiduApi;
import com.roy.bkapp.http.api.baidu.BaiduApiService;
import com.roy.bkapp.http.api.bmob.BmobApi;
import com.roy.bkapp.http.api.bmob.BmobApiService;
import com.roy.bkapp.http.api.douban.DoubanApi;
import com.roy.bkapp.http.api.douban.DoubanApiService;
import com.roy.bkapp.utils.UserPreference;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 1vPy(Roy) on 2017/5/11.
 */

@Module
public class HttpModule {
    @Singleton
    @Provides
    Retrofit.Builder provideRetrofitBuilder() {
        return new Retrofit.Builder();
    }

    @Singleton
    @Provides
    OkHttpClient.Builder provideOkHttpClientBuilder() {
        return new OkHttpClient.Builder();
    }

    @Singleton
    @Provides
    DoubanApi provideDoubanApi(Retrofit.Builder builder, OkHttpClient okHttpClient) {
        return createRetrofit(builder, DoubanApi.API_BASE_URL, okHttpClient).create(DoubanApi.class);
    }

    @Singleton
    @Provides
    BaiduApi provideBaiduApi(Retrofit.Builder builder, OkHttpClient okHttpClient) {
        return createRetrofit(builder, BaiduApi.API_BASE_URL, okHttpClient).create(BaiduApi.class);
    }

    @Singleton
    @Provides
    BmobApi provideBmobApi(Retrofit.Builder builder, OkHttpClient okHttpClient) {
        return createRetrofit(builder, BmobApi.API_BASE_URL, okHttpClient).create(BmobApi.class);
    }

    @Singleton
    @Provides
    DoubanApiService provideDoubanApiService(DoubanApi doubanApi) {
        return new DoubanApiService(doubanApi);
    }

    @Singleton
    @Provides
    BaiduApiService provideBaiduApiService(BaiduApi baiduApi) {
        return new BaiduApiService(baiduApi);
    }

    @Singleton
    @Provides
    BmobApiService provideBmobApiService(BmobApi bmobApi, UserPreference userPreference){
        return new BmobApiService(bmobApi,userPreference);
    }


    @Singleton
    @Provides
    OkHttpClient provideOkHttpClient(OkHttpClient.Builder builder) {
        File cacheDirectory = new File(BkKit.getContext().getCacheDir(), "BkAppCache");
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(cacheDirectory, cacheSize);
        return builder.cache(cache)
                .connectTimeout(10 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(10 * 1000, TimeUnit.MILLISECONDS)
                .writeTimeout(10 * 1000, TimeUnit.MILLISECONDS)
                .build();
    }

    private Retrofit createRetrofit(Retrofit.Builder builder, String base_url, OkHttpClient okHttpClient) {
        return builder.baseUrl(base_url)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
