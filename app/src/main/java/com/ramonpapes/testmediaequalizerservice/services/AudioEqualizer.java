package com.ramonpapes.testmediaequalizerservice.services;

public class AudioEqualizer {

    static {
        System.loadLibrary("equalizer");
    }

    private native int applyEqualization(short[] audioData, int[] gains);

    public int applyEqualizationToAudio(short[] audioData, int[] gains) {
        if (audioData == null || gains == null) {
            throw new IllegalArgumentException("audioData e gains não podem ser nulos");
        }
        return applyEqualization(audioData, gains);
    }
}
