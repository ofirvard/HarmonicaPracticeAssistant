package com.example.harmonicapracticeassistant.richter;

public class Note
{
    private final int noteNumber;
    private final int octave;
    private final boolean isSharp;

    public Note(RichterKey key, int halfSteps)
    {
        this.noteNumber = calculateNoteNumber(key, halfSteps);
        this.octave = calculateOctave(key, halfSteps);
        this.isSharp = key.isSharp();
    }

    public int getNoteNumber()
    {
        return noteNumber;
    }

    public int getOctave()
    {
        return octave;
    }

    @Override
    public String toString()
    {
        return noteNumberToString() + octave;
    }

    private int calculateNoteNumber(RichterKey key, int halfSteps)
    {
        int numberOfNotesInAnOctave = 12;

        return (((key.getNoteNumber() + halfSteps)
                % numberOfNotesInAnOctave)
                + numberOfNotesInAnOctave)
                % numberOfNotesInAnOctave;
    }

    private int calculateOctave(RichterKey key, int halfSteps)
    {
// TODO: 12/11/2022 check that this works 
        return (int) (key.getOctave() + Math.floor((double) (key.getNoteNumber() + halfSteps) / 12));
    }

    private String noteNumberToString()
    {
        switch (noteNumber)
        {
            case 0:
                return "C";

            case 1:
                return isSharp ? "C#" : "Db";

            case 2:
                return "D";

            case 3:
                return isSharp ? "D#" : "Eb";

            case 4:
                return "E";

            case 5:
                return "F";

            case 6:
                return isSharp ? "F#" : "Gb";

            case 7:
                return "G";

            case 8:
                return isSharp ? "G#" : "Ab";

            case 9:
                return "A";

            case 10:
                return isSharp ? "A#" : "Bb";

            case 11:
                return "B";

            default:
                return "";
        }
    }
}
