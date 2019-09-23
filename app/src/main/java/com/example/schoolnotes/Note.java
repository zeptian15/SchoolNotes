package com.example.schoolnotes;

public class Note {
    String noteId;
    String judul;
    String deskripsi;
    String timestamp;

    public Note() {
    }

    public Note(String id,String judul, String deskripsi, String timestamp) {
        this.noteId = id;
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.timestamp = timestamp;
    }

    public String getNoteId() {
        return noteId;
    }

    public String getJudul() {
        return judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
