package org.wheat.leaflets.entity;

public class PhotoParameters 
{
	private String url;
	private int minSideLength;
	private int maxNumOfPixels;
	private boolean mFixWidth;
	private int mImageViewWidth;
	/**
	 * photoType��ֵ:"primary" or "secondary" or "seller_logo" or "user_portrait"
	 */
	private String photoType;
	public PhotoParameters(String url,int minSideLength,int maxNumOfPixels,String photoType)
	{
		this(url,minSideLength,maxNumOfPixels,false,0,photoType);
	}
	
	/**
	 * 
	 * @param url				ͼƬ�����ص�ַ
	 * @param minSideLength		ͼƬ��С�ߵ�����
	 * @param maxNumOfPixels	ͼƬ��������
	 * @param fixWidth			�Ƿ񱣳�ͼƬ�ĸ߿���������Ϊtrue��imageViewWidth����С��0�����fixWidthΪfalse��imageViewWidth������Ч
	 * @param imageViewWidth	ͼƬ����ImageView�Ŀ��
	 * @param photoType   		�����ͼƬ���ͣ�seller_logo,primary,secondary��
	 */
	public PhotoParameters(String url,int minSideLength,int maxNumOfPixels,boolean fixWidth,int imageViewWidth,String photoType)
	{
		this.url=url;
		this.minSideLength=minSideLength;
		this.maxNumOfPixels=maxNumOfPixels;
		this.mFixWidth=fixWidth;
		this.mImageViewWidth=imageViewWidth;
		this.photoType=photoType;
	}
	
	public String getUrl()
	{
		return this.url;
	}
	
	public void setUrl(String url)
	{
		this.url=url;
	}
	
	public int getMinSideLength()
	{
		return this.minSideLength;
	}
	
	public void setMinSideLength(int minSideLength)
	{
		this.minSideLength=minSideLength;
	}
	
	public int getMaxNumOfPixels()
	{
		return this.maxNumOfPixels;
	}
	
	public void setMaxNumOfPixles(int maxNumOfPixels)
	{
		this.maxNumOfPixels=maxNumOfPixels;
	}
	
	public void setFixWidth(boolean fixWidth)
	{
		this.mFixWidth=fixWidth;
	}
	
	public boolean isFixWidth()
	{
		return this.mFixWidth;
	}
	
	public void setImageViewWidth(int imageViewWidth)
	{
		this.mImageViewWidth=imageViewWidth;
	}
	
	public int getImageViewWidth()
	{
		return this.mImageViewWidth;
	}

	public String getPhotoType() {
		return photoType;
	}

	public void setPhotoType(String photoType) {
		this.photoType = photoType;
	}

	
	
	
}
