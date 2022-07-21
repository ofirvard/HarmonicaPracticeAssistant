package com.example.harmonicapracticeassistant.editor2;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.example.harmonicapracticeassistant.R;
import com.example.harmonicapracticeassistant.enums.Bend;

import androidx.appcompat.app.AppCompatActivity;

public class Editor2Activity extends AppCompatActivity
{
    private EditText editText;
    private EditorUtil editorUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor2);

        editText = findViewById(R.id.editTextTextPersonName);
        editText.setShowSoftInputOnFocus(false);
        editText.setRawInputType(InputType.TYPE_CLASS_TEXT);

        editorUtil = new EditorUtil(editText);
    }

    public void writeNote(View view)
    {
        editorUtil.writeNote(this, view.getId());
    }

    public void halfStepBend(View view)
    {
        editorUtil.bend(Bend.HALF_STEP);
    }

    public void wholeStepBend(View view)
    {
        editorUtil.bend(Bend.WHOLE_STEP);
    }

    public void stepAndAHalfBend(View view)
    {
        editorUtil.bend(Bend.STEP_AND_A_HALF);
    }

    public void newLine(View view)
    {
        editorUtil.newLine();
    }

    public void backspace(View view)
    {
        // TODO: 24/06/2022 add long click
        editorUtil.smartDelete();
    }

    public void space(View view)
    {
        editorUtil.space();
    }
}