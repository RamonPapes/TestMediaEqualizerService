package com.ramonpapes.testmediaequalizerservice.interfaces;

public interface PlaybackInterface {
    void play(String path);
    void pause();
    void stop();
    void seekTo(int position);
    boolean isPlaying();
}
