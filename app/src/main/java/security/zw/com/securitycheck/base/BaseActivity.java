package security.zw.com.securitycheck.base;
/**
 * liangkang
 */

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;

public abstract class BaseActivity extends BaseSystemBarTintActivity {
	

    public volatile boolean isOnResume = false;

	private final long ForceStatInterval = 1000 * 60 * 10;
	private long lastForceStatTime;

	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			onHandleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getLayoutId());

		initView();
		initData();
		lastForceStatTime = System.currentTimeMillis();

//		PushProcesser.getInstance().addListener(mINoticeListener);
	}
	
	protected abstract int getLayoutId();
	protected abstract void initView();
	protected abstract void initData();
	

	public FragmentActivity getActivity() {
		return this;
	}

	protected void onHandleMessage(Message msg) {
		// handle message
	}



	public void hideSavingDialog(ProgressDialog dialog) {
		if (!isFinishing() && dialog != null && dialog.getWindow() != null && dialog.isShowing()) {
			try {
				dialog.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}



	@Override
	protected void onResume() {
		super.onResume();
		isOnResume = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		isOnResume = false;
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void finish() {
		super.finish();
	}
	
	@Override
	protected void onDestroy() {
		mHandler.removeCallbacksAndMessages(null);
		super.onDestroy();
	}

	protected <T extends View> T $(int resId) {
		return (T) findViewById(resId);
	}

    protected void postDelayed(Runnable runnable) {
		postDelayed(runnable, 0);
	}

    protected void postDelayed(Runnable runnable, long delay) {
		if (mHandler != null) {
			mHandler.removeCallbacks(runnable);

			if (!isFinishing()) {
				mHandler.postDelayed(runnable, delay);
			}
		}
	}

    protected void removeDelayed(Runnable runnable) {
		if (mHandler != null) {
			mHandler.removeCallbacks(runnable);
		}
	}

	private void forceStatIfNeed() {
		if (System.currentTimeMillis() - lastForceStatTime > ForceStatInterval) {
			lastForceStatTime = System.currentTimeMillis();
		}
	}
}
