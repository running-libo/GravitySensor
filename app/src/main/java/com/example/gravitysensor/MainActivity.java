package com.example.gravitysensor;

import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener {
    @Bind(R.id.textureview)
    TextureView textureview;
    @Bind(R.id.iv_close)
    ImageView ivClose;
    @Bind(R.id.more)
    ImageView more;
    @Bind(R.id.iv_switch)
    ImageView ivSwitch;
    @Bind(R.id.iv_pic)
    ImageView ivPic;
    @Bind(R.id.iv_beauty)
    ImageView ivBeauty;
    @Bind(R.id.iv_face)
    ImageView ivFace;
    @Bind(R.id.iv_filter)
    ImageView ivFilter;
    private TextureView textureView;
    private OpenCamera openCamera;
    private RotateSensorUtil sensorUtil;
    private ArrayList<View> rotateViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        openCamera();
        addViews();
        startAnim();
    }

    private void startAnim() {
        sensorUtil = new RotateSensorUtil(this,rotateViews);
    }

    private void addViews(){
        rotateViews.add(ivClose);
        rotateViews.add(more);
        rotateViews.add(ivSwitch);
        rotateViews.add(ivPic);
        rotateViews.add(ivBeauty);
        rotateViews.add(ivFace);
        rotateViews.add(ivFilter);
    }

    private void openCamera(){
        textureView = (TextureView) findViewById(R.id.textureview);
        openCamera = new OpenCamera(getApplicationContext(), textureView);

        openCamera.startCameraThread();

        if (!textureView.isAvailable()) {
            textureView.setSurfaceTextureListener(this);
        } else {
            openCamera.startPreview();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        sensorUtil.unregisterSensor();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        openCamera.setupCamera(width, height);  //设置相机参数,回调的是textureview的宽高
        openCamera.openCamera();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}
