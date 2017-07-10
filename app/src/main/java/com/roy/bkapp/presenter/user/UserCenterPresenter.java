package com.roy.bkapp.presenter.user;

import com.roy.bkapp.db.DbHelperService;
import com.roy.bkapp.http.RequestCallback;
import com.roy.bkapp.http.api.bmob.BmobApiService;
import com.roy.bkapp.model.collection.MovieCollection;
import com.roy.bkapp.model.user.UserInfo;
import com.roy.bkapp.presenter.BasePresenter;
import com.roy.bkapp.ui.view.user.UserCenterView;
import com.roy.bkapp.utils.UserPreference;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Administrator on 2017/5/22.
 */

public class UserCenterPresenter extends BasePresenter<UserCenterView> {

    private BmobApiService mBmobApiService;
    private UserPreference mUserPreference;
    private DbHelperService mDbHelperService;
    int collectionNum = 0;
    int syncSuccessNum = 0;
    int syncFailNum = 0;

    @Inject
    public UserCenterPresenter(BmobApiService bmobApiService, UserPreference userPreference, DbHelperService dbHelperService) {
        mBmobApiService = bmobApiService;
        mUserPreference = userPreference;
        mDbHelperService = dbHelperService;
    }

    public UserInfo readUserInfo() {
        return mUserPreference.readUserInfo();
    }

    public void saveUserInfo(UserInfo userInfo) {
        mUserPreference.saveUserInfo(userInfo);
    }

    public void saveSessionToken(String sessionToken) {
        mUserPreference.saveSessionToken(sessionToken);
    }

    public void saveUserHeader(String url) {
        mUserPreference.saveUserHeader(url);
    }

    public String readUserHeader() {
        return mUserPreference.readUserHeader();
    }

    public void saveIsSync(boolean isSync){
        mUserPreference.saveIsSync(isSync);
    }

    public boolean readIsSync(){
        return mUserPreference.readIsSync();
    }

    public void clearUserInfo() {
        mUserPreference.clearUserInfo();
    }

    public void modifyAvatar(String path) {
        mBmobApiService.modifyAvatar(path, new RequestCallback<String>() {
            @Override
            public void onSuccess(String s) {
                if (isAttached()) {
                    getView().modifyAvatarSuccess(s);
                }
            }

            @Override
            public void onFailure(String message) {
                if (isAttached()) {
                    getView().showError(message);
                }
            }
        });
    }

    public void syncCollection(List<MovieCollection> movieCollectionList) {
        collectionNum = movieCollectionList.size();
        mBmobApiService.syncCollection(movieCollectionList, new RequestCallback<String>() {
            @Override
            public void onSuccess(String s) {
                syncSuccessNum++;
                mDbHelperService.toggleSyncMovie(s, true);
                if ((syncSuccessNum + syncFailNum) == collectionNum) {
                    if (syncFailNum > 0) {
                        if (isAttached()) {
                            getView().showError("同步失败：" + syncFailNum + "个收藏未上传成功");
                        }
                    } else {
                        if (isAttached()) {
                            getView().syncCollectionSuccess("同步成功");
                        }
                    }
                    collectionNum = 0;
                    syncFailNum = 0;
                    syncSuccessNum = 0;
                }
            }

            @Override
            public void onFailure(String message) {
                syncFailNum++;
                if ((syncSuccessNum + syncFailNum) == collectionNum) {
                    if (syncFailNum > 0) {
                        if (isAttached()) {
                            getView().showError("同步失败：" + syncFailNum + "个收藏未上传成功");
                        }
                    } else {
                        if (isAttached()) {
                            getView().syncCollectionSuccess("同步成功");
                        }
                    }
                    collectionNum = 0;
                    syncFailNum = 0;
                    syncSuccessNum = 0;
                }
            }
        });
    }

    public List<MovieCollection> getLocalMovieCollection() {
        return mDbHelperService.selectAllCollection();
    }

    public void searchCloudCollectionNum(){
        mBmobApiService.searchCloudCollectionNum(new RequestCallback<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                if(isAttached()){
                    getView().searchNumSuccess(integer);
                }
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

}
