package ddr.example.com.svgmapclient.ui.adapter;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import ddr.example.com.svgmapclient.R;
import ddr.example.com.svgmapclient.base.BaseAdapter;
import ddr.example.com.svgmapclient.entity.point.BaseMode;
import ddr.example.com.svgmapclient.entity.point.PathLine;
import ddr.example.com.svgmapclient.entity.point.TargetPoint;
import ddr.example.com.svgmapclient.other.Logger;

/**
 * time ：2019/11/13
 * desc : Task的子项中排序的列表适配器
 */
public class BaseModeAdapter extends BaseAdapter<BaseMode> {

    public BaseModeAdapter(int layoutResId) {
        super(layoutResId);
    }

    public BaseModeAdapter(int layoutResId, @Nullable List<BaseMode> data) {
        super(layoutResId, data);
    }

    @Override
    public void setNewData(@Nullable List<BaseMode> data) {
        super.setNewData(data);
        Logger.e("--------列表数量："+data.size());
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, BaseMode item) {
        super.convert(helper, item);


    }


}
