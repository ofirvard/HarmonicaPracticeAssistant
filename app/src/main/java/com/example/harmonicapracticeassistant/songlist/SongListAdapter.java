package com.example.harmonicapracticeassistant.songlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;

import com.example.harmonicapracticeassistant.R;
import com.example.harmonicapracticeassistant.editor.EditorActivity;
import com.example.harmonicapracticeassistant.editor.Song;
import com.example.harmonicapracticeassistant.practice.PracticeActivity;
import com.example.harmonicapracticeassistant.settings.AppSettings;
import com.example.harmonicapracticeassistant.utils.ParcelIds;

import java.util.Comparator;
import java.util.List;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.SongListHolder>
{
    private List<SelectableSong> selectableSongs;
    private final Context context;
    private final AppSettings settings;
    private boolean isSelect;
    private final ActivityResultLauncher<Intent> activityResultLauncher;

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

    public static class SongListHolder extends RecyclerView.ViewHolder
    {
        public Button nameButton;
        public CheckBox checkBox;
        public ImageButton practiceButton;
        public ImageButton editButton;

        public SongListHolder(View v)
        {
            super(v);
            nameButton = v.findViewById(R.id.song_list_name_button);
            checkBox = v.findViewById(R.id.song_list_checkbox);
            practiceButton = v.findViewById(R.id.song_list_practice_button);
            editButton = v.findViewById(R.id.song_list_edit_button);
        }
    }

    @Override
    public SongListHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.song_list_button,
                        parent,
                        false);

        return new SongListHolder(v);
    }

    @Override
    public void onBindViewHolder(SongListHolder holder, int position)
    {
        holder.nameButton.setText(selectableSongs.get(position).getSong().getName());

        if (isSelect)
        {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(selectableSongs.get(position).isSelected());
        }

        holder.nameButton.setOnClickListener(view -> {
            activityResultLauncher.launch(getPracticeIntent(position));
        });

        holder.practiceButton.setOnClickListener(view -> {
            activityResultLauncher.launch(getPracticeIntent(position));
        });

        holder.editButton.setOnClickListener(view -> {
            Intent intent = new Intent(context, EditorActivity.class);
            intent.putExtra(ParcelIds.IS_NEW_SONG_PARCEL_ID, false);
            intent.putExtra(ParcelIds.SONG_PARCEL_ID, selectableSongs.get(position).getSong());
            intent.putExtra(ParcelIds.SETTINGS_PARCEL_ID, settings);

            activityResultLauncher.launch(intent);
        });

        holder.nameButton.setOnLongClickListener(view -> {
            // TODO: 23/11/2022 long press only starts select
            // TODO: 9/27/2022 long press opens menu (changes if select is true or false), short press opens editor (if select true then only selects)
            // TODO: 9/27/2022 open menu, and depending on if items are selected show a diffrent menu (also if select is true and the item is not selected just select it)

            return true;
        });

        holder.checkBox.setOnClickListener(view -> {
            selectableSongs.get(position).setSelected(!selectableSongs.get(position).isSelected());
            ((CheckBox) view).setChecked(selectableSongs.get(position).isSelected());
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

    private Intent getPracticeIntent(int position)
    {
        Intent intent = new Intent(context, PracticeActivity.class);
        intent.putExtra(ParcelIds.SONG_PARCEL_ID, selectableSongs.get(position).getSong());
        intent.putExtra(ParcelIds.SETTINGS_PARCEL_ID, settings);

        return intent;
    }
}
