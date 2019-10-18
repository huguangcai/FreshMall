package com.ysxsoft.freshmall.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.widget.ABSDialog;

public class ShareDialog extends ABSDialog {
    public ShareDialog(@NonNull Context context) {
        super(context);
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width= WindowManager.LayoutParams.MATCH_PARENT;
        params.height=WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.share_dialog_layout;
    }
}
