/** 
 * description：
 * @author wheat
 * date: 2015-3-12  
 * time: 下午8:06:56
 */ 
package org.wheat.leaflets.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.wheat.leaflets.R;
import org.wheat.leaflets.adapter.DistanceListAdapter;
import org.wheat.leaflets.adapter.LeafletClassListAdapter;
import org.wheat.leaflets.adapter.SortingWayListAdapter;
import org.wheat.leaflets.basic.DateTools;
import org.wheat.leaflets.basic.DeivceInformation;
import org.wheat.leaflets.data.UserLoginPreference;
import org.wheat.leaflets.entity.ConstantValue;
import org.wheat.leaflets.entity.LeafletsFields;
import org.wheat.leaflets.entity.PhotoParameters;
import org.wheat.leaflets.entity.PraisePost;
import org.wheat.leaflets.entity.ReturnData;
import org.wheat.leaflets.entity.json.LeafletsJson;
import org.wheat.leaflets.entity.json.PraisePostJson;
import org.wheat.leaflets.loader.HttpLoaderMethods;
import org.wheat.leaflets.loader.HttpUploadMethods;
import org.wheat.leaflets.loader.ImageLoader;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

/** 
 * description:
 * @author wheat
 * date: 2015-3-12  
 * time: 下午8:06:56
 */
public class FragmentMainInterface extends Fragment implements OnScrollListener
{
	
	private String testUserName="abc@qq.com";
	private final int PAGE_LENGTH=10;//每次请求数据页里面包含的最多数据项
	
	private String[] strDistance,strLeafletClass,strSortingWay;
	private int[] intDistanceMap;
	private String[] strLeafletClassMap;
	private String[] strSortingWayMap;
	
	private TextView tvDistance,tvLeafletClass,tvSortingWay;
	private int distanceIndex,leafletClassIndex,sortingWayIndex;
	
	private PullToRefreshListView mPullToRefreshListView;
	private List<ReturnData<LeafletsFields>> mListData;//保存listview数据项的数组
	private ImageLoader mImageLoader;//加载图片的对象
	private LayoutInflater mInflater;
	private FragmentMainInterfaceListAdapter adapter;
	
	private boolean isLoadingMore=false;//防止重复开启异步加载线程
	private View mFooterView;
	private TextView tvFooterText;
	private ProgressBar pbFooterLoading;
	private ListView mActualListView;//PulltoRefreshListView中真正的ListView
	
	private DisplayMetrics metric;
	
	private PopupWindow pwLeafletClass,pwSortingWay,pwDistance;
	
	private ListView lvLeafletClass;
	private ListView lvSortingWay;
	private ListView lvDistance;
	
	private ImageView ivTitleAvatar;
	private TitleAvatarListener listener;
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//获取设备信息
		metric = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
		
		mListData=new ArrayList<ReturnData<LeafletsFields>>();
		mImageLoader=ImageLoader.getInstance(getActivity().getApplicationContext());
		adapter=new FragmentMainInterfaceListAdapter();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mInflater=inflater;
		
		taskPool=new HashMap<String, ImageView>();
		View view=inflater.inflate(R.layout.fragment_main_interface, container,false);
		
		tvDistance=(TextView)view.findViewById(R.id.btFragment_main_interface_distance);
		tvLeafletClass=(TextView)view.findViewById(R.id.btFragment_main_interface_leaflet_class);
		tvSortingWay=(TextView)view.findViewById(R.id.btFragment_main_interface_sorting_way);
		ivTitleAvatar=(ImageView)view.findViewById(R.id.ivfragment_Main_interface_title_avatar);
		
		initialIndex();
		initialPopupWindow();
		
		
		
		mPullToRefreshListView=(PullToRefreshListView)view.findViewById(R.id.fragment_main_interface_refresh_list_view);
		mActualListView=mPullToRefreshListView.getRefreshableView();
		
		mFooterView=mInflater.inflate(R.layout.refresh_list_footer, null);
		pbFooterLoading=(ProgressBar)mFooterView.findViewById(R.id.refresh_list_footer_progressbar);
		tvFooterText=(TextView)mFooterView.findViewById(R.id.refresh_list_footer_text);

