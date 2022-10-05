package com.example.harmonicapracticeassistant.settings;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.harmonicapracticeassistant.utils.Constants;

import java.util.Objects;

public class AppSettings implements Parcelable
{
    private int defaultTextSize;
    private String defaultKey;// TODO: 09/03/2022 add this to recorder
    private boolean isKeyboardSlim;

    public AppSettings()
    {
        this.defaultTextSize = Constants.DEFAULT_TEXT_SIZE;
        this.defaultKey = Constants.DEFAULT_KEY;
        this.isKeyboardSlim = false;
    }

    public AppSettings(AppSettings oldSettings)
    {
        this.defaultTextSize = oldSettings.getDefaultTextSize();
        this.defaultKey = oldSettings.getDefaultKey();
        this.isKeyboardSlim = oldSettings.isKeyboardSlim();
    }

    protected AppSettings(Parcel in)
    {
        defaultTextSize = in.readInt();
        defaultKey = in.readString();
        isKeyboardSlim = in.readByte() != 0x00;
    }

    public int getDefaultTextSize()
    {
        return defaultTextSize;
    }

    public void setDefaultTextSize(int defaultTextSize)
    {
        this.defaultTextSize = defaultTextSize;
    }

    public String getDefaultKey()
    {
        return defaultKey;
    }

    public void setDefaultKey(String defaultKey)
    {
        this.defaultKey = defaultKey;
    }

    public boolean isKeyboardSlim()
    {
        return isKeyboardSlim;
    }

    public void setKeyboardSlim(boolean keyboardSlim)
    {
        isKeyboardSlim = keyboardSlim;
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
        dest.writeString(defaultKey);
        dest.writeByte((byte) (isKeyboardSlim ? 0x01 : 0x00));
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

    public boolean hasChanged(AppSettings newSettings)
    {
        return !Objects.equals(defaultKey, newSettings.getDefaultKey()) ||
                isKeyboardSlim != newSettings.isKeyboardSlim() ||
                defaultTextSize != newSettings.getDefaultTextSize();
    }
}