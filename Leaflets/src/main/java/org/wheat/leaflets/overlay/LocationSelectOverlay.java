package org.wheat.leaflets.overlay;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.mapapi.core.AMapException;
import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.geocoder.Geocoder;
import com.amap.mapapi.map.MapView;
import com.amap.mapapi.map.Overlay;

import org.wheat.leaflets.R;
import org.wheat.leaflets.entity.Constants;

import java.util.List;

/**
 * Created by Administrator on 2015/4/23.
 */
public class LocationSelectOverlay extends Overlay implements GestureDetector.OnGestureListener
{
    public PopupPanel mTipPanel;    //����һ�����������

    GeoPoint mSelectPoint;          //����һ��������������

    MapView mMapView;            //����һ����ͼ��ͼ����

    Context mContext;               //�����

    TextView mTipText=null;         //����һ���ı�����

    private static String nearbystr="";

    private GestureDetector gestureScanner; //����һ�����Ƽ�������

    private  Geocoder coder;           //����һ�������������

    private String addressName="";       //����һ����ַ�����ַ���



//������ͼĳ���ȡ��Ϣ�Ĺ��캯����

    public LocationSelectOverlay(Activity context,MapView mapView,PopupPanel panel)

    {

        this.mContext=context;

        this.mMapView=mapView;

        this.mTipPanel=panel;

        gestureScanner = new GestureDetector(this); //����һ�����Ƽ�������

        coder = new Geocoder(context);       //����һ�������������

    }

    //��Handler�������������ĵ�ַ��Ϣ����ʾ���ı�����

    private Handler mGeocoderHandler = new Handler()

    {

        public void handleMessage(Message msg)

        {

//����е�ַ��Ϣ����Ϣ���͹��������ı���������Ϊ�õ�ַ��Ϣ

            if(msg.what == Constants.REOCODER_RESULT)

            {

                if(mTipText!=null)

                    mTipText.setText(addressName);

            }

            //�����ʾ�������ı��������ñ�����Ϣ

            else if(msg.what == Constants.ERROR)

            {

                Toast.makeText(mContext, "��ȡ��ַʧ�ܣ�������", Toast.LENGTH_SHORT).show();

                removeTipPanel();

            }

        }

    };

    //��ʾ��������

    public boolean showTap(GeoPoint p) {

        View view = mTipPanel.getView();

        mMapView.removeView(view);

        //���ֲ�������

        MapView.LayoutParams geoLP = new MapView.LayoutParams(

                MapView.LayoutParams.WRAP_CONTENT,

                MapView.LayoutParams.WRAP_CONTENT, p,

                MapView.LayoutParams.BOTTOM_CENTER);

        //�������ڵ��ı���ʾ

        mTipText = (TextView) view.findViewById(R.id.get_map_location_pw_address);

        mTipText.setText("���ڼ��ص�ַ...");

        mTipText.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

            }

        });

//�ڵ�ͼ��ͼ����Ӹõ���������ͼ

        mMapView.addView(view, geoLP);

        return false;

    }

//�Ӿ�γ��������ȡ��Ӧ�ĵ�ַ��Ϣ

    public  void getAddressFromServer(final GeoPoint point,final Handler handler)

    {

        //����һ���߳�

        new Thread(){

            public void run()

            {

                try {

                    // ��������getFromLocation()������ȡ�õ��Ӧ��ǰ3����ַ��Ϣ

                    List<Address> address = coder.getFromLocation((double)point.getLatitudeE6()/1E6,

                            (double)point.getLongitudeE6()/1E6, 3);

                    if (address != null) {

                        //��ȡ��һ����ַ��Ϣ

                        Address addres = address.get(0);

                        addressName = "";

                        if(addres.getAdminArea()!=null)

                            addressName+=addres.getAdminArea();

                        if(addres.getSubLocality()!=null)

                            addressName += addres.getSubLocality();

                        if(addres.getFeatureName()!=null)

                            addressName += addres.getFeatureName();

                        addressName += "����";

                        handler.sendMessage(Message

                                .obtain(handler, Constants.REOCODER_RESULT));



                    }

                } catch (AMapException e) {

                    // TODO Auto-generated catch block

                    handler.sendMessage(Message

                            .obtain(handler, Constants.ERROR));

                }

            }

        }.start(); //�߳�����

    }

    //���ߵ�������

    public void removeTipPanel()

    {

        View view = mTipPanel.getView();

        mMapView.removeView(view);

    }

    //��ȡ���Ʋ���

    public boolean onTouchEvent(MotionEvent event, MapView mapView)

    {

        return gestureScanner.onTouchEvent(event);

    }



    @Override

    public boolean onDown(MotionEvent e) {

        // TODO Auto-generated method stub

        return false;

    }



    @Override

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,

                           float velocityY) {

        // TODO Auto-generated method stub



        return false;

    }

//������ͼ��������ʾ����ʾ�õ��ַ��Ϣ

    @Override

    public void onLongPress(MotionEvent e) {

        // TODO Auto-generated method stub

        int x = (int)e.getX();

        int y = (int)e.getY();

        mSelectPoint = mMapView.getProjection().fromPixels(x, y);

        //������ʾ��ʾ����

        showTap(mSelectPoint);

        //���ôӾ�γ�ȵ��ȡ��ַ��Ϣ����

        getAddressFromServer(mSelectPoint,mGeocoderHandler);

    }



    @Override

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

        // TODO Auto-generated method stub

        return false;

    }



    @Override

    public void onShowPress(MotionEvent e) {

        // TODO Auto-generated method stub



    }



    @Override

    public boolean onSingleTapUp(MotionEvent e) {

        // TODO Auto-generated method stub



        return false;

    }
}
