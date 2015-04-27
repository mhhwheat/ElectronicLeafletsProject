package org.wheat.leaflets.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.wheat.leaflets.R;
import org.wheat.leaflets.basic.ExitApplication;
import org.wheat.leaflets.basic.FileUtils;
import org.wheat.leaflets.basic.ImageUtils;
import org.wheat.leaflets.basic.StringUtils;
import org.wheat.leaflets.data.UserLoginPreference;
import org.wheat.leaflets.entity.ConstantValue;
import org.wheat.leaflets.entity.PhotoParameters;
import org.wheat.leaflets.entity.UserMsgFields;
import org.wheat.leaflets.entity.UserPreference;
import org.wheat.leaflets.entity.json.UserUpdateMsgJson;
import org.wheat.leaflets.loader.HttpUploadMethods;
import org.wheat.leaflets.loader.ImageLoader;
import org.wheat.leaflets.widget.LoadingDialog;

import java.io.File;
import java.util.Date;

/**
 * Created by Administrator on 2015/4/22.
 */
public class UserInformationActivity extends Activity
{
    private UserLoginPreference mPreference;
    private UserPreference userPreference;
    private LayoutInflater mInflater;
    private ImageLoader mImageLoader;

    private ImageView ivAvatar;
    private TextView tvUserSex;
    private EditText etUserNickName;
    private EditText etPersonalIntroduction;

    private ImageView ivTitleBack;
    private TextView tvTitleSave;

    private Dialog mSelectSexDialog;
    private LoadingDialog loadingDialog;

    private final static int CROP = 200;
    private final static String FILE_SAVE_PATH = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/ElectronicLeaflets/Portrait/";

    private Uri cropUri;
    private File protraitFile;
    private Bitmap protraitBitmap;
    private String protraitPath;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_user_information);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_user_information_title);

        mPreference=UserLoginPreference.getInstance(getApplicationContext());
        userPreference = mPreference.getUserPreference();
        mInflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mImageLoader=ImageLoader.getInstance(this);

        ivAvatar=(ImageView)findViewById(R.id.user_info_user_avatar);
        tvUserSex=(TextView)findViewById(R.id.user_infomation_sex);
        etUserNickName=(EditText)findViewById(R.id.user_information_nickname);
        etPersonalIntroduction=(EditText)findViewById(R.id.user_information_personal_introduction);

        ivTitleBack=(ImageView)findViewById(R.id.user_information_title_back_img);
        tvTitleSave=(TextView)findViewById(R.id.user_information_title_save);

        initial();

        ExitApplication.getInstance().addActivity(UserInformationActivity.this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;

        switch (requestCode)
        {
            case ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP:
                startActionCrop(data.getData());// 选图后裁剪
                break;
            case ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD:
                protraitBitmap=ImageUtils.loadImgThumbnail(protraitPath,200,200);
                ivAvatar.setImageBitmap(protraitBitmap);
                break;
        }
    }

    private void initial()
    {
        mImageLoader.addTask(new PhotoParameters(mPreference.getUserPreference().getUserAvatar(), 80, 80, "user_portrait"), ivAvatar);
        etUserNickName.setHint(mPreference.getUserPreference().getNickName());
        etPersonalIntroduction.setText(mPreference.getUserPreference().getPersonalIntroduction());
        String sex=mPreference.getUserPreference().getSex();
        if(sex!=null&&!sex.equals(""))
        {
            tvUserSex.setText(mPreference.getUserPreference().getSex());
        }
        initialDialog();
        initialListener();
    }

    private void initialListener()
    {
        tvUserSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectSexDialog.show();
            }
        });

        ivTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInformationActivity.this.finish();
            }
        });

        tvTitleSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDataChange()) {
                    loadingDialog = new LoadingDialog(UserInformationActivity.this);
                    new UpdateProtraitTask().execute();
                    loadingDialog.show();
                }else {
                    UserInformationActivity.this.finish();
                }

            }
        });

        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startImagePick();
            }
        });
    }

    private void initialDialog()
    {
        mSelectSexDialog=new Dialog(UserInformationActivity.this, R.style.ExitDialog);
        View layout=mInflater.inflate( R.layout.activity_user_infromation_sex_dialog, null,false);
        TextView tvMale=(TextView)layout.findViewById(R.id.sex_dialog_male);
        TextView tvFemale=(TextView)layout.findViewById(R.id.sex_dialog_female);

        tvMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvUserSex.setText("男");
                mSelectSexDialog.cancel();
            }
        });

        tvFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvUserSex.setText("女");
                mSelectSexDialog.cancel();
            }
        });

        mSelectSexDialog.setContentView(layout);
    }

    private boolean isDataChange()
    {
        UserPreference user=mPreference.getUserPreference();

        if(protraitFile!=null&&!protraitFile.getName().equals(user.getUserAvatar()))
        {
            return true;
        }

        if(!user.getPersonalIntroduction().equals(etPersonalIntroduction.getText().toString()))
        {
            return true;
        }

        if(!user.getSex().equals(tvUserSex.getText().toString()))
        {
            return  true;
        }

        return false;
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
            Toast.makeText(UserInformationActivity.this,"无法保存上传的头像，请检查SD卡是否挂载",Toast.LENGTH_SHORT).show();
            return null;
        }
