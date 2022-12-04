package com.example.harmonicapracticeassistant.utils;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TextUtil
{
    public static List<String> getLegalNotesList(String notes)
    {
        return Arrays.stream(notes.split("[ \n]"))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    public static Spannable getColorIllegalTabsSpannable(String tabs)
    {
        List<String> illegalTabs = HarmonicaUtils.findIllegalTabs(getLegalNotesList(tabs));

        Spannable spannable = new SpannableString(tabs);

        for (String illegalTab : illegalTabs)
            colorIllegalTab(tabs, spannable, illegalTab);

        return spannable;
    }

    private static void colorIllegalTab(String tabs, Spannable spannable, String illegalTab)
    {
        int index = tabs.indexOf(illegalTab);

        while (index >= 0)
        {
            // TODO: 9/1/2022 check if start or if space before
            if (index == 0 || tabs.charAt(index - 1) == ' ')
                spannable.setSpan(new ForegroundColorSpan(Color.RED), index, index + illegalTab.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

            index = tabs.indexOf(illegalTab, index + 1);
        }
    }
}
