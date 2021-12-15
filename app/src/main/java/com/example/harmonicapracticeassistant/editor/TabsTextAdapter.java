package com.example.harmonicapracticeassistant.editor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.harmonicapracticeassistant.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TabsTextAdapter extends RecyclerView.Adapter<TabsTextAdapter.ViewHolder>
{
    List<TabAndText> tabAndTexts;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tab_and_text_line, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
//        holder.tabs.setText(tabAndTexts.get(position).tabs);
//        holder.text.setText(tabAndTexts.get(position).text);
//        holder.tabs.setText("hello");
//        holder.text.setText("hello");
        holder.getTextView().setText("hello");

//        holder.tabs.setShowSoftInputOnFocus(false);
    }

    @Override
    public int getItemCount()
    {
        return tabAndTexts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        //        EditText tabs;
//        EditText text;
        TextView textView;

        public ViewHolder(View itemView)
        {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
//            this.tabs = itemView.findViewById(R.id.song_tabs);
//            this.text = itemView.findViewById(R.id.song_text);
        }

        public TextView getTextView()
        {
            return textView;
        }
    }

    public TabsTextAdapter(List<TabAndText> tabAndTexts)
    {
        this.tabAndTexts = tabAndTexts;
    }


}
