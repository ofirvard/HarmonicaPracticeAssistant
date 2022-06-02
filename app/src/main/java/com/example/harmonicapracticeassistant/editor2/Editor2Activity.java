package com.example.harmonicapracticeassistant.editor2;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.example.harmonicapracticeassistant.R;

import androidx.appcompat.app.AppCompatActivity;

public class Editor2Activity extends AppCompatActivity
{
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor2);

        editText = findViewById(R.id.editTextTextPersonName);
        editText.setInputType(EditorInfo.TYPE_NULL);
        editText.setShowSoftInputOnFocus(false);
        editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
    }

    public void test(View view)
    {
        editText.isCursorVisible();
        editText.getText().append("a");
        editText.getSelectionStart();
        editText.getSelectionEnd();
    }
}