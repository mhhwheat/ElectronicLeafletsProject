package org.wheat.leaflets.overlay;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.amap.mapapi.map.MapView;

import org.wheat.leaflets.R;

/**
 * Created by Administrator on 2015/4/23.
 */
public class PopupPanel
{
    private boolean isVisible = false;
    private MapView mMapView;
    private View popup;

    public PopupPanel(Activity paramActivity, MapView paramMapView)
    {
        this.mMapView = paramMapView;
        ViewGroup localViewGroup = (ViewGroup)this.mMapView.getParent();
        //设置弹出的视图是id为R.layout.activity_long_press_map的视图
        this.popup = paramActivity.getLayoutInflater().inflate(R.layout.activity_get_map_location_pw, localViewGroup, false);
    }

    public View getView(   )
    {
        return popup;
    }
}
