
package com.example.harmonicapracticeassistant;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity
{
    boolean isFABOpen = false;
    FrameLayout fab1;
    FrameLayout fab2;
    FrameLayout fab3;
//    FrameLayout fab4 = (FrameLayout) findViewById(R.id.over);
//    FrameLayout fab5 = (FrameLayout) findViewById(R.id.wave);
//    FrameLayout fab6 = (FrameLayout) findViewById(R.id.bracket_close);
//    FrameLayout fab7 = (FrameLayout) findViewById(R.id.bracket_open);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.menu);
        fab1 = (FrameLayout) findViewById(R.id.half_bend);
        fab2 = (FrameLayout) findViewById(R.id.whole_bend);
        fab3 = (FrameLayout) findViewById(R.id.whole_half_bend);

        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (!isFABOpen)
                {
                    showFABMenu();
                }
                else
                {
                    closeFABMenu(null);
                }
            }
        });


    }

    private void showFABMenu()
    {
        Toast.makeText(this, "open", Toast.LENGTH_SHORT).show();
        isFABOpen = true;
        fab1.setVisibility(View.VISIBLE);
        fab1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fab2.setVisibility(View.VISIBLE);
        fab2.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        fab3.setVisibility(View.VISIBLE);
        fab3.animate().translationY(-getResources().getDimension(R.dimen.standard_155));
        findViewById(R.id.close_button).setVisibility(View.VISIBLE);
    }

    public void closeFABMenu(View view)
    {
        Toast.makeText(this, "close", Toast.LENGTH_SHORT).show();
        isFABOpen = false;
        fab1.animate().translationY(0);
        fab1.setVisibility(View.GONE);
        fab2.animate().translationY(0);
        fab2.setVisibility(View.GONE);
        fab3.animate().translationY(0);
        fab3.setVisibility(View.GONE);
        findViewById(R.id.close_button).setVisibility(View.GONE);
    }

    public void toast(View view)
    {
        Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
    }
}