package com.example.harmonicapracticeassistant.richter;

import android.content.Context;

import com.example.harmonicapracticeassistant.utils.RawReader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RichterHarmonicaGenerator
{
    private static List<RichterKey> richterKeys;
    private static List<Tuning> richterTuning;

    public static void setUp(Context context)
    {
        if (richterKeys == null)
        {
            Gson gson = new Gson();

            Type listOfRichterKey = new TypeToken<ArrayList<RichterKey>>()
            {
            }.getType();

            richterKeys = gson.fromJson(RawReader.getRichterKeys(context), listOfRichterKey);
        }

        if (richterTuning == null)
        {
            Gson gson = new Gson();

            Type tuningList = new TypeToken<ArrayList<Tuning>>()
            {
            }.getType();

            richterTuning = gson.fromJson(RawReader.getRichterTuning(context), tuningList);
        }
    }
}
