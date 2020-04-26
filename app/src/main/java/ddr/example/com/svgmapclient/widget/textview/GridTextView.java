package ddr.example.com.svgmapclient.widget.textview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class GridTextView extends TextView {
    private boolean isSelected=false;
    public GridTextView(Context context) {
        super(context);
    }

    public GridTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setSelected(boolean isSelected){
        this.isSelected=isSelected;
    }

    public boolean getSelected(){
        return isSelected;
    }
}
