package net.petitviolet.exampleapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.petitviolet.shakableviewpager.ShakableViewPager;
import net.petitviolet.shakableviewpager.ShakableViewPagerHelper;

import java.lang.ref.WeakReference;


public class MainActivity extends ActionBarActivity {
    private ShakableViewPagerHelper mShakableViewPagerHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ShakableViewPager viewPager = (ShakableViewPager) findViewById(R.id.view_pager);
        MyAdapter adapter = new MyAdapter(this);
        viewPager.setAdapter(adapter);
        viewPager.enableSwipe(true);
        mShakableViewPagerHelper = new ShakableViewPagerHelper(this, viewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mShakableViewPagerHelper.register();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mShakableViewPagerHelper.unregister();
    }

    private static class MyAdapter extends PagerAdapter {

        private final WeakReference<Context> mContext;
        public MyAdapter(Context context) {
            mContext = new WeakReference<Context>(context);
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TextView textView = new TextView(mContext.get());
            textView.setText(String.format("This TextView is No.%d", position));
            textView.setTextSize(30);
            container.addView(textView, 0);
            return textView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
