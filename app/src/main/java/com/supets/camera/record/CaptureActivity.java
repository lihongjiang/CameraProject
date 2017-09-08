package com.supets.camera.record;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.supets.camera.R;


public class CaptureActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mScreenShortBtn;
    private Button mScreenRecordBtn;
    private MediaProjectionManager mMpMngr;
    private static final int REQUEST_MEDIA_PROJECTION = 1;
    private Intent mResultIntent = null;
    private int mResultCode = 0;
    public static final String TAG = "MainAc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mScreenShortBtn = (Button) findViewById(R.id.btn_screen_short);
        mScreenRecordBtn = (Button) findViewById(R.id.btn_screen_record);
        mScreenShortBtn.setOnClickListener(this);
        mScreenRecordBtn.setOnClickListener(this);
        mMpMngr = (MediaProjectionManager) getApplicationContext().getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        mResultIntent = ((MyApplication) getApplication()).getResultIntent();
        mResultCode = ((MyApplication) getApplication()).getResultCode();
    }

    @Override
    public void onClick(View view) {
        if (view == mScreenShortBtn) {
            startIntent();
        }
    }


    private void startIntent() {
        if (mResultIntent != null && mResultCode != 0) {
            bindMsgService(CaptureService.class);
        } else {
            startActivityForResult(mMpMngr.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MEDIA_PROJECTION) {
            if (resultCode == RESULT_OK) {
                Log.e(TAG, "get capture permission success!");
                mResultCode = resultCode;
                mResultIntent = data;
                ((MyApplication) getApplication()).setResultCode(resultCode);
                ((MyApplication) getApplication()).setResultIntent(data);
                ((MyApplication) getApplication()).setMpmngr(mMpMngr);

                bindMsgService(CaptureService.class);
            }
        }
    }


    public void bindMsgService(Class<?> classs) {
        Intent intent = new Intent(this, classs);
        bindService(intent, msgconnect, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindMsgService();
    }

    public void unbindMsgService() {
        unbindService(msgconnect);
    }

    private ServiceConnection msgconnect = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
