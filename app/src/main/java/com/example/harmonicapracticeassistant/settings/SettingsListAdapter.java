package com.example.harmonicapracticeassistant.settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.harmonicapracticeassistant.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SettingsListAdapter extends RecyclerView.Adapter<SettingsListAdapter.SettingViewHolder>
{
    private final List<Integer> settings;
    private final Context context;

    public SettingsListAdapter(List<Integer> settings, Context context)
    {
        this.settings = settings;
        this.context = context;
    }

    @NonNull
    @Override
    public SettingsListAdapter.SettingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new SettingsListAdapter.SettingViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.emtpy_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SettingsListAdapter.SettingViewHolder holder, int position)
    {
        holder.settingView = View.inflate(context, settings.get(position), holder.parent);
    }

    @Override
    public int getItemCount()
    {
        return settings.size();
    }

    protected static class SettingViewHolder extends RecyclerView.ViewHolder
    {
        ViewGroup parent;
        View settingView;

        public SettingViewHolder(View view)
        {
            super(view);
            parent = (ViewGroup) view;
            settingView = view.findViewById(R.id.empty_view);
        }
    }
}
