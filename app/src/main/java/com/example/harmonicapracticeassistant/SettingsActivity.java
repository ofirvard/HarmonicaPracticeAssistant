package com.example.harmonicapracticeassistant;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.OpenableColumns;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static android.util.Log.e;

public class SettingsActivity extends AppCompatActivity {
    private AppSettings settings;
    private EditText textSizePreview;
    private boolean newSongsImported;
    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settings = getIntent().getExtras().getParcelable(Keys.SETTINGS);

        textSizePreview = findViewById(R.id.text_size_preview);
        setPreviewText();
    }

    public void increase(View view) {// TODO: 10/10/2020 why dose this seem smaller?
        if (settings.getDefaultTextSize() < Keys.MAX_TEXT_SIZE) {
            settings.setDefaultTextSize(settings.getDefaultTextSize() + 1);
            setPreviewText();
        }
    }

    public void decrease(View view) {
        if (settings.getDefaultTextSize() > Keys.MIN_TEXT_SIZE) {
            settings.setDefaultTextSize(settings.getDefaultTextSize() - 1);
            setPreviewText();
        }
    }

    public void importSongs(View view) {
        // TODO: 10/10/2020 file picker
        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, 1);

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        startActivityForResult(Intent.createChooser(intent, "Choose a file"), Keys.FILE_PICKER_REQUEST_CODE);

    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(SettingsActivity.this, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(SettingsActivity.this,
                    new String[]{permission},
                    requestCode);
        } else {
            Toast.makeText(SettingsActivity.this,
                    "Permission already granted",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }


    public void exportSongs(View view) {
        // TODO: 10/10/2020 file saver? figure out how to let user save file
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Keys.FILE_PICKER_REQUEST_CODE)
            if (resultCode == RESULT_OK) {
                fileUri = data.getData();

                File file = new File(FileUtils.getPath(this, fileUri));
                List<Song> songs = SaveHandler.loadSongs(file);
// TODO: 10/13/20 find out why the file is empty 

                if (songs.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(R.string.bad_file);
                    builder.setPositiveButton(R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }

    }

    public void save(View view) {
        SaveHandler.saveSettings(getApplicationContext(), settings);

        Intent intent = new Intent();
        intent.putExtra(Keys.SETTINGS, settings);
        intent.putExtra(Keys.NEW_SONGS_IMPORTED, newSongsImported);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void setPreviewText() {
        textSizePreview.setText(String.format("%d", settings.getDefaultTextSize()));
        textSizePreview.setTextSize(TypedValue.COMPLEX_UNIT_SP, settings.getDefaultTextSize());
    }
}