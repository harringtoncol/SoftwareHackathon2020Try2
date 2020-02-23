package com.harrington.softwarehackathon2020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }


    private boolean Recording;

    AudioTrack audioTrack;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //String[] arrayOfStrings = new String[8];
      //  file = new File("findme.txt");
      //  Log.d("Path","path: " + Environment.getExternalStorageDirectory().getAbsolutePath() );
        final ImageButton recordButton = findViewById(R.id.buttonMic);
        final ImageButton playButton = findViewById((R.id.butn4));
        boolean b = false;

        playButton.setVisibility(View.INVISIBLE);

        recordButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // AudioPlaying audioPlayer = new AudioPlaying();

             //   Intent intent = new Intent(MainActivity.this, AudioPlaying.class);
               // startActivity(intent);


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Recording = true;
                        startRecord();

                    }
                }).start();
               // Toast.makeText(getBaseContext(), "YEs yes yes?", Toast.LENGTH_SHORT).show();
                //audioPlayer.run();
                // audio.onRequestPermissionsResult();
                //audio.startRecording();
                recordButton.setVisibility(View.INVISIBLE);
                playButton.setVisibility(View.VISIBLE);
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (file.exists()) {
                    playRecord();
                }

                recordButton.setVisibility(View.VISIBLE);
                playButton.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void playRecord() {
        int i = 0;

        int shortSizeInBytes = Short.SIZE/ Byte.SIZE;

        int bufferSizeInBytes = (int) file.length();
        short[] audioData = new short[bufferSizeInBytes];

        try {
            InputStream inputStream= new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            DataInputStream dataInputStream = new DataInputStream(bufferedInputStream);

            int j = 0;
try {

    while (dataInputStream.available() > 0){
        audioData[j] = dataInputStream.readShort();
        j++;


    }

    dataInputStream.close();

    //THIS IS WHERE WE EDIT SHIT BASED ON RYAN'S FREQUENCY CHANGER CODE THAT HE IS WRITING---------------------------------


    audioTrack = new AudioTrack(3,1,2, 2, bufferSizeInBytes, 1);

    audioTrack.play();
    audioTrack.write(audioData, 0, bufferSizeInBytes);
}catch(IOException e){
    e.printStackTrace();
}
        } catch (FileNotFoundException e) {

        }

    }

    private void startRecord() {
        File myFile = new File("/Internal storage/findthisapp", "test.pcm");

        try {
            myFile.createNewFile();
            OutputStream outputStream = new FileOutputStream(myFile);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);

            DataOutputStream dataOutputStream = new DataOutputStream(bufferedOutputStream);

            int minBufferSize = AudioRecord.getMinBufferSize(11025, 2, 2);

            short[] audioData = new short[minBufferSize];

            AudioRecord audioRecord = new AudioRecord(1, 11025, 2, 2, minBufferSize);

            audioRecord.startRecording();

            while (Recording) {
                int numberOfShort = audioRecord.read(audioData, 0, minBufferSize);
                for (int i = 0; i < numberOfShort; i++) {
                    dataOutputStream.writeShort(audioData[i]);

                }
            }

            if (Recording) {
                audioRecord.stop();
                Toast.makeText(getBaseContext(), "YEs yes yes?",
                        Toast.LENGTH_LONG).show();
                dataOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Recording = false;
        if (audioTrack != null) {
            audioTrack.release();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
