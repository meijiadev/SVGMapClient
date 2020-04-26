package ddr.example.com.svgmapclient.ui.adapter;

import android.content.Context;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import ddr.example.com.svgmapclient.R;
import ddr.example.com.svgmapclient.base.BaseAdapter;

/**
 * time : 2019/11/2
 * desc : String类型的列表适配器
 */
public class StringAdapter extends BaseAdapter<String> {
    private Context context;
    public StringAdapter(int layoutResId) {
        super(layoutResId);
    }

    public void setContext(Context context){
        this.context=context;
    }
    public StringAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    public void setNewData(@Nullable List<String> data) {
        super.setNewData(data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
        super.convert(helper, item);
        helper.setText(R.id.tv_area_name,item);

    }




}
