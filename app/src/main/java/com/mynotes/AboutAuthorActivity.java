package com.mynotes;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.expandable.ExpandableWidget;
import com.mynotes.databinding.ActivityAboutAuthorBinding;

public class AboutAuthorActivity extends AppCompatActivity {
    ActivityAboutAuthorBinding binding;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutAuthorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        ActionBar actionBar =  getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("About author");
    }
    
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }
    
}
