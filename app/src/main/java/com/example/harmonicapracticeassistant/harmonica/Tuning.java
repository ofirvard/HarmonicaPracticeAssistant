package com.example.harmonicapracticeassistant.harmonica;

import com.example.harmonicapracticeassistant.enums.Bend;

public class Tuning
{
    private final int hole;
    private final Bend bend;
    private final int halfSteps;

    public Tuning(int hole, Bend bend, int halfSteps)
    {
        this.hole = hole;
        this.bend = bend;
        this.halfSteps = halfSteps;
    }

    public int getHole()
    {
        return hole;
    }

    public Bend getBend()
    {
        return bend;
    }

    public int getHalfSteps()
    {
        return halfSteps;
    }

    @Override
    public String toString()
    {
        return hole + " | " + bend + " | " + halfSteps;
    }
}
