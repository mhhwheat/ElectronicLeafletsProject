/** 
 * description：
 * @author wheat
 * date: 2015-3-18  
 * time: 下午4:43:05
 */ 
package org.wheat.leaflets.data;


import java.io.IOException;
import java.io.StreamCorruptedException;

import org.wheat.leaflets.basic.SerializableUtil;
import org.wheat.leaflets.entity.SellerPreference;
import org.wheat.leaflets.entity.UserPreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/** 
 * description:
 * @author wheat
 * date: 2015-3-18  
 * time: 下午4:43:05
 */
public class UserLoginPreference 
{
	private static UserLoginPreference preference = null;  
    private SharedPreferences sharedPreference;  
    private String packageName;  
    
//    private static final String USER_NAME="userName";
//    private static final String PASSWORD = "password";  //密码  
//    private static final String IS_SAVE_PWD = "isSavePwd"; //是否保留密码 
//    private static final String USER_AVATAR="userAvatar";
    
    public static final int USER_LOGIN=1;
    public static final int SELLER_LOGIN=2;
    public static final int NO_USER_LOGIN=0;


    private static final String IS_FIRST_RUN="isFirstRun";//是否是第一次启动程序
    private static final String USER="user";
    private static final String SELLER="seller";
    //("user","seller","")
    private static final String LOGIN_STATE="login_state";
	private static final String LOCATION_LAT="lat";//设备的lat坐标
	private static final String LOCATION_LNG="lng";//设备的lng坐标
    
    private UserPreference mUser=null;
    private SellerPreference mSeller=null;
    
    public static synchronized UserLoginPreference getInstance(Context context){  
        if(preference == null)  
            preference = new UserLoginPreference(context);  
        return preference;  
    }  
    
    private  UserLoginPreference(Context context){  
        packageName = context.getPackageName() + "_user_login_preferences";  
        sharedPreference = context.getSharedPreferences(  
                packageName, Context.MODE_PRIVATE);  
    }
    
    public void setLoginState(int state)
    {
    	Editor editor=sharedPreference.edit();
    	editor.putInt(LOGIN_STATE, state);
    	editor.commit();
    }
    
    public int getLoginState()
    {
    	return sharedPreference.getInt(LOGIN_STATE, NO_USER_LOGIN);
    }
    
    public void firstRun()
    {
    	Editor editor=sharedPreference.edit();
    	editor.putBoolean(IS_FIRST_RUN, false);
    	editor.commit();
    }
    
    public boolean isFirstRun()
    {
    	return sharedPreference.getBoolean(IS_FIRST_RUN, true);
    }
    
    
    public void setUserPreference(UserPreference user)
    {
    	Editor editor=sharedPreference.edit();
    	String str="";
    	try
    	{
    		str=SerializableUtil.obj2Str(user);
    	}catch(IOException e)
    	{
    		e.printStackTrace();
    	}
    	editor.putString(USER, str);
    	editor.commit();
    	this.mUser=user;
    }
    
    public UserPreference getUserPreference()
    {
    	if(mUser==null)
    	{
    		mUser=new UserPreference();
    		String str=sharedPreference.getString(USER, "");
    		try
    		{
    			Object obj=SerializableUtil.str2Obj(str);
    			if(obj!=null)
    			{
    				mUser=(UserPreference)obj;
    			}
    			
    		}catch(StreamCorruptedException e)
    		{
    			e.printStackTrace();
    		}catch(IOException e)
    		{
    			e.printStackTrace();
    		}
    	}
    	return mUser;
    }
    
    
    public void setSellerPreference(SellerPreference seller)
    {
    	Editor editor=sharedPreference.edit();
    	String str="";
    	try
    	{
    		str=SerializableUtil.obj2Str(seller);
    	}catch(IOException e)
    	{
    		e.printStackTrace();
    	}
    	editor.putString(SELLER, str);
    	editor.commit();
    	this.mSeller=seller;
    }
    
    public SellerPreference getSellerPreference()
    {
    	if(mSeller==null)
    	{
    		mSeller=new SellerPreference();
    		String str=sharedPreference.getString(SELLER, "");
    		try
    		{
    			Object obj=SerializableUtil.str2Obj(str);
    			if(obj!=null)
    			{
    				mSeller=(SellerPreference)obj;
    			}
    			
    		}catch(StreamCorruptedException e)
    		{
    			e.printStackTrace();
    		}catch(IOException e)
    		{
    			e.printStackTrace();
    		}
    	}
    	return mSeller;
    }

	public void setLocationLat(double lat)
	{
		Editor editor=sharedPreference.edit();
		editor.putString(this.LOCATION_LAT,String.valueOf(lat));
		editor.commit();
	}

	public double getLocationLat()
	{
		return Double.parseDouble(sharedPreference.getString(LOCATION_LAT,"0"));
	}

	public void setLocationLng(double lng)
	{
		Editor editor=sharedPreference.edit();
		editor.putString(this.LOCATION_LNG,String.valueOf(lng));
		editor.commit();
	}

	public double getLocationLng()
	{
		return Double.parseDouble(sharedPreference.getString(LOCATION_LNG,"0"));
	}
    
}