		mPullToRefreshListView.setAdapter(adapter);
		mActualListView.addFooterView(mFooterView);
		
		initialListViewListener();
		
		ivTitleAvatar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(listener!=null)
				{
					listener.onTitleAvatarClicked();
				}
			}
		});
		
		tvFooterText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(!isLoadingMore)
				{
					isLoadingMore=true;
					pbFooterLoading.setVisibility(View.VISIBLE);
					tvFooterText.setText(R.string.list_footer_loading);
					loadMoreData();
				}
			}
		});
		
		return view;
	}
	
	
	
	@Override
	public void onResume() {
		super.onResume();
//		new UpdateDataTask(testUserName,"published").execute();
		refreshData();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==Activity.RESULT_OK&&requestCode==1)
		{
			int leafletId=data.getIntExtra("leaflet_id", -1);
			int newCommentCount=data.getIntExtra("new_comment_count", 0);
			
			ReturnData<LeafletsFields> leaflet=findLeafletsByID(mListData, leafletId);
			if(leaflet!=null)
			{
				if(leaflet.getDataFields().getCommentTimes()<newCommentCount)
				{
					leaflet.getDataFields().setCommentTimes(newCommentCount);
					adapter.notifyDataSetChanged();
				}
			}
			
		}
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch(scrollState)
		{
		case OnScrollListener.SCROLL_STATE_FLING:
			mImageLoader.lock();
			break;
		case OnScrollListener.SCROLL_STATE_IDLE:
			mImageLoader.unlock();
			break;
		case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
			mImageLoader.lock();
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
	}
	
	
	
	private class FragmentMainInterfaceListAdapter extends BaseAdapter
	{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mListData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ReturnData<LeafletsFields> listItem=mListData.get(position);
			ViewHolder holder=null;
			if(convertView==null)
			{
				holder=new ViewHolder();
				convertView=mInflater.inflate(R.layout.fragment_main_interface_list_item, null);
				holder.ivSellerAvatar=(ImageView)convertView.findViewById(R.id.fragment_main_interface_item_seller_avatar);
				holder.tvSellerName=(TextView)convertView.findViewById(R.id.fragment_main_interface_item_seller_name);
				holder.tvPublishTime=(TextView)convertView.findViewById(R.id.fragment_main_interface_item_publish_time);
				holder.tvLeafletType=(TextView)convertView.findViewById(R.id.fragment_main_interface_item_leaflet_type);
				holder.ivLeafletBrief=(ImageView)convertView.findViewById(R.id.fragment_main_interface_item_leaflet_brief);
				holder.tvPraiseTimes=(TextView)convertView.findViewById(R.id.fragment_main_interface_item_praise_times);
				holder.ivPraise=(ImageView)convertView.findViewById(R.id.fragment_main_interface_item_praise_button);
				holder.praiseView=convertView.findViewById(R.id.fragment_main_interface_item_praise_area);
				
				convertView.setTag(holder);
				
				holder.praiseView.setOnClickListener(new PraiseAreaOnClickListenter());
			}
			else
				holder=(ViewHolder)convertView.getTag();
			
			holder.praiseView.setTag(listItem);
			
			if(mPhotoWidth<=0)
			{
				holder.ivLeafletBrief.getViewTreeObserver().addOnGlobalLayoutListener(new GlobalLayoutLinstener(holder.ivLeafletBrief));
			}
			
			addTaskToPool(new PhotoParameters(listItem.getDataFields().getSellerLogoPath(), 50, 50*50,"seller_logo"), holder.ivSellerAvatar);
			holder.tvSellerName.setText(listItem.getDataFields().getSellerName());
			holder.tvPublishTime.setText(DateTools.getDifferenceFromDate(listItem.getDataFields().getPublishTime()));
			
			String result=findStringFromMap(strLeafletClass, strLeafletClassMap, listItem.getDataFields().getLeafletType());
			if(result!=null)
			{
				holder.tvLeafletType.setText(result);
			}
			
			addTaskToPool(new PhotoParameters(listItem.getDataFields().getBriefLeafletPath(), mPhotoWidth, 2*mPhotoWidth*mPhotoWidth, true,mPhotoWidth,"primary"), holder.ivLeafletBrief);
			holder.tvPraiseTimes.setText(String.valueOf(listItem.getDataFields().getPraiseTimes()));
			if(listItem.getDataFields().isPraise()==1)
				holder.ivPraise.setImageResource(R.drawable.praisefull);
			else
				holder.ivPraise.setImageResource(R.drawable.praise);
			
			return convertView;
		}
		
		private final class ViewHolder
		{
			public ImageView ivSellerAvatar;
			public TextView tvSellerName;
			public TextView tvPublishTime;
			public TextView tvLeafletType;
			public ImageView ivLeafletBrief;
			public TextView  tvPraiseTimes;
			public ImageView ivPraise;
			public View praiseView;
			
		}
		
	}
	
	private String findStringFromMap(String[] src,String[] target,String value)
	{
		int i;
		for(i=0;i<src.length;i++)
		{
			if(src[i].equals(value))
			{
				break;
			}
		}
		
		if(i<target.length)
		{
			return target[i];
		}
		
		return null;
	}
	
	
	private void initialListViewListener()
	{
		mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				
				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				
				
				Log.d("FragmentNeighbor", "UpdateDataTask will be executed");
				refreshData();
			}
		});
		
		mPullToRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
