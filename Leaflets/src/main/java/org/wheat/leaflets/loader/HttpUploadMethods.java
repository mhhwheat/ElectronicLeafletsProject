/** 
 * description��
 * @author wheat
 * date: 2015-3-18  
 * time: ����6:36:31
 */ 
package org.wheat.leaflets.loader;

import java.io.File;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.wheat.leaflets.entity.ConstantValue;
import org.wheat.leaflets.entity.CoordinatePostFields;
import org.wheat.leaflets.entity.json.CommentPostJson;
import org.wheat.leaflets.entity.json.CoordinatePostJson;
import org.wheat.leaflets.entity.json.PraisePostJson;
import org.wheat.leaflets.entity.json.UserMsgJson;
import org.wheat.leaflets.entity.json.UserUpdateMsgJson;
import org.wheat.leaflets.httptools.HttpConnectTools;
import org.wheat.leaflets.httptools.JsonTools;

/** 
 * description:
 * @author wheat
 * date: 2015-3-18  
 * time: ����6:36:31
 */
public class HttpUploadMethods 
{
	public static CommentPostJson postCommentPost(CommentPostJson comment) throws Exception
	{
		String json= HttpConnectTools.postJsonReturnJsonString(ConstantValue.HttpRoot+"set_comment", null, comment);
		return JsonTools.fromJson(new String(json.getBytes("8859_1"),"UTF-8"), CommentPostJson.class);
	}
	
	public static PraisePostJson postPraisePost(PraisePostJson praise) throws Exception
	{
		String json=HttpConnectTools.postJsonReturnJsonString(ConstantValue.HttpRoot+"set_praise", null, praise);
		if(json!=null)
			return JsonTools.fromJson(new String(json.getBytes("8859_1"),"UTF-8"), PraisePostJson.class);
		return null;
	}
	
	public static int removePraiseRecord(int leaflet_id,String userName) throws Exception
	{
		int returnCode=HttpConnectTools.getReturnCode(ConstantValue.HttpRoot + "remove_praise" + "?leaflet_id=" + leaflet_id + "&username=" + userName, null, null);
		return returnCode;
	}
	

	/**
	 * 
	 * @param photo   �ϴ�����������ͼƬ�ļ�
	 * @param photoName  ���ɵ�ͼƬid�����������ڷ�������
	 * @param photoType �ϴ���ͼƬ������(primary��secondary��seller_logo��portrait)
	 * @return  int �Զ���ɹ�����״̬��
	 * @throws Exception
	 */
	
	public static int uploadPhoto(File photo,String photoName,String photoType) throws Exception {
		System.out.println("in post photo method");
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

		HttpPost httppost = new HttpPost(ConstantValue.HttpRoot+"upload_image" + "?photo_type=" + photoType+"&photo_name="+photoName);

		FileEntity reqEntity = new FileEntity(photo, "binary/octet-stream");

		httppost.setEntity(reqEntity);
		reqEntity.setContentType("binary/octet-stream");
		System.out.println("executing request " + httppost.getRequestLine());
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity resEntity = response.getEntity();

		System.out.println(response.getStatusLine());
		//���ﲻ���Զ����statusCode����Ϊ�ϴ�ʧ�ܵ�ԭ��ֻܶ࣬�е�ϵͳ����������Ϣʱ����Ϊ�ϴ��ɹ�
		if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK)
			return ConstantValue.operateSuccess;//�ϴ��ɹ�
		else 
			return ConstantValue.uploadPhotoFailed;//�ϴ�ʧ��
	}
	
	public static int removeFavourite(String userName,int leaflet_id) throws Exception
	{
		int returnCode=HttpConnectTools.getReturnCode(ConstantValue.HttpRoot+"remove_favourite?"+"username="+userName+"&leaflet_id="+leaflet_id, null, null);
		return returnCode;
	}

	public static int setFavourite(String userName,int leaflet_id) throws Exception
	{
		int returnCode=HttpConnectTools.getReturnCode(ConstantValue.HttpRoot+"set_favourite?"+"username="+userName+"&leaflet_id="+leaflet_id, null, null);
		return returnCode;
	}

	public static int setUserData(UserUpdateMsgJson userMsg)
	{
		int returnCode=HttpConnectTools.postJsonReturnCode(ConstantValue.HttpRoot + "set_user_data", userMsg, null);
		return returnCode;
	}

	public static  int updateUserCoordinate(String userName,double lat,double lng)
	{
		CoordinatePostFields fields=new CoordinatePostFields();
		fields.setUserName(userName);
		fields.setLng(lng);
		fields.setLat(lat);
		CoordinatePostJson coordinatePost=new CoordinatePostJson();
		coordinatePost.setData(fields);

		int returnCode=HttpConnectTools.postJsonReturnCode(ConstantValue.HttpRoot+"update_user_coordinate",coordinatePost,null);
		return returnCode;
	}
}
