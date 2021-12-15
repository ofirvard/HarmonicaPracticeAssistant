package com.example.harmonicapracticeassistant.editor;

public class Tab
{
    int hole;//hole 0 can be space
    Boolean draw;
    BendOld bend;

    public Tab(int hole, Boolean draw, BendOld bend)
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

    public enum BendOld
    {
        OLD, HALF_STEP_BEND, WHOLE_STEP_BEND, STEP_AND_A_HALF_BEND
    }
}
