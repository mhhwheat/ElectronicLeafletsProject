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

        //ʹ��setContentView��������R.layout.activity_regeocoder�����ļ�����ʾ��ͼ

        setContentView(R.layout.activity_get_map_location);

        //��ȡ��ͼ��ͼ��id����ֵ��mMapView

        mMapView = ((MapView) findViewById(R.id.geocode_MapView));

        // �����������õ����ſؼ�

        mMapView.setBuiltInZoomControls(true);

        //ʵ����һ��locationSelectOverlay��

        mSelectLay = new LocationSelectOverlay(this, mMapView, new PopupPanel(this, mMapView));

        //���ù��ܼ��ص��˵�ͼ�ϣ����ó�����ͼ��ʾ�õ��ַ��Ϣ�Ĺ���

        mMapView.getOverlays().add(mSelectLay);
    }
}
