package com.example.ghichu.Interface;

import com.example.ghichu.DTO.Notes;

public interface OnNoteClickListener {
    void onDeleteClick (Notes notes) ;
    void onShareNote (Notes notes) ;
    void goToActivityUpdate (Notes notes) ;
    void onGetListByCategoryId (int id) ;
}
