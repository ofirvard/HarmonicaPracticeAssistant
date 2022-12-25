package com.example.harmonicapracticeassistant.settings;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.harmonicapracticeassistant.harmonica.HarmonicaTuningType;
import com.example.harmonicapracticeassistant.utils.Constants;

import java.util.Objects;

public class AppSettings implements Parcelable
{
    private int defaultTextSize;
    private String defaultKey;// TODO: 09/03/2022 add this to recorder
    private boolean isKeyboardSlim;
    private final int defaultFreq;// TODO: 12/19/2022 add choice
    private HarmonicaTuningType defaultTuningType;

    public AppSettings()
    {
        this.defaultTextSize = Constants.DEFAULT_TEXT_SIZE;
        this.defaultKey = Constants.DEFAULT_KEY;
        this.isKeyboardSlim = false;
        this.defaultFreq = Constants.DEFAULT_FREQ;
        this.defaultTuningType = Constants.DEFAULT_TUNING;
    }

    public AppSettings(AppSettings oldSettings)
    {
        this.defaultTextSize = oldSettings.getDefaultTextSize();
        this.defaultKey = oldSettings.getDefaultKey();
        this.isKeyboardSlim = oldSettings.isKeyboardSlim();
        this.defaultFreq = oldSettings.getFreq();
        this.defaultTuningType = oldSettings.getDefaultTuningType();
    }

    public int getFreq()
    {
        return defaultFreq;
    }

    public HarmonicaTuningType getDefaultTuningType()
    {
        return defaultTuningType;
    }

    protected AppSettings(Parcel in)
    {// TODO: 12/20/2022 add all new variables
        defaultTextSize = in.readInt();
        defaultKey = in.readString();
        isKeyboardSlim = in.readByte() != 0x00;
        defaultFreq = in.readInt();
        defaultTuningType = HarmonicaTuningType.valueOf(in.readString());
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

    public void setDefaultTuning(HarmonicaTuningType tuningType)
    {
        this.defaultTuningType = tuningType;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {// TODO: 12/20/2022 add all new variables
        dest.writeInt(defaultTextSize);
        dest.writeString(defaultKey);
        dest.writeByte((byte) (isKeyboardSlim ? 0x01 : 0x00));
        dest.writeInt(defaultFreq);
        dest.writeString(defaultTuningType.toString());
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
    {// TODO: 12/20/2022 add all new variables
        return !Objects.equals(defaultKey, newSettings.getDefaultKey()) ||
                isKeyboardSlim != newSettings.isKeyboardSlim() ||
                defaultTextSize != newSettings.getDefaultTextSize();
    }
}