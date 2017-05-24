package com.roy.bkapp.dagger.component;

import android.support.v4.app.Fragment;

import com.roy.bkapp.dagger.module.FragmentModule;
import com.roy.bkapp.dagger.scope.FragmentScope;
import com.roy.bkapp.ui.fragment.movie.MovieComeUpFragment;
import com.roy.bkapp.ui.fragment.movie.MovieHotFragment;
import com.roy.bkapp.ui.fragment.music.MusicBillFragment;

import dagger.Component;

/**
 * Created by 1vPy(Roy) on 2017/5/11.
 */

@FragmentScope
@Component(dependencies = {AppComponent.class},modules = {FragmentModule.class})
public interface FragmentComponent {

    void inject(MovieHotFragment fragment);

    void inject(MusicBillFragment fragment);

    void inject(MovieComeUpFragment fragment);
}
