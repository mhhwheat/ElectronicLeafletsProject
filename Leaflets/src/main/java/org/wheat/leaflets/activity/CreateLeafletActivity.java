package org.wheat.leaflets.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.wheat.leaflets.R;
import org.wheat.leaflets.basic.ExitApplication;
import org.wheat.leaflets.basic.FileUtils;
import org.wheat.leaflets.basic.ImageUtils;
import org.wheat.leaflets.basic.StringUtils;
import org.wheat.leaflets.basic.UTCtoLocal;
import org.wheat.leaflets.data.UserLoginPreference;
import org.wheat.leaflets.entity.ConstantValue;
import org.wheat.leaflets.entity.LeafletsFields;
import org.wheat.leaflets.entity.SellerPreference;
import org.wheat.leaflets.entity.json.LeafletUploadJson;
import org.wheat.leaflets.loader.HttpUploadMethods;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2015/4/23.
 */
public class CreateLeafletActivity extends Activity implements View.OnTouchListener
{
    private UserLoginPreference mPreference;

    private ImageView ivAddLeaflet;
    private Spinner spLeafletType;
    private EditText etLeafletDiscription;
    private EditText etStartTime;
    private EditText etEndTime;

    private ImageView ivTitleBack;
    private TextView tvButtonFinish;

    private File leafletFile;
    private Bitmap leafletBitmap;
    private String leafletFileName;

