package com.example.harmonicapracticeassistant.utils;

import android.content.Context;

import com.example.harmonicapracticeassistant.loaded.Note;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

public class NoteFinder
{
    public static List<Note> notes = null;

    public static void setUp(Context context)
    {
        if (notes == null)
        {
            Gson gson = new Gson();
            Type listOfNotes = new TypeToken<ArrayList<Note>>()
            {
            }.getType();
            notes = gson.fromJson(RawReader.getNoteFrequency(context), listOfNotes);
            notes.sort((o1, o2) -> o1.getFrequency() - o2.getFrequency());
        }
    }

    public static Note getNoteById(int id)
    {
        return notes.stream().filter(note -> note.getId() == id).findFirst().get();
    }

    public static Note getNoteByFrequency(int frequency)
    {
        try
        {
            return notes.stream().min(Comparator.comparingInt(note -> Math.abs(note.getFrequency() - frequency))).orElseThrow(NoSuchElementException::new);
        } catch (NoSuchElementException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
