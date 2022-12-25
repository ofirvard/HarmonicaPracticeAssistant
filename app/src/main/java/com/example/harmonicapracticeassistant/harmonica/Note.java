package com.example.harmonicapracticeassistant.harmonica;

import com.example.harmonicapracticeassistant.enums.MusicalNote;
import com.example.harmonicapracticeassistant.utils.MusicalNoteUtil;

import java.util.Comparator;

public class Note
{
    public static Comparator<Note> noteComparator = (n1, n2) -> {
        int octave1 = ((Note) n1).getOctave();
        int octave2 = ((Note) n2).getOctave();
        int octaveComp = Integer.compare(octave1, octave2);

        if (octaveComp != 0)
            return octaveComp;

        int musicalNote1 = ((Note) n1).getNoteNumber();
        int musicalNote2 = ((Note) n2).getNoteNumber();

        return Integer.compare(musicalNote1, musicalNote2);
    };

    private final MusicalNote musicalNote;
    private final int octave;
    private final boolean isSharp;
    private final float freq;

    public Note(MusicalNote musicalNote, int octave, float freq)
    {
        this.musicalNote = musicalNote;
        this.octave = octave;
        this.isSharp = true;
        this.freq = freq;
    }

    public Note(MusicalNote musicalNote, int octave, Note baseNote)
    {
        this.musicalNote = musicalNote;
        this.octave = octave;
        this.isSharp = true;
        this.freq = calculateFreq(baseNote);
    }

    public Note(Key key, int halfSteps, Note baseNote)
    {
        this.musicalNote = calculateNote(key, halfSteps);
        this.octave = calculateOctave(key, halfSteps);
        this.isSharp = key.isSharp();
        this.freq = calculateFreq(baseNote);
    }

    public int getNoteNumber()
    {
        return musicalNote.getNoteNumber();
    }

    public int getOctave()
    {
        return octave;
    }

    public float getFrequency()
    {
        return freq;
    }

    public boolean equals(Note note)
    {
        return musicalNote.equals(note.musicalNote) && octave == note.octave;
    }

    @Override
    public String toString()
    {
        return musicalNote.toString(isSharp) + octave;
    }

    private float calculateFreq(Note baseNote)
    {
        int octaveSteps = octave - baseNote.octave;
        int noteHalfSteps = musicalNote.getNoteNumber() - baseNote.musicalNote.getNoteNumber();

        return (float) (baseNote.freq * (Math.pow(2, (1.0 / 12.0) * (noteHalfSteps + (12 * octaveSteps)))));
    }

    private MusicalNote calculateNote(Key key, int halfSteps)
    {
        int numberOfNotesInAnOctave = 12;

        return MusicalNoteUtil.getMusicalNoteFromNumber((((key.getNoteNumber()
                + halfSteps)
                % numberOfNotesInAnOctave)
                + numberOfNotesInAnOctave)
                % numberOfNotesInAnOctave);
    }

    private int calculateOctave(Key key, int halfSteps)
    {
        return (int) (key.getOctave() + Math.floor((double) (key.getNoteNumber() + halfSteps) / 12));
    }
}
