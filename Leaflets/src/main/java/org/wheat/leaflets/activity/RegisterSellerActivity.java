 package org.wheat.leaflets.activity;

import org.wheat.leaflets.R;
import org.wheat.leaflets.basic.ExitApplication;
import org.wheat.leaflets.entity.json.RegisterSellerMsgJson;
import org.wheat.leaflets.loader.LoginAndRegister;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/** 
 * description:
 * @author wheat
 * date: 2015-3-4  
 * time: ����4:35:36
 */
public class RegisterSellerActivity extends Activity
{
	private EditText etRegisterEmail;
	private EditText etRegisterPassword;
	private EditText etRegisterSellerName;
	private Button btRegister;
	private ImageView ivTitleBack;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_register_seller);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_register_seller_title);
		
		etRegisterEmail=(EditText)findViewById(R.id.etRegister_seller_Email);
		etRegisterPassword=(EditText)findViewById(R.id.etRegister_seller_password);
		etRegisterSellerName=(EditText)findViewById(R.id.etRegister_seller_name);
		btRegister=(Button)findViewById(R.id.btRegister_seller);
		
		btRegister.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new RegisterSellerTask(etRegisterEmail.getText().toString(), etRegisterPassword.getText().toString(),etRegisterSellerName.getText().toString()).execute();
			}
		});

		initialTitle();
		
		ExitApplication.getInstance().addActivity(this);
	}

	private void initialTitle()
	{
		ivTitleBack=(ImageView)findViewById(R.id.register_seller_title_back_img);
		ivTitleBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				RegisterSellerActivity.this.finish();
			}
		});
	}
	
	private class RegisterSellerTask extends AsyncTask<Void, Void, RegisterSellerMsgJson>
	{
		private String strEmail;
		private String strPassword;
		private String strSellerName;
		
		public RegisterSellerTask(String email,String password,String sellerName)
		{
			this.strEmail=email;
			this.strPassword=password;
			this.strSellerName=sellerName;
		}

		@Override
		protected RegisterSellerMsgJson doInBackground(Void... params) {
			RegisterSellerMsgJson registerSellerMsgJson=null;
			try {
				registerSellerMsgJson=LoginAndRegister.synRegisterSeller(strEmail, strPassword,strSellerName);
			} catch (Throwable e) {
				e.printStackTrace();
			}
			return registerSellerMsgJson;
		}

		@Override
		protected void onPostExecute(RegisterSellerMsgJson result) {
			super.onPostExecute(result);
			if(result==null)
			{
				Log.d("RegisterActivity", "RegisterMsgJson is null!");
			}
				
			else
			{
				if(result.getCode()==1000)
				{
					Toast toast=Toast.makeText(RegisterSellerActivity.this, "ע��ɹ�", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
				else
				{
					Toast toast=Toast.makeText(RegisterSellerActivity.this, "ע��ʧ��", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
			}
		}
	}
}
