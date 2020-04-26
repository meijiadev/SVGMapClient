package ddr.example.com.svgmapclient.ui.dialog;

import android.text.InputFilter;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;


import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentActivity;
import ddr.example.com.svgmapclient.R;
import ddr.example.com.svgmapclient.base.BaseDialog;
import ddr.example.com.svgmapclient.common.MyDialogFragment;
import ddr.example.com.svgmapclient.other.InputFilterMinMax;

/**
 *    time   : 2019/02/27
 *    desc   : 输入对话框
 */
public final class InputDialog {

    public static final class Builder
            extends MyDialogFragment.Builder<Builder>
            implements View.OnClickListener,
            BaseDialog.OnShowListener,
            BaseDialog.OnDismissListener {

        private OnListener mListener;
        private boolean mAutoDismiss = true;

        private final TextView mTitleView;
        private final EditText mInputView;

        private final TextView mCancelView;
        private final TextView mConfirmView;

        public Builder(FragmentActivity activity) {
            super(activity);
            setContentView(R.layout.dialog_input);
            setAnimStyle(BaseDialog.AnimStyle.IOS);

            mTitleView = findViewById(R.id.tv_input_title);
            mInputView = findViewById(R.id.tv_input_message);

            mCancelView  = findViewById(R.id.tv_input_cancel);
            mConfirmView  = findViewById(R.id.tv_input_confirm);

            mCancelView.setOnClickListener(this);
            mConfirmView.setOnClickListener(this);

            addOnShowListener(this);
            addOnDismissListener(this);
        }

        public Builder setTitle(@StringRes int id) {
            return setTitle(getString(id));
        }
        public Builder setTitle(CharSequence text) {
            mTitleView.setText(text);
            return this;
        }

        /**
         * 设置EditText是否可见
         * @param visibility
         * @return
         */
        public Builder setEditVisibility(int visibility){
            mInputView.setVisibility(visibility);
            return this;
        }

        /**
         * 设置EditText的输入限制
         * @param num
         * @return
         */
        public Builder setEditNumAndSize(int num){
            mInputView.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
            mInputView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(num)});
            mInputView.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "999")});
            return this;
        }
        public Builder setHint(@StringRes int id) {
            return setHint(getString(id));
        }
        public Builder setHint(CharSequence text) {
            mInputView.setHint(text);
            return this;
        }

        public Builder setContent(@StringRes int id) {
            return setContent(getString(id));
        }
        public Builder setContent(CharSequence text) {
            mInputView.setText(text);
            int index = mInputView.getText().toString().length();
            if (index > 0) {
                mInputView.requestFocus();
                mInputView.setSelection(index);
            }
            return this;
        }

        public Builder setConfirm(@StringRes int id) {
            return setConfirm(getString(id));
        }
        public Builder setConfirm(CharSequence text) {
            mConfirmView.setText(text);
            return this;
        }

        public Builder setCancel(@StringRes int id) {
            return setCancel(getString(id));
        }
        public Builder setCancel(CharSequence text) {
            mCancelView.setText(text);

            return this;
        }

        /**
         * 判断是否自动弹窗消失
         * @param dismiss
         * @return
         */
        public Builder setAutoDismiss(boolean dismiss) {
            mAutoDismiss = dismiss;
            return this;
        }

        public Builder setListener(OnListener listener) {
            mListener = listener;
            return this;
        }

        @Override
        public InputDialog.Builder setThemeStyle(int id) {
            return super.setThemeStyle(id);
        }

        /**
         * {@link BaseDialog.OnShowListener}
         */
        @Override
        public void onShow(BaseDialog dialog) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    getSystemService(InputMethodManager.class).showSoftInput(mInputView, 0);
                }
            }, 300);
        }

        /**
         * {@link BaseDialog.OnDismissListener}
         */
        @Override
        public void onDismiss(BaseDialog dialog) {
            getSystemService(InputMethodManager.class).hideSoftInputFromWindow(mInputView.getWindowToken(), 0);
        }

        /**
         * {@link View.OnClickListener}
         */
        @Override
        public void onClick(View v) {
            if (mAutoDismiss) {
                dismiss();
            }

            if (mListener != null) {
                if (v == mConfirmView) {
                    // 判断输入是否为空
                    mListener.onConfirm(getDialog(), mInputView.getText().toString());
                } else if (v == mCancelView) {
                    mListener.onCancel(getDialog());
                }
            }
        }
    }

    public interface OnListener {

        /**
         * 点击确定时回调
         */
        void onConfirm(BaseDialog dialog, String content);

        /**
         * 点击取消时回调
         */
        void onCancel(BaseDialog dialog);
    }
}