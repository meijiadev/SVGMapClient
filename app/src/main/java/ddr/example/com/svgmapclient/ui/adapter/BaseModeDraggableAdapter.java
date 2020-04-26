package ddr.example.com.svgmapclient.ui.adapter;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import ddr.example.com.svgmapclient.R;
import ddr.example.com.svgmapclient.entity.point.BaseMode;
import ddr.example.com.svgmapclient.entity.point.PathLine;
import ddr.example.com.svgmapclient.entity.point.TargetPoint;

/**
 * time ：2020/04/14
 * desc : Task的子项列表适配器(可拖拽)
 */
public class BaseModeDraggableAdapter extends BaseItemDraggableAdapter<BaseMode,BaseViewHolder>{

    public BaseModeDraggableAdapter(List<BaseMode> data) {
        super(data);
    }

    public BaseModeDraggableAdapter(int layoutResId, List<BaseMode> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, BaseMode item) {

    }

    @Override
    public void setNewData(@Nullable List<BaseMode> data) {
        super.setNewData(data);
    }
}
