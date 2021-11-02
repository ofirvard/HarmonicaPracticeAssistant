package com.example.harmonicapracticeassistant.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.harmonicapracticeassistant.R;
import com.example.harmonicapracticeassistant.Song;
import com.example.harmonicapracticeassistant.utils.AppSettings;
import com.example.harmonicapracticeassistant.utils.Constants;
import com.example.harmonicapracticeassistant.utils.FileUtils;
import com.example.harmonicapracticeassistant.utils.SaveHandler;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class SettingsActivity extends AppCompatActivity {
    private AppSettings settings;
    private EditText textSizePreview;
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

        textSizePreview = findViewById(R.id.text_size_preview);
        setPreviewText();

        slim = findViewById(R.id.slim);
        slim.setChecked(settings.isKeyboardSlim());
        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted)
                    {
//                        Toast.makeText(this, "yes", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
//                        Toast.makeText(this, "no", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.FILE_PICKER_REQUEST_CODE)
            if (resultCode == RESULT_OK)
            {
                fileUri = data.getData();
                File file = new File(FileUtils.getPath(this, fileUri));
                List<Song> importedSongs = SaveHandler.loadSongs(file);
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
    {// TODO: 10/10/2020 why dose this seem smaller?
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
        SaveHandler.saveSettings(getApplicationContext(), settings);
        SaveHandler.saveSongs(getApplicationContext(), songs);

        Intent intent = new Intent();
        intent.putExtra(Constants.SETTINGS, settings);
        intent.putExtra(Constants.NEW_SONGS_IMPORTED, newSongsImported);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        save(null);
        super.onBackPressed();
    }

    private void setNewSongsImported(List<Song> songsImported) {
        newSongsImported = true;
        for (Song song : songsImported) {
            if (isNameTaken(song.getName())) {
                int i = 1;
                while (isNameTaken(song.getName() + "_" + i))
                    i++;
                song.setName(song.getName() + "_" + i);
            }
            songs.add(song);
        }
    }

    private boolean isNameTaken(String name) {
        for (Song song : songs)
            if (song.getName().equals(name))
                return true;

        return false;
    }

    private void setPreviewText() {
        textSizePreview.setText(String.format("%d", settings.getDefaultTextSize()));
        textSizePreview.setTextSize(TypedValue.COMPLEX_UNIT_SP, settings.getDefaultTextSize());
    }
}