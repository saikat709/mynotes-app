package com.mynotes.viewholder;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import com.google.android.material.snackbar.Snackbar;
import com.mynotes.R;
import com.mynotes.adapter.NoteAdapter;
import com.mynotes.datasets.Note;
import com.mynotes.listeners.OnItemClickListener;

public class NoteViewHolder extends RecyclerView.ViewHolder{
    TextView titleView;
    TextView contentView;
    LinearLayout coordinatorLayout;
    OnItemClickListener listener;
    NoteAdapter adapter;
    Note note;
    
    private boolean isSelfSelected;
    private int position = 0;
    
    public NoteViewHolder(NoteAdapter adapter, View view){
        super(view);
        this.adapter = adapter;
        
        titleView = (TextView) view.findViewById(R.id.title);
        contentView = (TextView) view.findViewById(R.id.content);
        coordinatorLayout = (LinearLayout) view.findViewById(R.id.coordinator);
        
        coordinatorLayout.setOnClickListener(v -> {
            if( !adapter.selectedNotes.isEmpty() && adapter.getUseSelectionMode() ){
                toggleSelection();
            }else{
                if(listener != null) listener.onClick(note, position);
            }  
        });
        
        coordinatorLayout.setOnLongClickListener( new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
               if(adapter.selectedNotes.isEmpty() && adapter.getUseSelectionMode()){
                  adapter.enterSelectionMode();
                  selectViewHolder();  
                  return true;  
               }
               return false;        
            }
       });
    }
    
    
    private void toggleSelection(){
        if(isSelfSelected){
            disSelectViewHolder();    
        } else{
            selectViewHolder();
        }       
    }
    
    
    public void selectViewHolder(){
        isSelfSelected = true;    
        coordinatorLayout.setBackgroundColor(Color.MAGENTA);
        adapter.selectedNotes.add(note);
    }
    
    public void disSelectViewHolder(){
        isSelfSelected = false;
        coordinatorLayout.setBackgroundColor(Color.TRANSPARENT);
        adapter.selectedNotes.remove(note);
        if (adapter.selectedNotes.isEmpty()){
            adapter.leaveSelectionMode();
        }    
    }
    
    public void setNoteData(Note note){
        this.note = note;
        titleView.setText(note.getTitle());
        contentView.setText(note.getContent());
    }
    
    public void setViewHolderPosition(int position){
        this.position = position;
    }
    
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    
}
