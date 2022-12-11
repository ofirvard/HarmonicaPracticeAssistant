package com.example.harmonicapracticeassistant.richter;

public class RichterKey
{
    private String keyName;
    private boolean isSharp;
    private Note baseNote;

    public String getKeyName()
    {
        return keyName;
    }

    public boolean isSharp()
    {
        return isSharp;
    }

    public int getNoteNumber()
    {
        return baseNote.getNoteNumber();
    }

    public int getOctave()
    {
        return baseNote.getOctave();
    }

    @Override
    public String toString()
    {
        String sharp;
        if (isSharp)
            sharp = "#";
        else
            sharp = "b";

        return keyName + " | " + sharp + " | " + baseNote.toString(this);
    }
}
