package com.mynotes.datasets;

import com.mynotes.constants.NoteState;

import java.util.Date;
import java.util.UUID;

public class Note {
  private String id;
  private NoteState state;
  private Date created_on;
  private Date updated_on;
  private Date trashed_on;
  private Date archieved_on;
  private Date notify_on;

  public String title;
  public String content;

  public Note(String title, String content) {
    this.title = title;
    this.content = content;

    this.state = NoteState.NORMAL;
    this.id = generateId();
    this.created_on = new Date();
  }

  public Note(String title) {
    this.title = title;

    this.content = "";
    this.state = NoteState.NORMAL;
    this.id = generateId();
    this.created_on = new Date();
  }
    
 public Note() {
    this.title = "";
    this.content = "";
    this.state = NoteState.NORMAL;
    this.id = generateId();
    this.created_on = new Date();
  }


  public void throwInTrash() {
    this.state = NoteState.TRASHED;
    this.trashed_on = new Date();
  }

  public void throwInArchieve() {
    this.state = NoteState.ARCHIEVED;
    this.archieved_on = new Date();
  }

  public void restore() {
    this.state = NoteState.NORMAL;
    this.trashed_on = null;
    this.archieved_on = null;
  }

  private String generateId() {
    return UUID.randomUUID().toString();
  }

  public String getId() {
    return this.id;
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return this.content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Date getCreated_on() {
    return this.created_on;
  }

  public NoteState getState() {
    return this.state;
  }

  public void setState(NoteState state) {
    this.state = state;
  }

  @Override
  public String toString() {
    return "Note[title="
        + title
        + ", content="
        + content
        + ", created_on="
        + created_on
        + ", state="
        + state
        + "]";
  }

  @Override
  public boolean equals(Object obj) {
    super.equals(obj);
    if (obj instanceof Note) {
      return this.id == ((Note) obj).getId();
    }
    return false;
  }

  public Date getUpdated_on() {
    return this.updated_on;
  }

  public void setUpdated_on(Date updated_on) {
    this.updated_on = updated_on;
  }

  public Date getTrashed_on() {
    return this.trashed_on;
  }

  public void setTrashed_on(Date trashed_on) {
    this.trashed_on = trashed_on;
  }

  public Date getArchieved_on() {
    return this.archieved_on;
  }

  public void setArchieved_on(Date archieved_on) {
    this.archieved_on = archieved_on;
  }

  public Date getNotify_on() {
    return this.notify_on;
  }

  public void setNotify_on(Date notify_on) {
    this.notify_on = notify_on;
  }
}
