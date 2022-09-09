package com.example.harmonicapracticeassistant.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.harmonicapracticeassistant.R;
import com.example.harmonicapracticeassistant.SettingsListAdapter;
import com.example.harmonicapracticeassistant.editor.Song;
import com.example.harmonicapracticeassistant.utils.AppSettings;
import com.example.harmonicapracticeassistant.utils.Constants;
import com.example.harmonicapracticeassistant.utils.FileUtils;
import com.example.harmonicapracticeassistant.utils.HarmonicaUtils;
import com.example.harmonicapracticeassistant.utils.LoadUtils;
import com.example.harmonicapracticeassistant.utils.SaveUtils;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SettingsActivity extends AppCompatActivity
{
    private AppSettings settings;
    private boolean newSongsImported = false;
    private Uri fileUri;
    private List<Song> songs;
    private SwitchMaterial slim;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settings = getIntent().getExtras().getParcelable(Constants.SETTINGS);
        songs = getIntent().getExtras().getParcelableArrayList(Constants.SONGS);

        setupRecyclerView();

        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (!isGranted)
                    {
                        Toast.makeText(this, "Permission was not granted", Toast.LENGTH_SHORT).show();
                    }
                });
        new Handler().postDelayed(this::setSettings, 100);
    }

    private void setSettings()
    {
        setPreviewText();
        slim = findViewById(R.id.slim);
        slim.setChecked(settings.isKeyboardSlim());
        setupKeySpinner();
    }

    private void setupRecyclerView()
    {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        SettingsListAdapter settingsListAdapter = new SettingsListAdapter(getSettingsList(), this);
        RecyclerView settingsRecyclerView = findViewById(R.id.settings_list);

        settingsRecyclerView.setAdapter(settingsListAdapter);
        settingsRecyclerView.setLayoutManager(layoutManager);
    }

    private List<Integer> getSettingsList()
    {
        return Arrays.asList(R.layout.settings_import_export_songs,
                R.layout.settings_keyboard_type,
                R.layout.settings_default_key,
                R.layout.settings_text_size);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.FILE_PICKER_REQUEST_CODE)
            if (resultCode == RESULT_OK)
            {
                fileUri = data.getData();
                File file = new File(FileUtils.getPath(this, fileUri));
                List<Song> importedSongs = SaveUtils.importSongs(file);
                if (importedSongs.isEmpty())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(R.string.bad_file);
                    builder.setPositiveButton(R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else
                    setNewSongsImported(importedSongs);
            }
        if (requestCode == Constants.FILE_SAVE_REQUEST_CODE)
            if (resultCode == RESULT_OK)
            {
                fileUri = data.getData();
                try
                {
                    Gson gson = new Gson();
                    OutputStream os = this.getContentResolver().openOutputStream(fileUri);
                    if (os != null)
                    {
                        os.write(gson.toJson(songs).getBytes());
                        os.close();
                    }
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
    }

    public void increase(View view)
    {
        if (settings.getDefaultTextSize() < Constants.MAX_TEXT_SIZE)
        {
            settings.setDefaultTextSize(settings.getDefaultTextSize() + 1);
            setPreviewText();
        }
    }

    public void decrease(View view)
    {
        if (settings.getDefaultTextSize() > Constants.MIN_TEXT_SIZE)
        {
            settings.setDefaultTextSize(settings.getDefaultTextSize() - 1);
            setPreviewText();
        }
    }

    public void importSongs(View view)
    {
        if (checkPermission())
        {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/json");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(Intent.createChooser(intent, "Choose a file"), Constants.FILE_PICKER_REQUEST_CODE);
        }
    }

    public boolean checkPermission()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
        {
            Toast.makeText(this, R.string.import_songs_fail, Toast.LENGTH_SHORT).show();
        }
        else
        {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        return false;
    }

    public void exportSongs(View view)
    {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/json");
        intent.putExtra(Intent.EXTRA_TITLE, "harmonica-songs.json");

        startActivityForResult(intent, Constants.FILE_SAVE_REQUEST_CODE);
    }

    public void save(View view)
    {
        settings.setKeyboardSlim(slim.isChecked());
        SaveUtils.saveSettings(getApplicationContext(), settings);
        SaveUtils.saveSongs(getApplicationContext(), songs);

        Intent intent = new Intent();
        intent.putExtra(Constants.SETTINGS, settings);
        intent.putExtra(Constants.NEW_SONGS_IMPORTED, newSongsImported);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed()
    {
        save(null);
        super.onBackPressed();
    }

    private void setNewSongsImported(List<Song> songsImported)
    {
        newSongsImported = true;
        for (Song song : songsImported)
        {
            if (isNameTaken(song.getName()))
            {
                int i = 1;
                while (isNameTaken(song.getName() + "_" + i))
                    i++;
                song.setName(song.getName() + "_" + i);
            }
            songs.add(song);
        }
    }

    private boolean isNameTaken(String name)
    {
        for (Song song : songs)
            if (song.getName().equals(name))
                return true;

        return false;
    }

    private void setPreviewText()
    {
        ((TextView) findViewById(R.id.text_size_preview)).setText(String.format("%d", settings.getDefaultTextSize()));
        ((TextView) findViewById(R.id.text_size_preview)).setTextSize(TypedValue.COMPLEX_UNIT_SP, settings.getDefaultTextSize());
    }

    private void setupKeySpinner()
    {
        Spinner keySpinner = findViewById(R.id.setting_default_key_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.spinner_item_text,
                HarmonicaUtils.getKeysName());

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_text);
        keySpinner.setAdapter(adapter);
        keySpinner.setSelection(HarmonicaUtils.getPositionOfKey(settings.getDefaultKey()));
        keySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                settings.setDefaultKey(HarmonicaUtils.getKeys().get(i).getKeyName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
            }
        });
    }
}