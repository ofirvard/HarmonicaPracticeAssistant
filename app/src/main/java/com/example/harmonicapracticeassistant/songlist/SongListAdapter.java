package com.example.harmonicapracticeassistant.songlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.example.harmonicapracticeassistant.R;
import com.example.harmonicapracticeassistant.editor.Song;
import com.example.harmonicapracticeassistant.settings.AppSettings;
import com.example.harmonicapracticeassistant.utils.IntentBuilder;
import com.example.harmonicapracticeassistant.utils.SaveUtils;

import java.util.Comparator;
import java.util.List;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.SongListHolder>
{
    private static final Comparator<SelectableSong> songComparator = Comparator
            .comparing((SelectableSong song) -> !song.getSong().isFavourite())
            .thenComparing(song -> song.getSong().getName());
    private final Context context;
    private final AppSettings settings;
    private final ActivityResultLauncher<Intent> activityResultLauncher;
    private boolean isSelect;
    private final List<SelectableSong> selectableSongs;

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
                        Song song = result.getData().getExtras().getParcelable(IntentBuilder.SONG_PARCEL_ID);

                        for (int i = 0; i < selectableSongs.size(); i++)
                        {
                            if (selectableSongs.get(i).getSong().getId().equals(song.getId()))
                            {
                                selectableSongs.get(i).getSong().update(song);
                                notifyItemChanged(i);
                            }
                        }
                    }
                });
    }

    public void sort()
    {
        selectableSongs.sort(songComparator);
    }

    public static class SongListHolder extends RecyclerView.ViewHolder
    {
        public Button nameButton;
        public CheckBox checkBox;
        public ImageButton practiceButton;
        public ImageButton editButton;
        public ImageView favouriteHeart;

        public SongListHolder(View v)
        {
            super(v);
            nameButton = v.findViewById(R.id.song_list_name_button);
            checkBox = v.findViewById(R.id.song_list_checkbox);
            practiceButton = v.findViewById(R.id.song_list_practice_button);
            editButton = v.findViewById(R.id.song_list_edit_button);
            favouriteHeart = v.findViewById(R.id.song_list_favourite_heart);
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
        // TODO: 12/1/2022 move the diffrent clicks to somewhere else 
        SelectableSong selectableSong = selectableSongs.get(position);
        holder.nameButton.setText(selectableSongs.get(position).getSong().getName());

        if (selectableSong.getSong().isFavourite())
            holder.favouriteHeart.setVisibility(View.VISIBLE);
        else
            holder.favouriteHeart.setVisibility(View.GONE);

        if (isSelect)
        {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(selectableSong.isSelected());
        }

        holder.nameButton.setOnClickListener(view -> {
            activityResultLauncher.launch(IntentBuilder.buildPracticeIntent(context,
                    selectableSong.getSong(),
                    settings));
        });

        holder.practiceButton.setOnClickListener(view -> {
            activityResultLauncher.launch(IntentBuilder.buildPracticeIntent(context,
                    selectableSong.getSong(),
                    settings));
        });

        holder.editButton.setOnClickListener(view -> {
            activityResultLauncher.launch(IntentBuilder.buildExistingSongEditorIntent(context,
                    selectableSong.getSong(),
                    settings));
        });

        holder.nameButton.setOnLongClickListener(view -> {
            PopupMenu popup = new PopupMenu(context, holder.nameButton);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.song_list_selectable_song_menu, popup.getMenu());

            popup.getMenu().findItem(R.id.favorite_song).setVisible(!selectableSong.getSong().isFavourite());
            popup.getMenu().findItem(R.id.unfavorite_song).setVisible(selectableSong.getSong().isFavourite());
            popup.getMenu().findItem(R.id.select).setVisible(!selectableSong.isSelected());
            popup.getMenu().findItem(R.id.unselect).setVisible(selectableSong.isSelected());

            popup.setOnMenuItemClickListener(menuItem -> {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.select)
                {
                    setSongSelect(selectableSong, true, holder.checkBox);

                    return true;
                }
                else if (itemId == R.id.unselect)
                {
                    setSongSelect(selectableSong, false, holder.checkBox);

                    return true;
                }
                else if (itemId == R.id.favorite_song)
                {
                    setSongFavourite(selectableSong, true, position, holder.favouriteHeart);

                    return true;
                }
                else if (itemId == R.id.unfavorite_song)
                {
                    setSongFavourite(selectableSong, false, position, holder.favouriteHeart);

                    return true;
                }
                return false;
            });
            popup.show();


            return true;
        });

        holder.checkBox.setOnClickListener(view ->
        {
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
        notifyItemRangeChanged(0, selectableSongs.size());
    }

    private void setSongSelect(SelectableSong selectableSong, boolean setSelect, CheckBox checkBox)
    {
        selectableSong.setSelected(setSelect);
        checkBox.setChecked(setSelect);

        if (!isSelect)
            setSelect(true);
    }

    private void setSongFavourite(SelectableSong selectableSong, boolean isFavourite, int fromPosition, ImageView favouriteHeart)
    {
        selectableSong.getSong().setFavourite(isFavourite);
        SaveUtils.saveSong(context, selectableSong.getSong());
        this.selectableSongs.sort(songComparator);

        if (selectableSong.getSong().isFavourite())
            favouriteHeart.setVisibility(View.VISIBLE);
        else
            favouriteHeart.setVisibility(View.GONE);

//        notifyItemMoved(fromPosition, selectableSongs.indexOf(selectableSong));


        notifyDataSetChanged();// TODO: 01/12/2022 see if you can do this better
    }
}
