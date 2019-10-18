package com.ysxsoft.freshmall.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.widget.ABSDialog;

public class ShowQrCodeDialog extends ABSDialog {

    public ShowQrCodeDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        ImageView img_cancle = getViewById(R.id.img_cancle);
        img_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.show_qrcode_dialog_layout;
    }
}
