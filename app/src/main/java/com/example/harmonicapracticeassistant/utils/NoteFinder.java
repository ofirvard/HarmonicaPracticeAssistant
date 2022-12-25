package com.example.harmonicapracticeassistant.utils;

import com.example.harmonicapracticeassistant.harmonica.Harmonica;
import com.example.harmonicapracticeassistant.harmonica.Note;

import static com.example.harmonicapracticeassistant.utils.Constants.DEVIATION_PERCENT;
import static com.example.harmonicapracticeassistant.utils.Constants.NA_NOTE_FREQUENCY;

public class NoteFinder
{
    // TODO: 05/02/2022 rewrite stuff here once old pitch detector is no longer user
    public static Note getNoteByFrequency(Harmonica harmonica, float frequency)
    {
        if (frequency == NA_NOTE_FREQUENCY)
            return null;

        Note closestNote = null;

        for (Note note : harmonica.getAllNotes())
            if (closestNote == null)
                closestNote = note;
            else if (Math.abs(note.getFrequency() - frequency) <
                    Math.abs(closestNote.getFrequency() - frequency))
                closestNote = note;


        if (isWithinFrequencyRange(closestNote, frequency))
            return closestNote;

        return null;
    }

    public static boolean isWithinFrequencyRange(Note note, float frequency)
    {
        double deviationFloor = note.getFrequency() - note.getFrequency() * DEVIATION_PERCENT;
        double deviationCeiling = note.getFrequency() + note.getFrequency() * DEVIATION_PERCENT;

        return deviationFloor < frequency && frequency < deviationCeiling;
    }
}
