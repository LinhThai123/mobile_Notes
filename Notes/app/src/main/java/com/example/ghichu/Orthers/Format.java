package com.example.ghichu.Orthers;

public class Format {
    public static String sortText (String text , int length){
        String newText ;
        if(text.length() > length){
            newText = text.substring(0 , length) ;
            return newText ;
        }
        return text ;
    }
}
