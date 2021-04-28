package com.example.harmonicapracticeassistant;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

public class SongBuilder2 extends AppCompatActivity
{
    Song2 song = new Song2();
    FloatingActionButton backspace;
    FloatingActionButton enter;
    FloatingActionButton space;
    FloatingActionButton edit;
    FloatingActionButton zoomIn;
    FloatingActionButton zoomOut;
    FloatingActionButton menu;
    boolean editMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_builder2);

        backspace = findViewById(R.id.backspace);
        space = findViewById(R.id.space);
        enter = findViewById(R.id.enter);
        edit = findViewById(R.id.edit);
        zoomIn = findViewById(R.id.zoom_in);
        zoomOut = findViewById(R.id.zoom_out);
        menu = findViewById(R.id.more_buttons);

        switchMode(null);
    }

    public void switchMode(View view)
    {
        if (editMode)
        {
            editMode = false;

            edit.setImageDrawable(getResources().getDrawable(R.drawable.pencil, getTheme()));

            backspace.setVisibility(View.GONE);
            enter.setVisibility(View.GONE);
            space.setVisibility(View.GONE);
            menu.setVisibility(View.GONE);
            zoomIn.setVisibility(View.VISIBLE);
            zoomOut.setVisibility(View.VISIBLE);
        }
        else
        {
            editMode = true;

            edit.setImageDrawable(getResources().getDrawable(R.drawable.floppy_disk, getTheme()));

            backspace.setVisibility(View.VISIBLE);
            enter.setVisibility(View.VISIBLE);
            space.setVisibility(View.VISIBLE);
            menu.setVisibility(View.VISIBLE);
            zoomIn.setVisibility(View.GONE);
            zoomOut.setVisibility(View.GONE);
        }
    }


    public void zoomIn(View view)
    {
    }

    public void zoomOut(View view)
    {
    }

    public void space(View view)
    {
    }

    public void backspace(View view)
    {
    }

    public void more_buttons(View view)
    {
    }

    public void enter(View view)
    {
    }

    public void note(View view)
    {
    }

    private void openMenu()
    {

    }

    private void closeMenu()
    {
    }
}