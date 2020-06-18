package com.example.musicvplayer.models;

import com.example.musicvplayer.util.VxUtil;

public class PlayBackTrack {

    public long mId;
    public long sourceId;
    public VxUtil.IdType mIdType;
    public int mCurrentPos;

    public PlayBackTrack(long mId, long sourceId, VxUtil.IdType mIdType, int mCurrentPos) {
        this.mId = mId;
        this.sourceId = sourceId;
        this.mIdType = mIdType;
        this.mCurrentPos = mCurrentPos;
    }
}
