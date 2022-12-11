package com.example.harmonicapracticeassistant.richter;

import com.example.harmonicapracticeassistant.harmonica.Key;

public class Hole
{
    private int number;
    private int bend;
    private Note note;

    public Hole(RichterKey key, Tuning tuning)
    {
        this.number = tuning.getHole();
        this.bend = tuning.getBend();
        this.note = new Note(key, tuning.getHalfSteps());
    }
}
