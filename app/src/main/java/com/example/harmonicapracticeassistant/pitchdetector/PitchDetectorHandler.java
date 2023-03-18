package com.example.harmonicapracticeassistant.pitchdetector;

import android.util.Log;

import com.example.harmonicapracticeassistant.utils.Tags;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchProcessor;

public class PitchDetectorHandler
{
    private boolean isRunning;
    private float frequency;
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

    public boolean isRunning()
    {
        return isRunning;
    }
}
