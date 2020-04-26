package ddr.example.com.svgmapclient.common;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.gyf.immersionbar.ImmersionBar;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.hjq.toast.ToastUtils;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import ddr.example.com.svgmapclient.R;
import ddr.example.com.svgmapclient.base.BaseActivity;
import ddr.example.com.svgmapclient.helper.ActivityStackManager;
import ddr.example.com.svgmapclient.helper.EventBusManager;

/**
 *  time    ：2019/10/25
 *  describe: Activity 基类
 */
public class DDRActivity extends BaseActivity implements OnTitleBarListener {
    /** 标题栏对象 */
    private TitleBar mTitleBar;
    /**
     * 状态栏沉浸
     */
    private ImmersionBar mImmersionBar;


    /**
     * 获取标题栏 id
     */
    protected int getTitleId() {
        return 0;
    }

    @Override
    protected void initActivity() {
        super.initActivity();
        ActivityStackManager.getInstance().onCreated(this);
    }

    @Override
    protected void initLayout() {
        super.initLayout();
        // 初始化标题栏的监听
        if (getTitleId() > 0) {
            // 勤快模式
            View view = findViewById(getTitleId());
            if (view instanceof TitleBar) {
                mTitleBar = (TitleBar) view;
            }
        } else if (getTitleId() == 0) {
            // 懒人模式
            mTitleBar = findTitleBar(getContentView());
        }
        if (mTitleBar != null) {
            mTitleBar.setOnTitleBarListener(this);
        }
        EventBusManager.register(this);
        initImmersion();
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }



    /**
     * 递归获取 ViewGroup 中的 TitleBar 对象
     */
    static TitleBar findTitleBar(ViewGroup group) {
        for (int i = 0; i < group.getChildCount(); i++) {
            View view = group.getChildAt(i);
            if ((view instanceof TitleBar)) {
                return (TitleBar) view;
            } else if (view instanceof ViewGroup) {
                TitleBar titleBar = findTitleBar((ViewGroup) view);
                if (titleBar != null) {
                    return titleBar;
                }
            }
        }
        return null;
    }


    /**
     * 是否使用沉浸式状态栏
     */
    public boolean isStatusBarEnabled() {
        return true;
    }

    /**
     * 初始化沉浸式
     */
    protected void initImmersion() {
        // 初始化沉浸式状态栏
        if (isStatusBarEnabled()) {
            statusBarConfig().init();

            // 设置标题栏沉浸
            if (getTitleId() > 0) {
                ImmersionBar.setTitleBar(this, findViewById(getTitleId()));
            } else if (mTitleBar != null) {
                ImmersionBar.setTitleBar(this, mTitleBar);
            }
        }
    }
    /**
     * 初始化沉浸式状态栏
     */
    protected ImmersionBar statusBarConfig() {
        // 在BaseActivity里初始化
        mImmersionBar = ImmersionBar.with(this)
                // 默认状态栏字体颜色为黑色
                .statusBarDarkFont(statusBarDarkFont());
        return mImmersionBar;
    }
    /**
     * 获取状态栏字体颜色
     */
    public boolean statusBarDarkFont() {
        // 返回真表示黑色字体
        return true;
    }

    /**
     * 获取状态栏沉浸的配置对象
     */
    public ImmersionBar getStatusBarConfig() {
        return mImmersionBar;
    }

    /**
     * 设置标题栏的标题
     */
    @Override
    public void setTitle(@StringRes int id) {
        setTitle(getString(id));
    }

    /**
     * 设置标题栏的标题
     */
    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        if (mTitleBar != null) {
            mTitleBar.setTitle(title);
        }
    }

    /**
     * 设置标题栏的左标题
     */
    public void setLeftTitle(int id) {
        if (mTitleBar != null) {
            mTitleBar.setLeftTitle(id);
        }
    }

    public void setLeftTitle(CharSequence text) {
        if (mTitleBar != null) {
            mTitleBar.setLeftTitle(text);
        }
    }

    public CharSequence getLeftTitle() {
        if (mTitleBar != null) {
            return mTitleBar.getLeftTitle();
        }
        return "";
    }

    /**
     * 设置标题栏的右标题
     */
    public void setRightTitle(int id) {
        if (mTitleBar != null) {
            mTitleBar.setRightTitle(id);
        }
    }

    public void setRightTitle(CharSequence text) {
        if (mTitleBar != null) {
            mTitleBar.setRightTitle(text);
        }
    }

    public CharSequence getRightTitle() {
        if (mTitleBar != null) {
            return mTitleBar.getRightTitle();
        }
        return "";
    }

    /**
     * 设置标题栏的左图标
     */
    public void setLeftIcon(int id) {
        if (mTitleBar != null) {
            mTitleBar.setLeftIcon(id);
        }
    }

    public void setLeftIcon(Drawable drawable) {
        if (mTitleBar != null) {
            mTitleBar.setLeftIcon(drawable);
        }
    }

    @Nullable
    public Drawable getLeftIcon() {
        if (mTitleBar != null) {
            return mTitleBar.getLeftIcon();
        }
        return null;
    }

    /**
     * 设置标题栏的右图标
     */
    public void setRightIcon(int id) {
        if (mTitleBar != null) {
            mTitleBar.setRightIcon(id);
        }
    }

    public void setRightIcon(Drawable drawable) {
        if (mTitleBar != null) {
            mTitleBar.setRightIcon(drawable);
        }
    }

    @Nullable
    public Drawable getRightIcon() {
        if (mTitleBar != null) {
            return mTitleBar.getRightIcon();
        }
        return null;
    }

    @Nullable
    public TitleBar getTitleBar() {
        return mTitleBar;
    }

    /**
     * {@link OnTitleBarListener}
     */

    /**
     * TitleBar 左边的View被点击了
     */
    @Override
    public void onLeftClick(View v) {
        onBackPressed();
    }

    /**
     * TitleBar 中间的View被点击了
     */
    @Override
    public void onTitleClick(View v) {}

    /**
     * TitleBar 右边的View被点击了
     */
    @Override
    public void onRightClick(View v) {}


    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBusManager.unregister(this);
        ActivityStackManager.getInstance().onDestroyed(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityStackManager.getInstance().onResume(this);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        overridePendingTransition(R.anim.activity_right_in, R.anim.activity_right_out);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_left_in, R.anim.activity_left_out);
    }

    /**
     * 显示吐司
     */
    public void toast(CharSequence text) {
        ToastUtils.show(text);
    }

    public void toast(@StringRes int id) {
        ToastUtils.show(id);
    }

    public void toast(Object object) {
        ToastUtils.show(object);
    }


}
