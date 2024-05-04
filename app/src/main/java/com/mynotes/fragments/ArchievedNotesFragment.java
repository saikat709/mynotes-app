package com.mynotes.fragments;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.mynotes.MainActivity;
import com.mynotes.ViewNoteActivity;
import com.mynotes.adapter.NoteAdapter;
import com.mynotes.constants.ConstValues;
import com.mynotes.datasets.NotesManager;
import com.mynotes.listeners.OnItemClickListener;
import java.util.List;
import com.mynotes.datasets.Note;
import com.mynotes.constants.NoteState;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.mynotes.R;

public class ArchievedNotesFragment extends Fragment {
    RecyclerView recyclerView;
    NoteAdapter adapter;
    TextView emptyIndicator;
    NotesManager notesManager;
    
    public ArchievedNotesFragment(){}
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
        super.onCreateView(inflater, viewGroup, bundle);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Archieved notes");
        
        return inflater.inflate(R.layout.fragment_archieved_layout, viewGroup, false);
    }
    
    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        emptyIndicator = (TextView)  view.findViewById(R.id.emptyIndicator);
        notesManager = NotesManager.getInstance();
        
        setupRecyclerViewWithAdapter();
    }
    
    
    private void setupRecyclerViewWithAdapter(){
        
        List<Note> notes = notesManager.getNotesOfState(NoteState.ARCHIEVED);
        if(notes.isEmpty()){
            emptyIndicator.setVisibility(View.VISIBLE);
        }
        adapter = new NoteAdapter(getContext(), notes, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        
        adapter.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onClick(Note note, int position){
                MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(getContext())
                    .setMessage("This note was intentionally archieved. You can restore this or delete.")
                    .setTitle("Restore this note?")
                    .setPositiveButton("Restore", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                             note.restore();
                             notesManager.storeInPreference();
                             goToViewNoteActivity(note.getId());
                        }
                    })
                    .setNegativeButton("Delete permanently", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            deletePermanently(note, position);
                        }
                    });
                    
                alert.show();    
            }
        });
    }
    
    
    private void deletePermanently(Note note, int position){
        notesManager.deletePermanently(note);
        List<Note> restNotes = notesManager.getNotesOfState(NoteState.ARCHIEVED);
        if(restNotes.isEmpty()) emptyIndicator.setVisibility(View.VISIBLE);
        
        adapter.setAdapterNotes(restNotes);
        adapter.notifyItemRemoved(position);
    }
    
    
    private void goToViewNoteActivity(String id){
        Intent intent = new Intent(getContext(), ViewNoteActivity.class);
        intent.putExtra(ConstValues.INTENT_NOTE_ID, id);
        startActivity(intent);
    }
    
}
