package com.example.harmonicapracticeassistant.harmonica;

import com.example.harmonicapracticeassistant.utils.NoteFinder;

public class Hole
{
    private final int number;
    private final int bend;
    private final Note note;

    public Hole(Key key, Tuning tuning, Note baseNote)
    {
        this.number = tuning.getHole();
        this.bend = tuning.getBend();
        this.note = new Note(key, tuning.getHalfSteps(), baseNote);
    }

    public String getHoleString()
    {
        return number + translateBend();
    }

    public Note getNote()
    {
        return note;
    }

    public int getNumber()
    {
        return number;
    }

    public int getBend()
    {
        return bend;
    }

    @Override
    public String toString()
    {
        return getHoleString() + " | " + note.toString();
    }

    public boolean isWithinFrequencyRange(float testFrequency)
    {
        return NoteFinder.isWithinFrequencyRange(note, testFrequency);
    }

    public boolean isNoteEqual(Note note)
    {
        return this.note.equals(note);
    }

    private String translateBend()
    {
        // TODO: 12/23/2022 change this to the enum 
        switch (bend)
        {
            case 1:
                return "'";

            case 2:
                return "\"";

            case 3:
                return "\"'";

            case 4:
                // TODO: 14/12/2022 this will be over blow/draw
                return "";

            default:
                return "";
        }
    }

    public String getNoteWithOctave()
    {
        return note.toString();
    }
}
