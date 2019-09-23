package com.example.schoolnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddNoteActivity extends AppCompatActivity {

    private EditText edtJudul, edtDeskripsi;
    private Button btnSimpan;

    DatabaseReference databaseNote;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        // Set Title
        getSupportActionBar().setTitle("Tambahkan Note");
        // Logika Insert Update
        bundle = getIntent().getExtras();
        final String action = bundle.getString("Action");


        databaseNote = FirebaseDatabase.getInstance().getReference("notes");

        edtJudul = findViewById(R.id.edt_judul);
        edtDeskripsi = findViewById(R.id.edt_deskripsi);
        btnSimpan = findViewById(R.id.btn_simpan);

        if(action.equals("Update")){
            edtJudul.setText(bundle.getString("Judul"));
            edtDeskripsi.setText(bundle.getString("Deskripsi"));
            btnSimpan.setText("Update");
            getSupportActionBar().setTitle("Update Note");
        }

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (action.equals("Insert")){
                    addNote();
                } else if(action.equals("Update")){
                    String noteId = bundle.getString("Id");
                    String judul = edtJudul.getText().toString().trim();
                    String deskripsi = edtDeskripsi.getText().toString().trim();

                    updateArtist(noteId, judul, deskripsi);
                }
            }
        });
    }

    private void addNote(){
        String judul = edtJudul.getText().toString().trim();
        String deskripsi = edtDeskripsi.getText().toString().trim();
        // Mengambil Waktu
        Date tanggal = Calendar.getInstance().getTime();
        System.out.println("Current time => " + tanggal);

        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        String timestamp = df.format(tanggal);

        if(!TextUtils.isEmpty(judul) && !TextUtils.isEmpty(deskripsi)){
            String id = databaseNote.push().getKey();

            Note note = new Note(id,judul,deskripsi,timestamp);

            databaseNote.child(id).setValue(note);

            Toast.makeText(this, "Note berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Masukan data dengan Baik!", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateArtist(String id, String judul, String deskripsi){
        // Mengambil Waktu
        Date tanggal = Calendar.getInstance().getTime();
        System.out.println("Current time => " + tanggal);

        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        String timestamp = df.format(tanggal);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("notes").child(id);

        Note note = new Note(id,judul,deskripsi,timestamp);

        databaseReference.setValue(note);
        Toast.makeText(this, "Note berhasil diupdate!", Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        bundle = getIntent().getExtras();
        String act = bundle.getString("Action");
        if(act.equals("Update")) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.main_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        bundle = getIntent().getExtras();
        switch (item.getItemId()){
            case R.id.action_delete:
                deleteNote(bundle.getString("Id"));
                break;
        }
        return true;
    }
    private void deleteNote(String id){
        DatabaseReference dbNote = FirebaseDatabase.getInstance().getReference("notes").child(id);

        dbNote.removeValue();
        Toast.makeText(this, "Note berhasil dihapus!", Toast.LENGTH_SHORT).show();
    }
}
