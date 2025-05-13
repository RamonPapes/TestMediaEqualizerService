package com.ramonpapes.testmediaequalizerservice;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.database.Cursor;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.ramonpapes.testmediaequalizerservice.services.AudioService;
import com.ramonpapes.testmediaequalizerservice.utils.FileUtil;

public class MainActivity extends AppCompatActivity {

    private Button btnSelect, btnPlay, btnPause, btnStop;
    private TextView txtFile;
    private String audioPath;
    private boolean isUserSeeking = false;
    private boolean isPlaying = false;


    private final ActivityResultLauncher<String> audioPicker =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    audioPath = FileUtil.getPathFromUri(this, uri);
                    txtFile.setText(getFileName(uri));
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();
    }

    private void setupViews() {
        btnSelect = findViewById(R.id.  btnSelect);
        btnPlay = findViewById(R.id.btnPlay);
        btnPause = findViewById(R.id.btnPause);
        btnStop = findViewById(R.id.btnStop);
        txtFile = findViewById(R.id.txtFile);

        btnSelect.setOnClickListener(v -> audioPicker.launch("audio/*"));

        btnPlay.setOnClickListener(v -> {
            if (audioPath != null) {
                Intent intent = new Intent(this, AudioService.class);
                intent.setAction("PLAY");
                intent.putExtra("path", audioPath);
                startService(intent);
            }
        });

        btnPause.setOnClickListener(v -> {
            Intent intent = new Intent(this, AudioService.class);
            intent.setAction("PAUSE");
            startService(intent);
        });

        btnStop.setOnClickListener(v -> {
            Intent intent = new Intent(this, AudioService.class);
            intent.setAction("STOP");
            startService(intent);
        });
    }

    private String getFileName(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        String name = "Arquivo selecionado";
        if (cursor != null) {
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            if (nameIndex >= 0 && cursor.moveToFirst()) {
                name = cursor.getString(nameIndex);
            }
            cursor.close();
        }
        return name;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint("DefaultLocale")
    private String formatTime(int millis) {
        int totalSeconds = millis / 1000;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}