
package com.roy.bkapp.model.music.billlist;

import java.util.List;

public class JsonSongListBean {

    private Billboard billboard;
    private Long error_code;
    private List<SongList> song_list;

    public Billboard getBillboard() {
        return billboard;
    }

    public void setBillboard(Billboard billboard) {
        this.billboard = billboard;
    }

    public Long getError_code() {
        return error_code;
    }

    public void setError_code(Long error_code) {
        this.error_code = error_code;
    }

    public List<SongList> getSong_list() {
        return song_list;
    }

    public void setSong_list(List<SongList> song_list) {
        this.song_list = song_list;
    }
}
