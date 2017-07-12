package com.example.admin.mp3player.Fragments;


import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.admin.mp3player.Common.AdapterList;
import com.example.admin.mp3player.Common.Item;
import com.example.admin.mp3player.Common.MainActivity;
import com.example.admin.mp3player.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.admin.mp3player.Online.adapterMusic.mPlayer;

/**
 * A simple {@link Fragment} subclass.
 */
public class MusicFragment extends Fragment implements AdapterList.Imusic, AdapterView.OnItemClickListener, View.OnClickListener {

    private AdapterList mAdapter;
    public static List<Item> mList;
    private ListView lv;
    public int count = 0;
    protected static String link;
    private TextView tvNameOn, tvArtistOn;
    private ImageView prev1, play1, next1;
    public static ImageView musicPlay;

    private static final String TAG = MusicFragment.class.getSimpleName();
    public static long id;
    public static int pos;
    public static int pos1;
    private String name, artist;
    private LinearLayout linearLayout;
    public LinearLayout layoutControl;
    public static MediaPlayer mediaPlayer;
    private Bundle bundle;
    public static int num = 0;
    private int count1 = 0;
    public static Bitmap bitmap;
    public static AdapterList adapterList;

    public MusicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_music, container, false);
        lv = (ListView) view.findViewById(R.id.lvList);
        // Inflate the layout for this fragment
        mList = new ArrayList<>();
        mediaPlayer = new MediaPlayer();

        ListMusic();
        mAdapter = new AdapterList(this, (MainActivity) getActivity(), getContext());
        lv.setAdapter(mAdapter);
        lv.setOnItemClickListener(this);
        lv.smoothScrollToPosition(mList.size());

        linearLayout = (LinearLayout) view.findViewById(R.id.mainLayout);
        layoutControl = (LinearLayout) view.findViewById(R.id.control);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayFragment playFragment1 = new PlayFragment();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();

                MusicFragment musicFragment = new MusicFragment();

                if (musicFragment.isVisible()) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.remove(musicFragment);
                    fragmentTransaction.commit();
                }

                if (playFragment1.isVisible()) {
                    FragmentManager fragmentManager1 = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                    fragmentTransaction1.remove(playFragment1);
                    fragmentTransaction1.commit();
                }

                playFragment1.setArguments(bundle);

