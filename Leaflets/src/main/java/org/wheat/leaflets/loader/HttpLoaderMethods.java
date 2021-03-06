/** 
 * description：
 * @author wheat
 * date: 2015-3-7  
 * time: 上午11:33:36
 */ 
package org.wheat.leaflets.loader;

import org.wheat.leaflets.entity.ConstantValue;
import org.wheat.leaflets.entity.json.BrowseHistoryJson;
import org.wheat.leaflets.entity.json.CommentGetJson;
import org.wheat.leaflets.entity.json.LeafletsJson;
import org.wheat.leaflets.entity.json.SellerMsgJson;
import org.wheat.leaflets.entity.json.UserMsgJson;
import org.wheat.leaflets.httptools.BitmapTools;
import org.wheat.leaflets.httptools.HttpConnectTools;
import org.wheat.leaflets.httptools.JsonTools;

import android.graphics.Bitmap;
import android.util.Log;

/** 
 * description:
 * @author wheat
 * date: 2015-3-7  
 * time: 上午11:33:36
 */
public class HttpLoaderMethods 
{
	/**
	 *
	 * @param photoPath 图片在服务端的路径
	 * @return
	 * @throws Throwable
	 */
	public static Bitmap downLoadBitmap(String photoPath,int minSideLength,int maxNumOfPixels,String photoType) throws Throwable
	{
		/*
		HashMap<String,String> hm=new HashMap<String,String>();
		hm.put("path", bitmapPath);
		byte[] data=encryptParamsToPost(hm);
		*/
		//Bitmap bm=HttpLoader.getPhoto( HttpRoot+"/servlet/DownLoadPhotoServlet",data, null);
		Bitmap bm=BitmapTools.getPhoto(ConstantValue.HttpRoot+"get_image?photo_type="+photoType+"&photo_name="+photoPath, null,minSideLength,maxNumOfPixels);
		
		Log.d("HttpLoaderMethods", ConstantValue.HttpRoot+"get_image?photo_type="+photoType+"&photo_name="+photoPath);
		if(bm!=null)
		{
			Log.d("HttpLoaderMethods", "getPhoto Success!");
		}
		else
		{
			Log.d("HttpLoaderMethods", "getPhoto fail!");
		}
		return bm;
	}

	/**
	 * 刷新Leaflet数据的api
	 * @param username 用户的邮箱
	 * @param distance 按距离进行筛选
	 * @param sortingWay 按不同的条件进行排序
	 * @param leafletType 按leaflet的类型进项筛选
	 * @param userType 用户的类型（user，seller，anonymous）
	 * @return
	 * @throws Throwable
	 */
	public static LeafletsJson flushLeafletData(String username,int distance,String sortingWay,String leafletType,String userType) throws Throwable
	{
		String json=HttpConnectTools.get(ConstantValue.HttpRoot + "flush_leaflet_data?username=" + username + "&rule_distance=" + distance + "&rule_time=" + sortingWay + "&rule_type=" + leafletType+"&user_type="+userType, null);
		if(json==null)
			return null;
//		Log.d("HttpLoaderMethod", "LeafletJson :"+json);
		return JsonTools.fromJson(new String(json.getBytes("8859_1"),"UTF-8"), LeafletsJson.class);
	}

	public static LeafletsJson getLeafletData(int offset_begin,int offset_end,String username) throws Throwable
	{
		String json=HttpConnectTools.get(ConstantValue.HttpRoot+"get_leaflet_data?username="+username+"&offset_begin="+offset_begin+"&offset_end="+offset_end, null);
		if(json==null)
			return null;
		return JsonTools.fromJson(new String(json.getBytes("8859_1"),"UTF-8"), LeafletsJson.class);
	}
	
//	/**
//	 * 为NeighborFragment加载更多数据时使用的Api，请求数据表中第offset_begin到第offset_end条数据
//	 * @param offset_begin
//	 * @param offset_end
//	 * @param username
//	 * @param order_rule   (oder_rule的三个值"published":按发布时间排序,"start":按即将开始的时间排序,"end":按即将结束时间排序)
//	 * @return
//	 * @throws Throwable
//	 */
//	public static LeafletsJson getNeighborPage(int offset_begin,int offset_end,String username,String order_rule) throws Throwable
//	{
//		String json=HttpConnectTools.get(ConstantValue.HttpRoot+"get_leaflet_data?username="+username+"&offset_begin="+offset_begin+"&offset_end="+offset_end+"&order_rule="+order_rule, null);
//		if(json==null)
//			return null;
//
//		return JsonTools.fromJson(new String(json.getBytes("8859_1"),"UTF-8"), LeafletsJson.class);
//	}
	
