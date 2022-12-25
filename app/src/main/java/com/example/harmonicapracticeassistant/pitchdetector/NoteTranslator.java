package com.example.harmonicapracticeassistant.pitchdetector;

import com.example.harmonicapracticeassistant.R;
import com.example.harmonicapracticeassistant.enums.NoteVisual;
import com.example.harmonicapracticeassistant.harmonica.Hole;

import java.util.List;

public class NoteTranslator
{
    private static NoteVisual noteVisual = NoteVisual.HOLES;

    public static String holesToString(boolean isSharp, List<Hole> holesDetected)//boolean isSharpKey, Pair<Note, List<Hole>> noteListPair)
    {
        switch (noteVisual)
        {
            case NOTE_WITH_OCTAVE:
                return holesDetected.get(0).getNoteWithOctave();

            case FREQUENCY:
                return Float.toString(holesDetected.get(0).getNote().getFrequency());

            case HOLES:
                StringBuilder holes = new StringBuilder();
                for (int i = 0; i < holesDetected.size(); i++)
                    if (i == holesDetected.size() - 1)
                        holes.append(holesDetected.get(i).getHoleString());
                    else
                        holes.append(holesDetected.get(i).getHoleString()).append("/");

                return holes.toString();

            default:
                return "";
        }
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
