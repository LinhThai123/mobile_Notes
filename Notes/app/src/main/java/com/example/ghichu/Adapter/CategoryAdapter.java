package com.example.ghichu.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ghichu.DTO.Category;
import com.example.ghichu.Interface.OnCategoryClickListener;
import com.example.ghichu.Interface.OnNoteClickListener;
import com.example.ghichu.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Context context ;
    private int layout ;
    private List<Category> listCategory ;
    private static int category_id = 0 ;

    private OnCategoryClickListener onCategoryClickListener ;
    private OnNoteClickListener onNoteClickListener ;

    int row_index = -1 ;

    public CategoryAdapter(Context context, int layout, List<Category> listCategory, OnCategoryClickListener onCategoryClickListener) {
        this.context = context;
        this.layout = layout;
        this.listCategory = listCategory;
        this.onCategoryClickListener = onCategoryClickListener;
    }

    public CategoryAdapter(Context context, int layout, List<Category> listCategory, OnNoteClickListener onNoteClickListener) {
        this.context = context;
        this.layout = layout;
        this.listCategory = listCategory;
        this.onNoteClickListener = onNoteClickListener;
    }

    public CategoryAdapter(Context context, int layout, List<Category> listCategory) {
        this.context = context;
        this.layout = layout;
        this.listCategory = listCategory;
    }

    @NonNull
    @Override
    //Create a viewholder object in which the view displays the data
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(layout,parent,false);
        
        return new ViewHolder(view);
    }

    @Override
    // Move element data to viewholder
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Category category = listCategory.get(position); // create list category at position
        holder.tvItemCategory.setText(category.getTitle());

        if(layout == R.layout.item_category) {
            holder.tvItemCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    row_index = position ;
                    category_id = category.getId();
                    onNoteClickListener.onGetListByCategoryId(category_id);
                    notifyDataSetChanged(); // Thông báo toàn bộ dữ liệu được thay đổi
                }
            });
            //  get element the first
            if(row_index == -1 ){
                row_index = 0 ;
                holder.tvItemCategory.setBackgroundResource(R.drawable.custom_background_item_category_color);
                holder.tvItemCategory.setTextColor(Color.parseColor("#ffffff"));
            }
            // if have element , get element at position
            if(row_index == position){
                holder.tvItemCategory.setBackgroundResource(R.drawable.custom_background_item_category_color);
                holder.tvItemCategory.setTextColor(Color.parseColor("#ffffff"));
            }
            // if don't have element
            else {
                holder.tvItemCategory.setBackgroundResource(R.drawable.custom_background_item_category_base);
                holder.tvItemCategory.setTextColor(Color.parseColor("#000000"));
            }
        }
        else if(layout == R.layout.item_list_category){
            // Element in first position
            if(category.getId() == 1  ){
                // Turn off activity
                holder.imgvMoreCategory.setEnabled(false);
                holder.imgvLockItem.setEnabled(false);
            }
            holder.imgvMoreCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupMenu(holder,category);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        // check list != null , count element
        if(listCategory != null){
            return listCategory.size();
        }
        return 0 ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemCategory ;
        ImageView imgvMoveItem , imgvLockItem , imgvMoreCategory ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // mapping
            tvItemCategory = itemView.findViewById(R.id.tvItemCategory) ;
            if(layout == R.layout.item_list_category){
                imgvMoveItem     = itemView.findViewById(R.id.imgvMoveItem) ;
                imgvLockItem     = itemView.findViewById(R.id.imgvLockItem) ;
                imgvMoreCategory = itemView.findViewById(R.id.imgvMoreCategory) ;
            }
        }
    }
    // create PopUpMenu (rename , delete )
    private void showPopupMenu (ViewHolder holder , Category category) {
        PopupMenu popupMenu = new PopupMenu(context, holder.imgvMoreCategory) ;
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_more_in_list_category , popupMenu.getMenu());
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.mnuChangeNameCategory:
                        // rename category
                         onCategoryClickListener.onUpdateClick(category);
                        break;
                    case R.id.mnuDeleteCategory:
                        // delete category
                        onCategoryClickListener.onDeleteClick(category);
                        break;
                }
                return false ;
            }
        });
    }
}
