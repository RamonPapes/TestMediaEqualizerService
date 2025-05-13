package com.ramonpapes.testmediaequalizerservice;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.ramonpapes.testmediaequalizerservice.services.AudioEqualizer;

import org.junit.jupiter.api.Test;

public class AudioPlayerEqualizerTest {

    @Test
    public void testApplyEqualization() {
        AudioEqualizer audioEqualizer = new AudioEqualizer();

        short[] audioData = new short[] {100, -100, 200, -200, 300, -300, 400, -400};

        int[] gains = new int[] {1000, 1200, 800}; // Ganho neutro, +20%, -20%

        int expectedSamples = audioData.length;

        int processedSamples = audioEqualizer.applyEqualizationToAudio(audioData, gains);

        assertEquals(expectedSamples, processedSamples, "O número de amostras processadas deve ser igual ao número de entrada.");
    }
}
