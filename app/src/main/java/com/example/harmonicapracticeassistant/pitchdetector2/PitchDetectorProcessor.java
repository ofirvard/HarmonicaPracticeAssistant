package com.example.harmonicapracticeassistant.pitchdetector2;

import com.example.harmonicapracticeassistant.harmonica.Key;
import com.example.harmonicapracticeassistant.utils.HarmonicaUtils;

import static com.example.harmonicapracticeassistant.utils.Constants.DEFAULT_KEY;

public class PitchDetectorProcessor
{
    // TODO: 1/30/2022 this will have a list of the notes and the active key, visual
    // TODO: 1/30/2022 will have a list of notes that is extened by hole and has to string ish kind of func  
    Key key;

    public PitchDetectorProcessor()
    {
        // TODO: 1/30/2022 set key
        key = HarmonicaUtils.getKey(DEFAULT_KEY);
    }

    public String processNewPitch()
    {
        // TODO: 1/30/2022 give this the pitch and it will add it and check with key
        return "";
    }
}
