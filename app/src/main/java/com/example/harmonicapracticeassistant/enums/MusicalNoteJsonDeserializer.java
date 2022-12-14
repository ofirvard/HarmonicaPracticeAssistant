package com.example.harmonicapracticeassistant.enums;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

@Deprecated
public class MusicalNoteJsonDeserializer implements JsonDeserializer<MusicalNote>
{
    @Override
    public MusicalNote deserialize(JsonElement musicalNote, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        return isNaturalNote(musicalNote.getAsString()) ?
                context.deserialize(musicalNote, NaturalNote.class) :
                context.deserialize(musicalNote, FlatNote.class);
    }

    private boolean isNaturalNote(String note)
    {
        return note.length() == 1;
    }
}
