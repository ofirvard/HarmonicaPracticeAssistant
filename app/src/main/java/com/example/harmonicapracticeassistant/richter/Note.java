package com.example.harmonicapracticeassistant.richter;

public class Note
{
    private final int numberOfNotesInAnOctave = 12;
    private int noteNumber;
    private int octave;

    public Note(RichterKey key, int halfSteps)
    {
        this.noteNumber = calculateNoteNumber(key, halfSteps);
        this.octave = calculateOctave(key, halfSteps);
    }

    public int getNoteNumber()
    {
        return noteNumber;
    }

    public int getOctave()
    {
        return octave;
    }

    public String toString(RichterKey key)
    {
        return noteNumberToString(noteNumber, key) + octave;
    }

    private int calculateNoteNumber(RichterKey key, int halfSteps)
    {
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

    private static String noteNumberToString(int semitone, RichterKey key)
    {
        switch (semitone)
        {
            case 0:
                return "C";

            case 1:
                return key.isSharp() ? "C#" : "Db";

            case 2:
                return "D";

            case 3:
                return key.isSharp() ? "D#" : "Eb";

            case 4:
                return "E";

            case 5:
                return "F";

            case 6:
                return key.isSharp() ? "F#" : "Gb";

            case 7:
                return "G";

            case 8:
                return key.isSharp() ? "G#" : "Ab";

            case 9:
                return "A";

            case 10:
                return key.isSharp() ? "A#" : "Bb";

            case 11:
                return "B";

            default:
                return "";
        }
    }
}
