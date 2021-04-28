package com.example.harmonicapracticeassistant;

public class Tab
{
    int hole;//hole 0 can be space
    Boolean draw;
    Bend bend;

    public Tab(int hole, Boolean draw, Bend bend)
    {
        this.hole = hole;
        this.draw = draw;
        this.bend = bend;
    }

    public String getNote()
    {
        String note = "";

        if (draw)
            note += "-" + hole;
        else
            note = "" + hole;

        switch (bend)
        {
            case HALF_STEP_BEND:
                note += "'";
                break;
            case WHOLE_STEP_BEND:
                note += "\"";
                break;
            case STEP_AND_A_HALF_BEND:
                note += "\"'";
                break;
        }

        return note;
    }

    enum Bend
    {
        NONE, HALF_STEP_BEND, WHOLE_STEP_BEND, STEP_AND_A_HALF_BEND
    }
}
