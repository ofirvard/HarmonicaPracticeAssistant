package com.example.harmonicapracticeassistant.utils;

import android.content.Context;

import com.example.harmonicapracticeassistant.editor.Song;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SaveUtils
{
    public static List<Song> importSongs(File file)
    {// TODO: 9/9/2022
        List<Song> songs = new ArrayList<>();

//        try
//        {
//            FileInputStream in = new FileInputStream(file);
//            songs = readSongs(in);
//        } catch (Exception e)
//        {K
//            e.printStackTrace();
//        }

        return songs;
    }

    public static void saveSongs(Context context, List<Song> Songs)
    {// TODO: 9/9/2022
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

    public static void saveSettings(Context context, AppSettings settings)
    {// TODO: 9/9/2022
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

}
