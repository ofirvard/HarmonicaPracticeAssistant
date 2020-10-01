package com.example.harmonicapracticeassistant;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Song implements Parcelable
{
    private String name;
    private String tabs;
    private boolean hasMainRecording;
    private List<String> recordings;

    public Song(String name, String tabs, boolean hasMainRecording, List<String> recordings)
    {
        this.name = name;
        this.tabs = tabs;
        this.hasMainRecording = hasMainRecording;
        this.recordings = recordings;
    }

    protected Song(Parcel in)
    {
        name = in.readString();
        tabs = in.readString();
        hasMainRecording = in.readByte() != 0x00;
        if (in.readByte() == 0x01)
        {
            recordings = new ArrayList<>();
            in.readList(recordings, String.class.getClassLoader());
        }
        else
        {
            recordings = null;
        }
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
        dest.writeString(tabs);
        dest.writeByte((byte) (hasMainRecording ? 0x01 : 0x00));
        if (recordings == null)
        {
            dest.writeByte((byte) (0x00));
        }
        else
        {
            dest.writeByte((byte) (0x01));
            dest.writeList(recordings);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Song> CREATOR = new Parcelable.Creator<Song>()
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

    @Override
    public String toString()
    {
        return "Song{" +
                "name='" + name + '\'' +
                '}';
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getTabs()
    {
        return tabs;
    }

    public void setTabs(String tabs)
    {
        this.tabs = tabs;
    }
}
