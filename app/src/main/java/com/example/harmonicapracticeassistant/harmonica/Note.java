package com.example.harmonicapracticeassistant.harmonica;

import com.example.harmonicapracticeassistant.enums.MusicalNote;
import com.example.harmonicapracticeassistant.utils.NoteFinder;

import androidx.annotation.NonNull;

import static com.example.harmonicapracticeassistant.utils.Constants.NA_NOTE_FREQUENCY;

public class Note
{
    private MusicalNote note;
    private int octave;
    private final float frequency;

    public Note(float frequency)
    {

        if (frequency != NA_NOTE_FREQUENCY)
        {
            Note note = NoteFinder.getNoteByFrequency(frequency);

            this.note = note.getNote();
            this.octave = note.getOctave();
            this.frequency = note.getFrequency();
        }
        else
            this.frequency = frequency;
    }

    public String getNote()
    {
        return note;
    }

    public int getOctave()
    {
        return octave;
    }

    public float getFrequency()
    {
        return frequency;
    }

    public String getNoteWithOctave()
    {
        return note + octave;
    }

    public boolean isSameNote(MusicalNote note, int octave)
    {
        return this.octave == octave && this.note == note;
    }

    public String getNoteString()
    {
        switch (note)
        {
            case C:
                return "C";

            case Db:
                return "C#/Db";

            case D:
                return "D";

            case Eb:
                return "D#/Eb";

            case E:
                return "E";

            case F:
                return "F";

            case Gb:
                return "F#/Gb";

            case G:
                return "G";

            case Ab:
                return "G#/Ab";

            case A:
                return "A";

            case Bb:
                return "A#/Bb";

            case B:
                return "B";
        }

        return "";
    }

    public String getNoteString(boolean isSHarp)
    {
        switch (note)
        {
            case C:
                return "C";

            case Db:
                if (isSHarp)
                    return "C#";
                return "Db";

            case D:
                return "D";

            case Eb:
                if (isSHarp)
                    return "D#";
                return "Eb";

            case E:
                return "E";

            case F:
                return "F";

            case Gb:
                if (isSHarp)
                    return "F#";
                return "Gb";

            case G:
                return "G";

            case Ab:
                if (isSHarp)
                    return "G#";
                return "Ab";

            case A:
                return "A";

            case Bb:
                if (isSHarp)
                    return "A#";
                return "Bb";

            case B:
                return "B";
        }

        return "";
    }

    @NonNull
    @Override
    public String toString()
    {
        return getNoteWithOctave();
    }
}
