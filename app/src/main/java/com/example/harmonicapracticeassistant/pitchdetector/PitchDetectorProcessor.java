package com.example.harmonicapracticeassistant.pitchdetector;

import com.example.harmonicapracticeassistant.harmonica.Harmonica;
import com.example.harmonicapracticeassistant.harmonica.HarmonicaTuningType;
import com.example.harmonicapracticeassistant.harmonica.Hole;
import com.example.harmonicapracticeassistant.harmonica.Key;
import com.example.harmonicapracticeassistant.settings.AppSettings;
import com.example.harmonicapracticeassistant.utils.HarmonicaUtils;
import com.example.harmonicapracticeassistant.utils.NoteFinder;

import java.util.List;

public class PitchDetectorProcessor
{
    // TODO: 1/30/2022 this will have a list of the notes and the active key, visual
    private Harmonica harmonica;

    public PitchDetectorProcessor(AppSettings appSettings)
    {
        this.harmonica = HarmonicaUtils.getHarmonica(appSettings.getDefaultTuningType(), appSettings.getDefaultKey());
    }

    public List<Hole> processNewPitch(float frequency)
    {
        return harmonica.getAllMatchingHoles(
                NoteFinder.getNoteByFrequency(harmonica, frequency));
    }

    public Key getKey()
    {
        return harmonica.getKey();
    }

    public void setHarmonica(HarmonicaTuningType tuningType, String keyName)
    {
        this.harmonica = HarmonicaUtils.getHarmonica(tuningType, keyName);
    }
}
