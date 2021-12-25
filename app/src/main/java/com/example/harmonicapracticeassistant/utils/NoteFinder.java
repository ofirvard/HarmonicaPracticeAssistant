package com.example.harmonicapracticeassistant.utils;

import com.example.harmonicapracticeassistant.enums.MusicalNote;
import com.example.harmonicapracticeassistant.harmonica.Hole;
import com.example.harmonicapracticeassistant.harmonica.Key;
import com.example.harmonicapracticeassistant.harmonica.Note;

import java.util.Comparator;
import java.util.NoSuchElementException;

import static com.example.harmonicapracticeassistant.utils.Constants.DEVIATION_PERCENT;
import static com.example.harmonicapracticeassistant.utils.Constants.NA_NOTE_FREQUENCY;

public class NoteFinder
{
    public static final Note nullNote = new Note(NA_NOTE_FREQUENCY);

    public static Note getNoteById(MusicalNote musicalNote, int octave)
    {
        for (Note note : HarmonicaUtils.getNotes())
        {
            if (note.isSameNote(musicalNote, octave))
                return note;
        }

        return nullNote;
    }

    public static Note getNoteByFrequency(float frequency)
    {
        if (frequency == NA_NOTE_FREQUENCY)
            return nullNote;

        try
        {
            Note closestNote = HarmonicaUtils.getNotes().stream().min(Comparator.comparingDouble(note -> Math.abs(note.getFrequency() - frequency))).orElseThrow(NoSuchElementException::new);

            if (!isOffFrequency(closestNote, frequency))
            {
                return closestNote;
            }
            else
                return nullNote;
        } catch (NoSuchElementException e)
        {
            e.printStackTrace();
        }
        return nullNote;
    }

    public static Note getNoteByFrequencyAndKey(float frequency, Key key)
    {
        Note note = getNoteByFrequency(frequency);

        // TODO: 12/23/2021 check that note is in key
        Hole hole = key.getHole(note.getMusicalNote(), note.getOctave());


        return nullNote;
    }

    private static boolean isOffFrequency(Note closestNote, float frequency)
    {
        double deviationFloor = frequency - frequency * DEVIATION_PERCENT;
        double deviationCeiling = frequency + frequency * DEVIATION_PERCENT;
        return closestNote.getFrequency() < deviationCeiling && closestNote.getFrequency() > deviationFloor;
    }
}
