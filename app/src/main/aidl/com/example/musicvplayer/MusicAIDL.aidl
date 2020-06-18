// MusicAIDL.aidl
package com.example.musicvplayer;

interface MusicAIDL {

    void open(in long[] list, int position, long sourceId, int type);
    void play();
    void stop();
    void pause();
    boolean isPlaying();
    long getAudioId();
    int getCurrentPos();
    long[] getsaveIdList();

}
