/** 
 * description��
 * @author wheat
 * date: 2015-1-7  
 * time: ����3:36:37
 */ 
package org.wheat.leaflets.data;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/** 
 * description:
 * @author wheat
 * date: 2015-1-7  
 * time: ����3:36:37
 */
public class SqliteDBManager 
{
	private SqliteDBHelper dbHelper;
	private SQLiteDatabase db;
	
	public SqliteDBManager(Context context)
	{
		dbHelper=new SqliteDBHelper(context);
		db=dbHelper.getWritableDatabase();
	}
	
//	/**
//	 * ��FollowFragment�����ݴ洢����follow_page��
//	 * @param list
//	 */
//	public void addToFollowPage(List<Photo> list)
//	{
//		String sql="INSERT INTO follow_page VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//		db.beginTransaction();
//		try
//		{
//			for(Photo photo:list)
//			{
//				db.execSQL(sql, new Object[]{photo.getAvatarPath(),photo.getNickName(),photo.getIsPraise(),photo.getPhotoDescription(),photo.getBeautyId(),
//						photo.getPhotoId(),photo.getCommentCount(),photo.getPraiseCount(),photo.getPhotoPath(),photo.getUserPhoneNumber(),photo.getUpLoadTimeToString()});
//			}
//			db.setTransactionSuccessful();
//		}
//		finally
//		{
//			db.endTransaction();
//		}
//	}
//	
//	/**
//	 * ���sqlite��follow_page�������
//	 * @return ����һ��Photo�����list
//	 * @throws ParseException 
//	 */
//	public List<Photo> getFromFollowPage()
//	{
//		ArrayList<Photo> list=new ArrayList<Photo>();
//		Cursor cursor=db.rawQuery("select * from follow_page", null);
//		while(cursor.moveToNext())
//		{
//			Photo photo=new Photo();
//			photo.setAvatarPath(cursor.getString(cursor.getColumnIndex("avatarPath")));
//			photo.setNickName(cursor.getString(cursor.getColumnIndex("nickname")));
//			photo.setIspraise(cursor.getInt(cursor.getColumnIndex("isPraise")));
//			photo.setPhotoDescription(cursor.getString(cursor.getColumnIndex("photoDescription")));
//			photo.setBeautyId(cursor.getInt(cursor.getColumnIndex("beautyId")));
//			photo.setPhotoId(cursor.getInt(cursor.getColumnIndex("photoId")));
//			photo.setCommentCount(cursor.getInt(cursor.getColumnIndex("commentCount")));
//			photo.setPraiseCount(cursor.getInt(cursor.getColumnIndex("praiseCount")));
//			photo.setPhotoPath(cursor.getString(cursor.getColumnIndex("photoPath")));
//			photo.setUserPhoneNumber(cursor.getString(cursor.getColumnIndex("userPhoneNumber")));
//			try {
//				photo.setUploadTime(cursor.getString(cursor.getColumnIndex("uploadTime")));
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//			list.add(photo);
//		}
//		return list;
//	}
//	
//	/**
//	 * ���sqlite��follow_page����������ݣ�������������id�ĳ�ʼֵ
//	 */
//	public void clearFollowPage()
//	{
//		db.execSQL("delete from follow_page");
//		db.execSQL("update sqlite_sequence SET seq = 0 where name ='follow_page'");
//	}
//	
//	/**
//	 * ��NeighborGridFragment��������ݴ浽sqlite���ݿ���
//	 * @param list
//	 */
//	public void addToNeighborPage(List<BeautyIntroduction> list)
//	{
//		String sql="INSERT INTO neighbor_page VALUES(null, ?, ?, ?, ?, ?, ?, ?)";
//		db.beginTransaction();
//		try{
//			for(BeautyIntroduction beauty:list)
//			{
//				db.execSQL(sql, new Object[]{beauty.getBeautyId(),beauty.getBeautyName(),beauty.getSchool(),beauty.getAvatarPath(),beauty.getDescription(),beauty.getPraiseTimes(),beauty.getCommentTimes()});
//				
//			}
//			db.setTransactionSuccessful();
//		}finally
//		{
//			db.endTransaction();
//		}
//	}
//	
//	/**
//	 * ��sqlite���ݿ��л�ȡNeighborGridFragment�Ļ���
//	 * @return
//	 */
//	public List<BeautyIntroduction> getFromNeighborPage()
//	{
//		ArrayList<BeautyIntroduction> list=new ArrayList<BeautyIntroduction>();
//		Cursor cursor=db.rawQuery("select * from neighbor_page", null);
//		while(cursor.moveToNext())
//		{
//			BeautyIntroduction beauty=new BeautyIntroduction();
//			beauty.setBeautyId(cursor.getInt(cursor.getColumnIndex("beautyId")));
//			beauty.setBeautyName(cursor.getString(cursor.getColumnIndex("beautyName")));
//			beauty.setSchool(cursor.getString(cursor.getColumnIndex("school")));
//			beauty.setAvatarPath(cursor.getString(cursor.getColumnIndex("avatarPath")));
//			beauty.setDescription(cursor.getString(cursor.getColumnIndex("photoDescription")));
//			beauty.setPraiseTimes(cursor.getInt(cursor.getColumnIndex("praiseTimes")));
//			beauty.setCommentTimes(cursor.getInt(cursor.getColumnIndex("commentTimes")));
//			
//			list.add(beauty);
//		}
//		return list;
//	}
//	
//	/**
//	 * ���NeighborGridFragment��sqlite���ݿ��еĻ���
//	 */
//	public void clearNeighborPage()
//	{
//		db.execSQL("delete from neighbor_page");
//		db.execSQL("update sqlite_sequence SET seq = 0 where name ='neighbor_page'");
//	}
//	
//	
//	/**
//	 * ��BeautyPersonalPageActivity��������ݴ浽sqlite���ݿ���
//	 * @param list
//	 */
//	public void addToBeautyPersonalPage(List<Photo> list)
//	{
//		String sql="INSERT INTO beauty_personal_page VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//		db.beginTransaction();
//		try
//		{
//			for(Photo photo:list)
//			{
//				db.execSQL(sql, new Object[]{photo.getAvatarPath(),photo.getNickName(),photo.getIsPraise(),photo.getPhotoDescription(),photo.getBeautyId(),
//						photo.getPhotoId(),photo.getCommentCount(),photo.getPraiseCount(),photo.getPhotoPath(),photo.getUserPhoneNumber(),photo.getUpLoadTimeToString()});
//			}
//			db.setTransactionSuccessful();
//		}
//		finally
//		{
//			db.endTransaction();
//		}
//	}
//	
//	/**
//	 * ��sqlite���ݿ��л�ȡBeautyPersonalPageActivity�Ļ���
//	 * @return
//	 */
//	public List<Photo> getFromBeautyPersonalPage(int beautyId)
//	{
//		ArrayList<Photo> list=new ArrayList<Photo>();
//		Cursor cursor=db.rawQuery("select * from beauty_personal_page where beautyId=?", new String[]{String.valueOf(beautyId)});
//		while(cursor.moveToNext())
//		{
//			Photo photo=new Photo();
//			photo.setAvatarPath(cursor.getString(cursor.getColumnIndex("avatarPath")));
//			photo.setNickName(cursor.getString(cursor.getColumnIndex("nickname")));
//			photo.setIspraise(cursor.getInt(cursor.getColumnIndex("isPraise")));
//			photo.setPhotoDescription(cursor.getString(cursor.getColumnIndex("photoDescription")));
//			photo.setBeautyId(cursor.getInt(cursor.getColumnIndex("beautyId")));
//			photo.setPhotoId(cursor.getInt(cursor.getColumnIndex("photoId")));
//			photo.setCommentCount(cursor.getInt(cursor.getColumnIndex("commentCount")));
//			photo.setPraiseCount(cursor.getInt(cursor.getColumnIndex("praiseCount")));
//			photo.setPhotoPath(cursor.getString(cursor.getColumnIndex("photoPath")));
//			photo.setUserPhoneNumber(cursor.getString(cursor.getColumnIndex("userPhoneNumber")));
//			try {
//				photo.setUploadTime(cursor.getString(cursor.getColumnIndex("uploadTime")));
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//			list.add(photo);
//		}
//		return list;
//	}
//	
//	
//	/**
//	 * ���BeautyPersonalPageActivity��sqlite���ݿ��еĻ���
//	 */
//	public void clearBeautyPersonalPage()
//	{
//		db.execSQL("delete from beauty_personal_page");
//		db.execSQL("update sqlite_sequence SET seq = 0 where name ='beauty_personal_page'");
//	}
//	
//	/**
//	 * ��TabSumFragment��������ݴ浽sqlite���ݿ���
//	 * @param list
//	 */
//	public void addToSumPage(List<BeautyIntroduction> list)
//	{
//		String sql="INSERT INTO sum_page VALUES(null, ?, ?, ?, ?, ?, ?, ?)";
//		db.beginTransaction();
//		try{
//			for(BeautyIntroduction beauty:list)
//			{
//				db.execSQL(sql, new Object[]{beauty.getBeautyId(),beauty.getBeautyName(),beauty.getSchool(),beauty.getAvatarPath(),beauty.getDescription(),beauty.getPraiseTimes(),beauty.getCommentTimes()});
//				
//			}
//			db.setTransactionSuccessful();
//		}finally
//		{
//			db.endTransaction();
//		}
//	}
//	
//	/**
//	 * ��sqlite���ݿ��л�ȡTabSumFragment�Ļ���
//	 * @return
//	 */
//	public List<BeautyIntroduction> getFromSumPage()
//	{
//		ArrayList<BeautyIntroduction> list=new ArrayList<BeautyIntroduction>();
//		Cursor cursor=db.rawQuery("select * from sum_page", null);
//		while(cursor.moveToNext())
//		{
//			BeautyIntroduction beauty=new BeautyIntroduction();
//			beauty.setBeautyId(cursor.getInt(cursor.getColumnIndex("beautyId")));
//			beauty.setBeautyName(cursor.getString(cursor.getColumnIndex("beautyName")));
//			beauty.setSchool(cursor.getString(cursor.getColumnIndex("school")));
//			beauty.setAvatarPath(cursor.getString(cursor.getColumnIndex("avatarPath")));
//			beauty.setDescription(cursor.getString(cursor.getColumnIndex("photoDescription")));
//			beauty.setPraiseTimes(cursor.getInt(cursor.getColumnIndex("praiseTimes")));
//			beauty.setCommentTimes(cursor.getInt(cursor.getColumnIndex("commentTimes")));
//			
//			list.add(beauty);
//		}
//		return list;
//	}
//	
//	/**
//	 * ���TabSumFragment��sqlite���ݿ��еĻ���
//	 */
//	public void clearSumPage()
//	{
//		db.execSQL("delete from sum_page");
//		db.execSQL("update sqlite_sequence SET seq = 0 where name ='sum_page'");
//	}
//	
//	
//	/**
//	 * ��TabNewFragment��������ݴ浽sqlite���ݿ���
//	 * @param list
//	 */
//	public void addToNewPage(List<BeautyIntroduction> list)
//	{
//		String sql="INSERT INTO new_page(beautyId,beautyName,school,avatarPath,photoDescription,praiseTimes,commentTimes) VALUES(?, ?, ?, ?, ?, ?, ?)";
//		db.beginTransaction();
//		try{
//			for(BeautyIntroduction beauty:list)
//			{
//				db.execSQL(sql, new Object[]{beauty.getBeautyId(),beauty.getBeautyName(),beauty.getSchool(),beauty.getAvatarPath(),beauty.getDescription(),beauty.getPraiseTimes(),beauty.getCommentTimes()});
//				
//			}
//			db.setTransactionSuccessful();
//		}finally
//		{
//			db.endTransaction();
//		}
//	}
//	
//	/**
//	 * ��sqlite���ݿ��л�ȡTabNewFragment�Ļ���
//	 * @return
//	 */
//	public List<BeautyIntroduction> getFromNewPage()
//	{
//		ArrayList<BeautyIntroduction> list=new ArrayList<BeautyIntroduction>();
//		Cursor cursor=db.rawQuery("select * from new_page", null);
//		while(cursor.moveToNext())
//		{
//			BeautyIntroduction beauty=new BeautyIntroduction();
//			beauty.setBeautyId(cursor.getInt(cursor.getColumnIndex("beautyId")));
//			beauty.setBeautyName(cursor.getString(cursor.getColumnIndex("beautyName")));
//			beauty.setSchool(cursor.getString(cursor.getColumnIndex("school")));
//			beauty.setAvatarPath(cursor.getString(cursor.getColumnIndex("avatarPath")));
//			beauty.setDescription(cursor.getString(cursor.getColumnIndex("photoDescription")));
//			beauty.setPraiseTimes(cursor.getInt(cursor.getColumnIndex("praiseTimes")));
//			beauty.setCommentTimes(cursor.getInt(cursor.getColumnIndex("commentTimes")));
//			
//			list.add(beauty);
//		}
//		return list;
//	}
//	
//	/**
//	 * ���TabNewFragment��sqlite���ݿ��еĻ���
//	 */
//	public void clearNewPage()
//	{
//		db.execSQL("delete from new_page");
//		db.execSQL("update sqlite_sequence SET seq = 0 where name ='new_page'");
//	}
//	
//	/**
//	 * ��TabRiseFragment��������ݴ浽sqlite���ݿ���
//	 * @param list
//	 */
//	public void addToRisePage(List<BeautyIntroduction> list)
//	{
//		String sql="INSERT INTO rise_page VALUES(null, ?, ?, ?, ?, ?, ?, ?)";
//		db.beginTransaction();
//		try{
//			for(BeautyIntroduction beauty:list)
//			{
//				db.execSQL(sql, new Object[]{beauty.getBeautyId(),beauty.getBeautyName(),beauty.getSchool(),beauty.getAvatarPath(),beauty.getDescription(),beauty.getPraiseTimes(),beauty.getCommentTimes()});
//				
//			}
//			db.setTransactionSuccessful();
//		}finally
//		{
//			db.endTransaction();
//		}
//	}
//	
//	/**
//	 * ��sqlite���ݿ��л�ȡTabRiseFragment�Ļ���
//	 * @return
//	 */
//	public List<BeautyIntroduction> getFromRisePage()
//	{
//		ArrayList<BeautyIntroduction> list=new ArrayList<BeautyIntroduction>();
//		Cursor cursor=db.rawQuery("select * from rise_page", null);
//		while(cursor.moveToNext())
//		{
//			BeautyIntroduction beauty=new BeautyIntroduction();
//			beauty.setBeautyId(cursor.getInt(cursor.getColumnIndex("beautyId")));
//			beauty.setBeautyName(cursor.getString(cursor.getColumnIndex("beautyName")));
//			beauty.setSchool(cursor.getString(cursor.getColumnIndex("school")));
//			beauty.setAvatarPath(cursor.getString(cursor.getColumnIndex("avatarPath")));
//			beauty.setDescription(cursor.getString(cursor.getColumnIndex("photoDescription")));
//			beauty.setPraiseTimes(cursor.getInt(cursor.getColumnIndex("praiseTimes")));
//			beauty.setCommentTimes(cursor.getInt(cursor.getColumnIndex("commentTimes")));
//			
//			list.add(beauty);
//		}
//		return list;
//	}
//	
//	/**
//	 * ���MyDetailPage��sqlite���ݿ��еĻ���
//	 */
//	public void clearRisePage()
//	{
//		db.execSQL("delete from rise_page");
//		db.execSQL("update sqlite_sequence SET seq = 0 where name ='rise_page'");
//	}
//	
//	/**
//	 * ��FollowFragment�����ݴ洢����my_detail_page��
//	 * @param list
//	 */
//	public void addToMyDetailPage(List<Photo> list)
//	{
//		String sql="INSERT INTO my_detail_page VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//		db.beginTransaction();
//		try
//		{
//			for(Photo photo:list)
//			{
//				db.execSQL(sql, new Object[]{photo.getAvatarPath(),photo.getNickName(),photo.getIsPraise(),photo.getPhotoDescription(),photo.getBeautyId(),
//						photo.getPhotoId(),photo.getCommentCount(),photo.getPraiseCount(),photo.getPhotoPath(),photo.getUserPhoneNumber(),photo.getUpLoadTimeToString()});
//			}
//			db.setTransactionSuccessful();
//		}
//		finally
//		{
//			db.endTransaction();
//		}
//	}
//	
//	/**
//	 * ���sqlite��my_detail_page�������
//	 * @return ����һ��Photo�����list
//	 * @throws ParseException 
//	 */
//	public List<Photo> getMyDetailPage()
//	{
//		ArrayList<Photo> list=new ArrayList<Photo>();
//		Cursor cursor=db.rawQuery("select * from my_detail_page", null);
//		while(cursor.moveToNext())
//		{
//			Photo photo=new Photo();
//			photo.setAvatarPath(cursor.getString(cursor.getColumnIndex("avatarPath")));
//			photo.setNickName(cursor.getString(cursor.getColumnIndex("nickname")));
//			photo.setIspraise(cursor.getInt(cursor.getColumnIndex("isPraise")));
//			photo.setPhotoDescription(cursor.getString(cursor.getColumnIndex("photoDescription")));
//			photo.setBeautyId(cursor.getInt(cursor.getColumnIndex("beautyId")));
//			photo.setPhotoId(cursor.getInt(cursor.getColumnIndex("photoId")));
//			photo.setCommentCount(cursor.getInt(cursor.getColumnIndex("commentCount")));
//			photo.setPraiseCount(cursor.getInt(cursor.getColumnIndex("praiseCount")));
//			photo.setPhotoPath(cursor.getString(cursor.getColumnIndex("photoPath")));
//			photo.setUserPhoneNumber(cursor.getString(cursor.getColumnIndex("userPhoneNumber")));
//			try {
//				photo.setUploadTime(cursor.getString(cursor.getColumnIndex("uploadTime")));
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//			list.add(photo);
//		}
//		return list;
//	}
//	
//	/**
//	 * ���sqlite��my_detail_page����������ݣ�������������id�ĳ�ʼֵ
//	 */
//	public void clearMyDetailPage()
//	{
//		db.execSQL("delete from my_detail_page");
//		db.execSQL("update sqlite_sequence SET seq = 0 where name ='my_detail_page'");
//	}
//	
//	public void addToPhotoComments(List<Comment> list)
//	{
//		String sql="INSERT INTO photo_comments VALUES(null, ?, ?, ?, ?, ?, ?, ?)";
//		db.beginTransaction();
//		try
//		{
//			for(Comment comment:list)
//			{
//				db.execSQL(sql, new Object[]{comment.getCommentID(),comment.getPhotoID(),comment.getUserPhoneNumber(),comment.getUserAvatar(),comment.getUserNickName(),comment.getCommentTimeToString(),comment.getCommentContent()});
//			}
//			db.setTransactionSuccessful();
//		}
//		finally
//		{
//			db.endTransaction();
//		}
//	}
//
//	public LinkedList<Comment> getPhotoComment(int photoId)
//	{
//		LinkedList<Comment> list=new LinkedList<Comment>();
//		Cursor cursor=db.rawQuery("select * from photo_comments where photoId=?", new String[]{String.valueOf(photoId)});
//		while(cursor.moveToNext())
//		{
//			Comment comment=new Comment();
//			comment.setCommentID(cursor.getInt(cursor.getColumnIndex("commentId")));
//			comment.setPhotoID(cursor.getInt(cursor.getColumnIndex("photoId")));
//			comment.setUserPhoneNumber(cursor.getString(cursor.getColumnIndex("userPhoneNumber")));
//			comment.setUserAvatar(cursor.getString(cursor.getColumnIndex("userAvatar")));
//			comment.setUserNickName(cursor.getString(cursor.getColumnIndex("userNickeName")));
//			comment.setCommentTime(cursor.getString(cursor.getColumnIndex("commentTime")));
//			comment.setCommentContent(cursor.getString(cursor.getColumnIndex("commentContent")));
//			list.add(comment);
//		}
//		return list;
//	}
//	
//	public void clearPhotoComments()
//	{
//		db.execSQL("delete from photo_comments");
//		db.execSQL("update sqlite_sequence SET seq = 0 where name ='photo_comments'");
//	}
}
