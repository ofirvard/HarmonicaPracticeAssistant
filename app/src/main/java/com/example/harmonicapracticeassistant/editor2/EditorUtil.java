package com.example.harmonicapracticeassistant.editor2;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;

import com.example.harmonicapracticeassistant.R;
import com.example.harmonicapracticeassistant.enums.Bend;
import com.example.harmonicapracticeassistant.utils.HarmonicaUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EditorUtil
{
    private final EditText editText;

    public EditorUtil(EditText editText)
    {
        this.editText = editText;
    }

    public String getEditTextString()
    {
        return editText.getText().toString();
    }

    public List<String> getNotesStringList()
    {
        return Arrays.stream(editText.getText().toString()
                        .split("[ \n]"))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    public void smartDelete()
    {
        if (editText.getSelectionStart() != editText.getSelectionEnd())
            deleteSelection();
        else
            deleteLastNote();
    }

    public void writeNote(Context context, int id)
    {
        String note = findNoteString(context, id);

        if (!note.equals(""))
        {
            space();
            editText.getText().replace(editText.getSelectionStart(),
                    editText.getSelectionEnd(),
                    note);
        }
    }

    public void space()
    {
        editText.getText().replace(editText.getSelectionStart(), editText.getSelectionEnd(), " ");
    }

    public void newLine()
    {
        editText.getText().insert(editText.getSelectionStart(), "\n");
    }

    public void bend(Bend bend)
    {
        editText.getText().insert(editText.getSelectionStart(), bend.toString());
    }

    public void colorIllegalTabs()
    {
        List<String> illegalTabs = HarmonicaUtils.findIllegalTabs(getNotesStringList());

        String tabs = editText.getText().toString();
        Spannable spannable = new SpannableString(tabs);

        for (String illegalTab : illegalTabs)
            colorIllegalTab(tabs, spannable, illegalTab);

        editText.setText(spannable);
    }

    private void colorIllegalTab(String tabs, Spannable spannable, String illegalTab)
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

    private String findNoteString(Context context, int id)
    {
        if (id == R.id.note_blow_1)
        {
            return context.getString(R.string.note_blow_1);
        }
        else if (id == R.id.note_blow_2)
        {
            return context.getString(R.string.note_blow_2);
        }
        else if (id == R.id.note_blow_3)
        {
            return context.getString(R.string.note_blow_3);
        }
        else if (id == R.id.note_blow_4)
        {
            return context.getString(R.string.note_blow_4);
        }
        else if (id == R.id.note_blow_5)
        {
            return context.getString(R.string.note_blow_5);
        }
        else if (id == R.id.note_blow_6)
        {
            return context.getString(R.string.note_blow_6);
        }
        else if (id == R.id.note_blow_7)
        {
            return context.getString(R.string.note_blow_7);
        }
        else if (id == R.id.note_blow_8)
        {
            return context.getString(R.string.note_blow_8);
        }
        else if (id == R.id.note_blow_9)
        {
            return context.getString(R.string.note_blow_9);
        }
        else if (id == R.id.note_blow_10)
        {
            return context.getString(R.string.note_blow_10);
        }
        else if (id == R.id.note_draw_1)
        {
            return context.getString(R.string.note_draw_1);
        }
        else if (id == R.id.note_draw_2)
        {
            return context.getString(R.string.note_draw_2);
        }
        else if (id == R.id.note_draw_3)
        {
            return context.getString(R.string.note_draw_3);
        }
        else if (id == R.id.note_draw_4)
        {
            return context.getString(R.string.note_draw_4);
        }
        else if (id == R.id.note_draw_5)
        {
            return context.getString(R.string.note_draw_5);
        }
        else if (id == R.id.note_draw_6)
        {
            return context.getString(R.string.note_draw_6);
        }
        else if (id == R.id.note_draw_7)
        {
            return context.getString(R.string.note_draw_7);
        }
        else if (id == R.id.note_draw_8)
        {
            return context.getString(R.string.note_draw_8);
        }
        else if (id == R.id.note_draw_9)
        {
            return context.getString(R.string.note_draw_9);
        }
        else if (id == R.id.note_draw_10)
        {
            return context.getString(R.string.note_draw_10);
        }

        return "";
    }

    private void deleteSelection()
    {
        editText.getText().replace(editText.getSelectionStart(), editText.getSelectionEnd(), "");
    }

    private void deleteLastNote()
    {

        if (editText.getSelectionStart() != 0)
            editText.getText().replace(editText.getSelectionStart() - 1, editText.getSelectionEnd(), "");

// TODO: 24/06/2022 smart delete: -7" -8| 9 -> -7"| 9
//        int end = editText.getSelectionEnd();
//
//        editText.getText().replace(getStartForDelete(end), end, "");
    }

//    private int getStartForDelete(int end)
//    {
//        int i;
//        // TODO: 24/06/2022 loop back and check if to delete the next char
//        for (i = end - 1; i >= 0; i--)
//        {
//            if (isDeleteNextChar())
//        }
//
//        return i;
//    }
//
//    private boolean checkIfDeleteSingleChar(int end)
//    {
//        // TODO: 24/06/2022 if " ",',",number delete till number (and minus)
//        String firstChar = editText.getText().toString().substring(end - 1, end);
//
//        return !firstChar.equals("\"") &&
//                !firstChar.equals("'") &&
//                editText.getText().toString().substring(0, 1).matches("[0-9]") &&
//                !firstChar.equals(" ");
//    }

}
