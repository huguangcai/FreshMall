package com.ysxsoft.freshmall.dialog;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.utils.ActivityPageManager;
import com.ysxsoft.freshmall.view.LoginActivity;
import com.ysxsoft.freshmall.widget.ABSDialog;

public class VersionUpdataDilaog extends ABSDialog {
    private Context context;

    public VersionUpdataDilaog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void initView() {
        TextView tv_ok = getViewById(R.id.tv_ok);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.version_update_dialog_layout;
    }
}
