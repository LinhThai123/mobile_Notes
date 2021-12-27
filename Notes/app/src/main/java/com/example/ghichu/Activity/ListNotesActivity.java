package com.example.ghichu.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ghichu.Adapter.NoteAdapter;
import com.example.ghichu.DAO.NotesDAO;
import com.example.ghichu.DTO.Notes;
import com.example.ghichu.Interface.OnNoteClickListener;
import com.example.ghichu.R;

import java.util.List;

public class ListNotesActivity extends AppCompatActivity implements View.OnClickListener, OnNoteClickListener {
    EditText edtSearch;
    RecyclerView rvSearchNote;
    ImageView imgCloseSearch;
    NoteAdapter noteAdapter;
    NotesDAO notesDAO;
    List<Notes> notesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_notes);
        notesDAO = new NotesDAO(this);
        mapping();
        showNotesTitle();

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                notesList = notesDAO.findNoteByTitle(editable.toString());
                if (edtSearch.length() != 0 ){
                    showNotesTitle();
                }
            }
        });
    }

    private void mapping() {
        edtSearch = findViewById(R.id.tvSearch);
        rvSearchNote = findViewById(R.id.rvSearchNote);
        imgCloseSearch = findViewById(R.id.imgCloseSearch);

        imgCloseSearch.setOnClickListener(this);
    }

    public void showNotesTitle() {
        noteAdapter = new NoteAdapter(this, R.layout.item_note, notesList , ListNotesActivity.this);
        rvSearchNote.setAdapter(noteAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvSearchNote.setLayoutManager(linearLayoutManager);

//        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
//        rvSearchNote.addItemDecoration(itemDecoration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgCloseSearch:
                finish();
                break;
        }
    }

    @Override
    public void onDeleteClick(Notes notes) {

    }

    @Override
    public void onShareNote(Notes notes) {

    }

    @Override
    public void goToActivityUpdate(Notes notes) {
        Intent intent = new Intent(ListNotesActivity.this , AddUpdateNoteActivity.class) ;
        intent.putExtra("notes" , notes ) ;
        startActivity(intent);
    }

    @Override
    public void onGetListByCategoryId(int id) {

    }
}