package com.roy.bkapp.ui.activity.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kyleduo.switchbutton.SwitchButton;
import com.roy.bkapp.BkKit;
import com.roy.bkapp.R;
import com.roy.bkapp.model.collection.MovieCollection;
import com.roy.bkapp.presenter.user.UserCenterPresenter;
import com.roy.bkapp.ui.activity.BaseSwipeBackActivity;
import com.roy.bkapp.ui.activity.helper.ImagePickActivity;
import com.roy.bkapp.ui.view.user.UserCenterView;
import com.roy.bkapp.utils.ImageUtils;
import com.roy.bkapp.utils.ToastUtils;
import com.roy.bkapp.utils.UserPreference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/5/22.
 */

public class UserCenterActivity extends BaseSwipeBackActivity<UserCenterView, UserCenterPresenter>
        implements UserCenterView
        , View.OnClickListener
        , CompoundButton.OnCheckedChangeListener {
    protected static final int REQUEST_CODE_PICKIMAGE = 1000;

    protected static final int FROM_ALBUM = 0;
    protected static final int FROM_CAMERA = 1;


    @BindView(R.id.btn_logout)
    AppCompatButton btn_logout;
    @BindView(R.id.avatar_set)
    LinearLayout avatar_set;
    @BindView(R.id.user_avatar)
    ImageView user_avatar;
    @BindView(R.id.user_name)
    TextView user_name;
    @BindView(R.id.user_telephone)
    TextView user_telephone;
    @BindView(R.id.user_email)
    TextView user_email;
    @BindView(R.id.sbtn_msg)
    SwitchButton sbtn_msg;
    @BindView(R.id.sbtn_sync)
    SwitchButton sbtn_sync;
    @BindView(R.id.disk_cache)
    TextView disk_cache;
    @BindView(R.id.cache_clear)
    LinearLayout cache_clear;
    @BindView(R.id.cloud_collection_num)
    TextView cloud_collection_num;

    private String path = Environment.getExternalStorageDirectory() + "/Android/data/com.roy.bkapp/";
    private File mCropFile = new File(path, "Crop.jpg");//裁剪后的File对象


    private ProgressDialog dialog;

    public static void start(Context context) {
        Intent intent = new Intent(context, UserCenterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usercenter);
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initViewAndEvent() {
        mToolbar.setTitle(R.string.user_center);
        mToolbar.setNavigationOnClickListener(v -> UserCenterActivity.this.finish());

        if (!mPresenter.readUserHeader().isEmpty()) {
            ImageUtils.displayImage(this, mPresenter.readUserHeader(), user_avatar);
        } else {
            ImageUtils.displayImage(this, R.drawable.avatar_default, user_avatar);
        }

        user_name.setText(mPresenter.readUserInfo().getUsername());
        user_telephone.setText(mPresenter.readUserInfo().getTelephone());

        if(mPresenter.readUserInfo().getEmail().isEmpty()){
            user_email.setText(getString(R.string.not_set));
        }else{
            user_email.setText(mPresenter.readUserInfo().getEmail());
        }

        disk_cache.setText(ImageUtils.getCacheSize());

        sbtn_sync.setChecked(mPresenter.readIsSync());

        dialog = new ProgressDialog(this);

        btn_logout.setOnClickListener(this);
        avatar_set.setOnClickListener(this);
        sbtn_msg.setOnCheckedChangeListener(this);
        sbtn_sync.setOnCheckedChangeListener(this);
        cache_clear.setOnClickListener(this);

        mPresenter.searchCloudCollectionNum();

    }

    @Override
    public void showError(String message) {
        ToastUtils.showToast(message);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_logout:
                mPresenter.clearUserInfo();
                BkKit.getUserAvatarListener().logout();
                this.finish();
                break;
            case R.id.avatar_set:
                showDialog();
                break;
            case R.id.cache_clear:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.cache_clear)
                        .setNegativeButton(R.string.confirm, (dialog1, which) -> ImageUtils.clearDiskCache(() -> disk_cache.setText(ImageUtils.getCacheSize())))
                        .setPositiveButton(R.string.cancel, (dialog12, which) -> dialog12.dismiss())
                        .show();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.sbtn_msg:
                if (isChecked) {
                    ToastUtils.showToast(R.string.msg_push_open);
                } else {
                    ToastUtils.showToast(R.string.msg_push_close);
                }
                break;
            case R.id.sbtn_sync:
                if (isChecked) {
                    new AlertDialog.Builder(this)
                            .setTitle("提示")
                            .setMessage("开启此功能后，您的收藏与删除都会同步到云端")
                            .setNegativeButton("确定开启", (dialog1, which) -> {
                                mPresenter.syncCollection(mPresenter.getLocalMovieCollection());
                                mPresenter.saveIsSync(true);
                                ToastUtils.showToast(R.string.collection_sync_open);
                            })
                            .setPositiveButton("取消开启", (dialog12, which) -> {
                                sbtn_sync.setChecked(false);
                                dialog12.dismiss();
                            })
                            .show();
                } else {
                    ToastUtils.showToast(R.string.collection_sync_close);
                    mPresenter.saveIsSync(false);
                }
                break;
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置头像");
        String[] items = {"选择本地照片", "拍照"};
        builder.setItems(items, (dialog1, which) -> {
            switch (which) {
                case FROM_ALBUM: // 选择本地照片
                    ImagePickActivity.start(this,REQUEST_CODE_PICKIMAGE,FROM_ALBUM);
                    break;
                case FROM_CAMERA: // 拍照
                    ImagePickActivity.start(this,REQUEST_CODE_PICKIMAGE,FROM_CAMERA);
                    break;
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case REQUEST_CODE_PICKIMAGE:
                    dialog.setMessage(getString(R.string.modifying));
                    dialog.show();
                    mPresenter.modifyAvatar(mCropFile.getAbsolutePath());
                    break;
            }
        }
    }

    @Override
    public void modifyAvatarSuccess(String s) {
        ToastUtils.showToast(R.string.modify_success);
        if (!mPresenter.readUserHeader().isEmpty()) {
            ImageUtils.displayImage(this, mPresenter.readUserHeader(), user_avatar);
        } else {
            ImageUtils.displayImage(this, R.drawable.avatar_default, user_avatar);
        }
        BkKit.getUserAvatarListener().uploadAvatar(mPresenter.readUserHeader());
        dialog.dismiss();
    }

    @Override
    public void syncCollectionSuccess(String s) {
        ToastUtils.showLongToast(s);
    }

    @Override
    public void searchNumSuccess(Integer integer) {
        cloud_collection_num.setText(integer+"条");
    }

}
