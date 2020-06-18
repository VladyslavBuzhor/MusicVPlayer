package com.example.musicvplayer.models;

public class Artist {
    public final long id;
    public final String artistName;
    public final int albumCount;
    public final int songCount;


    public Artist() {
        id = -1;
        artistName = "";
        albumCount = -1;
        songCount = -1;
    }

    public Artist(long id, String artistName, int albumCount, int songCount) {
        this.id = id;
        this.artistName = artistName;
        this.albumCount = albumCount;
        this.songCount = songCount;
    }
}
