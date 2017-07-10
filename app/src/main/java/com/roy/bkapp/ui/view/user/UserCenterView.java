package com.roy.bkapp.ui.view.user;

import com.roy.bkapp.model.collection.MovieCollection;
import com.roy.bkapp.ui.view.BaseView;

import java.util.List;

/**
 * Created by Administrator on 2017/5/22.
 */

public interface UserCenterView extends BaseView{
    void modifyAvatarSuccess(String s);

    void syncCollectionSuccess(String s);

    void searchNumSuccess(Integer integer);
}
