 package org.wheat.leaflets.activity;

import org.wheat.leaflets.R;
import org.wheat.leaflets.basic.ExitApplication;
import org.wheat.leaflets.basic.FileUtils;
import org.wheat.leaflets.basic.ImageUtils;
import org.wheat.leaflets.basic.StringUtils;
import org.wheat.leaflets.entity.ConstantValue;
import org.wheat.leaflets.entity.RegisterSellerMsg;
import org.wheat.leaflets.entity.UserMsgFields;
import org.wheat.leaflets.entity.json.RegisterSellerMsgJson;
import org.wheat.leaflets.entity.json.UserUpdateMsgJson;
import org.wheat.leaflets.loader.HttpUploadMethods;
import org.wheat.leaflets.loader.LoginAndRegister;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.Date;

 /**
 * description:
 * @author wheat
 * date: 2015-3-4  
 * time: 下午4:35:36
 */
public class RegisterSellerActivity extends Activity
{
	private EditText etRegisterEmail;
	private EditText etRegisterPassword;
	private EditText etRegisterSellerName;
	private Button btRegister;
	private ImageView ivTitleBack;
	private ImageView btMarkAddress;
	private EditText etAddress;
	private ImageView ivSellerLogo;

	private double sellerLat=Double.MAX_VALUE;
	private double sellerLng=Double.MAX_VALUE;

	private final static int CROP = 200;
	private final static String FILE_SAVE_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ "/ElectronicLeaflets/Portrait/";

