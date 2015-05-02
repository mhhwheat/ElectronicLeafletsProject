package org.wheat.leaflets.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import org.wheat.leaflets.R;

/**
 * Created by Administrator on 2015/5/1.
 */
public class TestDpPs extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics dm=this.getResources().getDisplayMetrics();

        setContentView(R.layout.activity_create_leaflet_layout);

        Log.d("TestDpPs","density="+dm.density);
        Log.d("TestDpPs","densityDpi="+dm.densityDpi);
        Log.d("TestDpPs","widthPixels="+dm.widthPixels);
        Log.d("TestDpPs","heightPixels="+dm.heightPixels);
        Log.d("TestDpPs","xdpi="+dm.xdpi);
        Log.d("TestDpPs","ydpi="+dm.ydpi);
    }
}
