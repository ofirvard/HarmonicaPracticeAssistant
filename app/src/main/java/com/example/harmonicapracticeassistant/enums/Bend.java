package com.example.harmonicapracticeassistant.enums;

public enum Bend
{
    NONE(""),
    HALF_STEP("'"),
    WHOLE_STEP("\""),
    STEP_AND_A_HALF("\"'"),
    OVER("o");

    private final String name;

    Bend(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