//				Toast.makeText(getActivity(), "End of List!", Toast.LENGTH_SHORT).show();
				if(!isLoadingMore)
				{
					isLoadingMore=true;
					pbFooterLoading.setVisibility(View.VISIBLE);
					tvFooterText.setText(R.string.list_footer_loading);
					loadMoreData();
				}
			}
		});
		
		mPullToRefreshListView.setOnScrollListener(this);
		
		mPullToRefreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				if(position>mListData.size())
					return;
				ReturnData<LeafletsFields> data=mListData.get(position-1);
				Intent intent=new Intent(getActivity(),LeafletDetailActivity.class);
				Bundle bundle=new Bundle();
				bundle.putInt("leaflet_id", data.getPrimaryKey());
				bundle.putString("publish_time", data.getDataFields().getUTCPublishTime());
				bundle.putString("seller_name", data.getDataFields().getSellerName());
				bundle.putString("start_time", data.getDataFields().getUTCStartTime());
				bundle.putString("leaflet_path", data.getDataFields().getLeafletPath());
				bundle.putString("brief_leaflet_path", data.getDataFields().getBriefLeafletPath());
				bundle.putString("end_time", data.getDataFields().getUTCEndTime());
				bundle.putInt("praise_times", data.getDataFields().getPraiseTimes());
				bundle.putString("leaflet_type", data.getDataFields().getLeafletType());
				bundle.putInt("comment_times", data.getDataFields().getCommentTimes());
				bundle.putString("seller_logo_path", data.getDataFields().getSellerLogoPath());
				bundle.putDouble("lat", data.getDataFields().getLat());
				bundle.putDouble("lng", data.getDataFields().getLng());
				bundle.putDouble("distance", data.getDataFields().getDistance());
				bundle.putInt("is_praise", data.getDataFields().isPraise());
				bundle.putString("seller_address", data.getDataFields().getSellerAddress());
				bundle.putString("leaflet_description", data.getDataFields().getLeafletDescription());
				bundle.putInt("is_favourite", data.getDataFields().getIsFavourite());
				bundle.putString("seller_email", data.getDataFields().getSellerUserName());
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}
	
	private void initialIndex()
	{
		distanceIndex=0;
		leafletClassIndex=0;
		sortingWayIndex=0;
		
		Resources res=getResources();
		//显示的内容
		strLeafletClass=res.getStringArray(R.array.leaflet_class_string_array);
		strDistance=res.getStringArray(R.array.distance_string_array);
		strSortingWay=res.getStringArray(R.array.sorting_way_string_array);
		
		//映射的网络请求参数
		intDistanceMap=res.getIntArray(R.array.distance_integer_array);
		strLeafletClassMap=res.getStringArray(R.array.string_array_leaflet_class);
		strSortingWayMap=res.getStringArray(R.array.string_array_sorting_way);
		
		tvDistance.setText(strDistance[distanceIndex]);
		tvLeafletClass.setText(strLeafletClass[leafletClassIndex]);
		tvSortingWay.setText(strSortingWay[sortingWayIndex]);
			
	}
	
	private void initialPopupWindow()
	{
		View leafletClassView=mInflater.inflate(R.layout.fragment_main_interface_leaflet_class_pw, null,false);
		View sortingWayView=mInflater.inflate(R.layout.fragment_main_interface_sorting_way_pw, null, false);
		View distanceView=mInflater.inflate(R.layout.fragment_main_interface_distance_pw, null, false);
		
		pwLeafletClass=new PopupWindow(leafletClassView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		pwSortingWay=new PopupWindow(sortingWayView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		pwDistance=new PopupWindow(distanceView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		
		pwLeafletClass.setAnimationStyle(R.style.popwin_anim_style);
		pwSortingWay.setAnimationStyle(R.style.popwin_anim_style);
		pwDistance.setAnimationStyle(R.style.popwin_anim_style);
		
		leafletClassView.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(pwLeafletClass!=null&&pwLeafletClass.isShowing())
				{
					pwLeafletClass.dismiss();
				}
				return false;
			}
		});
		
		sortingWayView.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(pwSortingWay!=null&&pwSortingWay.isShowing())
				{
					pwSortingWay.dismiss();
				}
				return false;
			}
		});
		
		distanceView.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(pwDistance!=null&&pwDistance.isShowing())
				{
					pwDistance.dismiss();
				}
				return false;
			}
		});
		
		// 设置允许在外点击消失
		pwLeafletClass.setOutsideTouchable(true);
		pwSortingWay.setOutsideTouchable(true);
		pwDistance.setOutsideTouchable(true);
		
		// 使其聚集 ，要想监听菜单里控件的事件就必须要调用此方法
		pwLeafletClass.setFocusable(true);
		pwSortingWay.setFocusable(true);
		pwDistance.setFocusable(true);
		
		//如果需要PopupWindow响应返回键，那么必须给PopupWindow设置一个背景才行
		ColorDrawable dw = new ColorDrawable(0X50000000);
		pwLeafletClass.setBackgroundDrawable(dw);
		pwSortingWay.setBackgroundDrawable(dw);
		pwDistance.setBackgroundDrawable(dw);
		
		lvLeafletClass=(ListView)leafletClassView.findViewById(R.id.leaflet_class_pw_listview);
		lvDistance=(ListView)distanceView.findViewById(R.id.distance_pw_listview);
		lvSortingWay=(ListView)sortingWayView.findViewById(R.id.sorting_way_pw_listview);
		
