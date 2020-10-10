package com.example.harmonicapracticeassistant;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SaveHandler
{
    static List<Song> loadSongs(Context context)
    {
        List<Song> Songs = new ArrayList<>();
        String data;

        try
        {
            FileInputStream in = context.openFileInput("songs.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            data = load(reader);
            JSONArray list = new JSONArray(data);
            Gson gson = new Gson();
            for (int i = 0; i < list.length(); i++)
                Songs.add(gson.fromJson(list.getJSONObject(i).toString(), Song.class));
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return Songs;
    }

    static void saveSongs(Context context, List<Song> Songs)
    {
        try
        {
            Gson gson = new Gson();
            String data = gson.toJson(Songs);
            FileOutputStream out = context.openFileOutput("songs.json", Context.MODE_PRIVATE);
            out.write(data.getBytes());
            out.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    static AppSettings loadSettings(Context context)
    {
        AppSettings settings = new AppSettings(Keys.DEFAULT_TEXT_SIZE);
        try
        {
            String data;
            FileInputStream in = context.openFileInput("settings.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            data = load(reader);
            Gson gson = new Gson();
            return gson.fromJson(data, AppSettings.class);
        } catch (Exception e)
        {
            e.printStackTrace();
            saveSettings(context, settings);
        }
        return settings;
    }

    static void saveSettings(Context context, AppSettings settings)
    {
        try
        {
            Gson gson = new Gson();
            String data = gson.toJson(settings);
            FileOutputStream out = context.openFileOutput("settings.json", Context.MODE_PRIVATE);
            out.write(data.getBytes());
            out.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static String load(BufferedReader reader)
    {
        try
        {
            StringBuilder builder = new StringBuilder();
            String aux;
            while ((aux = reader.readLine()) != null)
            {
                builder.append(aux);
            }
            return builder.toString();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }
}
