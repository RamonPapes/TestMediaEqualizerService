package com.ramonpapes.testmediaequalizerservice.modules;

import android.media.audiofx.Equalizer;

public class EqualizationModule {
    private Equalizer equalizer;

    public EqualizationModule(int audioSessionId) {
        equalizer = new Equalizer(0, audioSessionId);
        equalizer.setEnabled(true);
    }

    public void setBandLevel(short band, short level) {
        equalizer.setBandLevel(band, level);
    }

    public Equalizer getEqualizer() {
        return equalizer;
    }

    public void release() {
        equalizer.release();
    }
}
