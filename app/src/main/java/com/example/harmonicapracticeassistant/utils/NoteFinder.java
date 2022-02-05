package com.example.harmonicapracticeassistant.utils;

import com.example.harmonicapracticeassistant.enums.MusicalNote;
import com.example.harmonicapracticeassistant.harmonica.Note;

import java.util.Comparator;
import java.util.NoSuchElementException;

import static com.example.harmonicapracticeassistant.utils.Constants.DEVIATION_PERCENT;
import static com.example.harmonicapracticeassistant.utils.Constants.NA_NOTE_FREQUENCY;

public class NoteFinder
{
    public static Note getNoteById(MusicalNote musicalNote, int octave)
    {
        for (Note note : HarmonicaUtils.getNotes())
        {
            if (note.isSameNote(musicalNote, octave))
                return note;
        }

        return null;
    }

    // TODO: 05/02/2022 rewrite stuff here once old pitch detector is no longer user
    public static Note getNoteByFrequency(float frequency)
    {
        if (frequency == NA_NOTE_FREQUENCY)
            return null;

        try
        {
            Note closestNote = HarmonicaUtils.getNotes().stream().min(Comparator.comparingDouble(note -> Math.abs(note.getFrequency() - frequency))).orElseThrow(NoSuchElementException::new);

            if (!isOffFrequency(closestNote, frequency))
            {
                return closestNote;
            }
            else
                return null;
        } catch (NoSuchElementException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private static boolean isOffFrequency(Note closestNote, float frequency)
    {
        double deviationFloor = frequency - frequency * DEVIATION_PERCENT;
        double deviationCeiling = frequency + frequency * DEVIATION_PERCENT;
        return closestNote.getFrequency() < deviationCeiling && closestNote.getFrequency() > deviationFloor;
    }
}