//		Resources res=getResources();
//		strLeafletClass=res.getStringArray(R.array.leaflet_class_string_array);
//		strDistance=res.getStringArray(R.array.distance_string_array);
//		strSortingWay=res.getStringArray(R.array.sorting_way_string_array);
		
		lvLeafletClass.setAdapter(new LeafletClassListAdapter(strLeafletClass,mInflater));
		lvSortingWay.setAdapter(new SortingWayListAdapter(strSortingWay, mInflater));
		lvDistance.setAdapter(new DistanceListAdapter(strDistance,mInflater));
		
		lvLeafletClass.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parrent, View view, int position,
					long id) {
//				TextView tv=(TextView)view.findViewById(R.id.leaflet_class_list_item_text);
//				tvLeafletClass.setText(tv.getText().toString());
				if(leafletClassIndex!=position)
				{
					tvLeafletClass.setText(strLeafletClass[position]);
					leafletClassIndex=position;
					((LeafletClassListAdapter)lvLeafletClass.getAdapter()).setSelectedItemIndex(position);
					pwLeafletClass.dismiss();
					refreshData();
				}
			}
		});
		
		lvSortingWay.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parrent, View view, int position,
					long id) {
//				TextView tv=(TextView)view.findViewById(R.id.sorting_way_list_item_text);
//				tvSortingWay.setText(tv.getText().toString());
				if(sortingWayIndex!=position)
				{
					tvSortingWay.setText(strSortingWay[position]);
					sortingWayIndex=position;
					((SortingWayListAdapter)lvSortingWay.getAdapter()).setmSelectedItemIndex(position);
					pwSortingWay.dismiss();
					refreshData();
				}
			}
		});
		
		lvDistance.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parrent, View view, int position,
					long id) {
//				TextView tv=(TextView)view.findViewById(R.id.distance_list_item_text);
//				tvDistance.setText(tv.getText().toString());
				
				if(distanceIndex!=position)
				{
					tvDistance.setText(strDistance[position]);
					distanceIndex=position;
					((DistanceListAdapter)lvDistance.getAdapter()).setmSelectedItemIndex(position);
					pwDistance.dismiss();
					refreshData();
				}
			}
		});
		
		
		
		tvLeafletClass.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(pwLeafletClass.isShowing())
				{
					pwLeafletClass.dismiss();
				}else
				{
					pwLeafletClass.showAsDropDown(v);
				}
			}
		});
		
		tvSortingWay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(pwSortingWay.isShowing())
				{
					pwSortingWay.dismiss();
				}else
				{
					pwSortingWay.showAsDropDown(v);
				}
			}
		});
		
		tvDistance.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(pwDistance.isShowing())
				{
					pwDistance.dismiss();
				}else
				{
					pwDistance.showAsDropDown(v);
				}
			}
		});
	}
	
	public interface TitleAvatarListener
	{
		public void onTitleAvatarClicked();
	}
	
	public void setTitleAvatarListener(TitleAvatarListener listener)
	{
		this.listener=listener;
	}
	
	
	private void refreshData()
	{
		UserLoginPreference preference=UserLoginPreference.getInstance(getActivity());
		String userName="";
		if(preference.getLoginState()==UserLoginPreference.NO_USER_LOGIN)
		{
			userName=DeivceInformation.getAndroidId(getActivity());
		}
		else if(preference.getLoginState()==UserLoginPreference.SELLER_LOGIN)
		{
			userName=preference.getSellerPreference().getSellerEmail();
		}
		else if(preference.getLoginState()==UserLoginPreference.USER_LOGIN)
		{
			userName=preference.getUserPreference().getUserEmail();
		}
		new UpdateCoordinateTask(userName,preference.getLocationLat(),preference.getLocationLng()).execute();

	}

	private void loadMoreData()
	{
		UserLoginPreference preference=UserLoginPreference.getInstance(getActivity());
		String userName="";
		if(preference.getLoginState()==UserLoginPreference.NO_USER_LOGIN)
		{
			userName=DeivceInformation.getAndroidId(getActivity());
		}
		else if(preference.getLoginState()==UserLoginPreference.SELLER_LOGIN)
		{
			userName=preference.getSellerPreference().getSellerEmail();
		}
		else if(preference.getLoginState()==UserLoginPreference.USER_LOGIN)
		{
			userName=preference.getUserPreference().getUserEmail();
		}
		new LoadMoreTask(mListData.size()+1,mListData.size()+PAGE_LENGTH,userName).execute();
	}

	private class UpdateCoordinateTask extends AsyncTask<Void,Void,Integer>
	{
		private String userName;
		private double lat;
		private double lng;

		public UpdateCoordinateTask(String userName,double lat, double lng)
		{
			this.userName=userName;
			this.lat=lat;
			this.lng=lng;
		}

		@Override
		protected Integer doInBackground(Void... params) {
			return HttpUploadMethods.updateUserCoordinate(userName,lat,lng);
		}

		@Override
		protected void onPostExecute(Integer integer) {
			super.onPostExecute(integer);
			if(integer==200)
			{
				UserLoginPreference preference=UserLoginPreference.getInstance(getActivity());
				if(preference.getLoginState()==UserLoginPreference.NO_USER_LOGIN)
				{
					new UpdateDataTask(DeivceInformation.getAndroidId(getActivity()),strSortingWayMap[sortingWayIndex],intDistanceMap[distanceIndex],strLeafletClassMap[leafletClassIndex]).execute();
				}
				else if(preference.getLoginState()==UserLoginPreference.SELLER_LOGIN)
				{
					new UpdateDataTask(preference.getSellerPreference().getSellerEmail(),strSortingWayMap[sortingWayIndex],intDistanceMap[distanceIndex],strLeafletClassMap[leafletClassIndex]).execute();
				}
				else if(preference.getLoginState()==UserLoginPreference.USER_LOGIN)
				{
					new UpdateDataTask(preference.getUserPreference().getUserEmail(),strSortingWayMap[sortingWayIndex],intDistanceMap[distanceIndex],strLeafletClassMap[leafletClassIndex]).execute();
				}
			}
			else
			{
				Log.d("FragmentMainInterface","更新坐标失败");
			}
		}
	}
	
	/**
	 * 
	 * description:刷新ListView内容的异步线程
	 * @author wheat
	 * date: 2015-3-7  
	 * time: 下午7:12:59
	 */
	private class UpdateDataTask extends AsyncTask<Void, Void, LeafletsJson>
	{
		private String userName;
		private String sortingType;
		private int distance;
		private String leafletClass;
		
		public UpdateDataTask(String userName,String sortingType,int distance,String leafletClass)
		{
			this.userName=userName;
			this.sortingType=sortingType;
			this.distance=distance;
			this.leafletClass=leafletClass;
		}

		@Override
		protected LeafletsJson doInBackground(Void... params) {
			LeafletsJson json=null;
			try
			{
				json=HttpLoaderMethods.flushLeafletData(userName,distance,sortingType,leafletClass);
			}catch(Throwable e)
			{
				e.printStackTrace();
			}
			return json;
		}

		@Override
		protected void onPostExecute(LeafletsJson result) {
			if(result!=null&&result.getCode()==1000)
			{
				if(result.getData().size()>0)
				{
					synchronized (mListData) {
						mListData.clear();
						mListData=result.getData();
						adapter.notifyDataSetChanged();
					}
				}
			}
			
			mPullToRefreshListView.onRefreshComplete();
			
			if(result==null)
				onLoadComplete(true);
			else
				onLoadComplete(true);
			
			super.onPostExecute(result);
		}	
		
	}
	
	private class LoadMoreTask extends AsyncTask<Void, Void, LeafletsJson>
	{
		private int offsetStart;
		private int offsetEnd;
		private String userName;
		
		public LoadMoreTask(int offsetStart,int offsetEnd,String userName)
		{
			super();
			this.offsetStart=offsetStart;
			this.offsetEnd=offsetEnd;
			this.userName=userName;
		}
		
		@Override
		protected LeafletsJson doInBackground(Void... params) {
			LeafletsJson json=null;
			try
			{
				json=HttpLoaderMethods.getLeafletData(offsetStart, offsetEnd, userName);
			}catch(Throwable e)
			{
				e.printStackTrace();
			}
			return json;
		}

		@Override
		protected void onPostExecute(LeafletsJson result) {
			if(result!=null&&result.getCode()==1000)
			{
				if(result.getData()!=null&&result.getData().size()>0)
				{
					synchronized (mListData) {
						mListData.addAll(result.getData());
						adapter.notifyDataSetChanged();
					}
					onLoadComplete(false);
				}
				else
				{
					onLoadComplete(true);
				}
			}
			else if(result!=null&&result.getCode()==ConstantValue.NO_MORE_DATA)
			{
				Toast.makeText(getActivity(), "没有更多内容", Toast.LENGTH_SHORT).show();
				onLoadComplete(true);
			}
			else
				onLoadComplete(true);
			super.onPostExecute(result);
		}
		
	}
	
	
	/**
	 * 
	 * @param wasLoadNothing 加载完成后，是否内容没有增加,true表示内容没有增加,false表示内容增加了
	 */
	private void onLoadComplete(boolean wasLoadNothing)
	{
		isLoadingMore=false;
		if(wasLoadNothing)
		{
			pbFooterLoading.setVisibility(View.GONE);
			tvFooterText.setText(R.string.list_footer_no_more);
		}
	}
	
	
	
	
	
	private ReturnData<LeafletsFields> findLeafletsByID(List<ReturnData<LeafletsFields>> list,int leafletId)
	{
		ReturnData<LeafletsFields> leaflets=null;
		for(Iterator<ReturnData<LeafletsFields>> i=list.iterator();i.hasNext();)
		{
			leaflets=i.next();
			if(leaflets.getPrimaryKey()==leafletId)
				break;
		}
		return leaflets;
	}
	
	
