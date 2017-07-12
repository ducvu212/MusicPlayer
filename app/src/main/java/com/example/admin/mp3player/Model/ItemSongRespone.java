package com.example.admin.mp3player.Model;

/**
 * Created by minhd on 17/05/21.
 */

public class ItemSongRespone {

    private String dataCode ;
    private String title;

    public ItemSongRespone(String dataCode, String title) {
        this.dataCode = dataCode;
        this.title = title;
    }

    public String getDataCode() {
        return dataCode;
    }

    public void setDataCode(String dataCode) {
        this.dataCode = dataCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
