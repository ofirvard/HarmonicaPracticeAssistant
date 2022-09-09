package com.example.harmonicapracticeassistant.editor2;

public class Song
{
    private String name;
    private String notes;
    private int id;

    public Song(String name, String notes, int id)
    {
        this.name = name;
        this.notes = notes;
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getNotes()
    {
        return notes;
    }

    public void setNotes(String notes)
    {
        this.notes = notes;
    }

    public int getId()
    {
        return id;
    }
}
