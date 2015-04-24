package org.wheat.leaflets.loader;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.wheat.leaflets.cache.ImageFileCache;
import org.wheat.leaflets.cache.ImageMemoryCache;
import org.wheat.leaflets.entity.PhotoParameters;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class ImageLoader 
{
	
	private final boolean DEBUG=true;
	private static ImageLoader instance;
	
	private ExecutorService executorService;	//�̳߳�
	private ImageMemoryCache memoryCache;		//�ڴ滺��
	private ImageFileCache fileCache;			//�ļ�����
	private Map<String,View> taskMap;		//�������
	private boolean allowLoad=true;				//�Ƿ��������ͼƬ
	private static Context context;
	
	private ImageLoader(Context context)
	{
		this.context=context;
		// ��ȡ��ǰϵͳ��CPU��Ŀ
		int cupNums=Runtime.getRuntime().availableProcessors();
		//����ϵͳ��Դ��������̳߳ش�С
		this.executorService=Executors.newFixedThreadPool(cupNums+1);
		
		this.memoryCache=new ImageMemoryCache(context);
		this.fileCache=new ImageFileCache();
		this.taskMap=new HashMap<String, View>();
	}
	
	/**
	 * ʹ�õ�������֤����Ӧ����ֻ��һ���̳߳غ�һ���ڴ滺����ļ�����
	 * @param context
	 * @return Ψһ��ImageLoader����
	 */
	public static ImageLoader getInstance(Context context)
	{
		if(instance==null)
			instance=new ImageLoader(context);
		return instance;
	}
	
	/**
	 * �ָ�Ϊ��ʼ�ɼ���ͼƬ��״̬
	 */
	public void restore()
	{
		this.allowLoad=true;
	}
	
	/**
     * ��סʱ���������ͼƬ,����������
     */
	public void lock()
	{
		this.allowLoad=false;
	}
	
	/**
     * ����ʱ����ͼƬ,ֹͣ������ʱ�����ͼƬ
     */
	public void unlock()
	{
		this.allowLoad=true;
		doTask();
	}
	
	
	
	
	public void addTask(PhotoParameters parameters,View view)
	{
		Bitmap bitmap=null;
		if(parameters.getMinSideLength()==-1&&parameters.getMaxNumOfPixels()==-1)
		{
			bitmap=memoryCache.getBitmapFromCache(parameters.getUrl());
		}
		else
		{
			bitmap=memoryCache.getBitmapFromCache("thumbnail"+parameters.getUrl());
		}
			
		if(bitmap!=null)
		{
			if(view instanceof ImageView)
			{
				ImageView imageView=(ImageView)view;
				if(parameters.isFixWidth()&&parameters.getImageViewWidth()>0)
				{
					//����������ԭͼ,�ڹ̶���ȵ�����£���ImageView.(width:height)==Bitmap.(width:heigh)
					int width=parameters.getImageViewWidth();			
					int picWidth=bitmap.getWidth();
					int picHeight=bitmap.getHeight();
					int height = (int) (width * 1.0 / picWidth * picHeight);
					ViewGroup.LayoutParams params=imageView.getLayoutParams();
					params.width=width;
					params.height=height;
					imageView.setLayoutParams(params);
				}
				imageView.setImageBitmap(bitmap);
			}
			if(view instanceof RelativeLayout)
			{

				RelativeLayout layout=(RelativeLayout)view;
				if(parameters.isFixWidth()&&parameters.getImageViewWidth()>0)
				{
					int width=parameters.getImageViewWidth();
					int picWidth=bitmap.getWidth();
					int picHeight=bitmap.getHeight();
					int height = (int) (width * 1.0 / picWidth * picHeight);
					ViewGroup.LayoutParams params=layout.getLayoutParams();
//					RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(width, height);
					params.width=width;
					params.height=height;
					layout.setLayoutParams(params);
				}
				layout.setBackgroundDrawable(new BitmapDrawable(context.getResources(), bitmap));
			}
			
			synchronized (taskMap) {
				taskMap.remove(view.hashCode());
			}
		}
		else
		{
			synchronized (taskMap) {
				/**
                 * ��ΪListView��GridView��ԭ�����������Ƴ���Ļ��itemȥ�����������ʾ��item,
                 * �����img��item������ݣ����������taskMap�����ʼ���ǵ�ǰ��Ļ�ڵ�����ImageView��
                 */
				view.setTag(parameters);
				taskMap.put(Integer.toString(view.hashCode()), view);
			}
			if(allowLoad)
			{
				doTask();
			}
		}
	}
	
	/**
     * ���ش�������е�����ͼƬ
     */
	public void doTask()
	{
		synchronized (taskMap) {
			Collection<View> con=taskMap.values();
			for(View view:con)
			{
				if(view!=null)
				{
					if(view.getTag()!=null)
					{
						loadImage((PhotoParameters)view.getTag(), view);
					}
				}
			}
			taskMap.clear();
		}
	}
	
	private void loadImage(PhotoParameters parameters,View view)
	{
		this.executorService.submit(new TaskWithResult(new TaskHandler(parameters, view), parameters));
	}
	private Bitmap getBitmap(PhotoParameters parameters)
	{
		Bitmap result=null;
		
		
		// ���ڴ滺���л�ȡͼƬ
		if(parameters.getMinSideLength()==-1&&parameters.getMaxNumOfPixels()==-1)
		{
			result=memoryCache.getBitmapFromCache(parameters.getUrl());
		}
		else
		{
			result=memoryCache.getBitmapFromCache("thumbnail"+parameters.getUrl());
		}
		if(result==null){
			// �ļ������л�ȡ
			
			if(parameters.getMinSideLength()==-1&&parameters.getMaxNumOfPixels()==-1)
			{
				result=fileCache.getImage(parameters.getUrl());
			}
			else
			{
				result=fileCache.getImage("thumbnail"+parameters.getUrl());
			}
			
			if(result==null){
				// �������ȡ
				try {
					result=HttpLoaderMethods.downLoadBitmap(parameters.getUrl(),parameters.getMinSideLength(),parameters.getMaxNumOfPixels(),parameters.getPhotoType());
				} catch (Throwable e) {
					e.printStackTrace();
				}
				if(result!=null)
				{
					if(parameters.getMinSideLength()==-1&&parameters.getMaxNumOfPixels()==-1)
					{
						fileCache.saveBitmap(result, parameters.getUrl());
						memoryCache.addBitmapToCache(parameters.getUrl(), result);
					}
					else
					{
						fileCache.saveBitmap(result, "thumbnail"+parameters.getUrl());
						memoryCache.addBitmapToCache("thumbnail"+parameters.getUrl(), result);
					}
					
					
				}
				else
				{
					if(DEBUG)
					{
						Log.d("ImageLoader", "resultΪnull!!!!!!");
					}
				}
			}
			else
			{
				
				// ��ӵ��ڴ滺��
				if(parameters.getMinSideLength()==-1&&parameters.getMaxNumOfPixels()==-1)
				{
					memoryCache.addBitmapToCache(parameters.getUrl(), result);
				}
				else
				{
					memoryCache.addBitmapToCache("thumbnail"+parameters.getUrl(), result);
				}
			}
			
		}
		return result;
	}
	
	/**
	 * ���߳�����
	 * @author wheat
	 *
	 */
	private class TaskWithResult implements Callable<String>
	{
		private PhotoParameters parameters;
		private Handler handler;
		
		public TaskWithResult(Handler handler,PhotoParameters parameters)
		{
			this.parameters=parameters;
			this.handler=handler;
		}

		@Override
		public String call() throws Exception {
			Message msg=new Message();
			msg.obj=getBitmap(parameters);
			if(msg.obj!=null)
			{
				handler.sendMessage(msg);
			}
			return parameters.getUrl();
		}
	}
	
	//����ui
	private static class TaskHandler extends Handler
	{
		PhotoParameters parameters;
		View view;
		
		public TaskHandler(PhotoParameters parameters,View view)
		{
			this.parameters=parameters;
			this.view=view;
		}

		@Override
		public void handleMessage(Message msg) 
		{
			/*** �鿴ImageView��Ҫ��ʾ��ͼƬ�Ƿ񱻸ı�  ***/
			if(view.getTag().equals(parameters))
			{
				if(msg.obj!=null)
				{
					Bitmap bitmap=(Bitmap)msg.obj;
					
					if(view instanceof ImageView)
					{
						ImageView imageView=(ImageView)view;
						if(parameters.isFixWidth()&&parameters.getImageViewWidth()>0)
						{
							//����������ԭͼ,�ڹ̶���ȵ�����£���ImageView.(width:height)==Bitmap.(width:heigh)
							int width=parameters.getImageViewWidth();			
							int picWidth=bitmap.getWidth();
							int picHeight=bitmap.getHeight();
							int height = (int) (width * 1.0 / picWidth * picHeight);
							ViewGroup.LayoutParams params=imageView.getLayoutParams();
							params.width=width;
							params.height=height;
							imageView.setLayoutParams(params);
						}
						imageView.setImageBitmap(bitmap);
					}
					
					if(view instanceof RelativeLayout)
					{
						RelativeLayout layout=(RelativeLayout)view;
						if(parameters.isFixWidth()&&parameters.getImageViewWidth()>0)
						{
							int width=parameters.getImageViewWidth();
							int picWidth=bitmap.getWidth();
							int picHeight=bitmap.getHeight();
							int height = (int) (width * 1.0 / picWidth * picHeight);
							ViewGroup.LayoutParams params=layout.getLayoutParams();
//							RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(width, height);
							params.width=width;
							params.height=height;
							layout.setLayoutParams(params);
						}
						layout.setBackgroundDrawable(new BitmapDrawable(context.getResources(), bitmap));
					}
					
				}
			}
		}
		
		
	}
	
	
	
	
}
