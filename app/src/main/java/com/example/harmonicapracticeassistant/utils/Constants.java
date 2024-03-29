package com.example.harmonicapracticeassistant.utils;

import com.example.harmonicapracticeassistant.harmonica.HarmonicaTuningType;

public class Constants
{
    public static final String NEW_SONGS_IMPORTED = "NEW_SONGS_IMPORTED";
    public static final String JSON_END = ".json";
    public static final String NOT_APPLICABLE = "N/A";
    public static final String DEFAULT_KEY = "C";
    public static final String SETTINGS_FILE = "settings.json";
    public static final String SONGS_FILE = "songs.json";

    public static final double DEVIATION_PERCENT = 0.01;

    public static final float NA_NOTE_FREQUENCY = -1;

    public static final int MINIMUM_HERTZ_THRESHOLD = 10;
    public static final int DEFAULT_TEXT_SIZE = 15;
    public static final int MAX_TEXT_SIZE = 30;
    public static final int MIN_TEXT_SIZE = 10;

    public static final int SONG_LIST_UPDATE_CODE = 1;
    public static final int SETTINGS_REQUEST_CODE = 2;
    public static final int FILE_PICKER_REQUEST_CODE = 3;
    public static final int FILE_SAVE_REQUEST_CODE = 4;
    public static final int PITCH_DETECTOR_REQUEST_CODE = 5;

    public static final int NOTE_DRAW_TEN = -10;
    public static final int NOTE_BLOW_TEN = 10;
    public static final int NOTE_SPACE = 11;
    public static final int NOTE_ENTER = 12;
    public static final int BRACKET_OPEN = 14;
    public static final int BRACKET_CLOSE = 15;
    public static final int WAVE = 16;
    public static final int HALF_BEND = 17;
    public static final int WHOLE_BEND = 18;
    public static final int WHOLE_HALF_BEND = 19;
    public static final int OVER = 20;

    public static final int BACKSPACE_LONG_CLICK_INITIAL_DELAY = 500;
    public static final int BACKSPACE_LONG_CLICK_DELETE_DELAY = 50;
    public static final String NEW_LINE = "\n";
    public static final String SPACE = " ";
    public static final String EMPTY_STRING = "";

    public static final int DEFAULT_FREQ = 440;
    public static final HarmonicaTuningType DEFAULT_TUNING = HarmonicaTuningType.RICHTER;
    public static final long SLEEP_TIME = 100;
}
