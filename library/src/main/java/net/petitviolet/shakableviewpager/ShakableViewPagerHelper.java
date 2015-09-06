package net.petitviolet.shakableviewpager;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.lang.ref.WeakReference;

public class ShakableViewPagerHelper implements SensorEventListener {
    private static final String TAG = ShakableViewPagerHelper.class.getSimpleName();
    private static final int MOV_COUNTS = 5;
    private static final int MOV_THRESHOLD = 4;
    private static final float ALPHA = 0.8F;
    private static final int SHAKE_WINDOW_TIME_INTERVAL = 500; // milliseconds

    private WeakReference<OnShakeListener> mOnShakeListener;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private final float mGravity[] = new float[3];
    private int mCounter;
    private long mFirstMovTime;

    public ShakableViewPagerHelper(Context context, OnShakeListener onShakeListener) {
        mOnShakeListener = new WeakReference<>(onShakeListener);
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void register() {
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregister() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float maxAcc = calcMaxAcceleration(sensorEvent);
        if (maxAcc >= MOV_THRESHOLD) {
            if (mCounter == 0) {
                mCounter++;
                mFirstMovTime = System.currentTimeMillis();
            } else {
                long now = System.currentTimeMillis();
                if ((now - mFirstMovTime) < SHAKE_WINDOW_TIME_INTERVAL) {
                    mCounter++;
                } else {
                    resetAllData();
                    mCounter++;
                    return;
                }
//                Log.d(TAG, "Mov mCounter [" + mCounter + "]");

                if (mCounter >= MOV_COUNTS)
                    if (mOnShakeListener != null) {
                        Log.i(TAG, "shake event dispatched");
                        mOnShakeListener.get().onShake();
                        resetAllData();
                    }
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }


    private float calcMaxAcceleration(SensorEvent event) {
        mGravity[0] = calcGravityForce(event.values[0], 0);
        mGravity[1] = calcGravityForce(event.values[1], 1);
        mGravity[2] = calcGravityForce(event.values[2], 2);

        float accX = event.values[0] - mGravity[0];
        float accY = event.values[1] - mGravity[1];
        float accZ = event.values[2] - mGravity[2];

        float max1 = Math.max(accX, accY);
        return Math.max(max1, accZ);
    }

    // Low pass filter
    private float calcGravityForce(float currentVal, int index) {
        return ALPHA * mGravity[index] + (1 - ALPHA) * currentVal;
    }


    private void resetAllData() {
//        Log.d(TAG, "Reset all data");
        mCounter = 0;
        mFirstMovTime = System.currentTimeMillis();
    }
}

