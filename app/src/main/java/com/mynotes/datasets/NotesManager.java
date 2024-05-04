package com.mynotes.datasets;

import android.content.Context;
import android.content.SharedPreferences;

import android.util.Log;
import com.google.gson.Gson;
import com.mynotes.constants.NoteState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class NotesManager {
    private List<Note> notes;
    private SharedPreferences sharedPreference;
    private SharedPreferences.Editor preferenceEditor;
    private Gson gson;

    private static NotesManager notesManagerInstance;

    public static final String SHARED_PREFERENCE_FOR_NOTES_KEY = "SHARED PREFERENCE FOR NOTES";
    public static final String NOTES_PREFERENCE_KEY = "NOTES";

    public static NotesManager getInstance() {
        if (notesManagerInstance == null) {
            notesManagerInstance = new NotesManager();
        }
        return notesManagerInstance;
    }

    public void initializeNotesManager(SharedPreferences sharedPreference) {
        this.sharedPreference = sharedPreference;
        this.preferenceEditor = sharedPreference.edit();
        this.gson = new Gson();
        this.notes = new ArrayList<>();
        getAndSetNotesFromPreference();
    }

    private void getAndSetNotesFromPreference() {
        String jsonString = this.sharedPreference.getString(NOTES_PREFERENCE_KEY, "");
        AllNotes allNotes = this.gson.fromJson(jsonString, AllNotes.class);
        if (allNotes != null) {
            this.notes = allNotes.notes;
        } else {
            this.notes = new ArrayList<>();
        }
    }

    public void storeInPreference() {
        String jsonString = getJsonStringFromNotes();
        preferenceEditor.putString(NOTES_PREFERENCE_KEY, jsonString);
        preferenceEditor.apply();
    }

    public String getJsonStringFromNotes() {
        AllNotes allNotes = new AllNotes(notes);
        return this.gson.toJson(allNotes);
    }

    public AllNotes getAllNotesClassFromString() {
        String jsonString = getJsonStringFromNotes();
        return this.gson.fromJson(jsonString, AllNotes.class);
    }

    public void addNote(Note note) {
        if (notes.contains(note)){
            notes.remove(note);
            note.setUpdated_on(new Date());
         }   
        notes.add(note);
        storeInPreference();
        notifyDataChanged();
    }
    

    private void notifyDataChanged() {
        getAndSetNotesFromPreference();
    }
    
    
    public void deletePermanently(Note note) {
    	notes.remove(note);
        storeInPreference();
        notifyDataChanged();
    }

    public Note getNoteOfId(String id) {
        for (Note note : notes) {
            if (id.equals(note.getId())) {
                return note;
            }
        }
        return new Note("");
    }
    

    public List<Note> getNotesOfState(NoteState desiredState) {
        List<Note> notesAsExpected = new ArrayList<>();
            for(Note note: notes){
                if(note.getState() == desiredState){
                   notesAsExpected.add(note);
                }
            }
        return notesAsExpected;
    }

    @Override
    public String toString() {
        return "NotesManager[notes=" + notes + "]";
    }

    private class AllNotes {
        public List<Note> notes = Collections.emptyList();

        public AllNotes(List<Note> notes) {
            this.notes = notes;
        }
    }

    public List<Note> getNotes() {
        return this.notes;
    }

}
