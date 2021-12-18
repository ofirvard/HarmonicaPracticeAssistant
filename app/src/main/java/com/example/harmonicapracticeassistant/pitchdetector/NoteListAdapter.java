package com.example.harmonicapracticeassistant.pitchdetector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.harmonicapracticeassistant.R;
import com.example.harmonicapracticeassistant.harmonica.Note;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteItemViewHolder>
{
    private final int LARGE_TEXT_SIZE = 35;
    private final int NORMAL_TEXT_SIZE = 30;
    private final List<Note> notes;
    private final Context context;

    private int centerNote = -1;
    private boolean isVisualInNotes = true;

    public NoteListAdapter(List<Note> notes, Context context)
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

        Note note = notes.get(position);
//        holder.noteText.setText(note);

        if (isVisualInNotes)
            holder.noteText.setText(note.getNoteWithOctave());
        else
            holder.noteText.setText(String.format("%s", note.getFrequency()));


// TODO: 21/11/2021 play with colors and figure out why it causes problems back and forth
//        if (position == centerNote)
//        {
//            holder.noteText.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
//            holder.noteText.setTextSize(LARGE_TEXT_SIZE);
//            holder.coloredBar.setBackgroundResource(R.color.colorAccent);
//        }
//        else
//        {
        holder.noteText.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        holder.noteText.setTextSize(NORMAL_TEXT_SIZE);
        holder.coloredBar.setBackgroundResource(R.color.colorPrimaryDark);
//        }
    }

    // TODO: 12/16/2021 is this needed 
    public void setCenterNote(int centerNote)
    {
        this.centerNote = centerNote;
        notifyDataSetChanged();
    }

    public boolean switchVisual()
    {
        isVisualInNotes = !isVisualInNotes;

        return isVisualInNotes;
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
