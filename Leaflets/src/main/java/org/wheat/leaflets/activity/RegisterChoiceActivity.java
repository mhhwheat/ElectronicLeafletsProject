
package org.wheat.leaflets.activity;

import org.wheat.leaflets.R;
import org.wheat.leaflets.basic.ExitApplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

/** 
 * description:
 * @author wheat
 * date: 2015-3-4  
 * time: ÏÂÎç4:37:16
 */
public class RegisterChoiceActivity extends Activity 
{

	private Button btRegisterUser;
	private Button btRegisterSeller;

	private ImageView ivTitleBack;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_register_choice);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_register_choice_title);
		
		btRegisterUser=(Button)findViewById(R.id.btRegister_choice_user);
		btRegisterSeller=(Button)findViewById(R.id.btRegister_choice_seller);
		
		btRegisterUser.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(RegisterChoiceActivity.this,RegisterActivity.class);
				startActivity(intent);
			}
		});
		
		btRegisterSeller.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(RegisterChoiceActivity.this,RegisterSellerActivity.class);
				startActivity(intent);
			}
		});
		initialTitle();
		
		ExitApplication.getInstance().addActivity(this);
	}

	private void initialTitle()
	{
		ivTitleBack=(ImageView)findViewById(R.id.register_choice_title_back_img);
		ivTitleBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				RegisterChoiceActivity.this.finish();
			}
		});
	}
	
}
