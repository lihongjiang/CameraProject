package com.supets.camera.camera2;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import java.util.List;

/**
 * CameraProject
 *
 * @user lihongjian
 * g
 * @description
 * @date 2017/9/8
 * @updatetime 2017/9/8
 */

public class BaseCameraActivity extends AppCompatActivity implements Camera.PreviewCallback, SurfaceHolder.Callback {


    Camera mCammera;
    SurfaceView mSurfaceView;
    SurfaceHolder mSurfaceHolder;
    int mCammeraId = 0;
    private int width = 720;
    private int height = 480;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSurfaceView = new SurfaceView(this);
        mSurfaceView.setLayoutParams(new FrameLayout.LayoutParams(720, -2));
        setContentView(mSurfaceView);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    public void initCamera() {

        try {
            //打开相机
            mCammera = Camera.open(mCammeraId);
            //绑定预览
            mCammera.setPreviewDisplay(mSurfaceHolder);
            mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

            //设置参数
            Camera.Parameters parameters = mCammera.getParameters();
            //14
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);


            //设置预览大小
            parameters.setPreviewSize(width, height);
            //设置拍照大小
            parameters.setPictureSize(width, height);
            parameters.setPictureFormat(ImageFormat.NV21);

            //设置预览方向
            mCammera.setDisplayOrientation(90);//竖屏
            //设置拍照方向
            parameters.setRotation(0);

            List<Camera.Size> pictureSizes = parameters.getSupportedPictureSizes();
            for (Camera.Size size : pictureSizes) {
                Log.d("picturesize", "width=" + size.width + ",height=" + size.height);
            }

            List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
            for (Camera.Size size : previewSizes) {
                Log.d("previewSizes", "width=" + size.width + ",height=" + size.height);
            }

            mCammera.setParameters(parameters);//设置参数
            mCammera.setPreviewCallback(this);//设置每帧回调
            mCammera.startPreview();//启动预览

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void destoryCamera() {
        if (mCammera == null) {
            return;
        }
        mCammera.setPreviewCallback(null);//先停止预览回调
        mCammera.release();//释放相机
        mCammera = null;//相机置空
    }


    public void changeCamera() {
        mCammeraId = 1 - mCammeraId;
        destoryCamera();//先销毁
        initCamera();//初始化相机
    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        Camera.Size size = camera.getParameters().getPreviewSize();
        Log.d("getPreviewSize", "width=" + size.width + ",height=" + size.height);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        changeCamera();
        Log.d("surfaceCreated", "width=" + width + ",height=" + height);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width,
                               int height) {
        Log.d("surfaceChanged", "width=" + width + ",height=" + height);
        this.width=width;
        this.height=height;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        destoryCamera();
    }
}
