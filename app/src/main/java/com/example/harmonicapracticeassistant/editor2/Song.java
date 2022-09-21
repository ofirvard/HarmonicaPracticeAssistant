package com.example.harmonicapracticeassistant.editor2;

import java.util.UUID;

public class Song
{
    private String name;
    private String notes;
    private UUID id;

    public Song(String name, String notes)
    {
        this.name = name;
        this.notes = notes;
        this.id = UUID.randomUUID();
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

    public UUID getId()
    {
        return id;
    }
}
