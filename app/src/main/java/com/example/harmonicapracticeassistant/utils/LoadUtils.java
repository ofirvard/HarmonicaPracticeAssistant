package com.example.harmonicapracticeassistant.utils;

import android.content.Context;
import android.util.Log;

import com.example.harmonicapracticeassistant.editor.Song;
import com.example.harmonicapracticeassistant.settings.AppSettings;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LoadUtils
{
    static Type songListType = new TypeToken<ArrayList<Song>>()
    {
    }.getType();

    public static List<Song> loadSongs(Context context)
    {
        List<Song> songs = new ArrayList<>();
        String data = "";
        try
        {
            data = readFromContext(context, Constants.SONGS_FILE);
            Gson gson = new Gson();
            songs = gson.fromJson(data, songListType);
        } catch (JsonSyntaxException e)
        {
            Log.d(Tags.ERROR, String.format("Failed to convert %s to List<Song>", data), e);
        } catch (IOException e)
        {
            Log.d(Tags.ERROR, "Failed to read song list", e);
        } catch (NullPointerException e)
        {
            Log.d(Tags.ERROR, String.format("Failed to read song list, possible data corruption: %s", data), e);
        }

        return songs;
    }

    public static AppSettings loadSettings(Context context)
    {
        AppSettings settings = new AppSettings();
        String data = "";
        try
        {
            data = readFromContext(context, Constants.SETTINGS_FILE);
            Gson gson = new Gson();

            return gson.fromJson(data, AppSettings.class);
        } catch (JsonSyntaxException e)
        {
            Log.d(Tags.ERROR, String.format("Failed to convert %s to Settings", data), e);

        } catch (IOException e)
        {
            Log.d(Tags.ERROR, "Failed to read settings", e);
        }
        SaveUtils.saveSettings(context, settings);

        return settings;
    }

    public static String readFileInputStream(FileInputStream in) throws IOException
    {
        String data;
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        data = load(reader);

        return data;
    }

    private static String readFromContext(Context context, String fileName) throws IOException
    {
        return readFileInputStream(context.openFileInput(fileName));
    }

    private static String load(BufferedReader reader) throws IOException
    {
        StringBuilder builder = new StringBuilder();
        String aux;
        while ((aux = reader.readLine()) != null)
        {
            builder.append(aux);
        }
        return builder.toString();
    }
}
