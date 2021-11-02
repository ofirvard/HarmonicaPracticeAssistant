package com.example.harmonicapracticeassistant;

import android.animation.Animator;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class SongBuilder2 extends AppCompatActivity
{
    Song2 song = new Song2();

    private boolean isFABOpen = true;
    boolean editMode = true;
    TabsTextAdapter tabsTextAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_builder2);

        tabsTextAdapter = new TabsTextAdapter(song.tabAndTexts);

        ((RecyclerView) findViewById(R.id.recycler_view_song_tabs_and_text)).setAdapter(tabsTextAdapter);

        switchMode(null);
    }

    public void switchMode(View view)
    {
        clear();

        if (editMode)
        {
            editMode = false;

            ((FloatingActionButton) findViewById(R.id.edit)).setImageDrawable(getResources().getDrawable(R.drawable.pencil, getTheme()));

            findViewById(R.id.song_title).setEnabled(true);
            findViewById(R.id.action_buttons_2).setVisibility(View.GONE);
            findViewById(R.id.zoom_in).setVisibility(View.VISIBLE);
            findViewById(R.id.zoom_out).setVisibility(View.VISIBLE);
        }
        else
        {
            editMode = true;

            ((FloatingActionButton) findViewById(R.id.edit)).setImageDrawable(getResources().getDrawable(R.drawable.floppy_disk, getTheme()));

            findViewById(R.id.song_title).setEnabled(false);
            findViewById(R.id.action_buttons_2).setVisibility(View.VISIBLE);
            findViewById(R.id.zoom_in).setVisibility(View.GONE);
            findViewById(R.id.zoom_out).setVisibility(View.GONE);
        }
    }


    public void zoomIn(View view)
    {
        clear();
    }

    public void zoomOut(View view)
    {
        clear();
    }

    public void space(View view)
    {
        clear();
    }

    public void backspace(View view)
    {
        clear();
    }

    public void enter(View view)
    {
        clear();

        song.addLine();
        tabsTextAdapter.notifyDataSetChanged();
    }

    public void note(View view)
    {
        clear();
    }

    public void openFABMenu(View view)
    {
        if (!isFABOpen)
        {
            isFABOpen = true;
            findViewById(R.id.song_title).setEnabled(false);

            findViewById(R.id.half_bend).setVisibility(View.VISIBLE);
            findViewById(R.id.whole_bend).setVisibility(View.VISIBLE);
            findViewById(R.id.step_and_a_half_bend).setVisibility(View.VISIBLE);

            findViewById(R.id.half_bend).animate().translationY(0);
            findViewById(R.id.whole_bend).animate().translationY(0);
            findViewById(R.id.step_and_a_half_bend).animate().translationY(0);
        }
        else closeFABMenu();
    }

    private void closeFABMenu()
    {
        if (isFABOpen)
        {
            isFABOpen = false;
            findViewById(R.id.song_title).setEnabled(true);

            findViewById(R.id.half_bend).animate().translationY(getResources().getDimension(R.dimen.fab_1));
            findViewById(R.id.whole_bend).animate().translationY(getResources().getDimension(R.dimen.fab_2));
            findViewById(R.id.step_and_a_half_bend).animate().translationY(getResources().getDimension(R.dimen.fab_3)).setListener(new Animator.AnimatorListener()
            {
                @Override
                public void onAnimationStart(Animator animator)
                {

                }

                @Override
                public void onAnimationEnd(Animator animator)
                {
                    if (!isFABOpen)
                    {
                        findViewById(R.id.half_bend).setVisibility(View.GONE);
                        findViewById(R.id.whole_bend).setVisibility(View.GONE);
                        findViewById(R.id.step_and_a_half_bend).setVisibility(View.GONE);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animator)
                {

                }

                @Override
                public void onAnimationRepeat(Animator animator)
                {

                }
            });
        }
    }

    public void clear()
    {
        findViewById(R.id.song_title).clearFocus();
        closeFABMenu();
    }

    public void hello(View view)
    {
        Toast.makeText(this, R.string.import_songs_fail, Toast.LENGTH_SHORT).show();
    }
}