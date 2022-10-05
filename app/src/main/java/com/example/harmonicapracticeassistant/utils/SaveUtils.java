package com.example.harmonicapracticeassistant.utils;

import android.content.Context;
import android.util.Log;

import com.example.harmonicapracticeassistant.editor.Song;
import com.example.harmonicapracticeassistant.settings.AppSettings;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SaveUtils
{
    public static boolean importSongs(Context context, File file)
    {// TODO: 9/9/2022
        try
        {
            saveSong(context, new Song("Imported Song",
                    LoadUtils.readFileInputStream(new FileInputStream(file))));

            return true;
        } catch (IOException e)
        {
            Log.d(Tags.ERROR, "Failed to import song: " + file.getName());

            return false;
        }
    }

    public static boolean saveSong(Context context, Song song)
    {
        List<Song> songs = LoadUtils.loadSongs(context);
        songs.add(song);

        return saveSongs(context, songs);
    }

    public static boolean removeSong(Context context, String uuid)
    {
        List<Song> songs = LoadUtils.loadSongs(context);
        songs.removeIf(song -> song.getId().equals(uuid));

        return saveSongs(context, songs);
    }

    public static boolean removeSongs(Context context, List<String> uuidList)
    {
        List<Song> songs = LoadUtils.loadSongs(context);

        for (String uuid : uuidList)
            songs.removeIf(song -> song.getId().equals(uuid));

        return saveSongs(context, songs);
    }

    public static boolean removeAllSong(Context context)
    {
        return saveSongs(context, new ArrayList<>());
    }

    public static boolean saveSettings(Context context, AppSettings settings)
    {
        return saveData(context, Constants.SETTINGS_FILE, new Gson().toJson(settings));
    }

    private static boolean saveSongs(Context context, List<Song> songs)
    {
        return saveData(context, Constants.SONGS_FILE, new Gson().toJson(songs));
    }

    private static boolean saveData(Context context, String fileName, String data)
    {
        try
        {
            FileOutputStream out = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            out.write(data.getBytes());
            out.close();

            return true;
        } catch (Exception e)
        {
            Log.d(Tags.ERROR, "Failed to save: " + data, e);

            return false;
        }
    }
}
