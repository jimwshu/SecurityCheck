package security.zw.com.securitycheck;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import security.zw.com.securitycheck.base.BaseSystemBarTintActivity;
import security.zw.com.securitycheck.utils.LogUtils;

// 登陆
public class MapActivity extends BaseSystemBarTintActivity {

    String address;

    @Override
    protected boolean isNeedImmersiveStatusBar() {
        return true;
    }

    public static void launch(Context ctx, String address) {
        Intent intent = new Intent(ctx, MapActivity.class);
        intent.putExtra("address", address);
        ctx.startActivity(intent);
    }

    public static void launch(Context ctx, String address, boolean red) {
        Intent intent = new Intent(ctx, MapActivity.class);
        intent.putExtra("address", address);
        intent.putExtra("red", red);
        ctx.startActivity(intent);
    }

    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private BitmapDescriptor mCurrentMarker;
    private boolean red;
    GeoCoder geoCoder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        setContentView(R.layout.activity_map);
        address = getIntent().getStringExtra("address");
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        geoCoder = GeoCoder.newInstance();
        red = getIntent().getBooleanExtra("red", false);
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                LogUtils.e("geoCodeResult: " + geoCodeResult.getAddress() + " " + geoCodeResult.getLocation());
                if (geoCodeResult != null && geoCodeResult.getLocation() != null) {
                    setLocation(geoCodeResult);
                }
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

            }
        });
        GeoCodeOption option = new GeoCodeOption()
                .city("北京")
                .address(address);
        geoCoder.geocode(option);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    public void setLocation(GeoCodeResult result) {

        mBaiduMap.clear();
        //定义Maker坐标点

        LatLng point = result.getLocation();

//构建Marker图标
        BitmapDescriptor bitmap;
        if (red) {
            bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.a);
        } else {
            bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.b);
        }

//构建MarkerOption，用于在地图上添加Marker

        OverlayOptions option = new MarkerOptions()
                .zIndex(18)
                .perspective(true)
                .position(point)
                .icon(bitmap);

//在地图上添加Marker，并显示

        mBaiduMap.addOverlay(option);

        MapStatus mMapStatus = new MapStatus.Builder()
                .target(point)
                .zoom(18)
                .build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.setMapStatus(mMapStatusUpdate);
/*        mBaiduMap.clear();
        mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
                .icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.loc)));
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
                .getLocation()));*/
    }

}

