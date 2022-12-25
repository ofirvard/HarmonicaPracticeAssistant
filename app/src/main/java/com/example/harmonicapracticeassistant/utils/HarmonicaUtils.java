package com.example.harmonicapracticeassistant.utils;

import android.content.Context;

import com.example.harmonicapracticeassistant.enums.Bend;
import com.example.harmonicapracticeassistant.enums.MusicalNote;
import com.example.harmonicapracticeassistant.enums.MusicalNoteJsonDeserializer;
import com.example.harmonicapracticeassistant.enums.NaturalNote;
import com.example.harmonicapracticeassistant.harmonica.Harmonica;
import com.example.harmonicapracticeassistant.harmonica.HarmonicaTuningType;
import com.example.harmonicapracticeassistant.harmonica.Key;
import com.example.harmonicapracticeassistant.harmonica.Note;
import com.example.harmonicapracticeassistant.harmonica.RichterHarmonicaGenerator;
import com.example.harmonicapracticeassistant.settings.AppSettings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HarmonicaUtils
{
    private static final List<Note> notes = new ArrayList<>();
    private static final List<Key> keys = new ArrayList<>();
    private static List<String> legalTabs = null;
    private static final List<Harmonica> harmonicaList = new ArrayList<>();
    private static Note baseNote;

    public static void setUp(Context context, AppSettings appSettings)
    {
        baseNote = new Note(NaturalNote.A, 4, appSettings.getFreq());
        setupKeys(context);// TODO: 12/20/2022 sort by type and then key 
        setupLegalTabs(context);
        harmonicaList.addAll(RichterHarmonicaGenerator.generateRichterHarmonicas(context, appSettings));
        generateAllNotes();
    }

    private static void setupKeys(Context context)
    {
        Gson gson = new GsonBuilder().
                registerTypeAdapter(MusicalNote.class, new MusicalNoteJsonDeserializer()).
                create();

        Type listOfRichterKey = new TypeToken<ArrayList<com.example.harmonicapracticeassistant.harmonica.Key>>()
        {
        }.getType();

        keys.addAll(gson.fromJson(RawReader.getRichterKeys(context), listOfRichterKey));
    }

    public static List<Key> getKeys(HarmonicaTuningType tuningType)
    {
        return keys.stream()
                .filter(key -> key.isSameTuningType(tuningType))
                .collect(Collectors.toList());
    }

    public static Harmonica getHarmonica(HarmonicaTuningType tuningType, String keyName)
    {
        return harmonicaList.stream()
                .filter(harmonica -> harmonica.getTuningType() == tuningType &&
                        harmonica.getKey().getKeyName().equals(keyName))
                .findFirst()
                .orElse(null);
    }

    public static List<Note> getNotes()
    {
        return notes;
    }

    public static List<Key> getKeys()
    {
        return Collections.unmodifiableList(keys);
    }

    public static List<String> getKeysName(HarmonicaTuningType tuningType)
    {
        return keys.stream()
                .filter(key -> key.isSameTuningType(tuningType))
                .map(Key::getKeyName)
                .collect(Collectors.toList());
    }

    public static int getPositionOfKey(AppSettings appSettings)
    {
        return getKeysName(appSettings.getDefaultTuningType())
                .indexOf(appSettings.getDefaultKey());
    }

    public static Key getKey(AppSettings appSettings)
    {
        return getKeys(appSettings.getDefaultTuningType()).stream()
                .findFirst()
                .filter(key -> key.getKeyName().equals(appSettings.getDefaultKey()))
                .orElse(null);
    }

    public static List<String> findIllegalTabs(List<String> tabs)
    {
        return tabs.stream()
                .filter(s -> !legalTabs.contains(s))
                .distinct()
                .collect(Collectors.toList());
    }

    private static void setupLegalTabs(Context context)
    {
        if (legalTabs == null)
        {
            Type listOfString = new TypeToken<List<String>>()
            {
            }.getType();

            Gson gson = new Gson();
            legalTabs = gson.fromJson(RawReader.getLegalTabs(context), listOfString);
        }
    }

    public static String getBendString(Bend bend)
    {// TODO: 19/01/2022 make this use enum string
        return bend.toString();
    }

    private static void generateAllNotes()
    {
        for (int octave = 1; octave < 9; octave++)
            for (int noteNumber = 0; noteNumber < 12; noteNumber++)
                notes.add(new Note(MusicalNoteUtil.getMusicalNoteFromNumber(noteNumber),
                        octave,
                        baseNote));
    }

    public static Note getBaseNote()
    {
        return baseNote;
    }
}
