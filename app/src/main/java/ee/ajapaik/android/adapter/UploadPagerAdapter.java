package ee.ajapaik.android.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import ee.ajapaik.android.widget.WebImageView;

public class UploadPagerAdapter extends PagerAdapter {
    private final Context context;
    private final List<Bitmap> m_bitmaps;

    public UploadPagerAdapter(Context context, List<Bitmap> m_bitmaps) {
        this.context = context;
        this.m_bitmaps = m_bitmaps;
    }

    @Override
    public int getCount() {
        return m_bitmaps.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        WebImageView imageView = new WebImageView(context);
        imageView.setImageBitmap(m_bitmaps.get(position));
        container.addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }

    public Bitmap getBitmap(int position) {
        return m_bitmaps.get(position);
    }
}
