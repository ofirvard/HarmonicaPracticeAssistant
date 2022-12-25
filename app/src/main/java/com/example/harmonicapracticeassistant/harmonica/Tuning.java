package com.example.harmonicapracticeassistant.harmonica;

public class Tuning
{
    private int hole;
    private int bend;
    private int halfSteps;

    public int getHole()
    {
        return hole;
    }

    public int getBend()
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
