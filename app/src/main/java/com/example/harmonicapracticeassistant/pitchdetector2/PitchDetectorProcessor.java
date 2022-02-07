package com.example.harmonicapracticeassistant.pitchdetector2;

import android.util.Pair;

import com.example.harmonicapracticeassistant.harmonica.Hole;
import com.example.harmonicapracticeassistant.harmonica.Key;
import com.example.harmonicapracticeassistant.harmonica.Note;
import com.example.harmonicapracticeassistant.utils.HarmonicaUtils;

import java.util.List;

import static com.example.harmonicapracticeassistant.utils.Constants.DEFAULT_KEY;
import static com.example.harmonicapracticeassistant.utils.Constants.NO_KEY;

public class PitchDetectorProcessor
{
    // TODO: 1/30/2022 this will have a list of the notes and the active key, visual
    private Key key;

    public PitchDetectorProcessor()
    {
        key = HarmonicaUtils.getKey(DEFAULT_KEY);
    }

    public Pair<Note, List<Hole>> processNewPitch(float frequency)
    {
        // TODO: 1/30/2022 give this the pitch and it will add it and check with key
        // TODO: 05/02/2022 add to a list

        List<Hole> holes = key.findHoles(frequency);

        if (holes.size() == 0)
            return null;

        return new Pair<>(holes.get(0).getNote(), holes);
    }

    public Key getKey()
    {
        return key;
    }

    public void setCurrentKey(Key key)
    {
        this.key = key;
    }

    public boolean isCurrentKeyNone()
    {
        return key.getKeyName().equals(NO_KEY);
    }
}
