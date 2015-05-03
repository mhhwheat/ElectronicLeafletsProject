package org.wheat.leaflets.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.wheat.leaflets.R;
import org.wheat.leaflets.basic.FileUtils;
import org.wheat.leaflets.basic.ImageUtils;
import org.wheat.leaflets.basic.StringUtils;
import org.wheat.leaflets.data.UserLoginPreference;
import org.wheat.leaflets.entity.ConstantValue;
import org.wheat.leaflets.entity.PhotoParameters;
import org.wheat.leaflets.entity.SellerMsgFields;
import org.wheat.leaflets.entity.SellerPreference;
import org.wheat.leaflets.entity.json.SellerUpdateMsgJson;
import org.wheat.leaflets.loader.HttpUploadMethods;
import org.wheat.leaflets.loader.ImageLoader;

import java.io.File;
import java.util.Date;

/**
 * Created by Administrator on 2015/5/3.
 */
public class SellerMessageEditorActivity extends Activity
{
    private UserLoginPreference mPreference;
    private ImageLoader mImageLoader;
    private SellerPreference sellerPreference;
    private SellerMsgFields sellerMsgFields;

    private ImageView ivSellerLogo;
    private EditText etSellerName;
    private EditText etSellerPhone;
    private EditText etSellerMail;
    private EditText etSellerAddress;
    private ImageView ivLocationAddress;
    private EditText etSellerProfile;

