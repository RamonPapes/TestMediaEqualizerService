package com.ramonpapes.testmediaequalizerservice;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import com.ramonpapes.testmediaequalizerservice.services.AudioService;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AudioServiceTest {


    @Mock
    private MediaPlayer mockMediaPlayer;
    @Mock
    private AudioManager mockAudioManager;
    @Mock
    private NotificationManager mockNotificationManager;
    @Mock
    private Notification mockNotification;
    @Mock
    private MediaSessionCompat mockMediaSession;
    @Mock
    private IBinder mockBinder;

    private AudioService audioService;

    @BeforeEach
    public void setUp() {
        audioService = new AudioService() {
            @Override
            public Object getSystemService(String name){
                switch (name){
                    case Context.AUDIO_SERVICE:
                        return mockAudioManager;
                    case Context.NOTIFICATION_SERVICE:
                        return mockNotificationManager;
                    default:
                        return getSystemService(name);
                }
            }
        };

        audioService.setMediaPlayer(mockMediaPlayer);
        audioService.setAudioManager(mockAudioManager);
        audioService.setMediaSession(mockMediaSession);
    }

    @Test
    public void testPlayAudio() {
        String audioPath = "path/to/audio/file";

        try {
            audioService.playAudio(audioPath);

            Mockito.verify(mockMediaPlayer).reset();
            Mockito.verify(mockMediaPlayer).setDataSource(audioPath);
            Mockito.verify(mockMediaPlayer).prepare();
            Mockito.verify(mockMediaPlayer).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPauseAudio() {
        Mockito.when(mockMediaPlayer.isPlaying()).thenReturn(true);

        // Chama o método pauseAudio
        audioService.pauseAudio();

        // Verifica se o método pause foi chamado
        verify(mockMediaPlayer).pause();
    }

    @Test
    public void testStopAudio() {
        // Chama o método stopAudio
        audioService.stopAudio();

        // Verifica se o MediaPlayer foi parado e o serviço foi interrompido
        Mockito.verify(mockMediaPlayer).stop();
        // Pode-se adicionar verificações para stopForeground e stopSelf, se necessário
    }

    @Test
    public void testCreateNotificationChannel() {
        // Verifica se o canal de notificação é criado, apenas para Android O e superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioService.createNotificationChannel();
            // Verifica se o canal foi criado corretamente
            verify(mockNotificationManager).createNotificationChannel(any());
        }
    }

    @Test
    public void testCreateNotification() {
        // Simula a criação de uma notificação
        Mockito.doNothing().when(mockNotificationManager).notify(anyInt(), any(Notification.class));
        Notification notification = audioService.createNotification();
        assertNotNull(notification);
    }
}


