package com.ramonpapes.testmediaequalizerservice.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.ramonpapes.testmediaequalizerservice.MainActivity;
import com.ramonpapes.testmediaequalizerservice.R;

public class AudioService extends Service {
    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public void setAudioManager(AudioManager audioManager) {
        this.audioManager = audioManager;
    }

    public void setMediaSession(MediaSessionCompat mediaSession) {
        this.mediaSession = mediaSession;
    }

    private static final String TAG = "AudioService";

    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private MediaSessionCompat mediaSession;

    private static final String CHANNEL_ID = "AudioServiceChannel";
    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service criado");


        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnErrorListener((mp, what, extra) -> {
            Log.e(TAG, "Erro no MediaPlayer: what=" + what + ", extra=" + extra);
            stopSelf();
            return true;
        });

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        mediaSession = new MediaSessionCompat(this, "AudioService");


        createNotificationChannel();
        Notification notification = createNotification();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) { // API 34+
            startForeground(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK);
        } else {
            startForeground(NOTIFICATION_ID, notification);
        }

        Log.d(TAG, "Componentes inicializados");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand chamado");
        if (intent != null && intent.getAction() != null) {
            String action = intent.getAction();
            Log.d(TAG, "Ação recebida: " + action);


            switch (action) {
                case "PLAY":
                    String path = intent.getStringExtra("path");
                    playAudio(path);
                    break;
                case "PAUSE":
                    pauseAudio();
                    break;
                case "STOP":
                    stopAudio();
                    break;
                case "SEEK_TO":
                    seekAudio(intent.getIntExtra("position", 0));
                    break;
                default:
                    Log.w(TAG, "Ação desconhecida: " + action);
                    break;
            }
        }

        return START_STICKY;
    }

    public void playAudio(String path) {
        if (path == null || path.isEmpty()) {
            Log.e(TAG, "Caminho de áudio inválido");
            return;
        }

        try {
            if (mediaPlayer.isPlaying()) {
                Log.d(TAG, "Já está tocando");
                return;
            }

            if (mediaPlayer.getCurrentPosition() > 0) {
                mediaPlayer.start();
                Log.d(TAG, "Reprodução retomada");
            } else {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(path);
                mediaPlayer.prepare();
                mediaPlayer.start();
                Log.d(TAG, "Reprodução iniciada: " + path);
            }
            // Inicia a atualização apenas quando começa a tocar
//            handler.post(updateRunnable);
        } catch (Exception e) {
            Log.e(TAG, "Erro ao reproduzir o áudio: " + e.getMessage(), e);
        }
    }

    public void pauseAudio() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
//            handler.removeCallbacks(updateRunnable);
            Log.d(TAG, "Reprodução pausada");
        } else {
            Log.d(TAG, "MediaPlayer não está tocando");
        }
    }

    public void stopAudio() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
//            handler.removeCallbacks(updateRunnable);
            Log.d(TAG, "Reprodução parada");
        }

        stopForeground(true);
        stopSelf();
        Log.d(TAG, "Serviço finalizado após stop");
    }

    public void seekAudio(int position) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(position);
            // Forçar atualização imediata
            Intent intent = new Intent("AUDIO_PROGRESS");
            intent.putExtra("current", position);
            intent.putExtra("duration", mediaPlayer.getDuration());
            intent.putExtra("isPlaying", mediaPlayer.isPlaying());
            sendBroadcast(intent);
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Serviço destruído");

        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }

        if (mediaSession != null) {
            mediaSession.release();
        }

//        handler.removeCallbacks(updateRunnable);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Audio Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }

    public Notification createNotification() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
        );

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Audio Player")
                .setContentText("Tocando música...")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSession.getSessionToken()))
                .build();
    }

}
