package com.mynotes;

import android.view.Menu;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import android.view.View;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import com.mynotes.adapter.NoteAdapter;
import com.mynotes.constants.NoteState;
import com.mynotes.databinding.ActivityMainBinding;
import com.mynotes.datasets.Note;
import com.mynotes.datasets.NotesManager;
import com.mynotes.fragments.ArchievedNotesFragment;
import com.mynotes.fragments.MainNotesFragment;
import com.mynotes.fragments.TrashedNotesFragment;
import com.mynotes.services.ReminderService;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NotesManager notesManager;
    
    private ActionBar actionBar;
    private NoteAdapter adapter;
    
    private boolean selectionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        navigationView = binding.navigationView;
        setupNavigationDrawer();
        setupNotesManager();
        
        startApplicationServices();
    }
    
    public void setupNotesManager(){
        notesManager = NotesManager.getInstance();
        SharedPreferences pref = getSharedPreferences(NotesManager.SHARED_PREFERENCE_FOR_NOTES_KEY, Context.MODE_PRIVATE);
        notesManager.initializeNotesManager(pref);
    }
    
    
    public void setupNavigationDrawer(){
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        
        drawerLayout = binding.drawerLayout;
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if(selectionMode)
            getMenuInflater().inflate(R.menu.options_main_note_selected, menu);
        return true;
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)) return true;
        int id = item.getItemId();
        if(id == android.R.id.home){
            adapter.disSelectAll();
            leaveSelectionMode();
        }else if(id == R.id.trash){
            trashSelectedNotes();
        }else if(id == R.id.archieve){
            archieveSelectedNotes();
        }
        return super.onOptionsItemSelected(item);
    }
    
    
    private void archieveSelectedNotes(){
        for(Note note: adapter.getSelectedNotes()){
            note.throwInArchieve();
         }   
        adapter.disSelectAll();
        leaveSelectionMode();
        updateAdapterData();
    }
    
    
    private void trashSelectedNotes(){
        for(Note note: adapter.getSelectedNotes()){
            note.throwInTrash();
         }   
        adapter.disSelectAll();
        leaveSelectionMode();
        updateAdapterData();
    }
    
    private void updateAdapterData(){
        List<Note> restNotes = notesManager.getNotesOfState(NoteState.NORMAL);
        if (restNotes.isEmpty()){
            MainNotesFragment mainFrag = (MainNotesFragment) getSupportFragmentManager().findFragmentByTag("main");
            mainFrag.emptyIndicator.setVisibility(View.VISIBLE);
        }
        adapter.setAdapterNotes(restNotes);
        adapter.notifyDataSetChanged();
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        
        setupDrawerFragment(new MainNotesFragment(), "main");
        
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();  
                transaction.addToBackStack(null);
                          
                int id = item.getItemId();
                    
                if(id == R.id.notes){
                    transaction.replace(R.id.frameLayout, new MainNotesFragment(), "main" );
                }else if(id == R.id.archieved){
                    transaction.replace(R.id.frameLayout, new ArchievedNotesFragment() );
                }else if(id == R.id.trashed){
                    transaction.replace(R.id.frameLayout, new TrashedNotesFragment() );
                }else if(id == R.id.settings){
                    startActivity(new Intent(getApplicationContext(),SettingsActivity.class ));
                }else if(id == R.id.about ){
                    startActivity(new Intent(getApplicationContext(),AboutAuthorActivity.class ));
                }
                transaction.commit();  
                drawerLayout.close();
                return true;    
            }
        });
    }
    
    public void startApplicationServices(){
        startService(new Intent(this, ReminderService.class));
    }
    
    public void setupDrawerFragment(Fragment fragment, String tag) {
    	FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment, tag);
        transaction.commit();
    }
    
    public void enterSelectionMode(NoteAdapter adapter){
        this.adapter = adapter;
        
        actionBar.setTitle("item(s) Selected");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        
        selectionMode = true;
        invalidateOptionsMenu();
    }
    
    
    public void leaveSelectionMode(){
        actionBar.setTitle("My Notes");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        
        selectionMode = false;
        invalidateOptionsMenu();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.binding = null;
    }
    
    
}
