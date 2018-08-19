package security.zw.com.securitycheck;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import security.zw.com.securitycheck.base.BaseSystemBarTintActivity;
import security.zw.com.securitycheck.bean.ProjectDetail;
import security.zw.com.securitycheck.bean.ProjectInfo;
import security.zw.com.securitycheck.fragment.HomeFragment;
import security.zw.com.securitycheck.fragment.MyFragment;
import security.zw.com.securitycheck.utils.imagepicker.ImagesPickerActivity;
import security.zw.com.securitycheck.utils.toast.ToastUtil;


public class MainActivity extends BaseSystemBarTintActivity {


    public static void launch(Context ctx) {
        Intent intent = new Intent(ctx, MainActivity.class);
        ctx.startActivity(intent);
    }

    public static void launch(Context ctx, int index) {
        Intent intent = new Intent(ctx, MainActivity.class);
        intent.putExtra("index", index);
        ctx.startActivity(intent);
    }


    /*
 * 是否设置沉浸式状态栏
 */
    protected boolean isNeedImmersiveStatusBar() {
        return true;
    }

    protected int getImmersiveStatusBarColor() {
        return R.color.colorPrimary;
    }

    private ViewPager mPager;
    private HomePagerAdapter mPagerAdapter;

    public class HomePagerAdapter extends FragmentPagerAdapter {

        public HomePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragents.get(position);
        }

        @Override
        public int getCount() {
            return mFragents.size();
        }
    }


    private RelativeLayout homeRel;
    private SimpleDraweeView homeImg;
    private TextView homeName;


    private RelativeLayout myRel;
    private SimpleDraweeView myImg;
    private TextView myName;


    private List<Fragment> mFragents = new ArrayList<Fragment>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //如果activity被回收，则清除fragment缓存
        if (null != savedInstanceState) {
            savedInstanceState.remove("android:support:fragments");
        }
        setContentView(R.layout.activity_main);

        if (SecurityApplication.mUser == null) {
            LoginActivity.launch(this);
            finish();
        }

        initWidget();
        initData();
        initPagerAdapter();

    }

    private void initWidget() {
        mPager = (ViewPager) findViewById(R.id.pager);
        homeRel = (RelativeLayout) findViewById(R.id.home_rel);
        homeImg = (SimpleDraweeView) findViewById(R.id.home_img);
        homeName = (TextView) findViewById(R.id.home_name);



        myRel = (RelativeLayout) findViewById(R.id.my_rel);
        myImg = (SimpleDraweeView) findViewById(R.id.my_img);
        myName = (TextView) findViewById(R.id.my_name);

        setSelect(0);
    }







    private void initData() {
        mFragents.add(new HomeFragment());
        mFragents.add(new MyFragment());
    }

    private void initPagerAdapter() {
        mPagerAdapter = new HomePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        //ViewPager界面滑动的监听器
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                setSelect(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mPager.setOffscreenPageLimit(1);

        homeRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelect(0);
            }
        });

        myRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelect(1);
            }
        });
    }

    public void setSelect(int i) {
        switch (i) {
            case 0:

                homeImg.setImageResource(R.mipmap.home_click);
                homeName.setTextColor(0xff7255f6);


                myImg.setImageResource(R.mipmap.my_unclick);
                myName.setTextColor(0xffb7b6bc);

                mPager.setCurrentItem(i, false);

                break;
            case 1:

                homeImg.setImageResource(R.mipmap.home_unclick);
                homeName.setTextColor(0xff777e98);

                myImg.setImageResource(R.mipmap.my_click);
                myName.setTextColor(0xff777e98);
                mPager.setCurrentItem(i, false);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }




    Handler exitHandler = new Handler();
    private static Boolean isExit = false;
    private static Boolean hasTask = false;
    Runnable task = new Runnable() {

        @Override
        public void run() {
            isExit = false;
            hasTask = false;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isExit) {
                isExit = true;
                ToastUtil.Long( "再按一次返回键退出");
                if (!hasTask) {
                    hasTask = true;
                    exitHandler.postDelayed(task, 3000);
                }
            } else {
                finish();
                exitHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        System.exit(0);
                    }
                }, 800);
            }
            return true;
        }


        return super.onKeyDown(keyCode, event);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int index = intent.getIntExtra("index", 0);
        setSelect(index);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == 300) {
            String res = data.getStringExtra("result");
            if (res.startsWith("se://id=")) {
                int id = Integer.parseInt(res.substring(8)) ;
                ProjectInfo p = new ProjectInfo();
                p.id = id;
                ProjectDetailActivity.launch(MainActivity.this, p);
            }
        } else if (requestCode == RESULT_OK){
            IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
            if(intentResult != null) {
                if(intentResult.getContents() == null) {
                    Toast.makeText(this,"内容为空",Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this,"扫描成功",Toast.LENGTH_LONG).show();
                    // ScanResult 为 获取到的字符串
                    String ScanResult = intentResult.getContents();
                    if (ScanResult.startsWith("se://id=")) {
                        int id = Integer.parseInt(ScanResult.substring(8)) ;
                        ProjectInfo p = new ProjectInfo();
                        p.id = id;
                        ProjectDetailActivity.launch(MainActivity.this, p);
                    }

                }
            } else {
                super.onActivityResult(requestCode,resultCode,data);
            }
        }
    }
}
