package com.mynotes;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.mynotes.constants.ConstValues;
import com.mynotes.databinding.ActivityViewNoteBinding;
import com.mynotes.datasets.Note;
import com.mynotes.datasets.NotesManager;

public class ViewNoteActivity extends AppCompatActivity {
    ActivityViewNoteBinding binding;
    
    TextView titleView;
    TextView contentView;
    TextView editedOn;
    TextView reminderTime;
    NotesManager noteManager;
    
    Note note;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Viewing note");
        
        titleView = binding.title;
        contentView = binding.content;
        editedOn = binding.editedOn;
        reminderTime = binding.reminderDate;
        
        noteManager = NotesManager.getInstance();
        
        String id = getIntent().getStringExtra(ConstValues.INTENT_NOTE_ID);
        note = noteManager.getNoteOfId(id);
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        setUpDataFromNote();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        
        if (item.getItemId() == R.id.edit){
            goToEditNoteActivity();
        }else if(item.getItemId() == android.R.id.home){
            goToMainActivity();
        }else if(item.getItemId() == R.id.trash) {
        	note.throwInTrash();
            noteManager.storeInPreference();
            goToMainActivity();
        }else if(item.getItemId() == R.id.archieve) {
        	note.throwInArchieve();
            noteManager.storeInPreference();
            goToMainActivity();
        }
        return true;
    }
    
    
    public void setUpDataFromNote(){
        titleView.setText(note.getTitle());
        contentView.setText(note.getContent());
        if(note.getNotify_on() != null ) reminderTime.setText("Reminder: "+ note.getNotify_on().toLocaleString());
        if(note.getUpdated_on() != null ) editedOn.setText( "Edited on: "+ note.getUpdated_on().toLocaleString());
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu arg0) {
        super.onCreateOptionsMenu(arg0);
        getMenuInflater().inflate(R.menu.options_viewnote_activity_menu, arg0);
        return true;
    }
    
    
    private void goToEditNoteActivity(){
        Intent intent = new Intent(this, EditNoteActivity.class);
        intent.putExtra(ConstValues.INTENT_NOTE_ID, note.getId());
        startActivity(intent);
        finish();
    }
    
    private void goToMainActivity(){
        finish();
    }
}
