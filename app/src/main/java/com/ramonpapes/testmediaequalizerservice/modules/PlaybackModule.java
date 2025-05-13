package com.ramonpapes.testmediaequalizerservice.modules;

import android.media.MediaPlayer;
import android.util.Log;

import com.ramonpapes.testmediaequalizerservice.interfaces.PlaybackInterface;

public class PlaybackModule implements PlaybackInterface {
    private MediaPlayer mediaPlayer;

    public PlaybackModule() {
        mediaPlayer = new MediaPlayer();
    }

    @Override
    public void play(String path) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            Log.e("PlaybackModule", "Erro ao reproduzir áudio", e);
        }
    }

    @Override
    public void pause() {
        if (mediaPlayer.isPlaying()) mediaPlayer.pause();
    }

    @Override
    public void stop() {
        if (mediaPlayer.isPlaying()) mediaPlayer.stop();
    }

    @Override
    public void seekTo(int position) {
        mediaPlayer.seekTo(position);
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void release() {
        mediaPlayer.release();
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
}