//                linearLayout.setVisibility(View.GONE);
//                tabLayout.setVisibility(View.GONE);
                transaction.add(R.id.main, playFragment1);
                transaction.addToBackStack("AHIHI");
                transaction.commit();
            }
        });
        tvNameOn = (TextView) view.findViewById(R.id.name1);
        tvArtistOn = (TextView) view.findViewById(R.id.artist1);

        layoutControl.setVisibility(View.GONE);

        prev1 = (ImageView) view.findViewById(R.id.prev1);
        play1 = (ImageView) view.findViewById(R.id.play1);
        next1 = (ImageView) view.findViewById(R.id.next1);
        musicPlay = (ImageView) view.findViewById(R.id.musicPlay);

        prev1.setOnClickListener(this);
        play1.setOnClickListener(this);
        next1.setOnClickListener(this);

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.tranlate);
        tvNameOn.startAnimation(animation);

        return view;
    }

    private void ListMusic() {
        String list[] = new String[]{
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.COMPOSER,
                MediaStore.Audio.Media.ALBUM_ID
        };

        Cursor cursor = getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                list, null, null, null);

        if (cursor == null) {
            return;
        }

        int indexTitle = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
        int indexData = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
        int indexArtist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
        int indexAlbum = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
        int indexComposer = cursor.getColumnIndex(MediaStore.Audio.Media.COMPOSER);
        int indexAlbumId = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);


        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            String path = cursor.getString(indexData);
            String title = cursor.getString(indexTitle);
            String artist = cursor.getString(indexArtist);
            String album = cursor.getString(indexAlbum);
            String composer = cursor.getString(indexComposer);
            id = cursor.getLong(indexAlbumId);

            Log.d(TAG, "path" + path);
            Log.d(TAG, "title" + title);
            Log.d(TAG, "artist" + artist);
            Log.d(TAG, "album" + album);
            Log.d(TAG, "composer" + composer);
            Log.d(TAG, "idddddddd" + id);
            Log.d(TAG, "TAG" + "=========================================");

            mList.add(new Item(path, title, artist, album, composer, id));

            ContentResolver musicResolve = getActivity().getContentResolver();
            Uri smusicUri = android.provider.MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
            Cursor music = musicResolve.query(smusicUri, null         //should use where clause(_ID==albumid)
                    , null, null, null);

            link = path;

            Cursor cursor1 = getActivity().managedQuery(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    new String[]{
                            MediaStore.Audio.Albums._ID,
                            MediaStore.Audio.Albums.ALBUM_ART},
                    MediaStore.Audio.Albums._ID + "=?",
                    new String[]{String.valueOf(MediaStore.Audio.Media.ALBUM_ID)},
                    null);

            cursor1.moveToFirst();
            while (!cursor1.isAfterLast()) {
                String path1 = cursor1.getString(cursor1.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                Log.d("AAAAAAAAAAAA", path1);
                // do whatever you need to do
            }

            cursor.moveToNext();


            cursor1.moveToNext();

        }

        cursor.close();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Item getItem(int position) {
        return mList.get(position);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        layoutControl.setVisibility(View.VISIBLE);

        adapterList = new AdapterList(this, (MainActivity) getActivity(),
                getContext());

        setImagePlay(position);

        mPlayer = new MediaPlayer();

        if (mPlayer.isPlaying()) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;

        }

        count++;


        bundle = new Bundle();
        bundle.putString("key", mList.get(position).getTitle());
        bundle.putString("key1", mList.get(position).getArtist());
        bundle.putInt("key2", position);
        bundle.putInt("key3", mList.size());

        name = mList.get(position).getTitle();
        artist = mList.get(position).getArtist();
        pos = position;
        pos1 = position;

        tvNameOn.setText(name);
        tvArtistOn.setText(artist);

        if (count == 1) {

            try {

                mediaPlayer = new MediaPlayer();
                mediaPlayer.reset();
                mediaPlayer.setDataSource(mList.get(pos).getPath());
                mediaPlayer.prepare();
                mediaPlayer.start();
                // mediaPlayer.release();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }

            mediaPlayer = new MediaPlayer();

            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(mList.get(pos).getPath());
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        if (mediaPlayer.isPlaying()){
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
            musicPlay.startAnimation(animation);
        } else {
        }


        play1.setImageResource(R.drawable.icnotifpause);

    }

    @Override
    public void onClick(View v) {

        count1++;

        switch (v.getId()) {

//            case R.id.name1:
//
//
//                break;

            case R.id.prev1:

                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;

                if (pos > 0) {
                    pos = pos - 1;
                } else
                    pos = 0;

                setImagePlay(pos);

                Uri uri2 = Uri.parse(mList.get(pos).getPath());
                mediaPlayer = MediaPlayer.create(getContext(), uri2);
                mediaPlayer.start();
                tvNameOn.setText(mList.get(pos).getTitle());
                tvArtistOn.setText(mList.get(pos).getArtist());


                break;

            case R.id.play1:

                num++;

                if ((count1 > 2 && count1 % 2 == 0) || count1 == 1) {
                    play1.setImageResource(R.drawable.icnotifplay);
                    mediaPlayer.pause();
                } else {
                    play1.setImageResource(R.drawable.icnotifpause);
                    mediaPlayer.start();
                }

                break;

            case R.id.next1:

                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;

                pos1 = pos1 + 1;

                if (pos1 == mList.size()) {
                    pos1 = 0;
                }

                setImagePlay(pos1);

                //observable

                Uri uri3 = Uri.parse(mList.get(pos1).getPath());
                mediaPlayer = MediaPlayer.create(getContext(), uri3);
                tvNameOn.setText(mList.get(pos1).getTitle());
                tvArtistOn.setText(mList.get(pos1).getArtist());

                Log.d("NAMEEEEEEEEEEEEEEEEE", mList.get(pos1).getTitle() + "\t" + pos1);
                mediaPlayer.start();

                break;

            default:
                break;

        }
    }

    private void setImagePlay(int position) {
        bitmap = adapterList.getAlbumart(Uri.parse(mList.get(position).getPath()));

        if (bitmap != null) {
            musicPlay.setImageBitmap(bitmap);
        } else musicPlay.setImageResource(R.drawable.musical);
    }

}
