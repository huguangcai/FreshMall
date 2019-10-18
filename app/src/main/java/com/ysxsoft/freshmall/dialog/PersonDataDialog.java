package com.ysxsoft.freshmall.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.widget.ABSDialog;

public class PersonDataDialog extends ABSDialog {

    public PersonDataDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        TextView tv_cancle = getViewById(R.id.tv_cancle);
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.person_data_dialog_layout;
    }
}