//        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
//                .format(new Date());
        String thePath = ImageUtils.getAbsolutePathFromNoStandardUri(uri);

        String timeStamp=String.valueOf((new Date()).getTime());

        // 如果是标准Uri
        if (StringUtils.isEmpty(thePath)) {
            thePath = ImageUtils.getAbsoluteImagePath(UserInformationActivity.this, uri);
        }
        String ext = FileUtils.getFileFormat(thePath);
        ext = StringUtils.isEmpty(ext) ? "jpg" : ext;
        // 照片命名
        String cropFileName = mPreference.getUserPreference().getUserEmail() + timeStamp + "." + ext;
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
            if(protraitFile==null||protraitFile.getName().equals(mPreference.getUserPreference().getUserAvatar())) {
                return ConstantValue.operateSuccess;
            }

            try {
                returnCode=HttpUploadMethods.uploadPhoto(protraitFile,protraitFile.getName(),"portrait");
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
                    UserUpdateMsgJson userMsg=new UserUpdateMsgJson();
                    UserMsgFields fields = new UserMsgFields();

                    //更新SharePreference的内容
                    if(protraitFile==null) {
                        fields.setUserAvatar(userPreference.getUserAvatar());
                    }
                    else
                    {
                        fields.setUserAvatar(protraitFile.getName());
                    }
                    if (etUserNickName.getText().toString().trim().equals("")||userPreference.getNickName().equals(etUserNickName.getText().toString())) {
                        fields.setNickName(userPreference.getNickName());
                    }
                    else
                    {
                        fields.setNickName(etUserNickName.getText().toString());
                    }
                    fields.setSex(tvUserSex.getText().toString());

                    if (etPersonalIntroduction.getText().toString().trim().equals("") || userPreference.getPersonalIntroduction().equals(etUserNickName.getText().toString())) {
                        fields.setPersonalIntroduction(userPreference.getPersonalIntroduction());
                    }
                    else
                    {
                        fields.setPersonalIntroduction(etPersonalIntroduction.getText().toString());
                    }
                    fields.setEmail(userPreference.getUserEmail());
                    fields.setLng(mPreference.getLocationLng());
                    fields.setLat(mPreference.getLocationLat());
                    fields.setPhoneNumber(userPreference.getPhoneNumber());
                    fields.setUserName(userPreference.getUserEmail());

                    userMsg.setData(fields);
                    new UpdateUserInformationTask(userMsg).execute();
                    break;
                case ConstantValue.uploadPhotoFailed:
                    Toast.makeText(UserInformationActivity.this,"上传图片失败",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }


    private class UpdateUserInformationTask extends AsyncTask<Void,Void,Integer>
    {
        private UserUpdateMsgJson userMsg;

        public  UpdateUserInformationTask(UserUpdateMsgJson userMsg)
        {
            this.userMsg=userMsg;
        }

        @Override
        protected Integer doInBackground(Void... params)
        {
            int returnCode= HttpUploadMethods.setUserData(userMsg);
            if(returnCode!=200)
            {
                Log.d("UserInformationActivity",String.valueOf(returnCode));
            }
            return returnCode;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            switch (result)
            {
                case 200:
                    userPreference.setNickName(userMsg.getData().getNickName());
                    userPreference.setPersonalIntroduction(userMsg.getData().getPersonalIntroduction());
                    userPreference.setSex(userMsg.getData().getSex());
                    userPreference.setUserAvatar(userMsg.getData().getUserAvatar());
                    mPreference.setUserPreference(userPreference);

                    loadingDialog.dismiss();
                    UserInformationActivity.this.finish();
                    break;
                default:
                    loadingDialog.dismiss();
                    Toast.makeText(UserInformationActivity.this, "更新用户信息失败", Toast.LENGTH_LONG);
                    UserInformationActivity.this.finish();
                    break;
            }
        }
    }

//    private class GetUserInformationTask extends AsyncTask<Void,Void,UserUpdateMsgJson>
//    {
//        private String userEmail;
//
//        public GetUserInformationTask(String userEmail)
//        {
//            this.userEmail=userEmail;
//        }
//
//        @Override
//        protected UserUpdateMsgJson doInBackground(Void... params) {
//            UserUpdateMsgJson userMsg=null;
//            try {
//                userMsg = HttpLoaderMethods.getUserData(this.userEmail);
//            } catch (Throwable throwable) {
//                throwable.printStackTrace();
//            }
//            return userMsg;
//        }
//
//        @Override
//        protected void onPostExecute(UserUpdateMsgJson UserUpdateMsgJson) {
//            super.onPostExecute(UserUpdateMsgJson);
//
//            if(UserUpdateMsgJson!=null&&UserUpdateMsgJson.getCode()==1000)
//            {
//
//            }
//        }
//    }
}
