package com.mynotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.MainThread;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.gson.internal.Streams;
import com.mynotes.constants.ConstValues;
import com.mynotes.databinding.ActivityEditNoteBinding;
import com.mynotes.datasets.Note;
import com.mynotes.datasets.NotesManager;

import com.mynotes.services.ReminderService;
import java.util.Date;

public class EditNoteActivity extends AppCompatActivity {
    ActivityEditNoteBinding binding;
    
    TextInputEditText titleInput;
    TextInputEditText contentInput;
    NotesManager noteManager;
    Note note;
    TextView reminderText;
    
    MaterialButton addReminderButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add note");
        
        titleInput = binding.title;
        contentInput = binding.content;
        addReminderButton = binding.addReminderButton;
        reminderText = binding.reminderDate;
        
        noteManager = NotesManager.getInstance();
        
        String id = getIntent().getStringExtra(ConstValues.INTENT_NOTE_ID);
        note = noteManager.getNoteOfId(id);
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        setUpDataFromNote();
        addReminderButton.setOnClickListener((v)->{ showDateTimePicker(); });
    }
    
    public void setUpDataFromNote(){
        titleInput.setText(note.getTitle());
        contentInput.setText(note.getContent());
        if(note.getNotify_on() != null ) reminderText.setText("Reminder: "+ note.getNotify_on().toLocaleString());
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu arg0) {
        super.onCreateOptionsMenu(arg0);
        getMenuInflater().inflate(R.menu.options_editnote_activity_menu, arg0);
        return true;
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.done){
            doneEditingNote();
            goToViewNoteActivity(note.getId());
        }else if(item.getItemId() == android.R.id.home){
            goToMainActivity();
        }
        return true;
    }
    
    
    @SuppressWarnings("unchecked")
    private void showDateTimePicker(){
        MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker()
            .build();
        datePicker.addOnPositiveButtonClickListener(
            new MaterialPickerOnPositiveButtonClickListener<Long>(){
                @Override
                public void onPositiveButtonClick(Long selection){
                    Date date = new Date(selection);
                    setReminderOnNote(date);
                    
                    getChoosenTime(date);
            }
        });
        datePicker.show(getSupportFragmentManager(), "Reminder Date");
    }
    
    private void getChoosenTime(Date date){
        MaterialTimePicker timePicker = new MaterialTimePicker.Builder().build();
        timePicker.show(getSupportFragmentManager(), "time");
        timePicker.addOnPositiveButtonClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                date.setHours(timePicker.getHour());
                date.setMinutes(timePicker.getMinute());  
                
                setReminderOnNote(date);
            }
            
        });
    }
    
    private void setReminderOnNote(Date date){
        note.setNotify_on(date);     
        String dateStr = date.toLocaleString();
        reminderText.setText("Reminder: " + dateStr);    
    }
    
    
    private void doneEditingNote(){
        String title = titleInput.getText().toString().strip();
        String content = contentInput.getText().toString().strip();
        if(!"".equals(title) || !"".equals(content)){
            note.setTitle("".equals(title)?"No title":title);
            note.setContent(content);
            noteManager.addNote(note);
            startService(new Intent(this, ReminderService.class));
        }
    }
    
    private void goToViewNoteActivity(String id){
        Intent intent = new Intent(this, ViewNoteActivity.class);
        intent.putExtra(ConstValues.INTENT_NOTE_ID, id);
        startActivity(intent);
        finish();
    }
    
    private void goToMainActivity(){
        if(!"".equals(note.getTitle()) || !"".equals(note.getContent())){
            confirmExitWithoutSaving();
        }else{
            finish();
        }
    }
    
    
    private void confirmExitWithoutSaving(){
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(this)
                    .setTitle("Save changes?")
                    .setMessage("Changes detected. You should save those.");
        dialog.setPositiveButton(R.string.dialog_positive_button, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                doneEditingNote();
                finish();   
            }
        });
        dialog.setNegativeButton(R.string.dialog_negative_button, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        });
        
        if( note.getTitle().equals(titleInput.getText().toString().strip())
            || note.getContent().equals(contentInput.toString().strip() ) ){
                finish();
        }else{
            dialog.show();
        }   
    }
    
    @Override
    @MainThread
    public void onBackPressed() {
        confirmExitWithoutSaving();
    }
    
}
