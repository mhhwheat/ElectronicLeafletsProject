package org.wheat.leaflets.activity;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;

import org.wheat.leaflets.R;
import org.wheat.leaflets.basic.ExitApplication;

/**
 * Created by Administrator on 2015/4/24.
 */
public class MapLocationActivity extends Activity implements AMap.OnMapLoadedListener,AMap.OnMapLongClickListener,AMapLocationListener
{

    private AMap aMap;
    private MapView mapView;
    private Marker marker;// 有跳动效果的marker对象
    private LatLng latLng;

    private ImageView ivTitleBack;
    private TextView tvFinish;

    private LocationManagerProxy mLocationManagerProxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_map_location);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_map_location_title);

        mapView=(MapView)findViewById(R.id.map_location_map_view);
        mapView.onCreate(savedInstanceState);
        initialAMap();
        // 开启界面的时候就定位
        mLocationManagerProxy = LocationManagerProxy.getInstance(this);
        mLocationManagerProxy.setGpsEnable(false);
        mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, -1, 150, this);

        initialTitle();

        ExitApplication.getInstance().addActivity(this);
    }

    private void initialAMap(  )
    {
        if(aMap==null)
        {
            aMap=mapView.getMap();
            setUpMap();
        }
    }

    private void setUpMap() {
        aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
        aMap.setOnMapLongClickListener(this);
    }

    private void addMarkersToMap(LatLng latlng)
    {
        MarkerOptions options=new MarkerOptions();
        options.position(latlng);
        options.draggable(true);
        marker=aMap.addMarker(options);
    }

    private void initialTitle( )
    {
        ivTitleBack=(ImageView)findViewById(R.id.map_location_title_back_img);
        tvFinish=(TextView)findViewById(R.id.map_location_title_finish);

        ivTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(0,null);
                MapLocationActivity.this.finish();
            }
        });

        tvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(latLng==null)
                {
                    Toast.makeText(MapLocationActivity.this,"还没标记任何地址",Toast.LENGTH_LONG);
                }
                else
                {
                    Intent intent=new Intent();
                    intent.putExtra("lat",latLng.latitude);
                    intent.putExtra("lng",latLng.longitude);
                    setResult(1, intent);
                    MapLocationActivity.this.finish();
                }
            }
        });
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onMapLoaded() {
//        //设置中心点和缩放比例
//        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latlng));
//        aMap.moveCamera(CameraUpdateFactory.zoomTo(12));
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        if(marker!=null)
            marker.remove();
        addMarkersToMap(latLng);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation!=null&&aMapLocation.getAMapException().getErrorCode() == 0)
        {
            latLng=new LatLng(aMapLocation.getLatitude(),aMapLocation.getLongitude());
        }

        //设置中心点和缩放比例
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
        aMap.moveCamera(CameraUpdateFactory.zoomTo(12));
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(0,null);
            MapLocationActivity.this.finish();
        }
        return true;
    }
}
