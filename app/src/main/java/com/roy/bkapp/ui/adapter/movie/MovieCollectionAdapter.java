package com.roy.bkapp.ui.adapter.movie;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.roy.bkapp.R;
import com.roy.bkapp.model.collection.MovieCollection;
import com.roy.bkapp.utils.ImageUtils;
import com.roy.bkapp.utils.ScreenUtils;

import java.util.List;

/**
 * Created by 1vPy(Roy) on 2017/5/25.
 */

public class MovieCollectionAdapter extends BaseQuickAdapter<MovieCollection, BaseViewHolder> {

    public MovieCollectionAdapter(int layoutResId, List<MovieCollection> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MovieCollection item) {
        ViewGroup.LayoutParams params = helper.getView(R.id.iv_movie).getLayoutParams();
        params.width = (ScreenUtils.getScreenWidthDp(mContext)) / 3;
        params.height = (int) ((420.0 / 300.0) * params.width);
        helper.getView(R.id.iv_movie).setLayoutParams(params);
        ImageUtils.displayImage(mContext,item.getImageUrl(),(ImageView) helper.getView(R.id.iv_movie));
        helper.setText(R.id.tv_name,item.getMovieName());
        if(item.getIsSync() >0){
            helper.setVisible(R.id.iv_sync,true);
        }else{
            helper.setVisible(R.id.iv_sync,false);
        }
    }
}
