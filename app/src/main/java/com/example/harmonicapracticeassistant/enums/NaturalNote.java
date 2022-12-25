package com.example.harmonicapracticeassistant.enums;

public enum NaturalNote implements MusicalNote
{
    C("C", 0),
    D("D", 2),
    E("E", 4),
    F("F", 5),
    G("G", 7),
    A("A", 9),
    B("B", 11);

    private final String name;
    private final int number;

    NaturalNote(String name, int number)
    {
        this.name = name;
        this.number = number;
    }

    @Override
    public String toString(boolean isSharp)
    {
        return name;
    }

    @Override
    public int getNoteNumber()
    {
        return number;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
