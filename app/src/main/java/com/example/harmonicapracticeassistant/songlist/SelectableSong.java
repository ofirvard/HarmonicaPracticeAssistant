package com.example.harmonicapracticeassistant.songlist;

import com.example.harmonicapracticeassistant.editor.Song;

public class SelectableSong
{
    private boolean isSelected;
    private Song song;

    public SelectableSong(Song song)
    {
        this.song = song;
        this.isSelected = false;
    }

    public Song getSong()
    {
        return song;
    }

    public void setSong(Song song)
    {
        this.song = song;
    }

    public boolean isSelected()
    {
        return isSelected;
    }

    public void setSelected(boolean selected)
    {
        isSelected = selected;
    }

    @Override
    public String toString()
    {
        return song.toString();
    }
}
