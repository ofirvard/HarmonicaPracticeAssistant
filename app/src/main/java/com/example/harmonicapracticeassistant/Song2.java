package com.example.harmonicapracticeassistant;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Song2 implements Parcelable
{
    String name;
    List<TabAndText> tabAndTexts;

    public Song2()
    {
        this.name = "New Song";
        this.tabAndTexts = new ArrayList<>();
    }

    public Song2(String name, List<TabAndText> tabAndTexts)
    {
        this.name = name;
        this.tabAndTexts = tabAndTexts;
    }

    protected Song2(Parcel in)
    {
        name = in.readString();
        if (in.readByte() == 0x01)
        {
            tabAndTexts = new ArrayList<TabAndText>();
            in.readList(tabAndTexts, TabAndText.class.getClassLoader());
        }
        else
        {
            tabAndTexts = null;
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
        if (tabAndTexts == null)
        {
            dest.writeByte((byte) (0x00));
        }
        else
        {
            dest.writeByte((byte) (0x01));
            dest.writeList(tabAndTexts);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Song2> CREATOR = new Parcelable.Creator<Song2>()
    {
        @Override
        public Song2 createFromParcel(Parcel in)
        {
            return new Song2(in);
        }

        @Override
        public Song2[] newArray(int size)
        {
            return new Song2[size];
        }
    };
}