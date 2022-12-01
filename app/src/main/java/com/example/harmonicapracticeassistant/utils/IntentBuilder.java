package com.example.harmonicapracticeassistant.utils;

import android.content.Context;
import android.content.Intent;

import com.example.harmonicapracticeassistant.editor.EditorActivity;
import com.example.harmonicapracticeassistant.editor.Song;
import com.example.harmonicapracticeassistant.practice.PracticeActivity;
import com.example.harmonicapracticeassistant.settings.AppSettings;

public class IntentBuilder
{
    // TODO: 01/12/2022 this is a static class that only it builds intents, make sure only here intents are made
    public static final String IS_NEW_SONG_PARCEL_ID = "IS_NEW_SONG";
    public static final String SONG_PARCEL_ID = "SONG";
    public static final String SETTINGS_PARCEL_ID = "SETTINGS";

    public static Intent buildExistingSongEditorIntent(Context context, Song song, AppSettings settings)
    {
        return new Intent(context, EditorActivity.class)
                .putExtra(IS_NEW_SONG_PARCEL_ID, false)
                .putExtra(SONG_PARCEL_ID, song)
                .putExtra(SETTINGS_PARCEL_ID, settings);
    }

    public static Intent buildNewSongEditorIntent(Context context, AppSettings settings)
    {
        return new Intent(context, EditorActivity.class)
                .putExtra(IS_NEW_SONG_PARCEL_ID, true)
                .putExtra(SETTINGS_PARCEL_ID, settings);
    }

    public static Intent buildPracticeIntent(Context context, Song song, AppSettings settings)
    {
        return new Intent(context, PracticeActivity.class)
                .putExtra(SONG_PARCEL_ID, song)
                .putExtra(SETTINGS_PARCEL_ID, settings);
    }

    public static Intent buildSaveResultSettingIntent(AppSettings settings)
    {
        return new Intent()
                .putExtra(SETTINGS_PARCEL_ID, settings);
    }

    public static Intent buildSaveResultSongIntent(Song song)
    {
        return new Intent()
                .putExtra(SONG_PARCEL_ID, song);
    }

    public static Intent buildBasicIntent(Context context, Class<?> cls, AppSettings settings)
    {
        return new Intent(context, cls)
                .putExtra(SETTINGS_PARCEL_ID, settings);
    }

    public static Intent buildExportIntent()
    {
        return new Intent(Intent.ACTION_CREATE_DOCUMENT)
                .addCategory(Intent.CATEGORY_OPENABLE)
                .setType("application/json")
                .putExtra(Intent.EXTRA_TITLE, "harmonica-songs.json");
    }

    public static Intent buildImportIntent()
    {
        return new Intent(Intent.ACTION_GET_CONTENT)
                .setType("application/json")
                .addCategory(Intent.CATEGORY_OPENABLE);
    }
}

