package com.roy.bkapp.model.music.playlist;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 1vPy(Roy) on 2017/4/17.
 */

public class MusicBean  implements Parcelable {
    private Integer id;
    private String musicName;
    private String singerName;
    private String musicId;

    public MusicBean(){

    }

    public MusicBean(String musicName,String singerName , String musicId){
        super();
        this.musicName = musicName;
        this.singerName = singerName;
        this.musicId = musicId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getMusicId() {
        return musicId;
    }

    public void setMusicId(String musicId) {
        this.musicId = musicId;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub

        dest.writeInt(id);
        dest.writeString(musicName);
        dest.writeString(singerName);
        dest.writeString(musicId);
    }

    public static final Creator<MusicBean> CREATOR = new Creator<MusicBean>() {

        @Override
        public MusicBean createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            MusicBean musicBean = new MusicBean();

            musicBean.setId(source.readInt());
            musicBean.setMusicName(source.readString());
            musicBean.setSingerName(source.readString());
            musicBean.setMusicId(source.readString());

            return musicBean;
        }

        @Override
        public MusicBean[] newArray(int size) {
            // TODO Auto-generated method stub
            return new MusicBean[size];
        }

    };

    public void readFromParcel(Parcel _reply) {
        // TODO Auto-generated method stub
        id = _reply.readInt();
        musicName = _reply.readString();
        singerName = _reply.readString();
        musicId = _reply.readString();
    }
}