	public static CommentGetJson getCommentContent(String userName,int leafletId) throws Throwable
	{
//		Log.d("HttpLoaderMethods", "flush comment");
		String json=HttpConnectTools.get(ConstantValue.HttpRoot+"flush_comment?username="+userName+"&leaflet_id="+leafletId, null);
//		Log.d("HttpLoaderMethods", json);
		if(json==null)
			return null;
		return JsonTools.fromJson(new String(json.getBytes("8859_1"),"UTF-8"), CommentGetJson.class);
	}
	
	public static CommentGetJson getCommentContent(String userName,int offsetStart,int offsetEnd) throws Throwable
	{
		String json=HttpConnectTools.get(ConstantValue.HttpRoot+"?username="+userName+"&offset_begin="+offsetStart+"&offsetEnd="+offsetEnd, null);
		if(json==null)
			return null;
		return JsonTools.fromJson(new String(json.getBytes("8859_1"),"UTF-8"), CommentGetJson.class);
	}
	
	public static BrowseHistoryJson getBrowseHistory(String userName) throws Throwable
	{
		String json=HttpConnectTools.get(ConstantValue.HttpRoot+"get_history?username="+userName, null);
		if(json==null)
			return null;
		return JsonTools.fromJson(new String(json.getBytes("8859_1"),"UTF-8"), BrowseHistoryJson.class);
	}
	
	public static UserMsgJson getUserData(String userName) throws Throwable
	{
		String json=HttpConnectTools.get(ConstantValue.HttpRoot+"get_user_data?username="+userName, null);
		if(json==null)
			return null;
		Log.d("HttpLoaderMethod","UserMessageJson="+json);
		return JsonTools.fromJson(new String(json.getBytes("8859_1"),"UTF-8"), UserMsgJson.class);
	}
	
	public static SellerMsgJson getSellerData(String sellerName) throws Throwable
	{
		String json=HttpConnectTools.get(ConstantValue.HttpRoot+"get_seller_data?userName="+sellerName, null);
		if(json==null)
			return null;
		return JsonTools.fromJson(new String(json.getBytes("8859_1"),"UTF-8"), SellerMsgJson.class);
	}
	
	public static LeafletsJson getMyCollection(String userName,int offsetBegin,int offsetEnd) throws Throwable
	{
		String json=HttpConnectTools.get(ConstantValue.HttpRoot+"get_favourite?"+"username="+userName+"&offset_begin="+offsetBegin+"&offset_end="+offsetEnd, null);
		
		if(json==null)
			return null;
		Log.d("HttpLoaderMethod", json);
		return JsonTools.fromJson(new String(json.getBytes("8859_1"),"UTF-8"), LeafletsJson.class);
	}
	
	public static LeafletsJson getSellerLeaflets(String userName) throws Throwable
	{

		String json=HttpConnectTools.get(ConstantValue.HttpRoot+"get_seller_leaflet?"+"username="+userName, null);
		if(json==null)
			return null;
		return JsonTools.fromJson(new String(json.getBytes("8859_1"),"UTF-8"), LeafletsJson.class);
	}
	
	public static LeafletsJson flushMyCollection(String userName) throws Throwable
	{
		String json=HttpConnectTools.get(ConstantValue.HttpRoot+"flush_favourite?"+"username="+userName, null);
		if(json==null)
			return null;
		Log.d("HttpLoaderMethod", json);
		return JsonTools.fromJson(new String(json.getBytes("8859_1"),"UTF-8"), LeafletsJson.class);
	}
	
}