    private String[] leafletClassArray;
    private String[] leafletClassArrayReflect;
    private int spinnerSelectedPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_create_leaflet_layout);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_create_leaflet_title);

        mPreference=UserLoginPreference.getInstance(getApplicationContext());
        initialView();

        ExitApplication.getInstance().addActivity(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case 1:
                getUploadFile(data.getData());
                leafletBitmap=ImageUtils.getAdapterWidthBitmap(leafletFile,200);
                ViewGroup.LayoutParams params=ivAddLeaflet.getLayoutParams();
                float density=this.getResources().getDisplayMetrics().density;
                params.height=(int)(leafletBitmap.getHeight()/density);
                params.width=(int)(leafletBitmap.getWidth()/density);
                ivAddLeaflet.setLayoutParams(params);
                ivAddLeaflet.setImageBitmap(leafletBitmap);
                break;
        }
    }

    private void initialView()
    {
        ivTitleBack=(ImageView)findViewById(R.id.create_leaflet_title_back_img);
        tvButtonFinish=(TextView)findViewById(R.id.create_leaflet_title_save);

        leafletClassArray=getResources().getStringArray(R.array.spinner_leaflet_class);
        leafletClassArrayReflect=getResources().getStringArray(R.array.spinner_leaflet_class_reflect);

        ivAddLeaflet=(ImageView)findViewById(R.id.ivCreate_leaflet_add_leaflet);
        spLeafletType=(Spinner)findViewById(R.id.spCreate_leaflet_spinner);
        etLeafletDiscription=(EditText)findViewById(R.id.etCreate_leaflet_description);
        etStartTime=(EditText)findViewById(R.id.etCreate_leaflet_start_time);
        etEndTime=(EditText)findViewById(R.id.etCreate_leaflet_end_time);

        ivTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateLeafletActivity.this.finish();
            }
        });

        tvButtonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkItem())
                {
                    new UploadLeafletTask().execute();
                }
                else
                {
                    return;
                }
            }
        });

        ivAddLeaflet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startImagePick();
            }
        });

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,leafletClassArray);
        spLeafletType.setAdapter(adapter);

        spLeafletType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerSelectedPosition=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        etStartTime.setOnTouchListener(this);
        etEndTime.setOnTouchListener(this);
    }

    private void startImagePick()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "选择图片"), 1);
    }

    private void getUploadFile(Uri uri)
    {
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
        } else {
            Toast.makeText(CreateLeafletActivity.this, "无法保存上传的头像，请检查SD卡是否挂载", Toast.LENGTH_SHORT).show();
        }
        String thePath = ImageUtils.getAbsolutePathFromNoStandardUri(uri);
        String timeStamp=String.valueOf((new Date()).getTime());

        // 如果是标准Uri
        if (StringUtils.isEmpty(thePath)) {
            thePath = ImageUtils.getAbsoluteImagePath(CreateLeafletActivity.this, uri);
        }
        String ext = FileUtils.getFileFormat(thePath);
        ext = StringUtils.isEmpty(ext) ? "jpg" : ext;
        // 照片命名
        leafletFileName =mPreference.getSellerPreference().getSellerEmail()+timeStamp + "." + ext;
        leafletFile=new File(thePath);
    }

    private boolean checkItem()
    {
        if(leafletFile==null)
        {
            Toast.makeText(this,"请选择上传的传单",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(spinnerSelectedPosition==0)
        {
            Toast.makeText(this,"请选择传单的类型",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(etLeafletDiscription.getText().toString().equals(""))
        {
            Toast.makeText(this,"填写活动的详情",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(etStartTime.getText().toString().equals(""))
        {
            Toast.makeText(this,"请选择活动的开始时间",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(etEndTime.getText().toString().equals(""))
        {
            Toast.makeText(this,"请选择活动的结束时间",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN)
        {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            View view=View.inflate(this,R.layout.create_leaflet_date_time_dialog,null);
            final DatePicker datePicker=(DatePicker)view.findViewById(R.id.create_leaflet_date_picker);
            final TimePicker timePicker=(TimePicker)view.findViewById(R.id.create_leaflet_time_picker);

            builder.setView(view);
            Calendar cal=Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());
            datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);

            timePicker.setIs24HourView(true);
            timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
            timePicker.setCurrentMinute(0);

            if(v.getId()==R.id.etCreate_leaflet_start_time)
            {
                int inputType=etStartTime.getInputType();
                etStartTime.setInputType(InputType.TYPE_NULL);
                etStartTime.onTouchEvent(event);
                etStartTime.setInputType(inputType);
                etStartTime.setSelection(etStartTime.getText().length());

                builder.setTitle("选择时间");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringBuffer sb=new StringBuffer();
                        sb.append(String.format("%d-%02d-%02d",
                                datePicker.getYear(),
                                datePicker.getMonth(),
                                datePicker.getDayOfMonth()));
                        sb.append(" ");
                        sb.append(timePicker.getCurrentHour())
                                .append(":").append(timePicker.getCurrentMinute());

                        etStartTime.setText(sb);
                        etEndTime.requestFocus();

                        dialog.cancel();
                    }
                });


            }
            else if(v.getId()==R.id.etCreate_leaflet_end_time)
            {
                int inputType=etEndTime.getInputType();
                etEndTime.setInputType(InputType.TYPE_NULL);
                etEndTime.onTouchEvent(event);
                etEndTime.setInputType(inputType);
                etEndTime.setSelection(etEndTime.getText().length());

                builder.setTitle("选择时间");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringBuffer sb = new StringBuffer();
                        sb.append(String.format("%d-%02d-%02d",
                                datePicker.getYear(),
                                datePicker.getMonth() + 1,
                                datePicker.getDayOfMonth()));
                        sb.append("  ");
                        sb.append(timePicker.getCurrentHour())
                                .append(":").append(timePicker.getCurrentMinute());
                        etEndTime.setText(sb);

                        dialog.cancel();
                    }
                });
            }

            Dialog dialog=builder.create();
            dialog.show();

        }
        return true;
    }

    private class UploadLeafletTask extends AsyncTask<Void,Void,Integer>
    {

        @Override
        protected Integer doInBackground(Void... params) {

            int returnCode=-1;
            try
            {
                returnCode= HttpUploadMethods.uploadPhoto(leafletFile,leafletFileName,"primary");
            }catch (Exception e)
            {
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
                    SellerPreference sellerMsg=mPreference.getSellerPreference();
                    LeafletsFields fields=new LeafletsFields();
                    fields.setSellerName(sellerMsg.getSellerName());
                    fields.setSellerLogoPath(sellerMsg.getSellerLogoPath());
                    fields.setBriefLeafletPath(leafletFileName);
                    fields.setLeafletPath(leafletFileName);
                    fields.setPublishTime(UTCtoLocal.localDate2UTC());
                    fields.setStartTime(UTCtoLocal.local2UTC(etStartTime.getText().toString(), new SimpleDateFormat("yyyy-MM-dd HH:mm")));
                    fields.setEndTime(UTCtoLocal.local2UTC(etEndTime.getText().toString(), new SimpleDateFormat("yyyy-MM-dd HH:mm")));
                    fields.setLeafletType(leafletClassArrayReflect[spinnerSelectedPosition]);
                    fields.setSellerLat(sellerMsg.getSellerLat());
                    fields.setSellerLng(sellerMsg.getSellerLng());
                    fields.setSellerAddress(sellerMsg.getSellerAddress());
                    fields.setLeafletDescription(etLeafletDiscription.getText().toString());
                    LeafletUploadJson leafletUpload=new LeafletUploadJson();
                    leafletUpload.setData(fields);
                    new UploadLeafletMsgTask(leafletUpload).execute();

                    break;
                case ConstantValue.uploadPhotoFailed:
                    Toast.makeText(CreateLeafletActivity.this,"上传图片失败",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    private class UploadLeafletMsgTask extends AsyncTask<Void,Void,Integer>
    {
        private LeafletUploadJson leafletUpload;

        public UploadLeafletMsgTask(LeafletUploadJson leafletUpload)
        {
            this.leafletUpload=leafletUpload;
        }

        @Override
        protected Integer doInBackground(Void... params) {

            int returnCode=-1;

            returnCode=HttpUploadMethods.uploadLeafletMsg(this.leafletUpload);

            return returnCode;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            switch (integer.intValue())
            {
                case ConstantValue.operateSuccess:
                    CreateLeafletActivity.this.finish();
                    break;
                case ConstantValue.uploadPhotoFailed:
                    Toast.makeText(CreateLeafletActivity.this,"创建新传单失败",Toast.LENGTH_LONG);
                    break;
            }

        }
    }
}
