package com.example.admin.mp3player.Fragments;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.admin.mp3player.Common.BlurBuilder;
import com.example.admin.mp3player.R;

import java.util.concurrent.TimeUnit;

import static com.example.admin.mp3player.Fragments.MusicFragment.adapterList;
import static com.example.admin.mp3player.Fragments.MusicFragment.bitmap;
import static com.example.admin.mp3player.Fragments.MusicFragment.mList;
import static com.example.admin.mp3player.Fragments.MusicFragment.mediaPlayer;
import static com.example.admin.mp3player.Fragments.MusicFragment.musicPlay;
import static com.example.admin.mp3player.Fragments.MusicFragment.num;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayFragment extends Fragment implements View.OnClickListener,
        SeekBar.OnSeekBarChangeListener, MediaPlayer.OnBufferingUpdateListener {

    private TextView tvNamePlay, tvArtistRun, tvTotalTime, tvRemainTime;
    private ImageView shuffe, prev, play, next, repeat, playImg;
    private SeekBar seekBar;
    private int startTime;
    private int finalTime;
    private Handler myHandler = new Handler();
    private int count = 0;
    private int position1;
    private int position;
    private LinearLayout layoutPlay;
    private BlurBuilder blurBuilder = new BlurBuilder();
    private Bitmap mBit;


    public PlayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play, container, false);

        if (mediaPlayer != null) {

            String name = getArguments().getString("key");
            String artist = getArguments().getString("key1");
            position1 = getArguments().getInt("key2");
            position = getArguments().getInt("key2");

            tvNamePlay = (TextView) view.findViewById(R.id.nameRun);
            tvArtistRun = (TextView) view.findViewById(R.id.artistRun);
            tvTotalTime = (TextView) view.findViewById(R.id.totalTime);
            tvRemainTime = (TextView) view.findViewById(R.id.timeRemain);

            layoutPlay = (LinearLayout) view.findViewById(R.id.layoutBlur);

            finalTime = mediaPlayer.getDuration();
            startTime = mediaPlayer.getCurrentPosition();

            tvNamePlay.setText(name);
            tvArtistRun.setText(artist);

            shuffe = (ImageView) view.findViewById(R.id.shuffe);
            prev = (ImageView) view.findViewById(R.id.prev);
            play = (ImageView) view.findViewById(R.id.play);
            next = (ImageView) view.findViewById(R.id.next);
            repeat = (ImageView) view.findViewById(R.id.repeat);
            playImg = (ImageView) view.findViewById(R.id.imgPlay);

            shuffe.setOnClickListener(this);
            prev.setOnClickListener(this);
            play.setOnClickListener(this);
            next.setOnClickListener(this);
            repeat.setOnClickListener(this);

            tvTotalTime.setText(String.format("%d min %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                    finalTime)))
            );

            tvRemainTime.setText(String.format("%d min %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                    startTime)))
            );


            seekBar = (SeekBar) view.findViewById(R.id.seekBar);
            seekBar.setClickable(false);
            seekBar.setMax(finalTime);
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            myHandler.postDelayed(UpdateSongTime, 100);
            seekBar.setOnSeekBarChangeListener(this);
            // seekBar.setOnSeekBarChangeListener(this);


            if (num % 2 == 0) {
                play.setImageResource(R.drawable.icnotifpause);
            }
            Log.d("AAAAAAAAAAAAAAAAAAAA", num + "");


            if (bitmap != null) {
                playImg.setImageBitmap(bitmap);
            }

            if (bitmap != null) {
                layoutPlay.setBackgroundDrawable(
                        new BitmapDrawable(BlurBuilder.blur(getContext(), bitmap)));

            }
        }
        // Inflate the layout for this fragment
        return view;
    }


    @Override
    public void onClick(View v) {

        count++;

        switch (v.getId()) {
            case R.id.shuffe:

                break;
            case R.id.prev:
                if (mediaPlayer != null) {

                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;

                    if (position > 0) {
                        position = position - 1;
                    } else
                        position = 0;

                    setImagePlay(position);

                    Uri uri = Uri.parse(mList.get(position).getPath());
                    mediaPlayer = MediaPlayer.create(getContext(), uri);
                    mediaPlayer.setOnBufferingUpdateListener(this);
                    mediaPlayer.start();
                    tvNamePlay.setText(mList.get(position).getTitle());
                    tvArtistRun.setText(mList.get(position).getArtist());
                }
                break;

            case R.id.play:
                if (mediaPlayer != null) {
                    if ((count > 2 && count % 2 == 0) || count == 1) {
                        play.setImageResource(R.drawable.icnotifplay);
                        mediaPlayer.pause();
                    } else {
                        play.setImageResource(R.drawable.icnotifpause);
                        mediaPlayer.start();
                    }
                }
                break;

            case R.id.next:

                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;

                    mediaPlayer = new MediaPlayer();

                    if (position1 < mList.size()) {
                        position1 = position1 + 1;
                    } else position1 = 0;

                    setImagePlay(position1);

                    try {
                        Uri uri1 = Uri.parse(mList.get(position1).getPath());
                        mediaPlayer = MediaPlayer.create(getContext(), uri1);
                        mediaPlayer.setOnBufferingUpdateListener(this);
                        tvNamePlay.setText(mList.get(position1).getTitle());
                        tvArtistRun.setText(mList.get(position1).getArtist());
                        mediaPlayer.start();

                    } catch (IndexOutOfBoundsException e) {
                    }
                }
                break;

            case R.id.repeat:


                break;


            default:
                break;

        }

    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            if (mediaPlayer != null) {
                startTime = mediaPlayer.getCurrentPosition();

                tvRemainTime.setText(String.format("%d min %d sec",
                        TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                        toMinutes((long) startTime)))
                );
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                if (mediaPlayer.getCurrentPosition() >= mediaPlayer.getDuration()) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;

                    mediaPlayer = new MediaPlayer();

                    if (position1 >= mList.size()) {
                        position1 = 0;
                    } else position1 += 1;

                    try {
                        Uri uri1 = Uri.parse(mList.get(position1).getPath());
                        mediaPlayer = MediaPlayer.create(getContext(), uri1);

                        tvNamePlay.setText(mList.get(position1).getTitle());
                        tvArtistRun.setText(mList.get(position1).getArtist());

                        mediaPlayer.start();

                        startTime = 0;
                        finalTime = mediaPlayer.getDuration();

                        tvTotalTime.setText(String.format("%d min %d sec",
                                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                                finalTime)))
                        );
                        setImagePlay(position1);
                        seekBar.setMax(finalTime);
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    } catch (IndexOutOfBoundsException e) {
                    }


                }
                myHandler.postDelayed(this, 100);
            }
        }
    };


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser && mediaPlayer != null) {
            int time = progress;
            if (time > mediaPlayer.getDuration())
                time = mediaPlayer.getDuration();

            mediaPlayer.pause();
            mediaPlayer.seekTo(time);
            mediaPlayer.start();
            mediaPlayer.setOnBufferingUpdateListener(this);


        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        seekBar.setSecondaryProgress(percent);
    }

    private void setImagePlay(int position) {
        if (position < mList.size()) {

            mBit = adapterList.getAlbumart(Uri.parse(mList.get(position).getPath()));

            if (mBit != null) {
                playImg.setImageBitmap(mBit);
                musicPlay.setImageBitmap(mBit);
                layoutPlay.setBackgroundDrawable(
                        new BitmapDrawable(BlurBuilder.blur(getContext(), mBit)));

            } else {
                playImg.setImageResource(R.drawable.musical);
                layoutPlay.setBackgroundResource(R.drawable.background);

            }

        }
    }

}
