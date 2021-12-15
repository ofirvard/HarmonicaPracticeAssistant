package com.example.harmonicapracticeassistant.editor;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.harmonicapracticeassistant.R;
import com.example.harmonicapracticeassistant.activities.App;
import com.example.harmonicapracticeassistant.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class Song implements Parcelable
{
    private String name;
    private boolean hasMainRecording;
    private List<String> recordings;
    private List<Tab> tabs;

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
            tabs = new ArrayList<Tab>();
            in.readList(tabs, Tab.class.getClassLoader());
        }
        else
        {
            tabs = null;
        }
    }

    public Song(String name, boolean hasMainRecording, List<String> recordings, List<Tab> tabs)
    {
        this.name = name;
        this.hasMainRecording = hasMainRecording;
        this.recordings = recordings;
        this.tabs = tabs;
    }

    public Song()
    {
        this.name = "";
        this.hasMainRecording = false;
        this.recordings = new ArrayList<>();
        this.tabs = new ArrayList<>();
    }

    public void addNote(Tab tab)
    {
        tabs.add(tab);
    }

    public void deleteLastNote()
    {
        if (tabs.size() > 0)
            tabs.remove(tabs.size() - 1);
    }

    @Override
    public String toString()//todo remove?
    {
        StringBuilder song = new StringBuilder();

//        for (Note note : notes)
//            song.append(noteTranslator(note));

        return song.toString();
    }

    public static String noteTranslator(int note)
    {
        switch (note)
        {
            case Constants.NOTE_SPACE:
                return "_";

            case Constants.NOTE_ENTER:
                return "\n";

//            case Keys.NOTE_DRAW_TEN:
//                return "-10 ";

//            case Keys.NOTE_BLOW_TEN:
//                return "+10 ";
            case Constants.BRACKET_OPEN:
                return App.getContext().getResources().getString(R.string.bracket_open);

            case Constants.BRACKET_CLOSE:
                return App.getContext().getResources().getString(R.string.bracket_close) + " ";

            case Constants.HALF_BEND:
                return App.getContext().getResources().getString(R.string.half_bend) + " ";

            case Constants.WHOLE_BEND:
                return App.getContext().getResources().getString(R.string.whole_bend) + " ";

            case Constants.WHOLE_HALF_BEND:
                return App.getContext().getResources().getString(R.string.whole_half_bend) + " ";

            case Constants.OVER:
                return App.getContext().getResources().getString(R.string.over);

            case Constants.WAVE:
                return App.getContext().getResources().getString(R.string.wave);

            default:
                if (note > 0)
                    return "+" + note + " ";
                else
                    return "" + note + " ";
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
        if (tabs == null)
        {
            dest.writeByte((byte) (0x00));
        }
        else
        {
            dest.writeByte((byte) (0x01));
            dest.writeList(tabs);
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
