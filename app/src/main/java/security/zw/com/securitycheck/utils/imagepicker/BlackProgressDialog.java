package security.zw.com.securitycheck.utils.imagepicker;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;


import security.zw.com.securitycheck.R;
import security.zw.com.securitycheck.utils.WindowUtils;


public class BlackProgressDialog extends AlertDialog {

    public BlackProgressDialog(Context context) {
        super(context);
    }

    public BlackProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    public BlackProgressDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(WindowUtils.dp2Px(180), WindowUtils.dp2Px(100));
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        FrameLayout fl = (FrameLayout) findViewById(android.R.id.custom);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_progress, null);
        setContentView(view);
    }
}
