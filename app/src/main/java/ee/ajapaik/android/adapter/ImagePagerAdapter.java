package ee.ajapaik.android.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import ee.ajapaik.android.data.Rephoto;
import ee.ajapaik.android.widget.WebImageView;

public class ImagePagerAdapter extends PagerAdapter {
    private final Context context;
    private final List<Rephoto> m_images;
    private static final int THUMBNAIL_SIZE = 400;

    public ImagePagerAdapter(Context context, List<Rephoto> rephotos) {
        this.context = context;
        this.m_images = rephotos;
    }

    @Override
    public int getCount() {
        return m_images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Rephoto rephoto = m_images.get(position);
        WebImageView imageView = new WebImageView(context);
        imageView.setImageURI(rephoto.getThumbnail(THUMBNAIL_SIZE));
        container.addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }

    public Rephoto getRephoto(int position) {
        return m_images.get(position);
    }
}
