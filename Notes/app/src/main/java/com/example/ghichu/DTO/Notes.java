package com.example.ghichu.DTO;

import java.io.Serializable;

// create model Notes
public class Notes implements Serializable {
    private int id ;
    private int category_id ;
    private String title ;
    private String color ;
    private String content ;
    private String createAt ;
    private String updateAt ;

    public Notes() {
    }

    public Notes(int id, int category_id, String title,String content, String color, String createAt, String updateAt) {
        this.id = id;
        this.category_id = category_id;
        this.title = title;
        this.color = color;
        this.content = content ;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCreateAt() {
        return createAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    @Override
    // show model Notes
    public String toString() {
        return "Notes{" +
                "id=" + id +
                ", category_id=" + category_id +
                ", title='" + title + '\'' +
                ", color='" + color + '\'' +
                ", content='" + content + '\'' +
                ", createAt='" + createAt + '\'' +
                ", updateAt='" + updateAt + '\'' +
                '}';
    }
    public String noteMapper (){
        return "{\"title\":\"" + title + "\" ,\"content\":\""+ content + "\"}";
    }
}
