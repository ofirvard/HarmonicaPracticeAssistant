package com.example.harmonicapracticeassistant.enums;

@Deprecated
public enum NaturalNote implements MusicalNote
{
    C("C"),
    D("D"),
    E("E"),
    F("F"),
    G("G"),
    A("A"),
    B("B");

    private final String name;

    NaturalNote(String name)
    {
        this.name = name;
    }

    @Override
    public String toString(boolean isSharp)
    {
        return name;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
