package com.example.ghichu.Fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.ghichu.Activity.AddUpdateNoteActivity;
import com.example.ghichu.Activity.ListCategoryActivity;
import com.example.ghichu.Activity.MainActivity;
import com.example.ghichu.Adapter.CategoryAdapter;
import com.example.ghichu.Adapter.NoteAdapter;
import com.example.ghichu.DAO.CategoryDAO;
import com.example.ghichu.DAO.NotesDAO;
import com.example.ghichu.DTO.Category;
import com.example.ghichu.DTO.Notes;
import com.example.ghichu.Interface.OnNoteClickListener;
import com.example.ghichu.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.hmsscankit.WriterException;
import com.huawei.hms.ml.scan.HmsBuildBitmapOption;
import com.huawei.hms.ml.scan.HmsScan;

import java.util.List;

public class NoteFragment extends Fragment implements View.OnClickListener , OnNoteClickListener {
    RecyclerView rvCategory, rvNote;
    ImageView imgAddCategory;
    FloatingActionButton fabAddNote , fabMucsic;
    List<Category> categoryList;
    List<Notes> notesList ;
    CategoryAdapter categoryAdapter;
    NoteAdapter noteAdapter ;
    CategoryDAO categoryDAO ;
    NotesDAO notesDAO ;
    MediaPlayer mediaPlayer ;

    SharedPreferences sharedPreferences ;
    int ROW = 1 ;
    int SORT = 1 ;
    private static final String TYPE_SORT_DESC = "DESC";
    private static final String TYPE_SORT_ASC = "ASC";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        mapping(view);
        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.bai1);
        mediaPlayer.start();

        imgAddCategory.setOnClickListener(this);
        fabAddNote.setOnClickListener(this);
        fabMucsic.setOnClickListener(this);

        sharedPreferences = getActivity().getSharedPreferences("option",getActivity().MODE_PRIVATE) ;
        ROW = sharedPreferences.getInt("ROW" ,1 ) ;
        SORT = sharedPreferences.getInt("SORT", 1);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        showDataCategory();
        showDataNote();
    }

    public void mapping(View view) {
        rvCategory = view.findViewById(R.id.lvCategory);
        rvNote = view.findViewById(R.id.rvNote);
        imgAddCategory = view.findViewById(R.id.imgAddCategory);
        fabAddNote = view.findViewById(R.id.fabAddNote);
        fabMucsic = view.findViewById(R.id.fabMusic);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgAddCategory:
                startActivity(new Intent(getActivity(), ListCategoryActivity.class));
                break;
            case R.id.fabAddNote:
                startActivity(new Intent(getActivity(), AddUpdateNoteActivity.class));
                break;
            case R.id.fabMusic:
                 if(mediaPlayer.isPlaying()){
                     mediaPlayer.pause();
                     fabMucsic.setImageResource(R.drawable.ic_music_off);
                 }
                 else{
                     mediaPlayer.start();
                     fabMucsic.setImageResource(R.drawable.ic_music);

                 }
                 break;
            default:
                break;
        }
    }
    public void showDataCategory(){
        categoryDAO = new CategoryDAO(getActivity());
        categoryList = categoryDAO.getAllCategory();
        categoryAdapter = new CategoryAdapter(getActivity(),R.layout.item_category , categoryList , this);
        rvCategory.setAdapter(categoryAdapter);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL);
        rvCategory.setLayoutManager(staggeredGridLayoutManager);
    }
    public void showDataNote () {
        notesDAO = new NotesDAO(getActivity()) ;
        if(MainActivity.SORT == 1){
            notesList = notesDAO.sortDate(TYPE_SORT_DESC);
        }
        else if( MainActivity.SORT == 2){
            notesList = notesDAO.sortDate(TYPE_SORT_ASC);
        }
        else if (MainActivity.SORT == 3){
            notesList = notesDAO.sortTitle(TYPE_SORT_ASC);
        }
        else if (MainActivity.SORT == 4){
            notesList = notesDAO.sortTitle(TYPE_SORT_DESC);
        }
        else {
            notesList = notesDAO.getAllNote() ;
        }
        setDataNotetoView();
    }
    public void setDataNotetoView () {
        StaggeredGridLayoutManager  staggeredGridLayoutManager ;
        noteAdapter = new NoteAdapter(getActivity() , R.layout.item_note , notesList , this) ;
        rvNote.setAdapter(noteAdapter);
        if(sharedPreferences != null ){
            staggeredGridLayoutManager = new StaggeredGridLayoutManager(ROW,StaggeredGridLayoutManager.VERTICAL) ;

        }
        else {
            staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        }
        rvNote.setLayoutManager(staggeredGridLayoutManager);
    }

    @Override
    public void onDeleteClick(Notes notes) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()) ;
        builder.setTitle("Warning") ;
        builder.setIcon(android.R.drawable.ic_delete) ;
        builder.setMessage(" Delete Note" ) ;
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                notesDAO = new NotesDAO(getActivity()) ;
                if(notesDAO.deleteNote(notes)){
                    Toast.makeText(getActivity(), getResources().getString(R.string.deleteNote), Toast.LENGTH_SHORT).show();
                    notesList.remove(notes) ;
                }
                else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.deleteNote2), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onShareNote(Notes notes) {
        int type = HmsScan.QRCODE_SCAN_TYPE ;
        int width = 400 ;
        int height = 400 ;

        HmsBuildBitmapOption option = new HmsBuildBitmapOption.Creator().setBitmapBackgroundColor(Color.BLACK)
                .setBitmapColor(R.color.white).setBitmapMargin(2).create() ;

        Bitmap qrBitmap = null ;
        try {
            qrBitmap = ScanUtil.buildBitmap(notes.noteMapper() , type , width , height , option) ;
        }
        catch (WriterException e) {
            Log.w("buildBitmap" , e) ;
        }
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.activity_qr_code);

        ImageView imgQECode = dialog.findViewById(R.id.imgQRCode) ;

        imgQECode.setImageBitmap(qrBitmap);

        dialog.show();
    }

    @Override
    public void goToActivityUpdate(Notes notes) {
        Intent intent = new Intent(getActivity() , AddUpdateNoteActivity.class) ;
        intent.putExtra("notes" , notes ) ;
        startActivity(intent);
    }

    @Override
    public void onGetListByCategoryId(int id) {
       if(id == 1) {
           notesList = notesDAO.getAllNote() ;
       }
       else {
           notesList = notesDAO.getNoteByCategoryId(id);
       }
       setDataNotetoView();
    }
}
