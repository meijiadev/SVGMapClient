package ddr.example.com.svgmapclient.ui.dialog;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentActivity;
import ddr.example.com.svgmapclient.R;
import ddr.example.com.svgmapclient.base.BaseDialog;
import ddr.example.com.svgmapclient.common.MyDialogFragment;

/**
 *    time   : 2018/12/2
 *    desc   : 等待加载对话框
 */
public final class WaitDialog {
    public static String message;
    public static final class Builder
            extends MyDialogFragment.Builder<Builder> {

        public final TextView mMessageView;

        public Builder(FragmentActivity activity) {
            super(activity);
            setContentView(R.layout.dialog_wait);
            setAnimStyle(BaseDialog.AnimStyle.TOAST);
            setBackgroundDimEnabled(false);
            setCancelable(false);

            mMessageView = findViewById(R.id.tv_wait_message);
        }

        public Builder setMessage(@StringRes int id) {
            return setMessage(getString(id));
        }
        public Builder setMessage(CharSequence text) {
            mMessageView.setText(text);
            mMessageView.setVisibility(text == null ? View.GONE : View.VISIBLE);
            return this;
        }


    }


}