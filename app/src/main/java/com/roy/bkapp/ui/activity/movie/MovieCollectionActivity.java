package com.roy.bkapp.ui.activity.movie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.roy.bkapp.R;
import com.roy.bkapp.model.collection.MovieCollection;
import com.roy.bkapp.presenter.movie.MovieCollectionPresenter;
import com.roy.bkapp.ui.activity.BaseSwipeBackActivity;
import com.roy.bkapp.ui.adapter.movie.MovieCollectionAdapter;
import com.roy.bkapp.ui.view.movie.MovieCollectionView;
import com.roy.bkapp.utils.LogUtils;
import com.roy.bkapp.utils.SnackBarUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 电影收藏Activity
 * Created by 1vPy(Roy) on 2017/5/25.
 */

public class MovieCollectionActivity extends BaseSwipeBackActivity<MovieCollectionView, MovieCollectionPresenter>
        implements MovieCollectionView
        , View.OnClickListener
        , BaseQuickAdapter.OnItemClickListener
        , BaseQuickAdapter.OnItemLongClickListener {

    private static final String TAG = MovieCollectionActivity.class.getSimpleName();

    @BindView(R.id.cdl_root)
    CoordinatorLayout cdl_root;

    @BindView(R.id.ryv_collection)
    RecyclerView ryv_collection;
    @BindView(R.id.fab_back_top)
    FloatingActionButton fab_back_top;

    private AlertDialog dialog;

    private MovieCollectionAdapter mMovieCollectionAdapter;
    private List<MovieCollection> mMovieCollectionList = new ArrayList<>();

    private int mNowPosition = 0;

    public static void start(Context context) {
        Intent intent = new Intent(context, MovieCollectionActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_collection);
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViewAndEvent() {
        mToolbar.setTitle(R.string.movie_collection);
        mToolbar.setNavigationOnClickListener(v -> this.finish());
        ryv_collection.setLayoutManager(new LinearLayoutManager(this));
        mMovieCollectionAdapter = new MovieCollectionAdapter(R.layout.item_collection, mMovieCollectionList);
        ryv_collection.setAdapter(mMovieCollectionAdapter);
        mPresenter.selectAllCollection();
        mMovieCollectionAdapter.setOnItemClickListener(this);
        mMovieCollectionAdapter.setOnItemLongClickListener(this);
        fab_back_top.setOnClickListener(this);
        ryv_collection.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) ryv_collection.getLayoutManager();
                mNowPosition = linearLayoutManager.findFirstVisibleItemPosition();
                if (mNowPosition > 0) {
                    if (!fab_back_top.isShown())
                        fab_back_top.show();
                } else {
                    if (fab_back_top.isShown())
                        fab_back_top.hide();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_back_top:
                if (mNowPosition < 10) {
                    ryv_collection.smoothScrollToPosition(0);
                } else {
                    ryv_collection.scrollToPosition(0);
                }
                fab_back_top.hide(new FloatingActionButton.OnVisibilityChangedListener() {
                    @Override
                    public void onHidden(FloatingActionButton fab) {
                        super.onHidden(fab);
                        SnackBarUtils.LongSnackbar(cdl_root, getString(R.string.is_top), SnackBarUtils.Info).show();
                    }
                });
                break;
        }
    }

    @Override
    public void movieCollection(List<MovieCollection> movieCollectionList) {
        mMovieCollectionList.addAll(movieCollectionList);
        mMovieCollectionAdapter.notifyDataSetChanged();
    }

    @Override
    public void deleteSuccess(String s) {
        SnackBarUtils.LongSnackbar(cdl_root, s, SnackBarUtils.Info).show();
        mMovieCollectionAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String message) {
        SnackBarUtils.LongSnackbar(cdl_root, message, SnackBarUtils.Warning).show();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("movieId", mMovieCollectionList.get(position).getMovieId());
        startActivityForResult(intent, 100);
    }

    /**
     * 判断用户在通过收藏界面进入电影详情页后是否删除收藏
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                int p = data.getIntExtra("position", -1);
                if (p >= 0) {
                    mMovieCollectionList.remove(p);
                    mMovieCollectionAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    /**
     * 长按删除收藏
     * @param adapter
     * @param view
     * @param position
     * @return
     */
    @Override
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
        ListView listView = new ListView(this);
        listView.setAdapter(new SimpleAdapter(this, menuItem(), R.layout.item_menu, new String[]{"menu_icon", "menu_name"}, new int[]{R.id.menu_icon, R.id.menu_name}));
        listView.setOnItemClickListener((parent, view1, position1, id) -> {
            switch (position1) {
                case 0:
                    LogUtils.log(TAG, "p:" + position, LogUtils.DEBUG);
                    mPresenter.deleteCollection(mMovieCollectionList.get(position).getMovieId());
                    mMovieCollectionList.remove(position);
                    dialog.dismiss();
                    break;

            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setView(listView);
        dialog = builder.show();
        return false;
    }

    private List<Map<String, Object>> menuItem() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(2);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("menu_icon", R.drawable.icon_delete);
        map.put("menu_name", getString(R.string.delete));
        list.add(map);

        return list;
    }
}
