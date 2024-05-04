package com.mynotes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.mynotes.datasets.Note;
import com.mynotes.listeners.OnItemClickListener;
import com.mynotes.listeners.OnSelectionStateListener;
import com.mynotes.viewholder.NoteViewHolder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.mynotes.R;

public class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder> {
  Context context;
  List<Note> notes;
  OnItemClickListener listener;
  OnSelectionStateListener selectionListener;

  private boolean useSelectionMode;
  public List<Note> selectedNotes;

  public NoteAdapter(Context context, List<Note> notes, boolean useSelectionMode) {
    this.context = context;
    this.notes = notes;
    this.selectedNotes = new ArrayList<>();
    this.useSelectionMode = useSelectionMode;
    Collections.reverse(notes);
  }
    
   public void setAdapterNotes(List<Note> notes){
       this.notes = notes;
        Collections.reverse(notes);
   }

  @Override
  public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(context);
    View view = inflater.inflate(R.layout.item_simple_note, parent, false);
    NoteViewHolder viewHolder = new NoteViewHolder(this, view);
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(final NoteViewHolder viewHolder, final int posotion) {
    Note currentNote = notes.get(posotion);
    viewHolder.setNoteData(currentNote);
    viewHolder.setOnItemClickListener(listener);
    viewHolder.setViewHolderPosition(posotion);
    if (selectedNotes.contains(currentNote)){
        viewHolder.selectViewHolder();
    }else{
        viewHolder.disSelectViewHolder();
    }        
  }

  @Override
  public int getItemCount() {
    return notes.size();
  }

  public void setOnItemClickListener(OnItemClickListener listener) {
    this.listener = listener;
  }

  public void setOnSelectionStateListener(OnSelectionStateListener selectionListener) {
    this.selectionListener = selectionListener;
  }

  public boolean getUseSelectionMode() {
    return this.useSelectionMode;
  }

  public void enterSelectionMode() {
    if(selectionListener != null) selectionListener.onSelectionState(true);
  }

  public void leaveSelectionMode() {
    if(selectionListener != null) selectionListener.onSelectionState(false);
  }

  public void selectAll() {
    selectedNotes = notes;
    notifyDataSetChanged();
  }

  public void disSelectAll() {
    selectedNotes.clear();
    notifyDataSetChanged();
  }

  public List<Note> getSelectedNotes() {
    return this.selectedNotes;
  }
    
}