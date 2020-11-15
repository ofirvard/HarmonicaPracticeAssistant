package com.example.harmonicapracticeassistant;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    private AppSettings settings;
    private EditText textSizePreview;
    private boolean newSongsImported;
    private Uri fileUri;
    private List<Song> songs;
    private SwitchMaterial slim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settings = getIntent().getExtras().getParcelable(Keys.SETTINGS);
        songs = getIntent().getExtras().getParcelableArrayList(Keys.SONGS);

        textSizePreview = findViewById(R.id.text_size_preview);
        setPreviewText();

        slim = findViewById(R.id.slim);
        slim.setChecked(settings.isKeyboardSlim());
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

            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        } else {
//            Toast.makeText(SettingsActivity.this,
//                    "Permission already granted",
//                    Toast.LENGTH_SHORT)
//                    .show();
        }
    }


    public void exportSongs(View view) {
//        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 1);
//        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, 1);
//
//        try {
//            File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//            File myFile = new File(folder, "harmonica-songs" + Calendar.getInstance().getTime().toString() + ".json");
//            Gson gson = new Gson();
//
//            FileOutputStream fileOutputStream = new FileOutputStream(myFile);
//            fileOutputStream.write(gson.toJson(songs).getBytes());
//            fileOutputStream.close();
//            Toast.makeText(getApplicationContext(), "Details Saved in " + myFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/json");
        intent.putExtra(Intent.EXTRA_TITLE, "harmonica-songs.json");

        startActivityForResult(intent, Keys.FILE_SAVE_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Keys.FILE_PICKER_REQUEST_CODE)
            if (resultCode == RESULT_OK) {
                fileUri = data.getData();

                File file = new File(FileUtils.getPath(this, fileUri));
                List<Song> songs = SaveHandler.loadSongs(file);
// TODO: 10/13/20 find out why the file is emptyZ

                if (songs.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(R.string.bad_file);
                    builder.setPositiveButton(R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        if (requestCode == Keys.FILE_SAVE_REQUEST_CODE)
            if (resultCode == RESULT_OK) {
                fileUri = data.getData();

                try {

                    Gson gson = new Gson();

                    OutputStream os = this.getContentResolver().openOutputStream(fileUri);
                    if (os != null) {
                        os.write(gson.toJson(songs).getBytes());
                        os.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
    }

    public void save(View view) {
        settings.setKeyboardSlim(slim.isChecked());
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