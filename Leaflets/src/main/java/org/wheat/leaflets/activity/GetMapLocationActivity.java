package org.wheat.leaflets.activity;

import android.os.Bundle;

import com.amap.mapapi.map.MapActivity;
import com.amap.mapapi.map.MapView;

import org.wheat.leaflets.R;
import org.wheat.leaflets.overlay.LocationSelectOverlay;
import org.wheat.leaflets.overlay.PopupPanel;

/**
 * Created by Administrator on 2015/4/23.
 *
 */
public class GetMapLocationActivity extends MapActivity {
    private MapView mMapView;

    LocationSelectOverlay mSelectLay;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //使用setContentView方法调用R.layout.activity_regeocoder布局文件，显示地图

        setContentView(R.layout.activity_get_map_location);

        //获取地图视图的id，赋值给mMapView

        mMapView = ((MapView) findViewById(R.id.geocode_MapView));

        // 设置启用内置的缩放控件

        mMapView.setBuiltInZoomControls(true);

        //实例化一个locationSelectOverlay类

        mSelectLay = new LocationSelectOverlay(this, mMapView, new PopupPanel(this, mMapView));

        //将该功能加载到此地图上，启用长按地图显示该点地址信息的功能

        mMapView.getOverlays().add(mSelectLay);
    }
}
