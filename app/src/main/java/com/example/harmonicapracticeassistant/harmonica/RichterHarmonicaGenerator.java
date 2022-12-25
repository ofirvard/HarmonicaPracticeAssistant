package com.example.harmonicapracticeassistant.harmonica;

import android.content.Context;

import com.example.harmonicapracticeassistant.settings.AppSettings;
import com.example.harmonicapracticeassistant.utils.HarmonicaUtils;
import com.example.harmonicapracticeassistant.utils.RawReader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RichterHarmonicaGenerator
{
    private static List<Tuning> richterTuning;

    public static List<Harmonica> generateRichterHarmonicas(Context context, AppSettings appSettings)
    {
        setUp(context);

        return HarmonicaUtils.getKeys(HarmonicaTuningType.RICHTER).stream()
                .map(key -> new Harmonica(key,
                        richterTuning,
                        HarmonicaTuningType.RICHTER,
                        HarmonicaUtils.getBaseNote()))
                .collect(Collectors.toList());
    }

    private static void setUp(Context context)
    {
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
