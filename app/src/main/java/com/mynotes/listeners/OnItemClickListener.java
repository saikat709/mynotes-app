package com.mynotes.listeners;
import android.view.View;
import com.mynotes.datasets.Note;

public interface OnItemClickListener {
    public abstract void onClick(Note note, int position);
}
