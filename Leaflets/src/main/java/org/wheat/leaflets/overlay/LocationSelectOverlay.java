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
    public PopupPanel mTipPanel;    //声明一个弹出框对象

    GeoPoint mSelectPoint;          //声明一个地理坐标点对象

    MapView mMapView;            //声明一个地图视图对象

    Context mContext;               //活动对象

    TextView mTipText=null;         //声明一个文本对象

    private static String nearbystr="";

    private GestureDetector gestureScanner; //声明一个手势监听对象

    private  Geocoder coder;           //声明一个逆地理编码对象

    private String addressName="";       //声明一个地址名称字符串



//长按地图某点获取信息的构造函数。

    public LocationSelectOverlay(Activity context,MapView mapView,PopupPanel panel)

    {

        this.mContext=context;

        this.mMapView=mapView;

        this.mTipPanel=panel;

        gestureScanner = new GestureDetector(this); //声明一个手势监听对象

        coder = new Geocoder(context);       //声明一个逆地理编码对象

    }

    //用Handler函数处理传递来的地址信息，显示在文本框中

    private Handler mGeocoderHandler = new Handler()

    {

        public void handleMessage(Message msg)

        {

//如果有地址信息的消息发送过来，将文本框中设置为该地址信息

            if(msg.what == Constants.REOCODER_RESULT)

            {

                if(mTipText!=null)

                    mTipText.setText(addressName);

            }

            //如果显示错误，则文本框中设置报错信息

            else if(msg.what == Constants.ERROR)

            {

                Toast.makeText(mContext, "获取地址失败，请重试", Toast.LENGTH_SHORT).show();

                removeTipPanel();

            }

        }

    };

    //显示弹出窗口

    public boolean showTap(GeoPoint p) {

        View view = mTipPanel.getView();

        mMapView.removeView(view);

        //布局参数设置

        MapView.LayoutParams geoLP = new MapView.LayoutParams(

                MapView.LayoutParams.WRAP_CONTENT,

                MapView.LayoutParams.WRAP_CONTENT, p,

                MapView.LayoutParams.BOTTOM_CENTER);

        //弹出窗口的文本显示

        mTipText = (TextView) view.findViewById(R.id.get_map_location_pw_address);

        mTipText.setText("正在加载地址...");

        mTipText.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

            }

        });

//在地图视图上添加该弹出窗口视图

        mMapView.addView(view, geoLP);

        return false;

    }

//从经纬度坐标点获取对应的地址信息

    public  void getAddressFromServer(final GeoPoint point,final Handler handler)

    {

        //声明一个线程

        new Thread(){

            public void run()

            {

                try {

                    // 逆地理编码getFromLocation()函数获取该点对应的前3个地址信息

                    List<Address> address = coder.getFromLocation((double)point.getLatitudeE6()/1E6,

                            (double)point.getLongitudeE6()/1E6, 3);

                    if (address != null) {

                        //获取第一个地址信息

                        Address addres = address.get(0);

                        addressName = "";

                        if(addres.getAdminArea()!=null)

                            addressName+=addres.getAdminArea();

                        if(addres.getSubLocality()!=null)

                            addressName += addres.getSubLocality();

                        if(addres.getFeatureName()!=null)

                            addressName += addres.getFeatureName();

                        addressName += "附近";

                        handler.sendMessage(Message

                                .obtain(handler, Constants.REOCODER_RESULT));



                    }

                } catch (AMapException e) {

                    // TODO Auto-generated catch block

                    handler.sendMessage(Message

                            .obtain(handler, Constants.ERROR));

                }

            }

        }.start(); //线程启动

    }

    //移走弹出窗口

    public void removeTipPanel()

    {

        View view = mTipPanel.getView();

        mMapView.removeView(view);

    }

    //获取手势操作

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

//长按地图，弹出提示框，显示该点地址信息

    @Override

    public void onLongPress(MotionEvent e) {

        // TODO Auto-generated method stub

        int x = (int)e.getX();

        int y = (int)e.getY();

        mSelectPoint = mMapView.getProjection().fromPixels(x, y);

        //调用显示提示框函数

        showTap(mSelectPoint);

        //调用从经纬度点获取地址信息函数

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
