/** 
 * description：
 * @author wheat
 * date: 2015-4-4  
 * time: 下午3:13:58
 */ 
package org.wheat.leaflets.activity;

import org.wheat.leaflets.R;
import org.wheat.leaflets.basic.ExitApplication;
import org.wheat.leaflets.data.UserLoginPreference;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;

/** 
 * description:
 * @author wheat
 * date: 2015-4-4  
 * time: 下午3:13:58
 */
public class StartActivity extends Activity implements AMapLocationListener
{

	private UserLoginPreference preference;
	//定位代理
	private LocationManagerProxy mLocationManagerProxy;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_layout);

		preference=UserLoginPreference.getInstance(getApplicationContext());

		// 开启界面的时候就定位
		mLocationManagerProxy = LocationManagerProxy.getInstance(this);
		mLocationManagerProxy.setGpsEnable(false);
		mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, -1, 150, this);
		
		new Thread() {
			public void run() {
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
//				handler.sendEmptyMessage(0);
			}

		}.start();
		ExitApplication.getInstance().addActivity(StartActivity.this);
	}
	
//	Handler handler = new Handler() {
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			if (UserLoginPreference.getInstance(getApplicationContext()).isFirstRun()) {
//				Intent intent = new Intent(StartActivity.this,
//						GuideActivity.class);
//				startActivity(intent);
//			} else {
//				Intent intent = new Intent(StartActivity.this,
//						MainInterfaceActivity.class);
//				startActivity(intent);
//			}
//			finish();
//		};
//	};
	
	//拦截返回键消息
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {

		}
		return true;
	}

	@Override
	public void onLocationChanged(AMapLocation aMapLocation) {
		if (aMapLocation!=null&&aMapLocation.getAMapException().getErrorCode() == 0) {
			preference.setLocationLat(aMapLocation.getLatitude());
			preference.setLocationLng(aMapLocation.getLongitude());
		}else{
			if(preference.isFirstRun())
			{
				preference.setLocationLat(39.917228);
				preference.setLocationLng(116.397224);
			}
		}
		if (preference.isFirstRun()) {
			Intent intent = new Intent(StartActivity.this,
					GuideActivity.class);
			startActivity(intent);
		} else {
			Intent intent = new Intent(StartActivity.this,
					MainInterfaceActivity.class);
			startActivity(intent);
		}
		finish();
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
}
