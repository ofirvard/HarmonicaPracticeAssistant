package com.example.harmonicapracticeassistant.harmonica;

public class Key
{
    private String keyName;
    private boolean isSharp;
    private HarmonicaTuningType tuningType;
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

    public boolean isSameTuningType(HarmonicaTuningType tuningType)
    {
        return this.tuningType == tuningType;
    }

    @Override
    public String toString()
    {
        String sharp;
        if (isSharp)
            sharp = "#";
        else
            sharp = "b";

        return tuningType + " | " + keyName + " | " + sharp + " | " + baseNote.toString();
    }
}
