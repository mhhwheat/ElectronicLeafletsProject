package org.wheat.leaflets.httptools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.wheat.leaflets.entity.ConstantValue;
import org.wheat.leaflets.entity.json.JsonBaseImpl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class HttpConnectTools 
{
	private static final int SO_TIMEOUT = 0;

	/**
	 * @description using the get method to post data and receive the json data
	 * @param url   the address
	 * @param data  data to post in the url ,using the map
	 * @param headers   set the request parameters ,default null
	 * @return   the response with the data
	 * @throws IOException
	 */
	public static String getReturnJsonData(String url,HashMap<String,String> data,HashMap<String,String>headers) throws IOException
	{
		if (url == null) {
            return null;
        }
		String newUrl=url;
		if(data!=null){
			try {
				newUrl=addDatatoUrl(url,data,"utf-8");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		HttpClient httpClient=new DefaultHttpClient(createHttpParams());
		
		HttpGet httpGet=new HttpGet(newUrl);
		
		//http��ͷ
		if(headers==null)
		{
			headers=new HashMap<String, String>();
		}
		addHeaders(httpGet, headers);//����http��ͷ
		
		//��ʼ����
		HttpResponse rsp=httpClient.execute(httpGet);
		System.out.println("hoga"+" "+rsp.getStatusLine().getStatusCode());
		String result=getStringContentFromHttpResponse(rsp);
		
		return result;
		
	}
	/**
	 * @description using the get method 
	 * @param url  this url without the data
	 * @param data
	 * @param headers
	 * @return the response code (without the data)
	 * @throws IOException
	 */
	public static int  getReturnCode(String url,HashMap<String,String> data,HashMap<String,String>headers) throws IOException
	{
		if (url == null) {
            return -1;
        }
		String newUrl=url;
		if(data!=null){
			try {
				newUrl=addDatatoUrl(url,data,"utf-8");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		HttpClient httpClient=new DefaultHttpClient(createHttpParams());
		
		HttpGet httpGet=new HttpGet(newUrl);
		
		//http��ͷ
		if(headers==null)
		{
			headers=new HashMap<String, String>();
		}
		addHeaders(httpGet, headers);//����http��ͷ
		
		//��ʼ����
		HttpResponse rsp=httpClient.execute(httpGet);
		
		
		return rsp.getStatusLine().getStatusCode();
		
	}
	
	/**
	 * @author  hogachen
	 * @description add the data which going to post  to the url paramters
	 * @param path
	 * @param params
	 * @param enc
	 * @return
	 * @throws Exception
	 */
	public static String addDatatoUrl(String path, Map<String, String> params, String enc)throws Exception{
		StringBuilder sb = new StringBuilder(path);  
        sb.append('?');  
        for(Entry<String, String> entry : params.entrySet()){
            sb.append(entry.getKey()).append('=')  
                .append(URLEncoder.encode(entry.getValue(), enc)).append('&');  
        }  
        sb.deleteCharAt(sb.length()-1);  
         System.out.println("hoga"+sb.toString()); 
         return sb.toString();
	}
	/**
	 * @author mhhwheat
	 * @description get data and return json data 
	 * @param url this url have include the data
	 * @param headers
	 * @return  null ��ʾ����ʧ�� ��ֻҪ���ӳɹ��ͻ������ݣ�����һ���ǻ�ȡ���ݳɹ����п��������ݲ�����[�������ݿ����ʧ�ܵ�]
	 * @throws IOException
	 */
	public static String get(String url,HashMap<String,String> headers) throws IOException
	{
		if (url == null) {
            return null;
        }
		
		HttpClient httpClient=new DefaultHttpClient(createHttpParams());
		
		HttpGet httpGet=new HttpGet(url);
		
		//http��ͷ
		if(headers==null)
		{
			headers=new HashMap<String, String>();
		}
		addHeaders(httpGet, headers);//����http��ͷ
		
		//��ʼ����
		HttpResponse rsp=httpClient.execute(httpGet);
		
		String result=getStringContentFromHttpResponse(rsp);
		
		return result;
		
	}
	/**
	 * @author mhhwheat
	 * @param url
	 * @param postData
	 * @param headers
	 * @return
	 * @throws IOException
	 */
	public static String post(String url,byte[] postData,HashMap<String,String> headers) throws IOException
	{
		if (postData == null || postData.length <= 0) {
            return null;
        }
		
		DefaultHttpClient httpClient=new DefaultHttpClient(createHttpParams());
		httpClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(0,false));
		HttpPost httpPost=new HttpPost(url);
		
		//http��ͷ
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        addHeaders(httpPost, headers);//����http��ͷ
        
        //д����
        ByteArrayEntity entity=new ByteArrayEntity(postData);
        httpPost.setEntity(entity);
        //��ʼ����
        HttpResponse rsp=httpClient.execute(httpPost);
        
        
        String result=getStringContentFromHttpResponse(rsp);
        
        return result;
	}
	
	/**
	 * @description using  json to deliver the data
	 * @author hogachen
	 * @param url  Ҫ�����url
	 * @param headers  �����ͷ����Ϣ
	 * @param object  Ҫ���͵Ķ���
	 * @return	���صĶ�������ֻ�Ǵ���StringEntity
	 * @throws IOException
	 */
	public static Object postJsonReturnJson(String url,HashMap<String,String> headers,Object object) throws IOException
	{
		
		DefaultHttpClient httpClient=new DefaultHttpClient(createHttpParams());
		httpClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(0,false));
		
		HttpPost httpPost=new HttpPost(url);
		
		//http��ͷ
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        addHeaders(httpPost, headers);//����http��ͷ
        
        String objectJson=JsonTools.toJson(object);
        
        StringEntity entity=new StringEntity(objectJson,"UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        
    
        //��ʼ����
        System.out.println("in post withoutdata1");
        HttpResponse rsp=httpClient.execute(httpPost);
        System.out.println("in post withoutdata2");
        String result=getStringContentFromHttpResponse(rsp);
//        System.out.println(result);
        
        if(result!=null){
        	return JsonTools.fromJson(result, JsonBaseImpl.class);
        }
        
        return null;
	}
	
	public static String postJsonReturnJsonString(String url,HashMap<String,String> headers,Object object) throws IOException
	{
		DefaultHttpClient httpClient=new DefaultHttpClient(createHttpParams());
		httpClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(0,false));
		
		HttpPost httpPost=new HttpPost(url);
		
		//http��ͷ
        if (headers == null) {
            headers = new HashMap<String, String>();
//            headers.put("content-type", "text/xml;charset=UTF-8");
        }
        addHeaders(httpPost, headers);//����http��ͷ
        
        String objectJson=JsonTools.toJson(object);
        
        StringEntity entity=new StringEntity(objectJson,"UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        
        
    
        //��ʼ����
        System.out.println("in post withoutdata1");
        HttpResponse rsp=httpClient.execute(httpPost);
        System.out.println("in post withoutdata2");
        return getStringContentFromHttpResponse(rsp);
		
		
	}
	
	/**
	 * 
	* @Description: TODO ʹ��post�����������ݶ��󵽷���ˣ���ֹ��ȡ���ݳ�ʱ�ش�
	* ����Ҫ���������ܷ��ش����Լ��ֶ��ش���ֱ������һ��ʱ�䣨���߳�ʱ�ˣ�ֱ�Ӿͱ��Ϊ�����������ã�
	* @author hogachen   
	* @date 2014��12��14�� ����7:35:11 
	* @version V1.0  
	* @param url
	* @param object
	* @param headers
	* @return
	* @throws IOException
	 */
	public static int postJsonReturnCode(String url, Object object,
			HashMap<String, String> headers) {

		DefaultHttpClient httpClient = new DefaultHttpClient(createHttpParams());
		// ��ֹ��ȡ���ݳ�ʱ�ش�,��������ظ�����
		httpClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(
						0, false));
		HttpPost httpPost = new HttpPost(url);

		// http��ͷ
		if (headers == null) {
			headers = new HashMap<String, String>();
		}
		// ����http��ͷ
		addHeaders(httpPost, headers);
		
		
		
		String objectJson = JsonTools.toJson(object);
		// д����
		ByteArrayEntity entity;
		try {
			entity = new ByteArrayEntity(objectJson.getBytes("UTF-8"));
			httpPost.setEntity(entity);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		/**
		 * �˴�������setStringEntity�������룬��֪��Ϊʲô
		 */
		// System.out.println(encodeObjectJson);
		// httpPost.setEntity(new StringEntity(encodeObjectJson));
		// ��ʼ����
		System.out.println("in post withoutdata1");
		HttpResponse rsp=null;
		try {
			rsp = httpClient.execute(httpPost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			return ConstantValue.SocketTimeoutException;
		}
		System.out.println("in post withoutdata2");
		return rsp.getStatusLine().getStatusCode();
	}
	
	
	
	
	/*
	public static Bitmap getPhoto(String url,byte[] postData,HashMap<String,String> headers) throws  IOException
	{
		if (postData == null || postData.length <= 0 || context == null) {
            return null;
        }
        
		context = context.getApplicationContext();
		
		HttpClient httpClient=new DefaultHttpClient(createHttpParams(context));
		
		HttpPost httpPost=new HttpPost(url);
		
		//http��ͷ
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        addHeaders(httpPost, headers);//����http��ͷ
        
        //д����
        ByteArrayEntity entity=new ByteArrayEntity(postData);
        httpPost.setEntity(entity);
        //��ʼ����
        HttpResponse rsp=httpClient.execute(httpPost);
		return getPhotoFromHttpResponse(rsp);
	}
	*/
	
	
	/**
     * ��http������뱨ͷ
     *
     * @param req
     * @param headers
     */
    public static void addHeaders(HttpRequest req, HashMap<String, String> headers) {
        try {
            if (headers == null || req == null) {
                return;
            }
            Iterator<Entry<String, String>> iter = headers.entrySet().iterator();
            while (iter.hasNext()) {
                Entry<String, String> entry = iter.next();
                String key = entry.getKey();
                String value = entry.getValue();
                req.addHeader(key, value);
            }

        } catch (Throwable e) {
        	e.printStackTrace();
        }
    }
	
    /**
     * ����http��Ӧ��ȡͼƬ
     * @param rsp
     * @return Bitmap
     * @throws IOException
     */
    public static Bitmap getPhotoFromHttpResponse(HttpResponse rsp) throws IOException
    {
    	if(rsp==null)
    		return null;
    	if(rsp.getStatusLine().getStatusCode()==HttpStatus.SC_OK)
    	{
    		HttpEntity entity=rsp.getEntity();
    		if(entity==null)
    		{
    			return null;
    		}
    		Header header=entity.getContentEncoding();
    		byte[] data;
    		if (header == null || header.getValue() == null) {
                 //˵��û��ѹ�� 
                 data=EntityUtils.toByteArray(entity);
            }
    		else
    			data=EntityUtils.toByteArray(entity);
    		return BitmapFactory.decodeByteArray(data, 0, data.length);
    	}
    	return null;
    	
    }
    
    /**
     * 
     * @param rsp
     * @return
     * @throws IOException
     */
    public static String getStringContentFromHttpResponse(HttpResponse rsp) throws IOException
    {
    	if(rsp==null)
    	{System.out.println("response is null");
    		return null;
    	}
    	if(rsp.getStatusLine().getStatusCode()==HttpStatus.SC_OK)
    	{
    		HttpEntity entity=rsp.getEntity();
    		if(entity==null)
    		{
    			return null;
    		}
    		Header header = entity.getContentEncoding();
            if (header == null || header.getValue() == null) {
                //˵��û��ѹ��
                return EntityUtils.toString(entity);
            }
            return EntityUtils.toString(entity);
    	}
    	System.out.println("������� �� "+rsp.getStatusLine().getStatusCode());
    	return null;
    }
    
    /**
     * ��entityתΪString��ͨ��Gzipѹ��
     *
     * @param stream
     * @return
     */
    public static String loadStringFromGzipStream(InputStream stream) throws IOException {

        GZIPInputStream gzin = new GZIPInputStream(stream);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len;
        byte[] buff = new byte[1024];
        int size = buff.length;
        while ((len = gzin.read(buff, 0, size)) > 0) {
            baos.write(buff, 0, len);
        }
        baos.flush();
        gzin.close();
        String rt = baos.toString("utf-8");
        baos.close();
        return rt;
    }
    
	/**
	 * ����http�������
	 * @param context
	 * @return
	 */
	public static HttpParams createHttpParams()
	{
		HttpParams params = new BasicHttpParams();
		

        // ����http��ʱ(10��)
        HttpConnectionParams.setConnectionTimeout(params, 10*1000);

        // ����socket��ʱ(15��)->(30��)-2013-05-14 �ȴ�����ʱ��
        HttpConnectionParams.setSoTimeout(params, 30*1000);

        ConnManagerParams.setTimeout(params, 1000); //�����ӳ��л�ȡ���ӵĳ�ʱʱ��  
        
        
//        Long CONN_MANAGER_TIMEOUT = 500L; //��ֵ�������Ӳ����õ�ʱ��ȴ���ʱʱ�䣬һ��Ҫ���ã����Ҳ���̫�� ()

        //���ύ����֮ǰ ���������Ƿ����
//        params.setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, true);
        // ���ô����Զ������ض���
        HttpClientParams.setRedirecting(params, true);
        
        
        return params;
	}
	
	
	
	
	
	/**
	 * 
	* @Description: �������ö�����
	* @author hogachen   
	* @date 2014��12��29�� ����1:11:25 
	* @version V1.0  
	* @param url
	* @param data
	* @param headers
	* @return
	* @throws IOException
	 */
	public static int  getReturnCodeShortTime(String url,HashMap<String,String> data,HashMap<String,String>headers,double requestTime) throws IOException
	{
		if (url == null) {
            return -1;
        }
		String newUrl=url;
		if(data!=null){
			try {
				newUrl=addDatatoUrl(url,data,"utf-8");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		HttpClient httpClient=new DefaultHttpClient(createHttpParams());
		
		HttpGet httpGet=new HttpGet(newUrl);
		
		//��ʼ����
		HttpResponse rsp=httpClient.execute(httpGet);	
		return rsp.getStatusLine().getStatusCode();
		
	}
	/**
	 * 
	* @Description: 
	* @author hogachen   
	* @date 2014��12��29�� ����1:13:09 
	* @version V1.0  
	* @param requestTime  �ȴ�������Ӧ��ʱ��
	* @return
	 */
	public static HttpParams createHttpParamsForTestServer(int requestTime)
	{
		HttpParams params = new BasicHttpParams();
		

        // ����http��ʱ(30��)
        HttpConnectionParams.setConnectionTimeout(params, 2*1000);

        // ����socket��ʱ(15��)->(30��)-2013-05-14 �ȴ�����ʱ��
        HttpConnectionParams.setSoTimeout(params, requestTime*1000);

        ConnManagerParams.setTimeout(params, 1000); //�����ӳ��л�ȡ���ӵĳ�ʱʱ��  
        
        
//        Long CONN_MANAGER_TIMEOUT = 500L; //��ֵ�������Ӳ����õ�ʱ��ȴ���ʱʱ�䣬һ��Ҫ���ã����Ҳ���̫�� ()

        //���ύ����֮ǰ ���������Ƿ����
//        params.setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, true);
        // ���ô����Զ������ض���
        HttpClientParams.setRedirecting(params, true);
        
        
        return params;
	}
}
