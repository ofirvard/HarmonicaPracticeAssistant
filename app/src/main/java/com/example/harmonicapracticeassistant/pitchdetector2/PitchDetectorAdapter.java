package com.example.harmonicapracticeassistant.pitchdetector2;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.harmonicapracticeassistant.R;
import com.example.harmonicapracticeassistant.harmonica.Hole;
import com.example.harmonicapracticeassistant.harmonica.Note;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PitchDetectorAdapter extends RecyclerView.Adapter<PitchDetectorAdapter.NoteItemViewHolder>
{
    private final List<Pair<Note, List<Hole>>> notesWithHoles = new ArrayList<>();
    private final PitchDetectorProcessor pitchDetectorProcessor;

    public PitchDetectorAdapter(PitchDetectorProcessor pitchDetectorProcessor)
    {
        this.pitchDetectorProcessor = pitchDetectorProcessor;
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

        // TODO: 07/02/2022 add make color of bottom and size of middle change
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

    @Override
    public int getItemCount()
    {
        return notesWithHoles.size();
    }

    public void addNoteWithHoles(Pair<Note, List<Hole>> noteWithHoles)
    {
        notesWithHoles.add(noteWithHoles);
        notifyDataSetChanged();

        // TODO: 07/02/2022 push to last item 
    }

    public void clear()
    {
        notesWithHoles.clear();
        notifyDataSetChanged();
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
