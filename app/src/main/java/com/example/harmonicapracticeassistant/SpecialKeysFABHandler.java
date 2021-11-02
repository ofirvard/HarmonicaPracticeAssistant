package com.example.harmonicapracticeassistant;

import android.animation.Animator;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SpecialKeysFABHandler
{
    private boolean isFABOpen = false;
    private View fabBGLayout;
    private FrameLayout fab1;
    private FrameLayout fab2;
    private FrameLayout fab3;
    private FrameLayout fab4;
    private FrameLayout fab5;
    private FrameLayout fab6;
    private FrameLayout fab7;
    private EditText title;
    private Activity activity;

    public SpecialKeysFABHandler(Activity activity)
    {
        this.activity = activity;

        fab1 = activity.findViewById(R.id.half_bend);
        fab2 = activity.findViewById(R.id.whole_bend);
        fab3 = activity.findViewById(R.id.step_and_a_half_bend);
//        fab4 = activity.findViewById(R.id.over);
//        fab5 = activity.findViewById(R.id.wave);
//        fab6 = activity.findViewById(R.id.bracket_open);
//        fab7 = activity.findViewById(R.id.bracket_close);
        title = activity.findViewById(R.id.song_title);

        FloatingActionButton fab = activity.findViewById(R.id.more_buttons);
        fab.setOnClickListener(view -> {
            if (!isFABOpen)
            {
                showFABMenu();
            }
            else
            {
                closeFABMenu();
            }
        });
        fabBGLayout = activity.findViewById(R.id.fab_bg_layout);
        fabBGLayout.setOnClickListener(view -> closeFABMenu());
    }

    public void closeFABMenu()
    {
        title.setEnabled(true);

        if (isFABOpen)
        {
            isFABOpen = false;
            fabBGLayout.setVisibility(View.GONE);

            fab1.animate().translationY(0);
            fab2.animate().translationY(0);
//            fab3.animate().translationY(0);
//            fab4.animate().translationY(0);
//            fab5.animate().translationY(0);
//            fab6.animate().translationY(0);
//            fab7.animate().translationY(0)
            fab3.animate().translationY(0).setListener(new Animator.AnimatorListener()
            {
                @Override
                public void onAnimationStart(Animator animator)
                {

                }

                @Override
                public void onAnimationEnd(Animator animator)
                {
                    if (!isFABOpen)
                    {
                        fab1.setVisibility(View.GONE);
                        fab2.setVisibility(View.GONE);
                        fab3.setVisibility(View.GONE);
//                        fab4.setVisibility(View.GONE);
//                        fab5.setVisibility(View.GONE);
//                        fab6.setVisibility(View.GONE);
//                        fab7.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animator)
                {

                }

                @Override
                public void onAnimationRepeat(Animator animator)
                {

                }
            });
        }
    }

    public void showFABMenu()
    {
        title.setEnabled(false);
        title.clearFocus();

        isFABOpen = true;
        fabBGLayout.setVisibility(View.VISIBLE);

        fab1.setVisibility(View.VISIBLE);
        fab2.setVisibility(View.VISIBLE);
        fab3.setVisibility(View.VISIBLE);
//        fab4.setVisibility(View.VISIBLE);
//        fab5.setVisibility(View.VISIBLE);
//        fab6.setVisibility(View.VISIBLE);
//        fab7.setVisibility(View.VISIBLE);

        fab1.animate().translationY(-activity.getResources().getDimension(R.dimen.fab_1));
        fab2.animate().translationY(-activity.getResources().getDimension(R.dimen.fab_2));
        fab3.animate().translationY(-activity.getResources().getDimension(R.dimen.fab_3));
//        fab4.animate().translationY(-activity.getResources().getDimension(R.dimen.fab_4));
//        fab5.animate().translationY(-activity.getResources().getDimension(R.dimen.fab_5));
//        fab6.animate().translationY(-activity.getResources().getDimension(R.dimen.fab_6));
//        fab7.animate().translationY(-activity.getResources().getDimension(R.dimen.fab_7));
    }
}
