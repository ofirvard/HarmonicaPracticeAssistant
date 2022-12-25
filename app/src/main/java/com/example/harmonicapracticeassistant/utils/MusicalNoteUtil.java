package com.example.harmonicapracticeassistant.utils;

import com.example.harmonicapracticeassistant.enums.FlatNote;
import com.example.harmonicapracticeassistant.enums.MusicalNote;
import com.example.harmonicapracticeassistant.enums.NaturalNote;

public class MusicalNoteUtil
{
    public static MusicalNote getMusicalNoteFromNumber(int noteNumber)
    {
        switch (noteNumber)
        {
            case 0:
                return NaturalNote.C;

            case 1:
                return FlatNote.Db;

            case 2:
                return NaturalNote.D;

            case 3:
                return FlatNote.Eb;

            case 4:
                return NaturalNote.E;

            case 5:
                return NaturalNote.F;

            case 6:
                return FlatNote.Gb;

            case 7:
                return NaturalNote.G;

            case 8:
                return FlatNote.Ab;

            case 9:
                return NaturalNote.A;

            case 10:
                return FlatNote.Bb;

            case 11:
                return NaturalNote.B;

            default:
                return null;
        }
    }
}
