package com.youstra.jack.audiotranfer;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.audiofx.Visualizer;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;


public class MainActivity extends ActionBarActivity {

    private Visualizer audioOutput = null;
    AudioTrack visualizedTrack = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createVisualizer();

        ToggleButton visualizeOutput = (ToggleButton) findViewById(R.id.visualizeOutputToggle);
        visualizeOutput.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    startVisualizer();
                }
                else{
                    stopVisualizer();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyVisualizer();
    }

    private void createVisualizer(){
        final int minBufferSize = AudioTrack.getMinBufferSize(Visualizer.getMaxCaptureRate(), AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_8BIT);
        visualizedTrack = new AudioTrack(AudioManager.STREAM_MUSIC, Visualizer.getMaxCaptureRate(), AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_8BIT, minBufferSize, AudioTrack.MODE_STREAM);
        visualizedTrack.play();
        audioOutput = new Visualizer(0); // get output audio stream

        audioOutput.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
            @Override
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
                visualizedTrack.write(waveform, 0, waveform.length);
            }

            @Override
            public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {

            }
        }, Visualizer.getMaxCaptureRate(), true, false); // waveform not freq data


    }

    private void startVisualizer(){
        audioOutput.setEnabled(true);
    }

    private void stopVisualizer() {
        audioOutput.setEnabled(false);
    }

    private void destroyVisualizer(){
        visualizedTrack.stop();
        visualizedTrack.release();
        audioOutput.release();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
