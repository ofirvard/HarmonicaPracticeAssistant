package com.example.harmonicapracticeassistant.pitchdetector;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchProcessor;

public class PitchDetector
{
    private Thread audioThread;
    private AudioDispatcher dispatcher;

    public void startDetector(PitchDetectorActivity activity)
    {
        PitchDetectionHandler pdh = (res, e) -> {
            final float pitchInHz = res.getPitch();
            activity.runOnUiThread(() -> activity.processPitch(pitchInHz));
        };

        AudioProcessor pitchProcessor = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);

        dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0);
        dispatcher.addAudioProcessor(pitchProcessor);

        audioThread = new Thread(dispatcher, "Audio Thread");
        audioThread.start();
    }

    public void stopDetector()
    {
        if (dispatcher != null)
            dispatcher.stop();
        if (audioThread != null)
            audioThread.interrupt();
    }
}
