package com.example.harmonicapracticeassistant.pitchdetector2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

        PitchDetectionHandler pdh = (res, e) -> {
            frequency = res.getPitch();
        };
        AudioProcessor p = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);

        dispatcher.addAudioProcessor(p);
        new Thread(dispatcher, "Audio Dispatcher").start();
        System.out.println("dispatcher has started");
        isRunning = true;
    }

    public void stop()
    {
        dispatcher.stop();
        System.out.println("dispatcher has been shutdown");
        isRunning = false;
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
