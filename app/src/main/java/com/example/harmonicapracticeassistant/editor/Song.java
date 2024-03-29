package com.example.harmonicapracticeassistant.editor;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

public class Song implements Parcelable
{
    private final String id;
    private String name;
    private String notes;
    private boolean favourite = false;

    public Song(String name, String notes)
    {
        this.name = name;
        this.notes = notes;
        this.id = UUID.randomUUID().toString();
    }

    protected Song(Parcel in)
    {
        name = in.readString();
        notes = in.readString();
        id = in.readString();
        favourite = in.readBoolean();
    }

    public static final Creator<Song> CREATOR = new Creator<Song>()
    {
        @Override
        public Song createFromParcel(Parcel in)
        {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size)
        {
            return new Song[size];
        }
    };

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

    public String getId()
    {
        return id;
    }

    public boolean isFavourite()
    {
        return favourite;
    }

    public void setFavourite(boolean favourite)
    {
        this.favourite = favourite;
    }

    @Override
    public String toString()
    {
        return String.format("%s | %s | %s", id, name, notes);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(name);
        dest.writeString(notes);
        dest.writeString(id);
        dest.writeBoolean(favourite);
    }

    public void update(Song song)
    {
        setFavourite(song.isFavourite());
        setName(song.getName());
        setNotes(song.getNotes());
    }
}
