package com.example.harmonicapracticeassistant.pitchdetector2;

import android.util.Log;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchProcessor;

public class PitchDetectorHandler
{
    // TODO: 1/29/2022 this will do the pitch detection
    private boolean isRunning;
    private float frequency;
    private AudioDispatcher dispatcher;

    public void start()
    {
        dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0);

        PitchDetectionHandler pdh = (res, e) -> frequency = res.getPitch();
        AudioProcessor p = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);

        dispatcher.addAudioProcessor(p);
        new Thread(dispatcher, "Audio Dispatcher").start();
        isRunning = true;
        Log.d("Hertz Update", "dispatcher started");
    }

    public void stop()
    {
        dispatcher.stop();
        isRunning = false;
        Log.d("Hertz Update", "dispatcher stopped");
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
