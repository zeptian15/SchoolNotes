package com.example.schoolnotes;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.List;

public class NoteList extends ArrayAdapter<Note> {

        private Activity context;
        private List<Note> noteList;

        public NoteList(Activity context, List<Note> noteList){
            super(context, R.layout.layout_note_list,noteList);
            this.context = context;
            this.noteList = noteList;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();

            View listViewItem = inflater.inflate(R.layout.layout_note_list, null, true);

            TextView tvTimeStamp = listViewItem.findViewById(R.id.tv_timestamp);
            TextView tvJudul = listViewItem.findViewById(R.id.tv_judul);
            TextView tvDeskripsi = listViewItem.findViewById(R.id.tv_deskripsi);

            Note note = noteList.get(position);

            tvTimeStamp.setText(note.getTimestamp());
            tvJudul.setText(note.getJudul());
            tvDeskripsi.setText(note.getDeskripsi());

            return listViewItem;
        }
}
