package com.harrington.softwarehackathon2020;

import android.Manifest;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.IOException;

import static android.os.Environment.getExternalStorageDirectory;

public class AudioPlaying {



    private Spinner spFrequency;

    public void run() {
        int n = 1;

       // ActivityCompat.requestPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, n);

        MediaRecorder mediaRecorder = new MediaRecorder();

        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);



       // mediaRecorder.setOutputFile(getExternalStorageDirectory().getAbsolutePath() + "/inputFile.txt");

        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        }catch(IOException e){
            Log.e("error", "error");
        }

    }



}


