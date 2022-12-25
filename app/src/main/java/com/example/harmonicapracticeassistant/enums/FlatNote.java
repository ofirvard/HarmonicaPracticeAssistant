package com.example.harmonicapracticeassistant.enums;

public enum FlatNote implements MusicalNote
{
    Db("D", "C", 1),
    Eb("E", "D", 3),
    Gb("G", "F", 6),
    Ab("A", "G", 8),
    Bb("B", "A", 10);

    private final String flatName;
    private final String sharpName;
    private final int number;

    FlatNote(String flatName, String sharpName, int number)
    {
        this.flatName = flatName;
        this.sharpName = sharpName;
        this.number = number;
    }

    @Override
    public String toString(boolean isSharp)
    {
        return isSharp ? sharpName + "#" : flatName + "b";
    }

    @Override
    public int getNoteNumber()
    {
        return number;
    }

    @Override
    public String toString()
    {
        return toString(true);
    }


}
