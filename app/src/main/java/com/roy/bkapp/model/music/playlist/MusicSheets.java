package com.roy.bkapp.model.music.playlist;

/**
 * Created by 1vPy(Roy) on 2017/4/21.
 */

public class MusicSheets {

    private Integer id;
    private String sheetsName;
    private int songCount;

    public MusicSheets() {

    }

    public MusicSheets(Integer id, String sheetsName, int songCount) {
        this.id = id;
        this.sheetsName = sheetsName;
        this.songCount = songCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSheetsName() {
        return sheetsName;
    }

    public void setSheetsName(String sheetsName) {
        this.sheetsName = sheetsName;
    }

    public int getSongCount() {
        return songCount;
    }

    public void setSongCount(int songCount) {
        this.songCount = songCount;
    }
}
