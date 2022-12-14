package com.example.harmonicapracticeassistant.richter;

import java.util.ArrayList;
import java.util.List;

public class Harmonica
{
    private List<Hole> holes;

    public Harmonica(RichterKey key, List<Tuning> tuningList)
    {
        this.holes = new ArrayList<>();

        for (Tuning tuning : tuningList)
            holes.add(new Hole(key, tuning));
    }


}
