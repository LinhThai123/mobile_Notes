package com.example.ghichu.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ghichu.Adapter.CategoryAdapter;
import com.example.ghichu.DAO.CategoryDAO;
import com.example.ghichu.DTO.Category;
import com.example.ghichu.Database.Mydatabase;
import com.example.ghichu.Interface.OnCategoryClickListener;
import com.example.ghichu.R;

import java.util.List;

public class ListCategoryActivity extends AppCompatActivity implements OnCategoryClickListener {
    Toolbar tbListCategory;
    RecyclerView lvCategory;
    CategoryAdapter categoryAdapter;
    List<Category> categoryList;
    CategoryDAO categoryDAO;
    Category category ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_category);
        mapping();
        categoryDAO = new CategoryDAO(this);
        Mydatabase mydatabase = new Mydatabase(this);
        setSupportActionBar(tbListCategory);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        showDataCategory();
    }

    private void mapping() {
        tbListCategory = findViewById(R.id.tbListCategory);
        lvCategory = findViewById(R.id.lvCategory);
    }

    //
    private void showDataCategory() {
        categoryList = categoryDAO.getAllCategory();
        categoryAdapter = new CategoryAdapter(ListCategoryActivity.this, R.layout.item_list_category, categoryList, this);
        lvCategory.setAdapter(categoryAdapter);

        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        lvCategory.setLayoutManager(gridLayoutManager);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        lvCategory.addItemDecoration(itemDecoration);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showActivityAddCategory(View view) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_add_update_category);
        dialog.setCanceledOnTouchOutside(false);

        EditText edtTitleCategory;
        Button btnCancelcategory, btnAddUpdateCategory;

        edtTitleCategory = dialog.findViewById(R.id.editTitleCategory);
        btnAddUpdateCategory = dialog.findViewById(R.id.btnAddUpdateCategory);
        btnCancelcategory = dialog.findViewById(R.id.btnCancelUpdateCategory);

        btnCancelcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnAddUpdateCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryDAO = new CategoryDAO(ListCategoryActivity.this);
                String titleCategory = edtTitleCategory.getText().toString();
                if(titleCategory.equalsIgnoreCase("")){
                    dialog.dismiss();
                }
                else {
                    for(int i = 0 ; i<categoryList.size() ; i++){
                        if(titleCategory.equalsIgnoreCase(categoryList.get(i).getTitle())){
                            Toast.makeText(ListCategoryActivity.this, "Tag already exist", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    category = new Category(0,titleCategory.trim(),"");
                    if(categoryDAO.insertCategory(category)){
                        Toast.makeText(ListCategoryActivity.this, getResources().getString(R.string.successfull), Toast.LENGTH_SHORT).show();
                        categoryList.add(category);
                        categoryAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                    else{
                        Toast.makeText(ListCategoryActivity.this, getResources().getString(R.string.failed), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        dialog.show();
    }

    @Override
    public void onUpdateClick(Category category) {
        String title = category.getTitle();

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_add_update_category);
        dialog.setCanceledOnTouchOutside(false);

        TextView tvTitleActivityAddUpdate ;
        EditText edtTitleCategory ;
        Button btnCancelCategory , btnAddUpdateCategory ;

        tvTitleActivityAddUpdate = dialog.findViewById(R.id.tvTitleActivityAddUpdate) ;
        edtTitleCategory = dialog.findViewById(R.id.editTitleCategory) ;
        btnAddUpdateCategory = dialog.findViewById(R.id.btnAddUpdateCategory) ;
        btnCancelCategory = dialog.findViewById(R.id.btnCancelUpdateCategory) ;

        tvTitleActivityAddUpdate.setText("Rename Tag");
        btnAddUpdateCategory.setText("Rename");
        edtTitleCategory.setText(title);

        btnCancelCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnAddUpdateCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titleNew = edtTitleCategory.getText().toString().trim() ;
                if(titleNew.equalsIgnoreCase("")){
                    dialog.dismiss();
                }
                else {
                    for(int i=0 ; i<categoryList.size() ; i++){
                        if(titleNew.equalsIgnoreCase(categoryList.get(i).getTitle())){
                            Toast.makeText(ListCategoryActivity.this, "Tag already exist", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    category.setTitle(titleNew);
                    if(categoryDAO.updateCategory(category)){
                        Toast.makeText(ListCategoryActivity.this, getResources().getString(R.string.successfull), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                    else {
                        Toast.makeText(ListCategoryActivity.this, getResources().getString(R.string.failed), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        dialog.show();
    }

    @Override
    public void onDeleteClick(Category category) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ListCategoryActivity.this);
        builder.setTitle("Warning !!! ") ;
        builder.setIcon(android.R.drawable.ic_delete) ;
        builder.setMessage("Delete this tag" ) ;
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(categoryDAO.deleteCategory(category)){
                    Toast.makeText(ListCategoryActivity.this, getResources().getString(R.string.successfull), Toast.LENGTH_SHORT).show();
                    categoryList.remove(category) ;
                    categoryAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(ListCategoryActivity.this, getResources().getString(R.string.failed), Toast.LENGTH_SHORT).show();
                }
            }
        }) ;
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

}