package com.example.harmonicapracticeassistant.songlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.harmonicapracticeassistant.R;
import com.example.harmonicapracticeassistant.editor.EditorActivity;
import com.example.harmonicapracticeassistant.editor.Song;
import com.example.harmonicapracticeassistant.utils.AppSettings;
import com.example.harmonicapracticeassistant.utils.ParcelIds;

import java.util.Comparator;
import java.util.List;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.MyViewHolder>
{
    private List<SelectableSong> selectableSongs;
    private Context context;
    private AppSettings settings;
    private boolean isSelect;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    public SongListAdapter(List<SelectableSong> songs, Context context, AppSettings settings)
    {
        this.selectableSongs = songs;
        this.context = context;
        this.settings = settings;
        this.isSelect = false;
        this.selectableSongs.sort(songComparator);

        activityResultLauncher = ((AppCompatActivity) context)
                .registerForActivityResult(new ActivityResultContracts
                        .StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK)
                    {
                        Song song = result.getData().getExtras().getParcelable(ParcelIds.SONG_PARCEL_ID);

                        for (int i = 0; i < selectableSongs.size(); i++)
                        {
                            if (selectableSongs.get(i).getSong().getId().equals(song.getId()))
                            {
                                selectableSongs.get(i).setSong(song);
                                notifyItemChanged(i);
                            }
                        }
                    }
                });
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        public Button button;
        public CheckBox checkBox;

        public MyViewHolder(View v)
        {
            super(v);
            button = v.findViewById(R.id.song_list_button);
            checkBox = v.findViewById(R.id.song_list_checkbox);
        }
    }

    @Override
    public SongListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.song_list_button,
                        parent,
                        false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position)
    {
        // TODO: 9/27/2022 long press opens menu (changes if select is true or false), short press opens editor (if select true then only selects)
        holder.button.setText(selectableSongs.get(position).getSong().getName());
        if (isSelect)
        {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(selectableSongs.get(position).isSelected());
        }
        holder.button.setOnClickListener(view -> {
            // TODO: 9/27/2022 move this to new launcher, make sure it gets it all
            Intent intent = new Intent(context, EditorActivity.class);
            intent.putExtra(ParcelIds.IS_NEW_SONG_PARCEL_ID, false);
            intent.putExtra(ParcelIds.SONG_PARCEL_ID, selectableSongs.get(position).getSong());
            intent.putExtra(ParcelIds.SETTINGS_PARCEL_ID, settings);

            activityResultLauncher.launch(intent);
        });

        holder.button.setOnLongClickListener(view -> {
            // TODO: 9/27/2022 open menu, and depending on if items are selected show a diffrent menu (also if select is true and the item is not selected just select it)

            return true;
        });
    }

    @Override
    public int getItemCount()
    {
        return selectableSongs.size();
    }

    public boolean isSelect()
    {
        return isSelect;
    }

    public void setSelect(Boolean select)
    {
        isSelect = select;
        // TODO: 9/27/2022 update notify
        notifyItemRangeChanged(0, selectableSongs.size());
    }

    private static final Comparator<SelectableSong> songComparator = Comparator.comparing(o -> o.getSong().getName());
}
