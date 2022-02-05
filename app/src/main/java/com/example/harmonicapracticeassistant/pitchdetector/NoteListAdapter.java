package com.example.harmonicapracticeassistant.pitchdetector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.harmonicapracticeassistant.R;
import com.example.harmonicapracticeassistant.enums.NoteVisual;
import com.example.harmonicapracticeassistant.harmonica.Hole;
import com.example.harmonicapracticeassistant.harmonica.Key;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.harmonicapracticeassistant.utils.Constants.NO_KEY;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteItemViewHolder>
{
    private final int LARGE_TEXT_SIZE = 35;
    private final int NORMAL_TEXT_SIZE = 30;
    private final List<Hole> notes;
    private final Context context;

    private NoteVisual noteVisual = NoteVisual.NOTE_WITH_OCTAVE;
    private int centerNote = -1;
    private boolean isVisualInNotes = true;
    private Key currentKey;

    public NoteListAdapter(List<Hole> notes, Context context)
    {
        this.notes = notes;
        this.context = context;
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

        // TODO: 12/23/2021 depending on visual chooses what to show
        Hole note = notes.get(position);

        switch (noteVisual)
        {
            case NOTE_WITH_OCTAVE:
                holder.noteText.setText(note.getNoteWithOctave());
                break;

            case FREQUENCY:
                holder.noteText.setText(String.format("%s", note.getFrequency()));
                break;

            case HOLES:
                holder.noteText.setText(String.format("%s", note.getHoleWithBend(context)));
                break;
        }
// TODO: 21/11/2021 play with colors and figure out why it causes problems back and forth
//        if (position == centerNote)
//        {
//            holder.noteText.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
//            holder.noteText.setTextSize(LARGE_TEXT_SIZE);
//            holder.coloredBar.setBackgroundResource(R.color.colorAccent);
//        }
//        else
//        {
//        holder.noteText.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
//        holder.noteText.setTextSize(NORMAL_TEXT_SIZE);
//        holder.coloredBar.setBackgroundResource(R.color.colorPrimaryDark);
//        }
    }

    // TODO: 12/16/2021 is this needed 
    public void setCenterNote(int centerNote)
    {
        this.centerNote = centerNote;
        notifyDataSetChanged();
    }

    public boolean isCurrentKeyNone()
    {
        return currentKey.getKeyName().equals(NO_KEY);
    }

    public Key getCurrentKey()
    {
        return currentKey;
    }

    public void setCurrentKey(Key currentKey)
    {
        this.currentKey = currentKey;
        notes.clear();
        notifyDataSetChanged();
    }

    public int switchVisual()
    {
        switch (noteVisual)
        {
            case NOTE_WITH_OCTAVE:
                noteVisual = NoteVisual.FREQUENCY;
                return R.string.frequency;

            case FREQUENCY:
                if (isCurrentKeyNone())
                {
                    noteVisual = NoteVisual.HOLES;
                    return R.string.holes;
                }
                else
                {
                    noteVisual = NoteVisual.NOTE_WITH_OCTAVE;
                    return R.string.notes;
                }

            default:
                noteVisual = NoteVisual.NOTE_WITH_OCTAVE;
                return R.string.notes;
        }
    }

    @Override
    public int getItemCount()
    {
        return notes.size();
    }

    public static class NoteItemViewHolder extends RecyclerView.ViewHolder
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
