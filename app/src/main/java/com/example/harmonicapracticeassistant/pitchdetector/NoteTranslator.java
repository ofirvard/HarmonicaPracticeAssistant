package com.example.harmonicapracticeassistant.pitchdetector;

import android.util.Pair;

import com.example.harmonicapracticeassistant.R;
import com.example.harmonicapracticeassistant.enums.NoteVisual;
import com.example.harmonicapracticeassistant.harmonica.Hole;
import com.example.harmonicapracticeassistant.harmonica.Note;

import java.util.List;

import static com.example.harmonicapracticeassistant.utils.Constants.NOT_APPLICABLE;

public class NoteTranslator
{
    private static NoteVisual noteVisual = NoteVisual.HOLES;

    public static NoteVisual getNoteVisual()
    {
        return noteVisual;
    }

    public static String holesToString(boolean isSharpKey, Pair<Note, List<Hole>> noteListPair)
    {
        if (noteListPair.second.size() == 0)
            return NOT_APPLICABLE;

        StringBuilder s;


        switch (noteVisual)
        {
            case NOTE_WITH_OCTAVE:
                s = new StringBuilder(noteListPair.first.getNoteWithOctave(isSharpKey));
                break;

            case FREQUENCY:
                s = new StringBuilder(Float.toString(noteListPair.first.getFrequency()));
                break;

            case HOLES:
                s = new StringBuilder(Integer.toString(noteListPair.second.get(0).getHole()));
                for (int i = 1; i < noteListPair.second.size(); i++)
                    s.append("/").append(noteListPair.second.get(i).getHole());
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + noteVisual);
        }

        return s.toString();
    }

    public static int switchVisual()
    {
        // NOTE_WITH_OCTAVE -> FREQUENCY -> HOLES -> NOTE_WITH_OCTAVE
        switch (noteVisual)
        {
            case NOTE_WITH_OCTAVE:
                noteVisual = NoteVisual.FREQUENCY;
                return R.string.frequency;

            case FREQUENCY:
                noteVisual = NoteVisual.HOLES;
                return R.string.holes;

            default:
                noteVisual = NoteVisual.NOTE_WITH_OCTAVE;
                return R.string.notes;
        }
    }
}
