package com.mynotes.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.mynotes.EditNoteActivity;
import com.mynotes.MainActivity;
import com.mynotes.R;
import com.mynotes.ViewNoteActivity;
import com.mynotes.adapter.NoteAdapter;
import com.mynotes.constants.ConstValues;
import com.mynotes.constants.NoteState;
import com.mynotes.datasets.Note;
import com.mynotes.datasets.NotesManager;
import com.mynotes.listeners.OnItemClickListener;
import com.mynotes.listeners.OnSelectionStateListener;

import java.util.List;


public class MainNotesFragment extends Fragment {
    RecyclerView recyclerView;
    NoteAdapter adapter;
    FloatingActionButton floatingActionButon;
    ActionBar actionBar;
    public TextView emptyIndicator;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
        super.onCreateView(inflater, viewGroup, bundle);
        return inflater.inflate(R.layout.fragment_main_layout, viewGroup, false);
    }
    
    
    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        emptyIndicator = (TextView)  view.findViewById(R.id.emptyIndicator);
        setupRecyclerViewWithAdapter();
        
        floatingActionButon = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        floatingActionButon.setOnClickListener(v->{
            goToCreateNoteActivity();
        });
        
    }
    
    private void setupRecyclerViewWithAdapter(){
        NotesManager notesManager = NotesManager.getInstance();
        List<Note> mainNotes = notesManager.getNotesOfState(NoteState.NORMAL);
        if(mainNotes.isEmpty()){
            emptyIndicator.setVisibility(View.VISIBLE);
        }
        
        adapter = new NoteAdapter(getContext(), mainNotes, true);
        
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        
        adapter.setOnItemClickListener( new OnItemClickListener(){
            @Override
            public void onClick(Note note, int position) {
                goToViewNoteActivity(note.getId());
            }
        });
        
        adapter.setOnSelectionStateListener(new OnSelectionStateListener(){
            @Override
            public void onSelectionState(boolean gotSelection) {
               
                if(gotSelection){
                   ((MainActivity) getActivity()).enterSelectionMode(adapter);
                    floatingActionButon.setVisibility(View.GONE);
                }else{
                   ((MainActivity) getActivity()).leaveSelectionMode();
                   floatingActionButon.setVisibility(View.VISIBLE);  
                }
                
            };
        });
    }
    
    private void goToCreateNoteActivity(){
        Intent intent = new Intent(getContext(), EditNoteActivity.class);
        intent.putExtra(ConstValues.INTENT_NOTE_ID, ConstValues.NOTE_ID_NEW);
        startActivity(intent);
    }
    
    private void goToViewNoteActivity(String id){
        Intent intent = new Intent(getContext(), ViewNoteActivity.class);
        intent.putExtra(ConstValues.INTENT_NOTE_ID, id);
        startActivity(intent);
    }
}
