package com.example.gravitysensor;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import java.util.ArrayList;

public class RotateSensorUtil implements SensorEventListener{
    private Context context;
    private SensorManager sensorManager;
    //默认旋转角度code，分别为 0,1/2,3,4/-1
    private int curRotateCode = 0;
    //记录当前的角度，每次旋转从此处开始
    private int curAngle = 0;
    //需要操作旋转的集合
    private ArrayList<View> views = new ArrayList<>();

    public RotateSensorUtil(Context context, ArrayList<View> views){

        this.context = context;
        this.views = views;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        //通过传感器管理器获取重力加速度传感器
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() != Sensor.TYPE_ACCELEROMETER){
            return;
        }

        float[] values = event.values;
        float ax = values[0];
        float ay = values[1];


        double g = Math.sqrt(ax * ax + ay * ay);
        double cos = ay / g;
        if (cos > 1) {
            cos = 1;
        } else if (cos < -1) {
            cos = -1;
        }
        double rad = Math.acos(cos);
        if (ax < 0) {
            rad = 2 * Math.PI - rad;
        }

        int uiRot = ((Activity)context).getWindowManager().getDefaultDisplay().getRotation();
        double uiRad = Math.PI / 2 * uiRot;
        rad -= uiRad;

        checkBundray((int) rad);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * 旋转检测
     * @param rotateCode
     */
    private void checkBundray(int rotateCode){
        if(rotateCode == 2) rotateCode = 1;
        if(rotateCode == -1) rotateCode = 4;
        int angle = 0;

        switch (rotateCode){
            case 0:
                angle = 0;
                break;
            case 1:
                angle = 90;
                break;
            case 3:
                angle = 180;
                break;
            case 4:
                angle = 270;
                break;
        }

        if(rotateCode == curRotateCode){

        }else{
            valueRotateAnim(angle);
            curAngle = angle;
            curRotateCode = rotateCode;
        }
    }

    private void valueRotateAnim(int angle){
        //特别处理从270-0度的反向旋转
        if(curAngle == 270){
            angle = 360;
        }
        for(int i=0;i<views.size();i++){
            startRotateAnim(views.get(i),300,curAngle,angle);
        }
    }

    public void unregisterSensor(){
        sensorManager.unregisterListener(this);
    }

    public void startRotateAnim(View view,long time,int fromAngle,float toAngle){
        ObjectAnimator animRotate = ObjectAnimator.ofFloat(view,"rotation",fromAngle,toAngle);
        animRotate.setDuration(time);
        animRotate.start();
    }

}
