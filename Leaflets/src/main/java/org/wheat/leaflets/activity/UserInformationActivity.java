package org.wheat.leaflets.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
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

import org.wheat.leaflets.R;
import org.wheat.leaflets.basic.ExitApplication;
import org.wheat.leaflets.data.UserLoginPreference;
import org.wheat.leaflets.entity.PhotoParameters;
import org.wheat.leaflets.entity.json.UserMsgJson;
import org.wheat.leaflets.loader.HttpLoaderMethods;
import org.wheat.leaflets.loader.HttpUploadMethods;
import org.wheat.leaflets.loader.ImageLoader;

/**
 * Created by Administrator on 2015/4/22.
 */
public class UserInformationActivity extends Activity
{
    private UserLoginPreference mPreference;
    private LayoutInflater mInflater;
    private ImageLoader mImageLoader;

    private ImageView ivAvatar;
    private TextView tvUserSex;
    private EditText etUserNickName;
    private EditText etPersonalIntroduction;

    private ImageView ivTitleBack;
    private TextView tvTitleSave;

    private PopupWindow pwSelectSex;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_user_information);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_user_information_title);

        mPreference=UserLoginPreference.getInstance(getApplicationContext());
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

    private void initial()
    {
        mImageLoader.addTask(new PhotoParameters(mPreference.getUserPreference().getUserAvatar(),80,80,"user_portrait"), ivAvatar);
        etUserNickName.setHint(mPreference.getUserPreference().getNickName());
        etPersonalIntroduction.setText(mPreference.getUserPreference().getPersonalIntroduction());
        initialPopupWindow();
        initialListener();
    }

    private void initialListener()
    {
        tvUserSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pwSelectSex.isShowing())
                {
                    pwSelectSex.dismiss();
                }
                else
                {
                    pwSelectSex.showAtLocation(v,Gravity.CENTER,0,0);
                }
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

            }
        });

        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initialPopupWindow( )
    {
        View view=mInflater.inflate(R.layout.activity_user_information_sex_pw,null);
        pwSelectSex=new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT,true);

        // 设置允许在外点击消失
        pwSelectSex.setOutsideTouchable(true);

        // 使其聚集 ，要想监听菜单里控件的事件就必须要调用此方法
        pwSelectSex.setFocusable(true);

        //如果需要PopupWindow响应返回键，那么必须给PopupWindow设置一个背景才行
        ColorDrawable dw = new ColorDrawable(0X50000000);
        pwSelectSex.setBackgroundDrawable(dw);
    }

    private class UpdateUserInformationTask extends AsyncTask<Void,Void,Void>
    {
        private UserMsgJson userMsg;

        public  UpdateUserInformationTask(UserMsgJson userMsg)
        {
            this.userMsg=userMsg;
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            int returnCode= HttpUploadMethods.setUserData(userMsg);
            if(returnCode!=200)
            {
                Log.d("UserInformationActivity",String.valueOf(returnCode));
            }
            return null;
        }
    }

//    private class GetUserInformationTask extends AsyncTask<Void,Void,UserMsgJson>
//    {
//        private String userEmail;
//
//        public GetUserInformationTask(String userEmail)
//        {
//            this.userEmail=userEmail;
//        }
//
//        @Override
//        protected UserMsgJson doInBackground(Void... params) {
//            UserMsgJson userMsg=null;
//            try {
//                userMsg = HttpLoaderMethods.getUserData(this.userEmail);
//            } catch (Throwable throwable) {
//                throwable.printStackTrace();
//            }
//            return userMsg;
//        }
//
//        @Override
//        protected void onPostExecute(UserMsgJson userMsgJson) {
//            super.onPostExecute(userMsgJson);
//
//            if(userMsgJson!=null&&userMsgJson.getCode()==1000)
//            {
//
//            }
//        }
//    }
}
