package com.example.harmonicapracticeassistant.pitchdetector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import com.example.harmonicapracticeassistant.utils.Tags;
import com.lbbento.pitchuptuner.GuitarTuner;
import com.lbbento.pitchuptuner.GuitarTunerListener;
import com.lbbento.pitchuptuner.audio.PitchAudioRecorder;
import com.lbbento.pitchuptuner.service.TunerResult;

import androidx.annotation.NonNull;
import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchProcessor;

public class PitchDetectorHandler
{
    private boolean isRunning;
    private float frequency;
    private float testFrequency;
    private AudioDispatcher dispatcher;

    public void start()
    {
        dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0);

        PitchDetectionHandler pdh = (res, e) -> {
            Log.d(Tags.TAROS, String.valueOf(res.getPitch()));
            frequency = res.getPitch();
        };
        AudioProcessor p = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);

        dispatcher.addAudioProcessor(p);
        new Thread(dispatcher, "Audio Dispatcher").start();
        isRunning = true;
        Log.d(Tags.HERTZ_UPDATE, "dispatcher started");
    }

    public void newStart(Context context)
    {
        @SuppressLint("MissingPermission") PitchAudioRecorder pitchAudioRecorder = new PitchAudioRecorder(new AudioRecord(MediaRecorder.AudioSource.DEFAULT,
                44100,
                AudioFormat.CHANNEL_IN_DEFAULT,
                AudioFormat.ENCODING_PCM_16BIT,
                AudioRecord.getMinBufferSize(44100,
                        AudioFormat.CHANNEL_IN_DEFAULT,
                        AudioFormat.ENCODING_PCM_16BIT)));

        GuitarTunerListener guitarTunerListener = new GuitarTunerListener()
        {
            @Override
            public void onNoteReceived(@NonNull TunerResult tunerResult)
            {
                if (tunerResult.getExpectedFrequency() != 0)
                {
                    Log.d(Tags.LBBENTO, tunerResult.getExpectedFrequency() + "|" + tunerResult.getDiffFrequency());
                    testFrequency = (float) tunerResult.getExpectedFrequency();
                }
            }

            @Override
            public void onError(@NonNull Throwable throwable)
            {

            }
        };

        GuitarTuner guitarTuner = new GuitarTuner(pitchAudioRecorder, guitarTunerListener);

        guitarTuner.start();
    }

    public void stop()
    {// TODO: 08/03/2022 fix this
        if (dispatcher != null && isRunning)
            dispatcher.stop();
        isRunning = false;
        Log.d(Tags.HERTZ_UPDATE, "dispatcher stopped");
    }

    public float getFrequency()
    {
        return frequency;
    }

    public float getTestFrequency()
    {
        return testFrequency;
    }


    public boolean isRunning()
    {
        return isRunning;
    }
}
