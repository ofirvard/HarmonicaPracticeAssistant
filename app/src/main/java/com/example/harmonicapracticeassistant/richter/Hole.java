package com.example.harmonicapracticeassistant.richter;

public class Hole
{
    private final int number;
    private final int bend;
    private final Note note;

    public Hole(RichterKey key, Tuning tuning)
    {
        this.number = tuning.getHole();
        this.bend = tuning.getBend();
        this.note = new Note(key, tuning.getHalfSteps());
    }

    public String getHoleString()
    {
        return number + translateBend();
    }

    @Override
    public String toString()
    {
        return getHoleString() + " | " + note.toString();
    }

    private String translateBend()
    {
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
}
