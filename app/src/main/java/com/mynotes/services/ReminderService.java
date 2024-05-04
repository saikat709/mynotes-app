package com.mynotes.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import com.mynotes.ViewNoteActivity;
import com.mynotes.R;
import android.app.PendingIntent;
import com.mynotes.constants.NoteState;
import com.mynotes.datasets.Note;
import com.mynotes.datasets.NotesManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ReminderService extends Service{
    NotesManager manager;
    
    @Override
    public int onStartCommand(Intent intend, int arg1, int arg2) {
        super.onStartCommand(intend, arg1, arg2);
        
        cleanTrashedNotes();
        
        manager = NotesManager.getInstance();
        List<Note> notes = manager.getNotesOfState(NoteState.NORMAL);
        
        for(Note note: notes){
            if(note.getNotify_on() != null ){
                sheduleANotification(note);
            }
        }
        
        return START_STICKY;
    }
    
    
    public void sheduleANotification(Note note){
        long currentTime = new Date().getTime();
        long targetTime = note.getNotify_on().getTime();
        long timeToDelay = targetTime - currentTime;
        
        
        Handler handler = new Handler(getMainLooper());
        Runnable task = ()->{
            sendReminderNotification(note);
        };
        if(timeToDelay > 0L ) handler.postDelayed(task, timeToDelay);
    }
    
    
    
    public void sendReminderNotification(Note note){
        int notificationId = new Random().nextInt();
        String title = "".equals(note.getTitle()) ? "No title" : note.getTitle();
        String content = "".equals(note.getContent()) ? "No text added" : note.getContent();
        
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel channel = manager.getNotificationChannel("Channel");
        if(channel == null){
            channel = new NotificationChannel("Channel", "reminder", NotificationManager.IMPORTANCE_HIGH);
        }
        manager.createNotificationChannel(channel);
        
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Channel");  // Constants.NOTIFICATION_CHANNEL_MUSIC_PLAYING);
        
        builder.setSmallIcon(R.drawable.add)
                .setContentTitle(title)
                .setContentText(content)
                .setSilent(false)
                .setAutoCancel(false)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        
        Intent intent = new Intent(this, ViewNoteActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        
        builder.setContentIntent(pendingIntent);
        manager.notify(notificationId, builder.build());
        
    }
    
    
    public void cleanTrashedNotes(){
        manager = NotesManager.getInstance();
        List<Note> notes = manager.getNotesOfState(NoteState.TRASHED);
        
        for(Note note: notes){
            if(note.getTrashed_on() != null ){
                long currentTime = new Date().getTime();
                long trashingTime = note.getTrashed_on().getTime();
                long timeToDelete = trashingTime + 7*24*3600*1000L;
                if(currentTime>timeToDelete){
                    manager.deletePermanently(note);
                }
            }
        }
    }
    
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
}
