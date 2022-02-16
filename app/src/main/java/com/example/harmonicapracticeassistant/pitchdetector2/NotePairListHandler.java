package com.example.harmonicapracticeassistant.pitchdetector2;

import android.util.Pair;

import com.example.harmonicapracticeassistant.enums.MusicalNote;
import com.example.harmonicapracticeassistant.harmonica.Hole;
import com.example.harmonicapracticeassistant.harmonica.Note;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotePairListHandler
{
    private final List<Pair<Note, List<Hole>>> notePairList = new ArrayList<>();
    private MusicalNote lastNote;

    public void add(Pair<Note, List<Hole>> notePair)
    {
        if (notePair != null)
        {
            if (lastNote != notePair.first.getMusicalNote())
            {
                lastNote = notePair.first.getMusicalNote();
                notePairList.add(notePair);
            }
        }
        else
            lastNote = null;
    }

    public void clear()
    {
        notePairList.clear();
    }

    public List<Pair<Note, List<Hole>>> getNotePairList()
    {
        return Collections.unmodifiableList(notePairList);
    }

    public int getLastPosition()
    {
        return notePairList.size() > 0 ? notePairList.size() - 1 : 0;
    }
}
