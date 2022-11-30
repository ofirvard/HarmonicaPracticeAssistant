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
import com.example.harmonicapracticeassistant.editor.EditorActivity;
import com.example.harmonicapracticeassistant.editor.Song;
import com.example.harmonicapracticeassistant.practice.PracticeActivity;
import com.example.harmonicapracticeassistant.settings.AppSettings;
import com.example.harmonicapracticeassistant.utils.ParcelIds;
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
    private List<SelectableSong> selectableSongs;

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

    public class SongListHolder extends RecyclerView.ViewHolder
//            implements View.OnCreateContextMenuListener
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
            activityResultLauncher.launch(getPracticeIntent(selectableSong));
        });

        holder.practiceButton.setOnClickListener(view -> {
            activityResultLauncher.launch(getPracticeIntent(selectableSong));
        });

        holder.editButton.setOnClickListener(view -> {
            Intent intent = new Intent(context, EditorActivity.class);
            intent.putExtra(ParcelIds.IS_NEW_SONG_PARCEL_ID, false);
            intent.putExtra(ParcelIds.SONG_PARCEL_ID, selectableSong.getSong());
            intent.putExtra(ParcelIds.SETTINGS_PARCEL_ID, settings);

            activityResultLauncher.launch(intent);
        });

        holder.nameButton.setOnLongClickListener(view -> {
            // TODO: 9/27/2022 long press opens menu (changes if select is true or false), short press opens editor (if select true then only selects)
            // TODO: 9/27/2022 open menu, and depending on if items are selected show a different menu (also if select is true and the item is not selected just select it)

            PopupMenu popup = new PopupMenu(context, holder.nameButton);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.song_list_selectable_song_menu, popup.getMenu());

            popup.getMenu().findItem(R.id.favorite_song).setVisible(!selectableSong.getSong().isFavourite());
            popup.getMenu().findItem(R.id.unfavorite_song).setVisible(selectableSong.getSong().isFavourite());
            // TODO: 01/12/2022 check if you need to hide
            popup.setOnMenuItemClickListener(menuItem -> {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.select)
                {
                    setSongSelect(selectableSong, true);

                    return true;
                }
                else if (itemId == R.id.unselect)
                {
                    setSongSelect(selectableSong, false);

                    return true;
                }
                else if (itemId == R.id.favorite_song)
                {
                    setSongFavourite(selectableSong, true);

                    return true;
                }
                else if (itemId == R.id.unfavorite_song)
                {
                    setSongFavourite(selectableSong, false);

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
        // TODO: 9/27/2022 update notify
        notifyItemRangeChanged(0, selectableSongs.size());
    }

    private Intent getPracticeIntent(SelectableSong selectableSong)
    {
        Intent intent = new Intent(context, PracticeActivity.class);
        intent.putExtra(ParcelIds.SONG_PARCEL_ID, selectableSong.getSong());
        intent.putExtra(ParcelIds.SETTINGS_PARCEL_ID, settings);

        return intent;
    }

    private void setSongSelect(SelectableSong selectableSong, boolean isSelect)
    {
        selectableSong.setSelected(isSelect);

        if (!isSelect)
            setSelect(true);
    }

    private void setSongFavourite(SelectableSong selectableSong, boolean isFavourite)
    {
        selectableSong.getSong().setFavourite(isFavourite);
        SaveUtils.saveSong(context, selectableSong.getSong());
        this.selectableSongs.sort(songComparator);

        notifyDataSetChanged();// TODO: 01/12/2022 see if you can do the sort better
    }
}
