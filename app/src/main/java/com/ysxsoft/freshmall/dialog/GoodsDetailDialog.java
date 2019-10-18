package com.ysxsoft.freshmall.dialog;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.widget.ABSDialog;

public class GoodsDetailDialog extends ABSDialog {
    public GoodsDetailDialog(@NonNull Context context) {
        super(context);
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
    }

    @Override
    protected void initView() {
        ImageView img_close = getViewById(R.id.img_close);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        TextView tv_old_price = getViewById(R.id.tv_old_price);
        tv_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        TextView tv_minus = getViewById(R.id.tv_minus);
        final TextView tv_num = getViewById(R.id.tv_num);
        TextView tv_add = getViewById(R.id.tv_add);

        tv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_num.getText().toString().equals("1")) {
                    tv_num.setText(1 + "");
                }else {
                    tv_num.setText(String.valueOf(Integer.valueOf(tv_num.getText().toString()) - 1));
                }
            }
        });
        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_num.setText(String.valueOf(Integer.valueOf(tv_num.getText().toString()) + 1));
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.detail_dialog_layout;
    }
}
