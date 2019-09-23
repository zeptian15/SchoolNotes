package com.example.schoolnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    DatabaseReference databaseNote;
    private ListView listViewNotes;
    private List<Note> noteList;
    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        databaseNote = FirebaseDatabase.getInstance().getReference("notes");

        fabAdd = findViewById(R.id.fab_add);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent add = new Intent(Home.this, AddNoteActivity.class);
                add.putExtra("Action", "Insert");
                startActivity(add);
            }
        });

        listViewNotes = findViewById(R.id.listViewNotes);
        noteList = new ArrayList<>();

        listViewNotes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Note note = noteList.get(i);

                showUpdateDialog(note.getNoteId(), note.getJudul(), note.getDeskripsi());

                return true;
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();

        databaseNote.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                noteList.clear();

                for(DataSnapshot noteSnapshot: dataSnapshot.getChildren()){
                    Note note = noteSnapshot.getValue(Note.class);

                    noteList.add(note);

                }

                NoteList adapter = new NoteList(Home.this, noteList);
                listViewNotes.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // Menampilkan Dialog
    private void showUpdateDialog(final String noteId, final String judul, final String deskripsi){
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.update_dialog,null);
        final Button btnUpdate = dialogView.findViewById(R.id.btn_update);
        final Button btnDelete = dialogView.findViewById(R.id.btn_delete);

        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle("Pilih Aksi :" + judul);

        final AlertDialog alertDialog = dialogBuilder.create();

        alertDialog.show();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent update = new Intent(Home.this, AddNoteActivity.class);
                update.putExtra("Action", "Update");
                update.putExtra("Id", noteId);
                update.putExtra("Judul", judul);
                update.putExtra("Deskripsi", deskripsi);

                startActivity(update);

                alertDialog.dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteNote(noteId);
            }
        });

    }
    private void deleteNote(String id){
        DatabaseReference dbNote = FirebaseDatabase.getInstance().getReference("notes").child(id);

        dbNote.removeValue();
        Toast.makeText(this, "Note berhasil dihapus!", Toast.LENGTH_SHORT).show();
    }
}
