package com.example.harmonicapracticeassistant.enums;

public interface MusicalNote
{
    String toString(boolean isSharp);

    int getNoteNumber();

    @Override
    String toString();
}
