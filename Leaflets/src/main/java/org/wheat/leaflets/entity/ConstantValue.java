package org.wheat.leaflets.entity;
/**
 * 
* @ClassName: ConstantValue 
* @Description: TODO 
��ȡ����ʧ�����֣�

1.���ݿ��������
2.���ݿⲻ��������
3.����ʧ��

�ϴ�/��������ʧ������
1.���ݿ��������
2.����ʧ��
* @author hogachen
* @date 2014��12��12�� ����6:30:01 
*
 */
public class ConstantValue {
//	public static final String HttpRoot="http://120.24.161.139:8000/leaflet/api/";//����
	public static final String HttpRoot="http://120.24.161.139:81/leaflet/api/"; //�ƶ�
	/**
	 * ʧ�ܵ�������ǧ���֣��ɹ�������ֻ��һ�֣�����ʧ�ܣ�
	 */
	//ʧ��״̬
	public static final int ClientParameterErr=1001;//�ͻ��������������

	public static final int InsertDbFailed=1002;//���ݿ����ʧ��

	public static final int uploadPhotoFailed=1004;

	public static final int updateCommentCountFailed=1009;
	
	public static final int dataNotInDBCannotUpdate=1006;

	public static final int getDataFailed=1010;

	public static final int deleRecordNotExist=1012;//Ҫɾ���ļ�¼������


	public static final int delBeautyButNotPhoto=1017;
	
	public static final int SocketTimeoutException=1018;//�������ݳ�ʱ
	
	public static final int ReUploadFailed=1019;
	public static final int ReUploadSuccess=1020;
	
	
	/**
	 * ��ȡ����ʧ�ܾ�������
	 */
	//ʧ��״̬
	public static final int DBException=1013;//���ݿ�ִ��SQL���������
	
	public static final int DataNotFoundInDB=1014;//���ݿ�û���������
	
	//�ɹ�״̬
	public static final int operateSuccess=1000;
	
	//�û���¼ʧ��
	public static final int loginFail=1021;
	
	//����ʧ��
	public static final int praiseFail=1026;
	
	//�ظ�����
	public static final int commentRepeat=1030;
	
	//û�л�ȡ�����������
	public static final int NO_MORE_DATA=1034;
	
	public static final String USER_LOGIN_SUCESS="User login success!";
	public static final String SELLER_LOGIN_SUCESS="Seller login success!";
}
