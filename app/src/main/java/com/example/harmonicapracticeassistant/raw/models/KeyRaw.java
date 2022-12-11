package com.example.harmonicapracticeassistant.raw.models;

import java.util.List;

public class KeyRaw
{
    private String keyName;
    private boolean isSharp;
    private List<HoleRaw> holes;

    public String getKeyName()
    {
        return keyName;
    }

    public boolean isSharp()
    {
        return isSharp;
    }

    public List<HoleRaw> getHoles()
    {
        return holes;
    }
}
