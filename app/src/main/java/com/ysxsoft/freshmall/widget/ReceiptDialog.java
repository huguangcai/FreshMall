package com.ysxsoft.freshmall.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.utils.AppUtil;

/**
 * Create By 胡
 * on 2020/1/8 0008
 */
public class ReceiptDialog extends ABSDialog {

    private int receiptType = 1;
    private int receiptContent = 0;
    private OnReceiptDialogListener onReceiptDialogListener;
    private Context mContext;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;

    public ReceiptDialog(@NonNull Context context) {
        super(context);
        mContext = context;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.width = AppUtil.getScreenWidth(context) * 4 / 5;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = AppUtil.getScreenHeight(context) * 3/ 4;
        getWindow().setAttributes(lp);
        getWindow().setGravity(Gravity.BOTTOM);
    }

    @Override
    protected void initView() {
        ImageView ivClose = getViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        final LinearLayout LL1 = getViewById(R.id.LL1);
        final LinearLayout LL2 = getViewById(R.id.LL2);
        final EditText etUnitName = getViewById(R.id.etUnitName);
        final EditText etNSNum = getViewById(R.id.etNSNum);
        tv1 = getViewById(R.id.tv1);
        tv2 = getViewById(R.id.tv2);
        tv1.setSelected(true);
        tv1.setTextColor(getContext().getResources().getColor(R.color.btn_color));
        final EditText etPhone = getViewById(R.id.etPhone);
        final EditText etEmail = getViewById(R.id.etEmail);
        tv3 = getViewById(R.id.tv3);
        tv4 = getViewById(R.id.tv4);
        tv3.setSelected(true);
        tv3.setTextColor(getContext().getResources().getColor(R.color.btn_color));
        LL1.setVisibility(View.GONE);
        LL2.setVisibility(View.GONE);

        TextView tv_ok = getViewById(R.id.tv_ok);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receiptType = 1;
                LL1.setVisibility(View.GONE);
                LL2.setVisibility(View.GONE);
                tv1.setSelected(true);
                tv2.setSelected(false);
                tv1.setTextColor(mContext.getResources().getColor(R.color.btn_color));
                tv2.setTextColor(mContext.getResources().getColor(R.color.color_333333));
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receiptType = 2;
                LL1.setVisibility(View.VISIBLE);
                LL2.setVisibility(View.VISIBLE);
                tv1.setSelected(false);
                tv2.setSelected(true);
                tv1.setTextColor(mContext.getResources().getColor(R.color.color_333333));
                tv2.setTextColor(mContext.getResources().getColor(R.color.btn_color));
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receiptContent = 0;
                tv3.setSelected(true);
                tv4.setSelected(false);
                tv3.setTextColor(mContext.getResources().getColor(R.color.btn_color));
                tv4.setTextColor(mContext.getResources().getColor(R.color.color_333333));
            }
        });
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receiptContent = 1;
                tv3.setSelected(false);
                tv4.setSelected(true);
                tv3.setTextColor(mContext.getResources().getColor(R.color.color_333333));
                tv4.setTextColor(mContext.getResources().getColor(R.color.btn_color));
            }
        });

        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (receiptType == 2) {
                    if (TextUtils.isEmpty(etUnitName.getText().toString().trim())) {
                        Toast.makeText(mContext, "单位名称不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(etNSNum.getText().toString().trim())) {
                        Toast.makeText(mContext, "纳税人识别号不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if (TextUtils.isEmpty(etPhone.getText().toString().trim())) {
                    Toast.makeText(mContext, "手机号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(etEmail.getText().toString().trim())) {
                    Toast.makeText(mContext, "邮箱不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (onReceiptDialogListener != null) {
                    onReceiptDialogListener.sure(String.valueOf(receiptType),etUnitName.getText().toString().trim(),etNSNum.getText().toString().trim(), etPhone.getText().toString().trim(), etEmail.getText().toString().trim(), String.valueOf(receiptContent));
                    dismiss();
                }
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.dialog_receipt_layout;
    }

    public interface OnReceiptDialogListener {
        void sure(String receiptType,String UnitName,String NSNum, String phone, String email, String receiptContent);
    }

    public void setOnReceiptDialogListener(OnReceiptDialogListener onReceiptDialogListener) {
        this.onReceiptDialogListener = onReceiptDialogListener;
    }
}
