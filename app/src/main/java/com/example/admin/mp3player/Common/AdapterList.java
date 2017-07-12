package com.example.admin.mp3player.Common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.mp3player.R;

/**
 * Created by Admin on 4/24/2017.
 */

public class AdapterList extends BaseAdapter {

    private Imusic mInter;
    private Context mContext;
    private MainActivity activity;
    private String path;
    public static Bitmap mBitmap;

    public AdapterList(Imusic imusic, MainActivity act, Context context) {
        mInter = imusic;
        mContext = context;
        activity = act;
    }

    @Override
    public int getCount() {
        return mInter.getCount();
    }

    @Override
    public Object getItem(int position) {
        return mInter.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        convertView = inflater.inflate(R.layout.item_music, parent, false);

        ImageView img = (ImageView) convertView.findViewById(R.id.music);
        TextView tvName = (TextView) convertView.findViewById(R.id.name);
        TextView tvArtist = (TextView) convertView.findViewById(R.id.artist);

        Item item = mInter.getItem(position);

        mBitmap = getAlbumart(Uri.parse(item.getPath()));
        if (getAlbumart(Uri.parse(item.getPath())) != null) {
            img.setImageBitmap(mBitmap);
        }
        tvName.setText(item.getTitle());
        tvArtist.setText(item.getArtist());


        return convertView;
    }

    public interface Imusic {
        int getCount();

        Item getItem(int position);
    }

    public Bitmap getAlbumart(Uri uri) {

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        byte[] rawArt;
        Bitmap art = null;
        BitmapFactory.Options bfo = new BitmapFactory.Options();

        mmr.setDataSource(mContext, uri);
        rawArt = mmr.getEmbeddedPicture();

// if rawArt is null then no cover art is embedded in the file or is not
// recognized as such.
        if (null != rawArt) {
            art = BitmapFactory.decodeByteArray(rawArt, 0, rawArt.length, bfo);
        }
        return art;
    }

}
