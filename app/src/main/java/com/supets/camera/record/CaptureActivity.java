package com.supets.camera.record;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.supets.camera.R;


public class CaptureActivity extends BaseCaptureActivity implements View.OnClickListener {
    private Button mScreenShortBtn;
    private Button mScreenRecordBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mScreenShortBtn = (Button) findViewById(R.id.btn_screen_short);
        mScreenRecordBtn = (Button) findViewById(R.id.btn_screen_record);
        mScreenShortBtn.setOnClickListener(this);
        mScreenRecordBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == mScreenShortBtn) {
            startIntent();
        }
    }

}
