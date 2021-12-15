package com.example.harmonicapracticeassistant.utils;

import android.content.Context;

import com.example.harmonicapracticeassistant.harmonica.Key;
import com.example.harmonicapracticeassistant.harmonica.Note;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HarmonicaUtils
{
    public static List<Note> notes = null;
    public static List<Key> keys = null;

    public static void setUp(Context context)
    {
        setupAllNotes(context);
        setupAllKeys(context);
    }

    private static void setupAllKeys(Context context)
    {
        if (keys == null)
        {
            Gson gson = new Gson();
            Type listOfHoles = new TypeToken<ArrayList<Key>>()
            {
            }.getType();
            keys = gson.fromJson(RawReader.getKeys(context), listOfHoles);
        }
    }

    private static void setupAllNotes(Context context)
    {
        if (notes == null)
        {
            Gson gson = new Gson();
            Type listOfNotes = new TypeToken<ArrayList<Note>>()
            {
            }.getType();
            notes = gson.fromJson(RawReader.getNoteFrequency(context), listOfNotes);
            notes.sort((o1, o2) -> (int) (o1.getFrequency() - o2.getFrequency()));
        }
    }
}
