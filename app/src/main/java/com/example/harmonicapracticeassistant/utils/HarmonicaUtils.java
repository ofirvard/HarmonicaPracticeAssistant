package com.example.harmonicapracticeassistant.utils;

import android.content.Context;

import com.example.harmonicapracticeassistant.enums.Bend;
import com.example.harmonicapracticeassistant.enums.MusicalNote;
import com.example.harmonicapracticeassistant.enums.MusicalNoteJsonDeserializer;
import com.example.harmonicapracticeassistant.harmonica.Key;
import com.example.harmonicapracticeassistant.harmonica.Note;
import com.example.harmonicapracticeassistant.raw.models.KewRaw;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HarmonicaUtils
{
    private static List<Note> notes = null;
    private static List<Key> keys = null;

    public static void setUp(Context context)
    {
        setupAllNotes(context);
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

        return keys.stream().map(Key::getKeyName).collect(Collectors.toList());
    }

    public static Key getKey(String keyString)
    {
        for (Key key : keys)
            if (key.getKeyName().equals(keyString))
                return key;

        return keys.get(0);
    }

    private static void setupAllKeys(Context context)
    {
        if (keys == null)
        {
            keys = new ArrayList<>();
            List<KewRaw> rawKeys = readRawKeys(context);

            convertRawKeysToKeys(rawKeys);

            keys.sort((key1, key2) -> key1.getKeyName().compareTo(key2.getKeyName()));
        }
    }

    private static List<KewRaw> readRawKeys(Context context)
    {
        Gson gson = new GsonBuilder().
                registerTypeAdapter(MusicalNote.class, new MusicalNoteJsonDeserializer()).
                create();
        Type rawKeyList = new TypeToken<ArrayList<KewRaw>>()
        {
        }.getType();

        return gson.fromJson(RawReader.getKeys(context), rawKeyList);
    }

    private static void convertRawKeysToKeys(List<KewRaw> rawKeys)
    {
        for (KewRaw rawKey : rawKeys)
        {
            Key key = new Key(rawKey);
            keys.add(key);
        }
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
