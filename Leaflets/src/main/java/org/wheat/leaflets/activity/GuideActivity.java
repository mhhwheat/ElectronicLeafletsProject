/** 
 * description��
 * @author wheat
 * date: 2015-4-4  
 * time: ����3:29:52
 */ 
package org.wheat.leaflets.activity;

import java.util.ArrayList;
import java.util.List;

import org.wheat.leaflets.R;
import org.wheat.leaflets.adapter.GuideViewPagerAdapter;
import org.wheat.leaflets.basic.ExitApplication;
import org.wheat.leaflets.fragment.FragmentGuideOne;
import org.wheat.leaflets.fragment.FragmentGuideThree;
import org.wheat.leaflets.fragment.FragmentGuideTwo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;

/** 
 * description:
 * @author wheat
 * date: 2015-4-4  
 * time: ����3:29:52
 */
public class GuideActivity extends FragmentActivity
{
	private ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide_layout);
		initialViewPage();
		ExitApplication.getInstance().addActivity(this);
	}
	
	private void initialViewPage()
	{
		mViewPager=(ViewPager)findViewById(R.id.activity_guide_viewpager);
		List<Fragment> list=new ArrayList<Fragment>();
		list.add(new FragmentGuideOne());
		list.add(new FragmentGuideTwo());
		list.add(new FragmentGuideThree());
		
		mViewPager.setAdapter(new GuideViewPagerAdapter(getSupportFragmentManager(), list));
		mViewPager.setCurrentItem(0);
	}
	
	//���ط��ؼ���Ϣ
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

		}
		return true;
	}
	
}
