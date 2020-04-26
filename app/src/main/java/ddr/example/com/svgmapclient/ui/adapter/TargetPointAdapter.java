package ddr.example.com.svgmapclient.ui.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ddr.example.com.svgmapclient.R;
import ddr.example.com.svgmapclient.base.BaseAdapter;
import ddr.example.com.svgmapclient.entity.point.TargetPoint;


/**
 * time: 2019/11/7
 * desc: 标记点列表适配器
 */
public class TargetPointAdapter extends BaseAdapter<TargetPoint> {


    public TargetPointAdapter(int layoutResId) {
        super(layoutResId);
    }


    public TargetPointAdapter(int layoutResId, @Nullable List<TargetPoint> data) {
        super(layoutResId, data);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void convert(@NonNull BaseViewHolder helper,TargetPoint item) {
        super.convert(helper, item);
        helper.setText(R.id.tv_area_name,item.getName());

    }


    @Override
    public void setNewData(@Nullable List<TargetPoint> data) {
        super.setNewData(data);
       // Logger.e("设置列表");

    }



    @Override
    public void setData(int index, @NonNull TargetPoint data) {
        super.setData(index, data);
    }
}
