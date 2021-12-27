package com.example.ghichu.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ghichu.Adapter.NoteAdapter;
import com.example.ghichu.DAO.CategoryDAO;
import com.example.ghichu.DAO.NotesDAO;
import com.example.ghichu.DTO.Category;
import com.example.ghichu.DTO.Notes;
import com.example.ghichu.Orthers.InvertingColor;
import com.example.ghichu.R;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.hmsscankit.WriterException;
import com.huawei.hms.ml.scan.HmsBuildBitmapOption;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import yuku.ambilwarna.AmbilWarnaDialog;

public class AddUpdateNoteActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_SCAN = 0x01 ;
    EditText edtTitleNote , edtContentNote ;
    TextView tvDateUpDateNote ;
    Spinner spnCategory ;
    ImageView imgCloseAddNote , imgBackgroundColorNote, imgSanQRCode , imgAddNote , imgColorText ;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm") ;
    Calendar calendar ;
    NotesDAO notesDAO ;
    CategoryDAO categoryDAO ;
    Notes notes ;
    Category category ;

    List<Category> categoryList ;
    ArrayAdapter categoryAdapter ;
    int defaultColor ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_note);
        categoryDAO = new CategoryDAO(AddUpdateNoteActivity.this) ;
        mapping() ;
        getCategory();
        calendar = Calendar.getInstance();
        setDateTimeUpadateNote();
        checkUpdate();
    }
    public void mapping() {
        edtTitleNote = findViewById(R.id.edtTitleNote) ;
        edtContentNote = findViewById(R.id.edtContentNote) ;
        tvDateUpDateNote = findViewById(R.id.tvDateUpdateNote) ;
        imgCloseAddNote = findViewById(R.id.imgCloseAddNote) ;
        imgBackgroundColorNote = findViewById(R.id.imgBackgroundColorNote) ;
        imgSanQRCode = findViewById(R.id.imgSanQRCode) ;
        imgAddNote = findViewById(R.id.imgAddNote) ;
        spnCategory = findViewById(R.id.spnCategory) ;
        imgColorText = findViewById(R.id.imgColorText);

        imgCloseAddNote.setOnClickListener(this);
        imgAddNote.setOnClickListener(this);
        imgSanQRCode.setOnClickListener(this);
        imgBackgroundColorNote.setOnClickListener(this);
        tvDateUpDateNote.setOnClickListener(this);
        imgColorText.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgCloseAddNote:
                finish();
                break;
            case R.id.imgAddNote:
                addNote();
                break;
            case R.id.imgBackgroundColorNote:
                openColorPicker();
                break;
            case R.id.imgSanQRCode:
                HmsScanAnalyzerOptions options = new HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.QRCODE_SCAN_TYPE, HmsScan.CODE128_SCAN_TYPE).create();
                ScanUtil.startScan(this, REQUEST_CODE_SCAN, options);
                break;
            case R.id.tvDateUpdateNote:
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Calendar chooseDate = Calendar.getInstance();
                        chooseDate.set(i,i1,i2);
                        String strDate = simpleDateFormat.format(chooseDate.getTime());
                        tvDateUpDateNote.setText(strDate);
                    }
                }, year,month,day);
                datePickerDialog.show();
                break ;
            case R.id.imgColorText:
                defaultColor = edtContentNote.getTextColors().getDefaultColor();
                AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {

                    }

                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        defaultColor = color ;
                        edtContentNote.setTextColor(defaultColor);
                    }
                });
                colorPicker.show();
                break;
        }
    }

    public void addNote(){
        notesDAO = new NotesDAO(AddUpdateNoteActivity.this) ;
        category = (Category) spnCategory.getSelectedItem();
        int category_id = category.getId();
        String title = edtTitleNote.getText().toString();
        String content = edtContentNote.getText().toString();
        String createAt = tvDateUpDateNote.getText().toString() ;
        String color = String.valueOf(((ColorDrawable)edtContentNote.getBackground()).getColor()) ;
//         String color = String.valueOf(edtContentNote.getTextColors().getDefaultColor());

        if (notes != null) {
            notes = new Notes(notes.getId() , category_id , title , content , color , notes.getCreateAt() , dateFormat.format(new Date()));
            if(notesDAO.updateNote(notes)){
                Toast.makeText(AddUpdateNoteActivity.this, getResources().getString(R.string.successfull), Toast.LENGTH_SHORT).show();
                finish();
            }
            else {
                Toast.makeText(AddUpdateNoteActivity.this, getResources().getString(R.string.failed), Toast.LENGTH_SHORT).show();

            }
        }
        else {
            notes = new Notes(0 , category_id , title , content , color , createAt , dateFormat.format(new Date()));
            if(notesDAO.insertNote(notes)){
                Toast.makeText(AddUpdateNoteActivity.this, getResources().getString(R.string.successfull), Toast.LENGTH_SHORT).show();
                 finish();
            }
            else {
                Toast.makeText(AddUpdateNoteActivity.this, getResources().getString(R.string.failed), Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void getCategory () {
        categoryList = categoryDAO.getAllCategory() ;
        categoryAdapter = new ArrayAdapter(AddUpdateNoteActivity.this , android.R.layout.simple_spinner_item ,categoryList) ;
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnCategory.setAdapter(categoryAdapter);

    }
    private void checkUpdate () {
        notes = (Notes) getIntent().getSerializableExtra("notes");
        if(notes != null) {
            edtTitleNote.setText(notes.getTitle());
            edtContentNote.setText(notes.getContent());
            tvDateUpDateNote.setText(notes.getUpdateAt());
            edtContentNote.setTextColor(InvertingColor.invertingColor(Integer.parseInt(notes.getColor())));
            edtContentNote.setBackgroundColor(Integer.parseInt(notes.getColor()));

            for(int i = 0 ; i< categoryList.size() ; i++){
                if(notes.getCategory_id() == categoryList.get(i).getId()) {
                    spnCategory.setSelection(i);
                }
            }
        }
    }

    public void setDateTimeUpadateNote (){
        if(tvDateUpDateNote.getText().toString().equals("")){
            tvDateUpDateNote.setText(dateFormat.format(new Date()));
        }

    }
    public void openColorPicker (){
         defaultColor = ((ColorDrawable)edtContentNote.getBackground()).getColor() ;
         AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(AddUpdateNoteActivity.this ,defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                defaultColor = color ;
                edtContentNote.setBackgroundColor(defaultColor);
                edtContentNote.setTextColor(InvertingColor.invertingColor(color));
            }
        });
        ambilWarnaDialog.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(permissions == null || grantResults == null || grantResults.length <2 ||
            grantResults[0] != PackageManager.PERMISSION_GRANTED ||
            grantResults[1] != PackageManager.PERMISSION_GRANTED){
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode != RESULT_OK || data == null){
            return;
        }
        if (requestCode == REQUEST_CODE_SCAN) {
            HmsScan obj = data.getParcelableExtra(ScanUtil.RESULT);

            if (obj instanceof HmsScan) {
                if (!TextUtils.isEmpty(obj.getOriginalValue())) {
                    try {
                        JSONObject jsonObject = new JSONObject(obj.getOriginalValue());
                        edtTitleNote.setText(jsonObject.getString("title"));
                        edtContentNote.setText(jsonObject.getString("content"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return;
            }
        }
    }

}