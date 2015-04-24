/**
 * description��
 * @author wheat
 * date: 2015-3-9  
 * time: ����8:06:32
 */
package org.wheat.leaflets.activity;



import java.util.ArrayList;

import org.wheat.leaflets.R;
import org.wheat.leaflets.activity.FragmentMainInterface.TitleAvatarListener;
import org.wheat.leaflets.basic.ExitApplication;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * description:
 * @author wheat
 * date: 2015-3-9  
 * time: ����8:06:32
 */
public class MainInterfaceActivity extends FragmentActivity implements AMapLocationListener,TitleAvatarListener
{
    private LayoutInflater mInflater;

//	private PopupWindow popupMenu;


    private SlidingMenu menu;//�໬�˵�

    private Fragment mCurrentFragment;
    private FragmentMainInterface mMainInterfaceFragment;
    private Fragment mFollowFragment;


    /**
     * �ߵµ�ͼ��λ��Ϣ
     */
    private LocationManagerProxy mLocationManagerProxy;
    private  MyLocation myLocation = new MyLocation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);  
//		setContentView(R.layout.activity_main_interface);
//		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_main_interface_title);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_interface);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mInflater=(LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        initialSlidingMenu();

        mMainInterfaceFragment=new FragmentMainInterface();
        mMainInterfaceFragment.setTitleAvatarListener(this);

        mFollowFragment=new FragmentFollow();
        switchFragment(null, mMainInterfaceFragment, R.id.replacing_fragment);
        mCurrentFragment=mMainInterfaceFragment;

        // ���������ʱ��Ͷ�λ
        mLocationManagerProxy = LocationManagerProxy.getInstance(this);
        mLocationManagerProxy.setGpsEnable(false);
        mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, -1, 150, this);

        ExitApplication.getInstance().addActivity(this);
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





//	private void initPopupMenu()
//	{
//		View menuView=mInflater.inflate(R.layout.main_interface_popup_menu, null,false);
//		popupMenu=new PopupWindow(menuView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
//		
//		// ����������������ʧ
//		popupMenu.setOutsideTouchable(true);
//		// ʹ��ۼ� ��Ҫ������˵���ؼ����¼��ͱ���Ҫ���ô˷���
//		popupMenu.setFocusable(true);
//		
//		//�����ҪPopupWindow��Ӧ���ؼ�����ô�����PopupWindow����һ����������
//		ColorDrawable dw = new ColorDrawable(0X00000000);
//		popupMenu.setBackgroundDrawable(dw);	
//	}


    /**
     * �л�Fragemtn,�滻����ΪR.id.replacing_fragment
     * @param from	�л�ǰ��Fragment
     * @param to	�л����Fragment
     * @param sourceId �滻�Ĳ���id
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



//	@Override
//	public void onItemClick(int item) {
//		switch(item)
//		{
//		case 0:
//			switchFragment(mCurrentFragment, mMainInterfaceFragment, R.id.replacing_fragment);
//			mCurrentFragment=mMainInterfaceFragment;
//			menu.toggle();
//			break;
//		case 1:
//			switchFragment(mCurrentFragment, mFollowFragment, R.id.replacing_fragment);
//			mCurrentFragment=mFollowFragment;
//			menu.toggle();
//			break;
//		}
//	}



    //�ߵµ�ͼapi�ӿ�

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
            // ��λ�ɹ��ص���Ϣ�����������Ϣ

            this.myLocation.setLat(amapLocation.getLatitude());
            this.myLocation.setLng(amapLocation.getLongitude());
            this.myLocation.setLocationMessage(amapLocation.getAddress());
            Toast.makeText(this, amapLocation.getAddress(),Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "��λʧ�ܣ��������GPS������", Toast.LENGTH_LONG).show();
        }

    }

    //���ط��ؼ���Ϣ
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
//	 * ���µļ���������������fragment�ܹ�����touch�¼� 
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
