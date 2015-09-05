package net.petitviolet.shakableviewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class ShakableViewPager extends ViewPager implements OnShakeListener {
    private static final String TAG = ShakableViewPager.class.getSimpleName();

    public ShakableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShakableViewPager(Context context) {
        super(context);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }
    @Override
    public void onShake() {
        Log.i(TAG, "Shake");
        setCurrentItem(getCurrentItem() + 1, true);
    }

}