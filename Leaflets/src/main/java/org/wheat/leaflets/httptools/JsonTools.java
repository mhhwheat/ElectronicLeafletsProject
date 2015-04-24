package org.wheat.leaflets.httptools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonTools {

	/*
	 * ��json��ʽ���ַ���תΪʵ�����
	 */
	public static <T> T fromJson(String json,Class<T> classOfT)
	{
		Gson gson=new GsonBuilder().create();
		return gson.fromJson(json, classOfT);
	}
	
	/*
	 * �Ѷ���תΪjson��ʽ���ַ���
	 */
	public static String toJson(Object object)
	{
		Gson gson=new GsonBuilder().create();
		return gson.toJson(object);
	}
}
