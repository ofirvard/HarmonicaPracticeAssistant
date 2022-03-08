package com.example.harmonicapracticeassistant.pitchdetector2;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.harmonicapracticeassistant.R;
import com.example.harmonicapracticeassistant.harmonica.Hole;
import com.example.harmonicapracticeassistant.harmonica.Note;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

public class PitchDetectorAdapter extends RecyclerView.Adapter<PitchDetectorAdapter.NoteItemViewHolder>
{
    private final List<Pair<Note, List<Hole>>> notesWithHoles;
    private final PitchDetectorProcessor pitchDetectorProcessor;
    private final Context context;
    private int centerPos = 0;
    private final RecyclerView.OnScrollListener onScrollListener;

    public PitchDetectorAdapter(Context context, PitchDetectorProcessor pitchDetectorProcessor, NotePairListHandler notePairListHandler, SnapHelper snapHelper)
    {
        this.context = context;
        this.pitchDetectorProcessor = pitchDetectorProcessor;
        this.notesWithHoles = notePairListHandler.getNotePairList();
        this.onScrollListener = createOnScrollListener(snapHelper);
    }

    @NonNull
    @Override
    public NoteItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new NoteItemViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pitch_detector_note_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteItemViewHolder holder, int position)
    {
        holder.noteText.setText(
                NoteTranslator.holesToString(
                        pitchDetectorProcessor.getKey().isSharp(),
                        notesWithHoles.get(position)));
        setNormalDecor(holder.noteText, holder.coloredBar);
    }

    @Override
    public int getItemCount()
    {
        return notesWithHoles.size();
    }

    public void setCenterNote(int newSnapPosition)
    {
        centerPos = newSnapPosition;
    }

    public RecyclerView.OnScrollListener getOnScrollListener()
    {
        return onScrollListener;
    }

    private RecyclerView.OnScrollListener createOnScrollListener(SnapHelper snapHelper)
    {
        return new RecyclerView.OnScrollListener()
        {
            int snapPosition = RecyclerView.NO_POSITION;

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);

                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

                int newSnapPosition = recyclerView.getChildAdapterPosition(snapHelper.findSnapView(layoutManager));
                if (newSnapPosition != RecyclerView.NO_POSITION && newSnapPosition != snapPosition)
                {
                    setViewDecor(layoutManager, snapPosition, false);
                    setViewDecor(layoutManager, newSnapPosition, true);

                    snapPosition = newSnapPosition;
                    setCenterNote(newSnapPosition);
                }
            }
        };
    }

    private void setViewDecor(RecyclerView.LayoutManager layoutManager, int position, boolean isCenter)
    {
        View view = layoutManager.findViewByPosition(position);
        if (view != null)
            if (isCenter)
                setCenterDecor(view.findViewById(R.id.notes_list_pitch_detector_text), view.findViewById(R.id.colored_bar));
            else
                setNormalDecor(view.findViewById(R.id.notes_list_pitch_detector_text), view.findViewById(R.id.colored_bar));
    }

    private void setCenterDecor(TextView noteText, View coloredBar)
    {
        setDecor(noteText, coloredBar,
                context.getResources().getColor(R.color.colorAccent, null),
                context.getResources().getColor(R.color.colorAccentDark, null));
    }

    private void setNormalDecor(TextView noteText, View coloredBar)
    {
        setDecor(noteText, coloredBar,
                context.getResources().getColor(R.color.grey, null),
                context.getResources().getColor(R.color.colorPrimaryDark, null));
    }

    private void setDecor(TextView noteText, View coloredBar, int textColor, int barColor)
    {
        noteText.setTextColor(textColor);
        coloredBar.setBackgroundColor(barColor);
    }

    protected static class NoteItemViewHolder extends RecyclerView.ViewHolder
    {
        TextView noteText;
        View coloredBar;

        public NoteItemViewHolder(View view)
        {
            super(view);
            noteText = view.findViewById(R.id.notes_list_pitch_detector_text);
            coloredBar = view.findViewById(R.id.colored_bar);
        }
    }
}
