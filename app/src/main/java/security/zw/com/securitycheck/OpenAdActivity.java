package security.zw.com.securitycheck;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import security.zw.com.securitycheck.base.BaseSystemBarTintActivity;
import security.zw.com.securitycheck.utils.imagepicker.ImagesPickerActivity;


/**
 *
 */
public class OpenAdActivity extends BaseSystemBarTintActivity {

    @Override
    protected boolean isNeedHideStatusBar() {
        return false;
    }

    public static final int start = 5, end = 0;

    Runnable removeCallbacks = new Runnable() {
        @Override
        public void run() {
            gotoMain();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_ad);
        findViewById(R.id.join).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().removeCallbacks(removeCallbacks);
                gotoMain();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(removeCallbacks, 3000);
    }

    private void gotoMain() {
        finish();
        ImagesPickerActivity.launch(OpenAdActivity.this, 3);
    }
}