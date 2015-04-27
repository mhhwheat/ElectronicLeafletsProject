package org.wheat.leaflets.activity;


import org.wheat.leaflets.R;
import org.wheat.leaflets.basic.ExitApplication;
import org.wheat.leaflets.data.UserLoginPreference;
import org.wheat.leaflets.entity.ConstantValue;
import org.wheat.leaflets.entity.SellerPreference;
import org.wheat.leaflets.entity.UserPreference;
import org.wheat.leaflets.entity.json.LoginMsgJson;
import org.wheat.leaflets.entity.json.SellerMsgJson;
import org.wheat.leaflets.entity.json.UserMsgJson;
import org.wheat.leaflets.loader.HttpLoaderMethods;
import org.wheat.leaflets.loader.LoginAndRegister;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/** 
 * description:
 * @author wheat
 * date: 2015-3-3  
 * time: ÏÂÎç12:42:41
 */
public class LoginActivity extends Activity
{
	
	private EditText etEmail;
	private EditText etPassword;
	private Button btLogin;
	private TextView tvRegister;
	private ImageView ivTitleBack;
	
	private UserLoginPreference sharePreference;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_login);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_login_title);

		etEmail=(EditText)findViewById(R.id.login_user_id);
		etPassword=(EditText)findViewById(R.id.login_pwd);
		btLogin=(Button)findViewById(R.id.btLogin);
		tvRegister=(TextView)findViewById(R.id.tvRegister);
		
		sharePreference=UserLoginPreference.getInstance(getApplicationContext());
		

		btLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d("LoginActivity", "button click!");
				new LoginTask(etEmail.getText().toString(), etPassword.getText().toString()).execute();
			}
		});
		
		tvRegister.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(LoginActivity.this,RegisterChoiceActivity.class);
				startActivity(intent);
			}
		});

		initialTitle();
		
		ExitApplication.getInstance().addActivity(this);
	}

	private void initialTitle()
	{
		ivTitleBack=(ImageView)findViewById(R.id.login_title_back_img);
		ivTitleBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LoginActivity.this.finish();
			}
		});
	}
	
	
	
	private class LoginTask extends AsyncTask<Void, Void, LoginMsgJson>
	{
		private String strEmail;
		private String strPassword;
		
		public LoginTask(String email,String password)
		{
			this.strEmail=email;
			this.strPassword=password;
		}

		@Override
		protected LoginMsgJson doInBackground(Void... params) {
			LoginMsgJson loginMsgJson=null;
			try {
				loginMsgJson=LoginAndRegister.synLogin(strEmail, strPassword);
//				LoginAndRegister.login();
			} catch (Throwable e) {
				e.printStackTrace();
			}
			return loginMsgJson;
		}

		@Override
		protected void onPostExecute(LoginMsgJson result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result==null)
			{
				Log.d("LoginActivity", "LoginMsgJson is null");
			}
			else
			{
				if(result.getCode()==1000)
				{
//					Toast toast=Toast.makeText(LoginActivity.this, "µÇÂ¼³É¹¦", Toast.LENGTH_LONG);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
					if(result.getMsg().equals(ConstantValue.USER_LOGIN_SUCESS))
					{
						new GetUserMsgTask(strEmail).execute();

					}
					else if(result.getMsg().equals(ConstantValue.SELLER_LOGIN_SUCESS))
					{
						new GetSellerMsgTask(strEmail).execute();
					}
				}
				if(result.getCode()==1021)
				{
					Toast toast=Toast.makeText(LoginActivity.this, "µÇÂ¼Ê§°Ü", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
			}
		}
		
	}
	
	public class GetUserMsgTask extends AsyncTask<Void, Void, UserMsgJson>
	{
		private String userName;
		
		public GetUserMsgTask(String userName)
		{
			this.userName=userName;
		}
		
		@Override
		protected UserMsgJson doInBackground(Void... params) {
			UserMsgJson json=null;
			try
			{
				json=HttpLoaderMethods.getUserData(userName);
			}catch(Throwable e)
			{
				e.printStackTrace();
			}
			return json;
		}

		@Override
		protected void onPostExecute(UserMsgJson result) {
			super.onPostExecute(result);
			if(result!=null&&result.getCode()==1000)
			{
				sharePreference.setLoginState(UserLoginPreference.USER_LOGIN);
				UserPreference preference=sharePreference.getUserPreference();
				preference.setNickName(result.getData().getDataFields().getNickName());
				preference.setUserAvatar(result.getData().getDataFields().getUserAvatar());
				preference.setUserEmail(result.getData().getDataFields().getUserName());
				preference.setLat(result.getData().getDataFields().getLat());
				preference.setLng(result.getData().getDataFields().getLng());
				preference.setPersonalIntroduction(result.getData().getDataFields().getPersonalIntroduction());
				preference.setPhoneNumber(result.getData().getDataFields().getPhoneNumber());
				preference.setSex(result.getData().getDataFields().getSex());
				sharePreference.setUserPreference(preference);
				setResult(1,null);
				LoginActivity.this.finish();
			}
			else
			{
				Log.d("LoginActivity","µÇÂ½Ê§°Ü");
			}
		}
	}
	
	public class GetSellerMsgTask extends AsyncTask<Void, Void, SellerMsgJson>
	{
		private String sellerName;
		
		public GetSellerMsgTask(String sellerName)
		{
			this.sellerName=sellerName;
		}

		@Override
		protected SellerMsgJson doInBackground(Void... params) {
			SellerMsgJson json=null;
			try
			{
				json=HttpLoaderMethods.getSellerData(sellerName);
			}catch(Throwable e)
			{
				e.printStackTrace();
			}
			
			return json;
		}

		@Override
		protected void onPostExecute(SellerMsgJson result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result!=null&&result.getCode()==1000)
			{
				sharePreference.setLoginState(UserLoginPreference.SELLER_LOGIN);
				SellerPreference preference=sharePreference.getSellerPreference();
				preference.setSellerLogoPath(result.getData().getSellerLogoPaht());
				preference.setSellerName(result.getData().getSellerName());
				preference.setSellerEmail(result.getData().getUserName());
				preference.setLat(result.getData().getLat());
				preference.setLng(result.getData().getLng());
				preference.setSellerAddress(result.getData().getSellerAddress());
				preference.setSellerPhoneNumber(result.getData().getPhoneNubmer());
				sharePreference.setSellerPreference(preference);

				setResult(2,null);
				LoginActivity.this.finish();
			}
			else
			{
				Log.d("LoginActivity","µÇÂ½Ê§°Ü");
			}
		}
	}
	
	
}
