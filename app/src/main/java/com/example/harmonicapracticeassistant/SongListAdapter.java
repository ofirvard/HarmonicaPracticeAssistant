package com.example.harmonicapracticeassistant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.MyViewHolder>
{
    private List<Song> songs;
    public OnBindCallback onBind;
    private Context context;

    public SongListAdapter(List<Song> songs, Context context)
    {
        this.songs = songs;
        this.context = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        public Button button;

        public MyViewHolder(View v)
        {
            super(v);
            button = v.findViewById(R.id.song_button);
        }
    }

    @Override
    public SongListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType)
    {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_list_button, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position)
    {
        if (onBind != null)
        {
            onBind.onViewBound(holder, position);
        }

        holder.button.setText(songs.get(position).getName());

        holder.button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
//                Context context = view.getContext();

                Intent intent = new Intent(context, SongActivity.class);
                intent.putExtra(Keys.IS_NEW_SONG, false);
                intent.putParcelableArrayListExtra(Keys.SONGS, (ArrayList<? extends Parcelable>) songs);
                intent.putExtra(Keys.SONG_POSITION, position);

//                context.startActivity(intent);
                ((Activity) context).startActivityForResult(intent, 1);

            }
        });
    }

    @Override
    public int getItemCount()
    {
        return songs.size();
    }

    public void setSongs(List<Song> songs)
    {
        this.songs = songs;
    }
}
