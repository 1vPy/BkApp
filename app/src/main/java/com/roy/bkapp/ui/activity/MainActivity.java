package com.roy.bkapp.ui.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.roy.bkapp.BkApplication;
import com.roy.bkapp.R;
import com.roy.bkapp.http.RequestCallback;
import com.roy.bkapp.model.user.login_config.LoginConfig;
import com.roy.bkapp.ui.activity.movie.MovieCollectionActivity;
import com.roy.bkapp.ui.activity.user.LoginRegisterActivity;
import com.roy.bkapp.ui.fragment.movie.MovieFragment;
import com.roy.bkapp.ui.fragment.music.MusicFragment;
import com.roy.bkapp.utils.SnackBarUtils;
import com.roy.bkapp.utils.UserPreference;

import java.lang.reflect.Field;

import butterknife.BindView;

public class MainActivity extends RootActivity implements NavigationView.OnNavigationItemSelectedListener
        , View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static long DOUBLE_CLICK_TIME = 0L;

    @BindView(R.id.rl_root)
    RelativeLayout rl_root;

    @BindView(R.id.dl_main)
    DrawerLayout dl_main;

    @BindView(R.id.nv_main)
    NavigationView nv_main;

    private Button user_login;

    private RelativeLayout viewSplash;

    private Fragment mNowFragment;
    private FragmentManager mFragmentManager;

    private MovieFragment mMovieFragment;
    private MusicFragment mMusicFragment;

    private ActionBarDrawerToggle mActionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLogin();
    }

    public void initLogin() {
        if (UserPreference.getUserPreference(this).readUserInfo() != null) {
            if (UserPreference.getUserPreference(this).readSessionToken() != null) {
                BkApplication.getAppComponent().getBmobApiService().loginConfig(UserPreference.getUserPreference(this).readUserInfo().getUsername(), new RequestCallback<LoginConfig>() {
                    @Override
                    public void onSuccess(LoginConfig loginConfig) {
                        if (loginConfig.getResults().size() > 0) {
                            if (!TextUtils.equals(loginConfig.getResults().get(0).getSessionToken(), UserPreference.getUserPreference(MainActivity.this).readSessionToken())) {
                                UserPreference.getUserPreference(getApplicationContext()).clearUserInfo();
                                new AlertDialog.Builder(MainActivity.this)
                                        .setTitle("警告")
                                        .setMessage("您的账号在其他设备上登录了，请注意！")
                                        .setCancelable(false)
                                        .setNegativeButton("重新登录", (dialog, which) -> LoginRegisterActivity.start(MainActivity.this))
                                        .setPositiveButton("忽略警告", (dialog, which) -> dialog.dismiss())
                                        .show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });
            }
        }
    }

    @Override
    protected void initViewAndEvent() {
        View headerView = nv_main.getHeaderView(0);
        user_login = (Button) headerView.findViewById(R.id.user_login);
        user_login.setOnClickListener(this);

        mToolbar.setTitle(R.string.movie);
        mFragmentManager = getSupportFragmentManager();
        mMovieFragment = MovieFragment.newInstance();
        mMusicFragment = MusicFragment.newInstance();

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.fl_main_container, mMovieFragment).commit();

        mNowFragment = mMovieFragment;

        mActionBarDrawerToggle =
                new ActionBarDrawerToggle(this, dl_main, mToolbar, R.string.open, R.string.close);
        mActionBarDrawerToggle.syncState();
        dl_main.addDrawerListener(mActionBarDrawerToggle);
        nv_main.setNavigationItemSelectedListener(this);

        //switchFragment(mMovieFragment,mMovieFragment);

        ViewGroup parent = (ViewGroup) dl_main.getParent();
        View.inflate(this, R.layout.view_splash, parent);
        viewSplash = (RelativeLayout) parent.findViewById(R.id.view_splash);

        viewSplash.setVisibility(View.VISIBLE);
        dl_main.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(() -> mHandler.sendEmptyMessage(0), 2000);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    startAnim();
                    break;
            }
        }
    };

    /**
     * splash结束动画
     */
    private void startAnim() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(viewSplash, "qwe", 1f, 0f);
        animator.setDuration(1000);
        animator.start();
        dl_main.setVisibility(View.VISIBLE);
        animator.addUpdateListener(animation -> {
            float a = (Float) animation.getAnimatedValue();
            viewSplash.setAlpha(a);
            dl_main.setAlpha(1f - a);
        });
    }

    /**
     * fragment 切换
     *
     * @param hide
     * @param show
     */
    public void switchFragment(Fragment hide, Fragment show) {
        if (mNowFragment != show) {
            mNowFragment = show;
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            if (!mNowFragment.isAdded()) {    // 先判断是否被add过
                transaction.hide(hide).add(R.id.fl_main_container, show).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(hide).show(show).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

    /**
     * 侧滑菜单点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_movie:
                switchFragment(mNowFragment, mMovieFragment);
                mToolbar.setTitle(R.string.movie);
                break;
            case R.id.item_music:
                switchFragment(mNowFragment, mMusicFragment);
                mToolbar.setTitle(R.string.music);
                break;
            case R.id.item_collection:
                MovieCollectionActivity.start(this);
                break;
            case R.id.item_setting:
                break;
            case R.id.item_feedback:
                break;
            case R.id.item_about:
                break;
        }
        dl_main.closeDrawers();
        return true;
    }

   /* @Override
    public void onBackPressed() {
        super.onBackPressed();
            if (dl_main.isDrawerOpen(Gravity.LEFT)) {
                dl_main.closeDrawers();
            } else {
                if ((System.currentTimeMillis() - DOUBLE_CLICK_TIME) > 2000) {
                    SnackBarUtils.LongSnackbar(rl_root,"再按一次退出",SnackBarUtils.Info).show();
                    DOUBLE_CLICK_TIME = System.currentTimeMillis();
                } else {
                    finish();
                }
            }
    }*/

    /**
     * 退出监听(连续点击两次退出)
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_MENU && dl_main != null) {
            if (dl_main.isDrawerOpen(Gravity.LEFT)) {
                dl_main.closeDrawer(Gravity.LEFT);
            } else {
                dl_main.openDrawer(Gravity.LEFT);
            }
            return true;
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (dl_main.isDrawerOpen(Gravity.LEFT)) {
                dl_main.closeDrawer(Gravity.LEFT);
            } else {
                if ((System.currentTimeMillis() - DOUBLE_CLICK_TIME) > 2000) {
                    SnackBarUtils.LongSnackbar(rl_root, "再按一次退出", SnackBarUtils.Info).show();
                    DOUBLE_CLICK_TIME = System.currentTimeMillis();
                } else {
                    finish();
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 侧滑菜单头部点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_login:
                LoginRegisterActivity.start(this);
                break;
        }
    }
}
