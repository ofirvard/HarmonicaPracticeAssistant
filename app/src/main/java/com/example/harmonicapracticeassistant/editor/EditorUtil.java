package com.example.harmonicapracticeassistant.editor;

import android.content.Context;
import android.widget.EditText;

import com.example.harmonicapracticeassistant.R;
import com.example.harmonicapracticeassistant.enums.Bend;
import com.example.harmonicapracticeassistant.utils.Constants;
import com.example.harmonicapracticeassistant.utils.TextUtil;

import java.util.List;

public class EditorUtil
{
    // TODO: 9/23/2022 merge all add char
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
        return TextUtil.getLegalNotesList(editText.getText().toString());
    }

    public void smartDelete()
    {
        if (editText.getSelectionStart() != editText.getSelectionEnd())
            deleteSelection();
        else
            deleteLastNote();
    }

    public void writeNote(Context context, boolean isBlow, int id)
    {
        String note = findNoteString(context, isBlow, id);

        if (!note.equals(""))
        {
            space();
            writeToEditText(note);
        }
    }

    public void space()
    {
        writeToEditText(Constants.SPACE);
    }

    public void newLine()
    {
        writeToEditText(Constants.NEW_LINE);
    }

    public void bend(Bend bend)
    {
        writeToEditText(bend.toString());
    }

    public void colorIllegalTabs()
    {
        editText.setText(TextUtil.getColorIllegalTabsSpannable(editText.getText().toString()));
    }

    private void writeToEditText(String s)
    {
        editText.getText().replace(editText.getSelectionStart(),
                editText.getSelectionEnd(),
                s);
    }

    private String findNoteString(Context context, boolean isBlow, int id)
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
        else if (id == R.id.note_1)
        {
            return isBlow ? context.getString(R.string.note_blow_1) : context.getString(R.string.note_draw_1);
        }
        else if (id == R.id.note_2)
        {
            return isBlow ? context.getString(R.string.note_blow_2) : context.getString(R.string.note_draw_2);
        }
        else if (id == R.id.note_3)
        {
            return isBlow ? context.getString(R.string.note_blow_3) : context.getString(R.string.note_draw_3);
        }
        else if (id == R.id.note_4)
        {
            return isBlow ? context.getString(R.string.note_blow_4) : context.getString(R.string.note_draw_4);
        }
        else if (id == R.id.note_5)
        {
            return isBlow ? context.getString(R.string.note_blow_5) : context.getString(R.string.note_draw_5);
        }
        else if (id == R.id.note_6)
        {
            return isBlow ? context.getString(R.string.note_blow_6) : context.getString(R.string.note_draw_6);
        }
        else if (id == R.id.note_7)
        {
            return isBlow ? context.getString(R.string.note_blow_7) : context.getString(R.string.note_draw_7);
        }
        else if (id == R.id.note_8)
        {
            return isBlow ? context.getString(R.string.note_blow_8) : context.getString(R.string.note_draw_8);
        }
        else if (id == R.id.note_9)
        {
            return isBlow ? context.getString(R.string.note_blow_9) : context.getString(R.string.note_draw_9);
        }
        else if (id == R.id.note_10)
        {
            return isBlow ? context.getString(R.string.note_blow_10) : context.getString(R.string.note_draw_10);
        }

        return "";
    }

    private void deleteSelection()
    {
        writeToEditText(Constants.EMPTY_STRING);
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
