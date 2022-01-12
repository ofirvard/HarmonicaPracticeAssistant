package com.example.harmonicapracticeassistant.enums;

public enum FlatNote implements MusicalNote
{
    Db("D", "C"),
    Eb("E", "D"),
    Gb("G", "F"),
    Ab("A", "G"),
    Bb("B", "A");

    private final String flatName;
    private final String sharpName;

    FlatNote(String flatName, String sharpName)
    {
        this.flatName = flatName;
        this.sharpName = sharpName;
    }

    @Override
    public String toString(boolean isflat)
    {
        return isflat ? flatName + "b" : sharpName + "#";
    }

    @Override
    public String toString()
    {
        return toString(true);
    }
}
