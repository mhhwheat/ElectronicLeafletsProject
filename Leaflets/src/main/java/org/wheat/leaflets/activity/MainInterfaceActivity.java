/**
 * description：
 * @author wheat
 * date: 2015-3-9  
 * time: 下午8:06:32
 */
package org.wheat.leaflets.activity;



import org.wheat.leaflets.R;
import org.wheat.leaflets.activity.FragmentMainInterface.TitleAvatarListener;
import org.wheat.leaflets.basic.ExitApplication;
import org.wheat.leaflets.data.UserLoginPreference;
import org.wheat.leaflets.entity.MyLocation;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * description:
 * @author wheat
 * date: 2015-3-9  
 * time: 下午8:06:32
 */
public class MainInterfaceActivity extends FragmentActivity implements AMapLocationListener,TitleAvatarListener
{
    private UserLoginPreference preference;
    private LayoutInflater mInflater;

//	private PopupWindow popupMenu;


    private SlidingMenu menu;//侧滑菜单

    private Fragment mCurrentFragment;
    private FragmentMainInterface mMainInterfaceFragment;
    private Fragment mFollowFragment;


    /**
     * 高德地图定位信息
     */
    private LocationManagerProxy mLocationManagerProxy;
    private  MyLocation myLocation = new MyLocation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_interface);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        preference=UserLoginPreference.getInstance(getApplicationContext());
        mInflater=(LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        initialSlidingMenu();

        mMainInterfaceFragment=new FragmentMainInterface();
        mMainInterfaceFragment.setTitleAvatarListener(this);

        mFollowFragment=new FragmentFollow();
        switchFragment(null, mMainInterfaceFragment, R.id.replacing_fragment);
        mCurrentFragment=mMainInterfaceFragment;

        // 开启界面的时候就定位
        mLocationManagerProxy = LocationManagerProxy.getInstance(this);
        mLocationManagerProxy.setGpsEnable(false);
        mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, -1, 150, this);

        ExitApplication.getInstance().addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("System out", "Activity->onResume");
    }

    private void initialSlidingMenu()
    {
        menu=new SlidingMenu(this);
        menu.setShadowWidth(0);
        menu.setBehindOffset(200);
        menu.setFadeDegree(0.35f);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.main_interface_sliding_menu);
        FragmentSlidingMenu slidingFragment=new FragmentSlidingMenu();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu_frame, slidingFragment).commit();
    }


    /**
     * 切换Fragemtn,替换布局为R.id.replacing_fragment
     * @param from	切换前的Fragment
     * @param to	切换后的Fragment
     * @param sourceId 替换的布局id
     */
    public void switchFragment(Fragment from,Fragment to,int sourceId)
    {
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        if(!to.isAdded())
        {
            if(from!=null)
            {
                transaction.hide(from).add(sourceId, to).commit();
            }
            else
            {
                transaction.add(sourceId, to).commit();
            }
        }
        else
        {
            if(from!=null)
            {
                transaction.hide(from).show(to).commit();
            }
            else
            {
                transaction.show(to).commit();
            }
        }
    }




    //高德地图api接口

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub

    }



    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }



    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }



    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }



    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation!=null&&amapLocation.getAMapException().getErrorCode() == 0) {
            // 定位成功回调信息，设置相关消息
            this.myLocation.setLat(amapLocation.getLatitude());
            this.myLocation.setLng(amapLocation.getLongitude());
            this.myLocation.setLocationMessage(amapLocation.getAddress());
            preference.setLocationLat(amapLocation.getLatitude());
            preference.setLocationLng(amapLocation.getLongitude());
            Toast.makeText(this, amapLocation.getAddress(),Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "定位失败，请检查你的GPS和网络", Toast.LENGTH_LONG).show();
        }

    }

    //拦截返回键消息
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

        }
        return true;
    }





    @Override
    public void onTitleAvatarClicked() {
        menu.toggle();
    }

//		private ArrayList<OnActivityResultListener> onActivityResultListeners=new ArrayList<MainInterfaceActivity.OnActivityResultListener>();
//		
//		public interface OnActivityResultListener
//		{
//			public void onMyActivityResult(int requestCode, int resultCode, Intent data);
//		}
//		
//		public void addOnActivityResultListener(OnActivityResultListener listenr)
//		{
//			onActivityResultListeners.add(listenr);
//		}
//		
//		public void removeOnActivityResultListener(OnActivityResultListener listenr)
//		{
//			onActivityResultListeners.remove(listenr);
//		}


//	/** 
//	 * 以下的几个方法用来，让fragment能够监听touch事件 
//	 */  
//	private ArrayList<ActivityOnTouchListener> onTouchListeners = new ArrayList<ActivityOnTouchListener>();  
//
//	@Override  
//	public boolean dispatchTouchEvent(MotionEvent ev) 
//	{  
//		for (ActivityOnTouchListener listener : onTouchListeners) 
//		{  
//			listener.onTouch(ev);  
//		}  
//		return super.dispatchTouchEvent(ev);  
//	}  
//
//	public void registerActivityOnTouchListener(ActivityOnTouchListener ActivityOnTouchListener) {  
//		onTouchListeners.add(ActivityOnTouchListener);  
//	}  
//
//	public void unregisterActivityOnTouchListener(ActivityOnTouchListener ActivityOnTouchListener) {  
//		onTouchListeners.remove(ActivityOnTouchListener);  
//	}  
//
//	public interface ActivityOnTouchListener
//	{
//		public boolean onTouch(MotionEvent ev);  
//	}
//	
}
