/** 
 * description��
 * @author wheat
 * date: 2014-12-25  
 * time: ����8:50:54
 */ 
package org.wheat.leaflets.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

/** 
 * description:
 * @author wheat
 * date: 2014-12-25  
 * time: ����8:50:54
 */
public class QuickReturnRelativeLayout extends RelativeLayout
{
	private QuickReturnRelativeLayout mQuickReturnRelativeLayout=this;
	
	/**
	 * ����ӵ�QuickReturnView
	 */
	private View mQuickReturnView=null;
	
	/**
	 * ��ǰʵ����QuickRetuanRelativeLayout�ĸ߶�
	 */
	private float mQuickReturnRelativeLayoutHeight;
	
	/**
	 * QuickReturnView�ĸ߶�
	 */
	private float mQuickReturnViewHeight;
	
	/**
	 * ǰһ��MotionEvent������x��y����
	 */
	private float mLastEventPositionY;
	private float mLastEventPositionX;
	
	/**
	 * ��ǰMotionEvent������x��y����
	 */
	private float mCurrentEventPositionY;
	private float mCurrentEventPositionX;
	
	
	/**
	 * QuickReturnViewӦ�ó��ֵ�λ�õ�Y��Ĵ�С
	 */
	private float mWantedPositionY;
	
	/**
	 * QuickReturnView��ȫ��ʾ����ĻQuickReturnLinearLayout֮��
	 */
	private static final int STATE_ONSCREEN = 0;
	
	/**
	 * QuickReturnView��ʾ��QuickReturnLinearLayout֮��
	 */
	private static final int STATE_OFFSCREEN = 1;
	
	/**
	 * QuickReturnView���ڴ��뿪QuickReturnLinearLayout��״̬���𽥻ص�QuickReturnLinearLayout
	 */
	private static final int STATE_RETURNING = 2;
	
	/**
	 * QuickReturnView��λ��״̬
	 */
	private int mState = STATE_ONSCREEN;

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public QuickReturnRelativeLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		this.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener());
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public QuickReturnRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener());
	}

	/**
	 * @param context
	 */
	public QuickReturnRelativeLayout(Context context) {
		super(context);
		this.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener());
	}
	

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if(ev.getAction()==MotionEvent.ACTION_DOWN)
		{
			mCurrentEventPositionX=ev.getX();
			mCurrentEventPositionY=ev.getY();
			onScrollGesture(0, 0);
		}
		else
		{
			mLastEventPositionX=mCurrentEventPositionX;
			mLastEventPositionY=mCurrentEventPositionY;
			mCurrentEventPositionX=ev.getX();
			mCurrentEventPositionY=ev.getY();
			onScrollGesture(mCurrentEventPositionX-mLastEventPositionX, mCurrentEventPositionY-mLastEventPositionY);
		}
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * ���QuickReturnView,����QuickReturnView����QuickReturnRelativeLayout�ĵײ�
	 * @param view
	 */
	public void addQuickReturnView(View view)
	{
		this.mQuickReturnView=view;
		view.measure(0, 0);
		mQuickReturnViewHeight=mQuickReturnView.getMeasuredHeight();
		LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		view.setLayoutParams(params);
		this.addView(view);
	}
	
	/**
	 * ����viewTree��Listener����ҪΪ�˻�ȡQuickReturnRelativeLayout����ʵ�߶�
	 * description:
	 * @author wheat
	 * date: 2014-12-29  
	 * time: ����12:41:34
	 */
	public class OnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener
	{
		@Override
		public void onGlobalLayout() {
			mQuickReturnRelativeLayoutHeight=mQuickReturnRelativeLayout.getHeight();
			if(mQuickReturnView!=null)
			{
				mWantedPositionY=mQuickReturnRelativeLayoutHeight-mQuickReturnViewHeight;
			}
			mQuickReturnRelativeLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
		}
		
	}
	
	
	/**
	 * QuickReturnView��Y��λ�÷����ı䣬��������QuickReturnView��λ��
	 */
	public void notifyQuickReturnViewTranslationYChange()
	{
		float mTranslationY=mWantedPositionY-mQuickReturnView.getTop();
		
		/** this can be used if the build is below honeycomb **/
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB) {
			TranslateAnimation anim = new TranslateAnimation(0, 0, mTranslationY,
					mTranslationY);
			anim.setFillAfter(true);
			anim.setDuration(0);
			mQuickReturnView.startAnimation(anim);
		} else {
			mQuickReturnView.setTranslationY(mTranslationY);
		}
	}
	
	public void onScrollGesture(float distanceX,float distanceY)
	{
		switch(mState)
		{
		case STATE_OFFSCREEN:
			if(distanceY>0)
			{
				mWantedPositionY=mQuickReturnRelativeLayoutHeight-distanceY;
				if(mWantedPositionY<(mQuickReturnRelativeLayoutHeight-mQuickReturnViewHeight))
				{
					mWantedPositionY=mQuickReturnRelativeLayoutHeight-mQuickReturnViewHeight;
					mState=STATE_ONSCREEN;
				}
				else
					mState=STATE_RETURNING;
			}
			else
				mWantedPositionY=mQuickReturnRelativeLayoutHeight;
			break;
		case STATE_RETURNING:
			if(distanceY>0)
			{
				mWantedPositionY=mWantedPositionY-distanceY;
				if(mWantedPositionY<(mQuickReturnRelativeLayoutHeight-mQuickReturnViewHeight))
				{
					mWantedPositionY=mQuickReturnRelativeLayoutHeight-mQuickReturnViewHeight;
					mState=STATE_ONSCREEN;
				}
			}
			else
			{
				mWantedPositionY=mWantedPositionY-distanceY;
				if(mWantedPositionY>mQuickReturnRelativeLayoutHeight)
				{
					mWantedPositionY=mQuickReturnRelativeLayoutHeight;
					mState=STATE_OFFSCREEN;
				}
			}
			break;
		case STATE_ONSCREEN:
			if(distanceY<0)
			{
				mWantedPositionY-=distanceY;
				if(mWantedPositionY>mQuickReturnRelativeLayoutHeight)
				{
					mWantedPositionY=mQuickReturnRelativeLayoutHeight;
					mState=STATE_OFFSCREEN;
				}
				else
				{
					mState=STATE_RETURNING;
				}
			}
			break;
			
		}
		
		if(mQuickReturnView!=null)
			notifyQuickReturnViewTranslationYChange();
	}
	
}
