package org.wheat.leaflets.entity;
/**
 * 
* @ClassName: ConstantValue 
* @Description: TODO 
获取数据失败三种：

1.数据库操作出错
2.数据库不存在数据
3.链接失败

上传/更新数据失败两种
1.数据库操作出错
2.链接失败
* @author hogachen
* @date 2014年12月12日 下午6:30:01 
*
 */
public class ConstantValue {
//	public static final String HttpRoot="http://120.24.161.139:8000/leaflet/api/";//调试
	public static final String HttpRoot="http://120.24.161.139:81/leaflet/api/"; //云端
	/**
	 * 失败的人生有千百种，成功的人生只有一种（更新失败）
	 */
	//失败状态
	public static final int ClientParameterErr=1001;//客户端数据请求错误

	public static final int InsertDbFailed=1002;//数据库插入失败

	public static final int uploadPhotoFailed=1004;

	public static final int updateCommentCountFailed=1009;
	
	public static final int dataNotInDBCannotUpdate=1006;

	public static final int getDataFailed=1010;

	public static final int deleRecordNotExist=1012;//要删除的记录不存在


	public static final int delBeautyButNotPhoto=1017;
	
	public static final int SocketTimeoutException=1018;//接收数据超时
	
	public static final int ReUploadFailed=1019;
	public static final int ReUploadSuccess=1020;
	
	
	/**
	 * 获取数据失败就这两种
	 */
	//失败状态
	public static final int DBException=1013;//数据库执行SQL语句出项错误
	
	public static final int DataNotFoundInDB=1014;//数据库没有相关数据
	
	//成功状态
	public static final int operateSuccess=1000;
	
	//用户登录失败
	public static final int loginFail=1021;
	
	//点赞失败
	public static final int praiseFail=1026;
	
	//重复评论
	public static final int commentRepeat=1030;
	
	//没有获取到更多的内容
	public static final int NO_MORE_DATA=1034;
	
	public static final String USER_LOGIN_SUCESS="User login success!";
	public static final String SELLER_LOGIN_SUCESS="Seller login success!";
}