    private ImageView ivTitleBack;
    private TextView tvTitleSave;

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
        setContentView(R.layout.activity_seller_msg_editor);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.activity_seller_msg_title);

        mImageLoader=ImageLoader.getInstance(SellerMessageEditorActivity.this);
        getSellerMsgFromPreference();
        initialViewItem();
        initialTitle();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP:
                if(resultCode!=RESULT_OK)
                    return;
                startActionCrop(data.getData());// 选图后裁剪
                break;
            case ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD:
                protraitBitmap=ImageUtils.loadImgThumbnail(protraitPath,200,200);
                ivSellerLogo.setImageBitmap(protraitBitmap);
                break;
            case 3:
                if(resultCode==0)
                    return;
                else if(resultCode==1)
                {
                    sellerMsgFields.setSellerLat(data.getDoubleExtra("lat",sellerPreference.getSellerLat()));
                    sellerMsgFields.setSellerLng(data.getDoubleExtra("lng", sellerPreference.getSellerLng()));
                    this.sellerLat=data.getDoubleExtra("lat",sellerPreference.getSellerLat());
                    this.sellerLng=data.getDoubleExtra("lng", sellerPreference.getSellerLng());
                }
                break;
            default:
                break;

        }
    }

    /**
     * 从本地的preference中去除Seller的信息
     */
    private void getSellerMsgFromPreference()
    {
        mPreference=UserLoginPreference.getInstance(getApplicationContext());
        sellerPreference=mPreference.getSellerPreference();
        sellerMsgFields=new SellerMsgFields();
        sellerMsgFields.setSellerLng(sellerPreference.getSellerLng());
        sellerMsgFields.setSellerLat(sellerPreference.getSellerLat());
        sellerMsgFields.setEmail(sellerPreference.getSellerEmail());
        sellerMsgFields.setLat(sellerPreference.getLat());
        sellerMsgFields.setLng(sellerPreference.getLng());
        sellerMsgFields.setPhoneNubmer(sellerPreference.getSellerPhoneNumber());
        sellerMsgFields.setProfile(sellerPreference.getProfile());
        sellerMsgFields.setSellerAddress(sellerPreference.getSellerAddress());
        sellerMsgFields.setSellerLogoPath(sellerPreference.getSellerLogoPath());
        sellerMsgFields.setSellerName(sellerPreference.getSellerName());
        sellerMsgFields.setUserName(sellerPreference.getSellerEmail());

    }

    private void initialViewItem()
    {
        ivSellerLogo=(ImageView)findViewById(R.id.seller_msg_edit_logo);
        etSellerName=(EditText)findViewById(R.id.seller_msg_edit_name);
        etSellerPhone=(EditText)findViewById(R.id.seller_msg_edit_tel);
        etSellerMail=(EditText)findViewById(R.id.seller_msg_edit_mail);
        etSellerAddress=(EditText)findViewById(R.id.seller_msg_edit_address);
        ivLocationAddress=(ImageView)findViewById(R.id.seller_msg_edit_location);
        etSellerProfile=(EditText)findViewById(R.id.seller_msg_edit_profile);

        mImageLoader.addTask(new PhotoParameters(sellerPreference.getSellerLogoPath(), 80, 80, "seller_logo"), ivSellerLogo);
        etSellerName.setHint(sellerPreference.getSellerName());
        if(sellerPreference.getSellerPhoneNumber()==null||sellerPreference.getSellerPhoneNumber().trim().equals(""))
        {
            etSellerPhone.setHint("联系电话");
        }
        else
        {
            etSellerPhone.setHint(sellerPreference.getSellerPhoneNumber());
        }
        if(sellerPreference.getSellerEmail()==null||sellerPreference.getSellerEmail().trim().equals(""))
        {
            etSellerMail.setHint("邮箱");
        }else
        {
            etSellerMail.setHint(sellerPreference.getSellerEmail());
        }
        etSellerAddress.setHint(sellerPreference.getSellerAddress());
        if(sellerPreference.getProfile()==null||sellerPreference.getProfile().trim().equals(""))
        {
            etSellerProfile.setHint("简介");
        }
        else {
            etSellerProfile.setText(sellerPreference.getProfile());
        }

        ivLocationAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerMessageEditorActivity.this, MapLocationActivity.class);
                startActivityForResult(intent, 3);
            }
        });

        ivSellerLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startImagePick();
            }
        });

    }

    private void initialTitle()
    {
        ivTitleBack=(ImageView)findViewById(R.id.seller_msg_editor_title_back_img);
        tvTitleSave=(TextView)findViewById(R.id.seller_msg_editor_title_save);

        ivTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SellerMessageEditorActivity.this.finish();
            }
        });

        tvTitleSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDataChange())
                {
                    new UploadSellerLogoTask().execute();
                }
            }
        });
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
            Toast.makeText(this, "无法保存上传的头像，请检查SD卡是否挂载", Toast.LENGTH_SHORT).show();
            return null;
        }
        String thePath = ImageUtils.getAbsolutePathFromNoStandardUri(uri);

        String timeStamp=String.valueOf((new Date()).getTime());

        // 如果是标准Uri
        if (StringUtils.isEmpty(thePath)) {
            thePath = ImageUtils.getAbsoluteImagePath(this, uri);
        }
        String ext = FileUtils.getFileFormat(thePath);
        ext = StringUtils.isEmpty(ext) ? "jpg" : ext;
        // 照片命名
        String cropFileName = sellerPreference.getSellerEmail()+ timeStamp + "." + ext;
        // 裁剪头像的绝对路径
        protraitPath = FILE_SAVE_PATH + cropFileName;
        protraitFile = new File(protraitPath);

        cropUri = Uri.fromFile(protraitFile);
        return this.cropUri;
    }

    private boolean isDataChange()
    {
        boolean change=false;
        if(protraitFile!=null&&!protraitFile.getName().equals(sellerPreference.getSellerLogoPath()))
        {
            change=true;
            sellerMsgFields.setSellerLogoPath(protraitFile.getName());
        }

        String sellerName=etSellerName.getText().toString();
        if(!sellerName.trim().equals("")&&!sellerName.equals(sellerPreference.getSellerName()))
        {
            if(!change)
                change=true;
            sellerMsgFields.setSellerName(sellerName);
        }

        String sellerTel=etSellerPhone.getText().toString();
        if(!sellerTel.trim().equals("")&&!sellerTel.equals(sellerPreference.getSellerPhoneNumber()))
        {
            if(!change)
                change=true;
            sellerMsgFields.setPhoneNubmer(sellerTel);
        }

        String sellerMail=etSellerMail.getText().toString();
        if(!sellerMail.trim().equals("")&&!sellerMail.equals(sellerPreference.getSellerEmail()))
        {
            if(!change)
                change=true;
            sellerMsgFields.setEmail(sellerMail);
        }

        String sellerAddress=etSellerAddress.getText().toString();
        if(!sellerAddress.trim().equals("")&&!sellerAddress.equals(sellerPreference.getSellerAddress()))
        {
            if(!change)
                change=true;
            sellerMsgFields.setSellerAddress(sellerAddress);
        }

        String sellerProfile=etSellerProfile.getText().toString();
        if(!sellerProfile.trim().equals("")&&!sellerProfile.equals(sellerPreference.getProfile()))
        {
            if(!change)
                change=true;
            sellerMsgFields.setProfile(sellerProfile);
        }

        if(sellerLat!=Double.MAX_VALUE&&sellerLng!=Double.MAX_VALUE)
        {
            if(!change)
                change=true;
            sellerMsgFields.setSellerLat(sellerLat);
            sellerMsgFields.setSellerLng(sellerLng);
        }

        return change;
    }

    private class UploadSellerLogoTask extends AsyncTask<Void,Void,Integer>
    {
        @Override
        protected Integer doInBackground(Void... params) {
            int returnCode= -1;
            try
            {
              returnCode= HttpUploadMethods.uploadPhoto(protraitFile,protraitFile.getName(),"seller_logo");
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            return returnCode;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            switch (integer.intValue())
            {
                case ConstantValue.uploadPhotoFailed:
                    Toast.makeText(SellerMessageEditorActivity.this,"上传图片失败",Toast.LENGTH_SHORT).show();
                    break;
                case ConstantValue.operateSuccess:
                    new UpdateSellerMsgTask().execute();
                    break;
                default:
                    break;
            }
        }
    }

    private class UpdateSellerMsgTask extends AsyncTask<Void,Void,Integer>
    {

        @Override
        protected Integer doInBackground(Void... params) {
            SellerUpdateMsgJson sellerMsg=new SellerUpdateMsgJson();
            sellerMsg.setData(sellerMsgFields);
            int returnCode=HttpUploadMethods.setSellerData(sellerMsg);
            return returnCode;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            switch (integer.intValue())
            {
                case 200:
                    sellerPreference.setSellerLogoPath(sellerMsgFields.getSellerLogoPath());
                    sellerPreference.setSellerName(sellerMsgFields.getSellerName());
                    sellerPreference.setSellerPhoneNumber(sellerMsgFields.getPhoneNubmer());
                    sellerPreference.setSellerEmail(sellerMsgFields.getEmail());
                    sellerPreference.setSellerAddress(sellerMsgFields.getSellerAddress());
                    sellerPreference.setProfile(sellerMsgFields.getProfile());
                    sellerPreference.setSellerLat(sellerMsgFields.getSellerLat());
                    sellerPreference.setSellerLng(sellerMsgFields.getSellerLng());
                    mPreference.setSellerPreference(sellerPreference);
                    break;
                default:
                    Toast.makeText(SellerMessageEditorActivity.this,"更新商家信息失败",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

}
