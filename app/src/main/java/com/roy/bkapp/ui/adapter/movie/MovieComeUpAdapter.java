package com.roy.bkapp.ui.adapter.movie;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.roy.bkapp.R;
import com.roy.bkapp.model.movie.Casts;
import com.roy.bkapp.model.movie.Directors;
import com.roy.bkapp.model.movie.Subjects;
import com.roy.bkapp.utils.ImageUtils;
import com.roy.bkapp.utils.ScreenUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/5/11.
 */

public class MovieComeUpAdapter extends BaseQuickAdapter<Subjects, BaseViewHolder> {
    public MovieComeUpAdapter(int layoutResId, List<Subjects> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Subjects item) {
        ViewGroup.LayoutParams params = helper.getView(R.id.iv_image).getLayoutParams();
        params.width = (ScreenUtils.getScreenWidthDp(mContext) - ScreenUtils.dipToPx(mContext, 80)) / 3;
        params.height = (int) ((420.0 / 300.0) * params.width);
        helper.getView(R.id.iv_image).setLayoutParams(params);

        ImageUtils.displayImage(mContext, item.getImages().getLarge(), (ImageView) helper.getView(R.id.iv_image));

        helper.setText(R.id.tv_name, item.getTitle());

        if (item.getGenres() != null && item.getGenres().size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String s : item.getGenres()) {
                stringBuilder.append(s + "/");
            }
            helper.setText(R.id.tv_type, "类型：" + stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1));
        }

        if (item.getDirectors() != null && item.getDirectors().size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Directors s : item.getDirectors()) {
                stringBuilder.append(s.getName() + "/");
            }
            helper.setText(R.id.tv_directors, "导演：" + stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1));
        }

        if (item.getCasts() != null && item.getCasts().size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Casts s : item.getCasts()) {
                stringBuilder.append(s.getName() + "/");
            }
            helper.setText(R.id.tv_stars, "主演：" + stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1));
        }
    }
}
