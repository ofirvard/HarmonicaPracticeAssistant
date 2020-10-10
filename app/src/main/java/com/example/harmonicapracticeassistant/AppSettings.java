package com.example.harmonicapracticeassistant;

import android.os.Parcel;
import android.os.Parcelable;

public class AppSettings implements Parcelable
{
    private int defaultTextSize;

    public AppSettings(int defaultTextSize)
    {
        this.defaultTextSize = defaultTextSize;
    }

    public int getDefaultTextSize()
    {
        return defaultTextSize;
    }

    public void setDefaultTextSize(int defaultTextSize)
    {
        this.defaultTextSize = defaultTextSize;
    }

    protected AppSettings(Parcel in)
    {
        defaultTextSize = in.readInt();
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(defaultTextSize);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<AppSettings> CREATOR = new Parcelable.Creator<AppSettings>()
    {
        @Override
        public AppSettings createFromParcel(Parcel in)
        {
            return new AppSettings(in);
        }

        @Override
        public AppSettings[] newArray(int size)
        {
            return new AppSettings[size];
        }
    };
}