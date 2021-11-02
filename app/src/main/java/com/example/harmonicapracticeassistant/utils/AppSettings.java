package com.example.harmonicapracticeassistant.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class AppSettings implements Parcelable {
    private int defaultTextSize;
    private boolean isKeyboardSlim;

    public AppSettings(int defaultTextSize, boolean isKeyboardSlim) {
        this.defaultTextSize = defaultTextSize;
        this.isKeyboardSlim = isKeyboardSlim;
    }

    protected AppSettings(Parcel in) {
        defaultTextSize = in.readInt();
        isKeyboardSlim = in.readByte() != 0x00;
    }

    public int getDefaultTextSize() {
        return defaultTextSize;
    }

    public void setDefaultTextSize(int defaultTextSize) {
        this.defaultTextSize = defaultTextSize;
    }

    public boolean isKeyboardSlim() {
        return isKeyboardSlim;
    }

    public void setKeyboardSlim(boolean keyboardSlim) {
        isKeyboardSlim = keyboardSlim;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(defaultTextSize);
        dest.writeByte((byte) (isKeyboardSlim ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<AppSettings> CREATOR = new Parcelable.Creator<AppSettings>() {
        @Override
        public AppSettings createFromParcel(Parcel in) {
            return new AppSettings(in);
        }

        @Override
        public AppSettings[] newArray(int size) {
            return new AppSettings[size];
        }
    };
}