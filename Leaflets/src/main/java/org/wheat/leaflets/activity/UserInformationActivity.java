package org.wheat.leaflets.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
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

/**
 * Created by Administrator on 2015/4/22.
 */
public class UserInformationActivity extends Activity
{
    private LayoutInflater mInflater;

    private ImageView ivAvatar;
    private TextView tvUserSex;
    private EditText etUserNickName;
    private EditText etPersonalIntroduction;

    private ImageView ivTitleBack;
    private TextView tvTitleSava;

    private PopupWindow pwSelectSex;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_user_information);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_user_information_title);

        mInflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ivAvatar=(ImageView)findViewById(R.id.user_info_user_avatar);
        tvUserSex=(TextView)findViewById(R.id.user_infomation_sex);
        etUserNickName=(EditText)findViewById(R.id.user_information_nickname);
        etPersonalIntroduction=(EditText)findViewById(R.id.user_information_personal_introduction);

        ivTitleBack=(ImageView)findViewById(R.id.user_information_title_back_img);
        tvTitleSava=(TextView)findViewById(R.id.user_information_title_save);

        ExitApplication.getInstance().addActivity(UserInformationActivity.this);

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

        tvTitleSava.setOnClickListener(new View.OnClickListener() {
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

        // ����������������ʧ
        pwSelectSex.setOutsideTouchable(true);

        // ʹ��ۼ� ��Ҫ������˵���ؼ����¼��ͱ���Ҫ���ô˷���
        pwSelectSex.setFocusable(true);

        //�����ҪPopupWindow��Ӧ���ؼ�����ô�����PopupWindow����һ����������
        ColorDrawable dw = new ColorDrawable(0X50000000);
        pwSelectSex.setBackgroundDrawable(dw);
    }

    private class UpdateUserInformationTask extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... params)
        {
            return null;
        }
    }
}
