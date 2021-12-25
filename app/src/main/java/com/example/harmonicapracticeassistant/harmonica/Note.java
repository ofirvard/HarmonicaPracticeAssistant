package com.example.harmonicapracticeassistant.harmonica;

import com.example.harmonicapracticeassistant.enums.MusicalNote;
import com.example.harmonicapracticeassistant.utils.NoteFinder;

import androidx.annotation.NonNull;

import static com.example.harmonicapracticeassistant.utils.Constants.NA_NOTE_FREQUENCY;

public class Note
{
    private MusicalNote musicalNote;
    private int octave;
    private float frequency;

    public Note(float frequency)
    {

        if (frequency != NA_NOTE_FREQUENCY)
        {
            Note note = NoteFinder.getNoteByFrequency(frequency);

            this.musicalNote = note.getMusicalNote();
            this.octave = note.getOctave();
            this.frequency = note.getFrequency();
        }
        else
            this.frequency = frequency;
    }

    public Note(Note note)
    {
        this.musicalNote = note.getMusicalNote();
        this.octave = note.getOctave();
        this.frequency = note.getFrequency();
    }

    public void setFrequency(float frequency)
    {
        this.frequency = frequency;
    }

    public MusicalNote getMusicalNote()
    {
        return musicalNote;
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
        return getNoteString() + octave;
    }

    public boolean isSameNote(MusicalNote note, int octave)
    {
        return this.octave == octave && this.musicalNote == note;
    }

    public boolean isSameNote(Note note)
    {
        return isSameNote(note.getMusicalNote(), note.getOctave());
    }

    public String getNoteString()
    {
        if (musicalNote == null)
            return "";

        switch (musicalNote)
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

    public String getNoteString(boolean isSharp)
    {
        switch (musicalNote)
        {
            case C:
                return "C";

            case Db:
                if (isSharp)
                    return "C#";
                return "Db";

            case D:
                return "D";

            case Eb:
                if (isSharp)
                    return "D#";
                return "Eb";

            case E:
                return "E";

            case F:
                return "F";

            case Gb:
                if (isSharp)
                    return "F#";
                return "Gb";

            case G:
                return "G";

            case Ab:
                if (isSharp)
                    return "G#";
                return "Ab";

            case A:
                return "A";

            case Bb:
                if (isSharp)
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
