package com.example.harmonicapracticeassistant.harmonica;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Harmonica
{
    private final HarmonicaTuningType tuningType;
    private final Key key;
    private final List<Hole> holes;

    public Harmonica(Key key, List<Tuning> tuningList, HarmonicaTuningType tuningType, Note baseNote)
    {
        this.key = key;
        this.tuningType = tuningType;
        this.holes = new ArrayList<>();

        for (Tuning tuning : tuningList)
            holes.add(new Hole(key, tuning, baseNote));
    }

    public HarmonicaTuningType getTuningType()
    {
        return tuningType;
    }

    public Key getKey()
    {
        return key;
    }

    public List<Hole> findHoles(Note note)
    {
        return holes.stream()
                .filter(hole -> hole.isNoteEqual(note))
                .collect(Collectors.toList());
    }

    public List<Note> getAllNotes()
    {
        return holes.stream()
                .map(Hole::getNote)
                .sorted(Comparator.comparingInt(Note::getOctave)
                        .thenComparingInt(Note::getNoteNumber))
                .collect(Collectors.toList());
    }

    @Override
    public String toString()
    {
        return key.toString();
    }

    public List<Hole> getAllMatchingHoles(Note note)
    {
        if (note == null)
            return new ArrayList<>();

        return holes.stream()
                .filter(hole -> hole.isNoteEqual(note))
                .collect(Collectors.toList());
    }
}
