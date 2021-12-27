package com.example.ghichu.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.ghichu.Adapter.NoteAdapter;
import com.example.ghichu.DAO.NotesDAO;
import com.example.ghichu.DTO.Notes;
import com.example.ghichu.Interface.OnNoteClickListener;
import com.example.ghichu.R;
import com.huawei.hms.ads.fi;

import java.util.ArrayList;
import java.util.List;

public class FindListDateActivity extends AppCompatActivity  implements View.OnClickListener, OnNoteClickListener {
    EditText edtStartDate , edtEndDate ;
    ImageView imgCloseDate ;
    RecyclerView rvfindDate ;
    Button btnfindDate ;
    NoteAdapter noteAdapter ;
    List<Notes> notesList ;
    NotesDAO notesDAO ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_list_date);
        notesDAO = new NotesDAO(FindListDateActivity.this) ;
        mapping();
        showfindDate();
    }
    public void mapping (){
        edtStartDate = findViewById(R.id.edtStartDate);
        edtEndDate   = findViewById(R.id.edtEndDate);
        imgCloseDate = findViewById(R.id.imgClosefindDate) ;
        rvfindDate   = findViewById(R.id.rvfindDate) ;
        btnfindDate  = findViewById(R.id.btnfindDate);

        imgCloseDate.setOnClickListener(this);
        btnfindDate.setOnClickListener(this);
    }

    public void showfindDate () {
        noteAdapter = new NoteAdapter(FindListDateActivity.this , R.layout.item_note , notesList , FindListDateActivity.this);
        rvfindDate.setAdapter(noteAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvfindDate.setLayoutManager(linearLayoutManager);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgClosefindDate:
                finish();
                break;
            case R.id.btnfindDate:
                String startDate = edtStartDate.getText().toString();
                String endDate = edtEndDate.getText().toString() ;
                notesList = notesDAO.findNoteByData(startDate,endDate);
                showfindDate();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu , menu);
        return true;
    }

    @Override
    public void onDeleteClick(Notes notes) {

    }

    @Override
    public void onShareNote(Notes notes) {

    }

    @Override
    public void goToActivityUpdate(Notes notes) {
        Intent intent = new Intent(FindListDateActivity.this , AddUpdateNoteActivity.class) ;
        intent.putExtra("notes" , notes ) ;
        startActivity(intent);
    }

    @Override
    public void onGetListByCategoryId(int id) {

    }
}