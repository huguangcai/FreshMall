package com.ysxsoft.freshmall.dialog;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.widget.ABSDialog;

public class DeleteDialog extends ABSDialog {
    public DeleteDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.delete_dialog_layout;
    }
}
