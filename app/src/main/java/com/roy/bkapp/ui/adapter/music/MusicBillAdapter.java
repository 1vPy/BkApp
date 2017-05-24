package com.roy.bkapp.ui.adapter.music;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.roy.bkapp.R;
import com.roy.bkapp.model.music.billcategory.Content;
import com.roy.bkapp.utils.ImageUtils;
import com.roy.bkapp.utils.ScreenUtils;

import java.util.List;

/**
 * Created by 1vPy(Roy) on 2017/5/11.
 */

public class MusicBillAdapter extends BaseQuickAdapter<Content,BaseViewHolder>{

    public MusicBillAdapter(int layoutResId, List<Content> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Content item) {
        ViewGroup.LayoutParams params = helper.getView(R.id.iv_bill).getLayoutParams();
        params.width = (ScreenUtils.getScreenWidthDp(mContext)) / 3;
        params.height = params.width;
        helper.getView(R.id.iv_bill).setLayoutParams(params);
        ImageUtils.displayImage(mContext, item.getPic_s260(), (ImageView) helper.getView(R.id.iv_bill));

        helper.setText(R.id.tv_bill_name, item.getName());
    }
}

