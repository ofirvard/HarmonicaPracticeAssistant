package com.example.harmonicapracticeassistant.pitchdetector2;

import com.example.harmonicapracticeassistant.R;
import com.example.harmonicapracticeassistant.enums.NoteVisual;
import com.example.harmonicapracticeassistant.harmonica.Hole;
import com.example.harmonicapracticeassistant.harmonica.Key;
import com.example.harmonicapracticeassistant.utils.HarmonicaUtils;

import java.text.DecimalFormat;
import java.util.List;

import static com.example.harmonicapracticeassistant.utils.Constants.DEFAULT_KEY;
import static com.example.harmonicapracticeassistant.utils.Constants.NOT_APPLICABLE;
import static com.example.harmonicapracticeassistant.utils.Constants.NO_KEY;

public class PitchDetectorProcessor
{
    // TODO: 1/30/2022 this will have a list of the notes and the active key, visual
    Key key;
    DecimalFormat decimalFormat = new DecimalFormat("#.##");
    NoteVisual noteVisual = NoteVisual.HOLES;
    // TODO: 05/02/2022 add list that is grouping of holes

    public PitchDetectorProcessor()
    {
        key = HarmonicaUtils.getKey(DEFAULT_KEY);
    }

    public String processNewPitch(float frequency)
    {
        // TODO: 1/30/2022 give this the pitch and it will add it and check with key
        // TODO: 05/02/2022 add to a list

        List<Hole> holes = key.findHoles(frequency);


        // TODO: 05/02/2022 here check what the visual is and procede acordingly


        // TODO: 05/02/2022 temporarly only show freq but only if its in the key
        return holesToString(holes);
    }


    private String holesToString(List<Hole> holes)
    {
        if (holes.size() == 0)
            return NOT_APPLICABLE;

        StringBuilder s;


        switch (noteVisual)
        {
            case NOTE_WITH_OCTAVE:
                s = new StringBuilder(holes.get(0).getNoteWithOctave(key.isSharp()));
                break;

            case FREQUENCY:
                s = new StringBuilder(Float.toString(holes.get(0).getFrequency()));
                break;

            case HOLES:
                s = new StringBuilder(Integer.toString(holes.get(0).getHole()));
                for (int i = 1; i < holes.size(); i++)
                    s.append("/").append(holes.get(i).getHole());
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + noteVisual);
        }

        return s.toString();
    }

    public int switchVisual()
    {
        // NOTE_WITH_OCTAVE -> FREQUENCY -> (HOLES only key) -> NOTE_WITH_OCTAVE
        switch (noteVisual)
        {
            case NOTE_WITH_OCTAVE:
                noteVisual = NoteVisual.FREQUENCY;
                return R.string.frequency;

            case FREQUENCY:
                if (key.getKeyName().equals(NO_KEY))
                {
                    noteVisual = NoteVisual.NOTE_WITH_OCTAVE;
                    return R.string.notes;
                }
                else
                {
                    noteVisual = NoteVisual.HOLES;
                    return R.string.holes;
                }

            default:
                noteVisual = NoteVisual.NOTE_WITH_OCTAVE;
                return R.string.notes;
        }
    }
}
