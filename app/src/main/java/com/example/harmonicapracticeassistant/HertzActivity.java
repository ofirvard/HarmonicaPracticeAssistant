package com.example.harmonicapracticeassistant;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class HertzActivity extends AppCompatActivity
{
    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hertz);

        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
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

    public boolean checkPermission()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        else if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED)
        {
            Toast.makeText(this, R.string.hertz_fail, Toast.LENGTH_SHORT).show();
        }
        else
        {
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Keys.HERTZ_REQUEST_CODE)
            if (resultCode == RESULT_OK)
            {
            }
    }
}