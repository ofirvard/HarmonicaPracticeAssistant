package com.example.harmonicapracticeassistant.utils;

import android.content.Context;

import com.example.harmonicapracticeassistant.enums.Bend;
import com.example.harmonicapracticeassistant.enums.MusicalNote;
import com.example.harmonicapracticeassistant.enums.MusicalNoteJsonDeserializer;
import com.example.harmonicapracticeassistant.harmonica.Hole;
import com.example.harmonicapracticeassistant.harmonica.Key;
import com.example.harmonicapracticeassistant.harmonica.Note;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.harmonicapracticeassistant.utils.Constants.NO_KEY;

public class HarmonicaUtils
{
    private static List<Note> notes = null;
    private static List<Key> keys = null;

    public static void setUp(Context context)
    {
        setupAllNotes(context);
        // TODO: 12/23/2021 fill keys with frequencys
        setupAllKeys(context);
    }

    public static List<Note> getNotes()
    {
        return notes;
    }

    public static List<Key> getKeys()
    {
        return keys;
    }

    public static List<String> getKeysName()
    {
        return keys.stream().map(Key::getKey).collect(Collectors.toList());
    }

    public static Key getKey(String keyString)
    {
        for (Key key : keys)
            if (key.getKey().equals(keyString))
                return key;

        return null;
    }

    private static void setupAllKeys(Context context)
    {
        if (keys == null)
        {
            Gson gson = new GsonBuilder().
                    registerTypeAdapter(MusicalNote.class, new MusicalNoteJsonDeserializer()).
                    create();
            Type listOfHoles = new TypeToken<ArrayList<Key>>()
            {
            }.getType();

            keys = gson.fromJson(RawReader.getKeys(context), listOfHoles);
            keys.sort((key1, key2) -> key1.getKey().compareTo(key2.getKey()));

            for (Key key : keys)
                key.setHolesFrequencies();

            keys.add(0, new Key(NO_KEY, createNoKeyList()));
        }
    }

    private static List<Hole> createNoKeyList()
    {
        List<Hole> holeList = new ArrayList<>();

        for (Note note : notes)
            holeList.add(new Hole(note));

        return holeList;
    }

    private static void setupAllNotes(Context context)
    {
        if (notes == null)
        {
            Gson gson = new GsonBuilder().
                    registerTypeAdapter(MusicalNote.class, new MusicalNoteJsonDeserializer()).
                    create();
            Type listOfNotes = new TypeToken<ArrayList<Note>>()
            {
            }.getType();

            notes = gson.fromJson(RawReader.getNoteFrequency(context), listOfNotes);
            notes.sort((o1, o2) -> Float.compare(o1.getFrequency(), o2.getFrequency()));
        }
    }

    public static String getBendString(Bend bend)
    {// TODO: 19/01/2022 make this use enum string
        return bend.toString();
    }
}