	private Uri cropUri;
	private File protraitFile;
	private Bitmap protraitBitmap;
	private String protraitPath;
	
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
		btMarkAddress=(ImageView)findViewById(R.id.btRegister_seller_mark_address);
		etAddress=(EditText)findViewById(R.id.etRegister_seller_address);
		ivSellerLogo=(ImageView)findViewById(R.id.ivRegister_seller_logo);

		
		btRegister.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(checkItem())
					new UpdateProtraitTask().execute();
			}
		});

		btMarkAddress.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(RegisterSellerActivity.this,MapLocationActivity.class);
				startActivityForResult(intent,3);
			}
		});

		ivSellerLogo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startImagePick();
			}
		});

		initialTitle();
		
		ExitApplication.getInstance().addActivity(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==3&&resultCode==1&&data!=null)
		{
			sellerLat=data.getDoubleExtra("lat",Double.MAX_VALUE);
			sellerLng=data.getDoubleExtra("lng",Double.MAX_VALUE);
		}

		switch (requestCode)
		{
			case ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP:
				startActionCrop(data.getData());// 选图后裁剪
				break;
			case ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD:
				protraitBitmap=ImageUtils.loadImgThumbnail(protraitPath,200,200);
				ivSellerLogo.setImageBitmap(protraitBitmap);
				break;
		}
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

	private boolean checkItem()
	{
		if(etRegisterEmail.getText().toString().trim().equals(""))
		{
			Toast.makeText(RegisterSellerActivity.this,"邮箱不能为空",Toast.LENGTH_LONG).show();
			return false;
		}

		if(etRegisterPassword.getText().toString().trim().equals(""))
		{
			Toast.makeText(RegisterSellerActivity.this,"密码不能为空",Toast.LENGTH_LONG).show();
			return false;
		}

		if(etRegisterSellerName.getText().toString().trim().equals(""))
		{
			Toast.makeText(RegisterSellerActivity.this,"店名不能为空",Toast.LENGTH_LONG).show();
			return false;
		}

		if(etAddress.getText().toString().trim().equals(""))
		{
			Toast.makeText(RegisterSellerActivity.this,"商店地址不能为空",Toast.LENGTH_LONG).show();
			return false;
		}

		if(sellerLat==Double.MAX_VALUE||sellerLng==Double.MAX_VALUE)
		{
			Toast.makeText(RegisterSellerActivity.this,"必须标记商店的地址",Toast.LENGTH_LONG).show();
			return false;
		}

		if(protraitFile==null)
		{
			Toast.makeText(RegisterSellerActivity.this,"必须上传商店的logo",Toast.LENGTH_LONG).show();
			return false;
		}

		return true;

	}

	/**
	 * 选择图片裁剪
	 *
	 */
	private void startImagePick() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("image/*");
		startActivityForResult(Intent.createChooser(intent, "选择图片"), ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP);
	}

	/**
	 * 拍照后裁剪
	 *
	 * @param data
	 *            原始图片
	 */
	private void startActionCrop(Uri data) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(data, "image/*");
		intent.putExtra("output", this.getUploadTempFile(data));
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);// 裁剪框比例
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", CROP);// 输出图片大小
		intent.putExtra("outputY", CROP);
		intent.putExtra("scale", true);// 去黑边
		intent.putExtra("scaleUpIfNeeded", true);// 去黑边
		startActivityForResult(intent, ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD);
	}

	private Uri getUploadTempFile(Uri uri)
	{
		String storageState = Environment.getExternalStorageState();
		if (storageState.equals(Environment.MEDIA_MOUNTED)) {
			File savedir = new File(FILE_SAVE_PATH);
			if (!savedir.exists()) {
				savedir.mkdirs();
			}
		} else {
			Toast.makeText(RegisterSellerActivity.this,"无法保存上传的头像，请检查SD卡是否挂载",Toast.LENGTH_SHORT).show();
			return null;
		}
		String thePath = ImageUtils.getAbsolutePathFromNoStandardUri(uri);

		String timeStamp=String.valueOf((new Date()).getTime());

		// 如果是标准Uri
		if (StringUtils.isEmpty(thePath)) {
			thePath = ImageUtils.getAbsoluteImagePath(RegisterSellerActivity.this, uri);
		}
		String ext = FileUtils.getFileFormat(thePath);
		ext = StringUtils.isEmpty(ext) ? "jpg" : ext;
		// 照片命名
		String cropFileName =timeStamp + "." + ext;
		// 裁剪头像的绝对路径
		protraitPath = FILE_SAVE_PATH + cropFileName;
		protraitFile = new File(protraitPath);

		cropUri = Uri.fromFile(protraitFile);
		return this.cropUri;
	}

	private class UpdateProtraitTask extends AsyncTask<Void,Void,Integer>
	{
		@Override
		protected Integer doInBackground(Void... params) {
			int returnCode=-1;
			try {
				returnCode= HttpUploadMethods.uploadPhoto(protraitFile, protraitFile.getName(), "portrait");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return returnCode;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			switch (result.intValue())
			{
				case ConstantValue.operateSuccess:
					RegisterSellerMsg registerSellerMsg=new RegisterSellerMsg();
					registerSellerMsg.setEmail(etRegisterEmail.getText().toString());
					registerSellerMsg.setPassword(etRegisterPassword.getText().toString());
					registerSellerMsg.setSellerName(etRegisterSellerName.getText().toString());
					registerSellerMsg.setSellerLat(sellerLat);
					registerSellerMsg.setSellerLng(sellerLng);
					registerSellerMsg.setSellerAddress(etAddress.getText().toString());
					registerSellerMsg.setSellerLogo(etRegisterEmail.getText().toString()+protraitFile.getName());
					new RegisterSellerTask(registerSellerMsg).execute();
					break;
				case ConstantValue.uploadPhotoFailed:
					Toast.makeText(RegisterSellerActivity.this,"上传图片失败",Toast.LENGTH_LONG).show();
					break;
			}
		}
	}
	
	private class RegisterSellerTask extends AsyncTask<Void, Void, RegisterSellerMsgJson>
	{
		private RegisterSellerMsg sellerMsg;
		
		public RegisterSellerTask(RegisterSellerMsg sellerMsg)
		{
			this.sellerMsg=sellerMsg;
		}

		@Override
		protected RegisterSellerMsgJson doInBackground(Void... params) {
			RegisterSellerMsgJson registerSellerMsgJson=null;
			try {
				registerSellerMsgJson=LoginAndRegister.synRegisterSeller(sellerMsg);
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
					Toast toast=Toast.makeText(RegisterSellerActivity.this, "注册成功", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
				else
				{
					Toast toast=Toast.makeText(RegisterSellerActivity.this, "注册失败", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
			}
		}
	}
}
