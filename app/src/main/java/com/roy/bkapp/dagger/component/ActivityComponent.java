package com.roy.bkapp.dagger.component;

import com.roy.bkapp.dagger.module.ActivityModule;
import com.roy.bkapp.dagger.scope.ActivityScope;
import com.roy.bkapp.ui.activity.MainActivity;
import com.roy.bkapp.ui.activity.movie.MovieCollectionActivity;
import com.roy.bkapp.ui.activity.movie.MovieDetailActivity;
import com.roy.bkapp.ui.activity.user.LoginRegisterActivity;
import com.roy.bkapp.ui.activity.user.UserCenterActivity;

import dagger.Component;

/**
 * Created by 1vPy(Roy) on 2017/5/11.
 */

@ActivityScope
@Component(dependencies = AppComponent.class, modules = {ActivityModule.class})
public interface ActivityComponent {
    void inject(MainActivity activity);

    void inject(MovieDetailActivity activity);

    void inject(LoginRegisterActivity activity);

    void inject(UserCenterActivity activity);

    void inject(MovieCollectionActivity activity);
}
