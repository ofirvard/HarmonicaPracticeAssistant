package com.example.harmonicapracticeassistant;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.harmonicapracticeassistant.activities.SongActivity;
import com.example.harmonicapracticeassistant.utils.AppSettings;
import com.example.harmonicapracticeassistant.utils.Constants;
import com.example.harmonicapracticeassistant.utils.SaveHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.MyViewHolder> {
    private List<Song> songs;
    private Context context;
    private AppSettings settings;

    public SongListAdapter(List<Song> songs, Context context, AppSettings settings) {
        this.songs = songs;
        this.context = context;
        this.settings = settings;
        Collections.sort(this.songs, songComparator);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public Button button;

        public MyViewHolder(View v) {
            super(v);
            button = v.findViewById(R.id.song_button);
        }
    }

    @Override
    public SongListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_list_button, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.button.setText(songs.get(position).getName());

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
//                Context context = view.getContext();

                Intent intent = new Intent(context, SongActivity.class);
                intent.putExtra(Constants.IS_NEW_SONG, false);
                intent.putParcelableArrayListExtra(Constants.SONGS, (ArrayList<? extends Parcelable>) songs);
                intent.putExtra(Constants.SONG_POSITION, position);
                intent.putExtra(Constants.SETTINGS, settings);

//                context.startActivity(intent);
                ((Activity) context).startActivityForResult(intent, 1);

            }
        });

        holder.button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.delete_message);
                builder.setMessage(R.string.delete_message_dialog);

                builder.setNegativeButton(R.string.no, null);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        songs.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, songs.size());
                        SaveHandler.saveSongs(context, songs);
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
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

    private static Comparator<Song> songComparator = new Comparator<Song>()
    {
        @Override
        public int compare(Song o1, Song o2)
        {
            return o1.getName().compareTo(o2.getName());
        }
    };
}
