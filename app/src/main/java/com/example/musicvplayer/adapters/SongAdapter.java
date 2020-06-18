package com.example.musicvplayer.adapters;

import android.content.ContentUris;
import android.net.Uri;
import android.os.Handler;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicvplayer.models.Song;
import com.example.musicvplayer.music.PlayerServices;
import com.example.musicvplayer.util.VxUtil;
import com.example.musicvplayer.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.VH> {

    public static List<Song> songList;
    private long[] mIds;

    public SongAdapter(List<Song> songList) {
        this.songList = songList;
        mIds = getIds();
    }
    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.song_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Song song = songList.get(position);

        if(song!=null){
            holder.titles.setText(song.title);
            holder.artists.setText(song.artistName);
            ImageLoader.getInstance().displayImage(getImage(song.albumId).toString(),holder.imageView,
                    new DisplayImageOptions.Builder().cacheInMemory(true).showImageOnLoading(R.drawable.note)
                            .resetViewBeforeLoading(true).build());
        }
    }

    public static Uri getImage(long albumId) {
    return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"),albumId);
    }

    @Override
    public int getItemCount() {
        return songList!=null?songList.size():0;
    }

    private long[] getIds() {
        long[] res = new long[getItemCount()];
        for (int i=0;i<getItemCount();i++){
            res[i] = songList.get(i).id;
        }
        return res;
    }


    public class VH extends RecyclerView.ViewHolder implements View.OnClickListener {
private ImageView imageView;
private TextView titles, artists;
        public VH(@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView)itemView.findViewById(R.id.songthumb);
            titles = (TextView)itemView.findViewById(R.id.songname);
            artists = (TextView)itemView.findViewById(R.id.artistname);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        PlayerServices.playAll(mIds,getAdapterPosition(),songList.get(getAdapterPosition()).id, VxUtil.IdType.NA);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            },100);
        }
    }
}
