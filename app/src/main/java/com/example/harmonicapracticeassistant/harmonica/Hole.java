package com.example.harmonicapracticeassistant.harmonica;

import com.example.harmonicapracticeassistant.enums.Bend;

public class Hole
{
    private final int number;
    private final Bend bend;
    private final Note note;

    public Hole(Key key, Tuning tuning, Note baseNote)
    {
        this.number = tuning.getHole();
        this.bend = tuning.getBend();
        this.note = new Note(key, tuning.getHalfSteps(), baseNote);
    }

    public String getHoleString()
    {
        return number + bend.toString();
    }

    public Note getNote()
    {
        return note;
    }

    @Override
    public String toString()
    {
        return getHoleString() + " | " + note.toString();
    }

    public boolean isNoteEqual(Note note)
    {
        return this.note.equals(note);
    }

    public String getNoteWithOctave()
    {
        return note.toString();
    }
}
