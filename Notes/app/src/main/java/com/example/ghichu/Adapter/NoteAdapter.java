package com.example.ghichu.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ghichu.DAO.CategoryDAO;
import com.example.ghichu.DAO.NotesDAO;
import com.example.ghichu.DTO.Category;
import com.example.ghichu.DTO.Notes;
import com.example.ghichu.Interface.OnNoteClickListener;
import com.example.ghichu.Orthers.Format;
import com.example.ghichu.Orthers.InvertingColor;
import com.example.ghichu.R;

import java.util.List;

import yuku.ambilwarna.AmbilWarnaDialog;


public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    Context context ;
    int layout ;
    List<Notes> notesList ;
    OnNoteClickListener noteClickListener ;
    List<Category> categoryList ;
    CategoryDAO categoryDAO ;
    NotesDAO notesDAO ;

    int backgroundItemColor ;

    public NoteAdapter(Context context, int layout, List<Notes> notesList) {
        this.context = context;
        this.layout = layout;
        this.notesList = notesList;
    }

    public NoteAdapter(Context context, int layout, List<Notes> notesList, OnNoteClickListener noteClickListener) {
        this.context = context;
        this.layout = layout;
        this.notesList = notesList;
        this.noteClickListener = noteClickListener;
    }

    @NonNull
    @Override
    //Create a viewholder object in which the view displays the data
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    // Move element data to viewholder
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
         Notes notes = notesList.get(position) ; // create list at position
          backgroundItemColor = Integer.parseInt(notes.getColor()) ;
          Drawable drawable = context.getResources().getDrawable(R.drawable.custom_background_item_note);
          drawable.setColorFilter(backgroundItemColor, PorterDuff.Mode.SRC_ATOP);
          holder.llItemNote.setBackground(drawable);

         // set color for tag
         holder.tvTitle.setTextColor(InvertingColor.invertingColor(backgroundItemColor));
         holder.tvContent.setTextColor(InvertingColor.invertingColor(backgroundItemColor));
//         holder.tvContent.setTextColor(backgroundItemColor);
         holder.tvDateUpdate.setTextColor(InvertingColor.invertingColor(backgroundItemColor));

         // set color for search

         holder.tvTitle.setText(notes.getTitle());
         holder.tvContent.setText(Format.sortText(notes.getContent() , 130));
         holder.tvDateUpdate.setText(notes.getUpdateAt());

         holder.itemView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 noteClickListener.goToActivityUpdate(notes);
             }
         });
         // Hold the mouse to display the menu
         holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
             @Override
             public boolean onLongClick(View view) {
                 showPopUpMenu(holder , notes);
                 return true;
             }
         });
    }

    @Override
    // count element
    public int getItemCount() {
        if(notesList != null){
            return notesList.size() ;
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llItemNote , lnItemNote;
        TextView tvTitle , tvContent , tvDateUpdate ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            llItemNote = itemView.findViewById(R.id.llItemNote);
            tvTitle    = itemView.findViewById(R.id.tvTitle) ;
            tvContent  = itemView.findViewById(R.id.tvContent) ;
            tvDateUpdate = itemView.findViewById(R.id.tvDateUpdate) ;

        }
    }
    private void openColorPicker(ViewHolder holder , Notes notes){
        backgroundItemColor = Integer.parseInt(notes.getColor()) ;
        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(context, backgroundItemColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                // set color
                notes.setColor(String.valueOf(color));
                holder.tvTitle.setTextColor(InvertingColor.invertingColor(color));
                holder.tvContent.setTextColor(InvertingColor.invertingColor(color));
                holder.tvDateUpdate.setTextColor(InvertingColor.invertingColor(color));

                NotesDAO notesDAO = new NotesDAO(context) ;
                if(notesDAO.updateNote(notes)){
                    Toast.makeText(context,"Successfull", Toast.LENGTH_SHORT).show();
                }
                notifyDataSetChanged();
            }
        });
        ambilWarnaDialog.show(); // show up menu
    }
    private void updateCategoryOfNote (Notes notes){
        Dialog dialog = new Dialog(context) ;
        dialog.setContentView(R.layout.dialog_update_category_of_note);

        ListView lvCategory = dialog.findViewById(R.id.lvCategory) ;
        categoryDAO = new CategoryDAO(context) ;
        notesDAO = new NotesDAO(context) ;
        categoryList = categoryDAO.getAllCategory();

        ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1 ,categoryList) ;
        lvCategory.setAdapter(arrayAdapter);

        lvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                notes.setCategory_id(categoryList.get(i).getId());
                if(notesDAO.updateNote(notes)){
                    Toast.makeText(context,"Successfull", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }
    @SuppressLint("RestrictedApi")
    public void showPopUpMenu (ViewHolder holder , Notes notes){
        MenuBuilder builder = new MenuBuilder(context) ;
        MenuInflater menuInflater = new MenuInflater(context) ;
        menuInflater.inflate(R.menu.popup_menu_item_note , builder);

        MenuPopupHelper menuPopupHelper = new MenuPopupHelper(context , builder , holder.itemView) ;
        menuPopupHelper.setForceShowIcon(true);

        builder.setCallback(new MenuBuilder.Callback() {
            @Override
            public boolean onMenuItemSelected(@NonNull MenuBuilder menu, @NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.mnuChangeTag:
                        updateCategoryOfNote(notes); // Change name
                        break;
                    case R.id.mnuChangeBackgroud:
                        openColorPicker(holder,notes); // Change color
                        break;
                    case R.id.mnuShare:  // Share note by qr
                        noteClickListener.onShareNote(notes);
                        break;
                    case R.id.mnuDeleteNote:
                        noteClickListener.onDeleteClick(notes); // delete notes
                        break;

                }
                return false;
            }

            @Override
            public void onMenuModeChange(@NonNull MenuBuilder menu) {

            }
        });
        menuPopupHelper.show();
    }
}
