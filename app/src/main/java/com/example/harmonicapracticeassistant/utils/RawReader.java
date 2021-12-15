package com.example.harmonicapracticeassistant.utils;

import android.content.Context;

import com.example.harmonicapracticeassistant.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RawReader
{
    public static String getNoteFrequency(Context context)
    {
        return read(context, R.raw.note_frequency);
    }

    public static String getKeys(Context context)
    {
        return read(context, R.raw.harmonica_keys);
    }

    private static String read(Context context, int id)
    {
        try
        {
            InputStream inputStream = context.getResources().openRawResource(id);
            StringBuilder file = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = reader.readLine();
            while (line != null)
            {
                file.append(line);
                line = reader.readLine();
            }

            return file.toString();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return "";
    }
}