//	private class CommentAreaOnClickListener implements OnClickListener
//	{
//		private ReturnData<LeafletsFields> listItem;
//		@Override
//		public void onClick(View v) {
//			listItem=(ReturnData<LeafletsFields>)v.getTag();
//			
//			Intent intent=new Intent(getActivity(),CommentContentActivity.class);
//			Bundle bundle=new Bundle();
//			bundle.putString("user_name", userName);
//			bundle.putInt("leaflet_id", listItem.getPrimaryKey());
//			bundle.putInt("comment_count", listItem.getDataFields().getCommentTimes());
//			intent.putExtras(bundle);
//			startActivityForResult(intent, 1);
//		}
//		
//	}
	
	private class PraiseAreaOnClickListenter implements OnClickListener
	{
		private ReturnData<LeafletsFields> leaflet;

		@Override
		public void onClick(View v) {
			leaflet=(ReturnData<LeafletsFields>)v.getTag();
			if(leaflet.getDataFields().isPraise()==0)
			{
				leaflet.getDataFields().setPraise(1);
				leaflet.getDataFields().setPraiseTimes(leaflet.getDataFields().getPraiseTimes()+1);
				adapter.notifyDataSetChanged();
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						PraisePost praisePost=new PraisePost();
						praisePost.setLeafletId(leaflet.getPrimaryKey());
						praisePost.setUserName(testUserName);
						
						PraisePostJson json=new PraisePostJson();
						json.setData(praisePost);
						try{
							HttpUploadMethods.postPraisePost(json);
						}catch(Exception e)
						{
							e.printStackTrace();
						}
					}
				}).start();
			}
			else
			{
				leaflet.getDataFields().setPraise(0);
				leaflet.getDataFields().setPraiseTimes(leaflet.getDataFields().getPraiseTimes()-1);
				adapter.notifyDataSetChanged();
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						try{
							int returnCode=HttpUploadMethods.removePraiseRecord(leaflet.getPrimaryKey(), testUserName);
							System.out.println("return code="+returnCode);
						}catch(Exception e)
						{
							e.printStackTrace();
						}
					}
				}).start();
			}
		}
	}
	
	
	private int mPhotoWidth=0;
	private int mMinSideLength=0;
	private int mMaxNumOfPixles=0;
	//已经获取到正确的ImageWidth
	private boolean allowFix=false;
	private Map<String,ImageView> taskPool;
	

	public class GlobalLayoutLinstener implements OnGlobalLayoutListener
	{
		private View view;
		public GlobalLayoutLinstener(View view)
		{
			this.view=view;
		}

		@Override
		public void onGlobalLayout() {
			if(mPhotoWidth<=0&&view.getWidth()>0)
			{
				mPhotoWidth=view.getWidth();
				mMinSideLength=(int)(mPhotoWidth*metric.density);
				mMaxNumOfPixles=2*mMinSideLength*mMinSideLength;
				unLockTaskPool();
				view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
			}
			
		}
		
	}
	
	/**
	 * 锁住时，不能加载自适配图片，只能加载固定图片
	 */
	public void lockTaskPool()
	{
		this.allowFix=false;
	}
	
	/**
	 * 解除锁定后，可以加载自适配图片
	 */
	public void unLockTaskPool()
	{
		if(!allowFix)
		{
			this.allowFix=true;
		}
		doTaskInPool();
	}
	
	public void addTaskToPool(PhotoParameters parameters,ImageView img)
	{
		if(!parameters.isFixWidth())
		{
			mImageLoader.addTask(parameters, img);
		}
		else
		{
			synchronized (taskPool) {
				img.setTag(parameters);
				taskPool.put(Integer.toString(img.hashCode()), img);
			}
			if(allowFix)
			{
				doTaskInPool();
			}	
		}
	}
	
	public void doTaskInPool()
	{
		synchronized (taskPool) {
			Collection<ImageView> con=taskPool.values();
			for(ImageView img:con)
			{
				if(img!=null)
				{
					if(img.getTag()!=null)
					{
						PhotoParameters pp=(PhotoParameters)img.getTag();
						pp.setMinSideLength(mMinSideLength);
						pp.setMaxNumOfPixles(mMaxNumOfPixles);
						pp.setImageViewWidth(mPhotoWidth);
						mImageLoader.addTask(pp, img);
					}
				}
			}
			taskPool.clear();
		}
	}

	
	
}
