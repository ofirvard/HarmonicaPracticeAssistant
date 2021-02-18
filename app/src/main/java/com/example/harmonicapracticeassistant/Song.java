package com.example.harmonicapracticeassistant;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Song implements Parcelable
{
    private String name;
    private boolean hasMainRecording;
    private List<String> recordings;
    private List<Integer> notes;

    protected Song(Parcel in)
    {
        name = in.readString();
        hasMainRecording = in.readByte() != 0x00;
        if (in.readByte() == 0x01)
        {
            recordings = new ArrayList<String>();
            in.readList(recordings, String.class.getClassLoader());
        }
        else
        {
            recordings = null;
        }
        if (in.readByte() == 0x01)
        {
            notes = new ArrayList<Integer>();
            in.readList(notes, Integer.class.getClassLoader());
        }
        else
        {
            notes = null;
        }
    }

    public Song(String name, boolean hasMainRecording, List<String> recordings, List<Integer> notes)
    {
        this.name = name;
        this.hasMainRecording = hasMainRecording;
        this.recordings = recordings;
        this.notes = notes;
    }

    public Song()
    {
        this.name = "";
        this.hasMainRecording = false;
        this.recordings = new ArrayList<>();
        this.notes = new ArrayList<>();
    }

    public void addNote(int note)
    {
        notes.add(note);
    }

    public void deleteLastNote()
    {
        if (notes.size() > 0)
            notes.remove(notes.size() - 1);
    }

    @Override
    public String toString()
    {
        StringBuilder song = new StringBuilder();

        for (int note : notes)
            song.append(noteTranslator(note));

        return song.toString();
    }

    public static String noteTranslator(int note)
    {
        switch (note)
        {
            case Keys.NOTE_SPACE:
                return " __ ";

            case Keys.NOTE_ENTER:
                return "\n";

//            case Keys.NOTE_DRAW_TEN:
//                return "-10 ";

//            case Keys.NOTE_BLOW_TEN:
//                return "+10 ";

            default:
                if (note > 0)
                    return "+" + note + "";
                else
                    return "" + note + "";
        }
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
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
        if (notes == null)
        {
            dest.writeByte((byte) (0x00));
        }
        else
        {
            dest.writeByte((byte) (0x01));
            dest.writeList(notes);
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
}
