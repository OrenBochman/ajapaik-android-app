package ee.ajapaik.android.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ee.ajapaik.android.AlbumActivity;
import ee.ajapaik.android.R;
import ee.ajapaik.android.data.Album;

public class AlbumAdapter extends ArrayAdapter<Album> {
    private static final int THUMBNAIL_SIZE = 250;

    public AlbumAdapter(Context context, List<Album> items) {
        super(context, 0, items);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final Album album = getItem(position);
        ImageView imageView;
        ImageButton button;
        TextView textView;

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_album_item, parent, false);
        }

        button = convertView.findViewById(R.id.button_action);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlbumActivity.start(v.getContext(), album);
            }
        });

        imageView = convertView.findViewById(R.id.image_background);
        imageView.setImageURI(album.getThumbnail(THUMBNAIL_SIZE));

        textView = convertView.findViewById(R.id.text_title);
        textView.setText(album.getTitle());

        return convertView;
    }
}
