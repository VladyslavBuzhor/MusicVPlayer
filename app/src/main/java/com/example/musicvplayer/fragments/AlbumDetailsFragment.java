package com.example.musicvplayer.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicvplayer.R;
import com.example.musicvplayer.adapters.AlbumSongAdapter;
import com.example.musicvplayer.dataloader.AlbumLoader;
import com.example.musicvplayer.dataloader.AlbumSongLoader;
import com.example.musicvplayer.models.Album;
import com.example.musicvplayer.models.Song;
import com.example.musicvplayer.widgets.SquareImageView;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import static com.example.musicvplayer.adapters.SongAdapter.getImage;

public class AlbumDetailsFragment extends Fragment {

private CollapsingToolbarLayout collapsingToolbarLayout;
private long album_id;
private List<Song> songList = new ArrayList<>();
private Album album;
private ImageView imageView;
private SquareImageView squareImageView;
private TextView atrname, ade;
private RecyclerView recyclerView;
private AlbumSongAdapter adapter;


    public static AlbumDetailsFragment newInstance(long id) {

        Bundle args = new Bundle();
        args.putLong("_ID",id);

        AlbumDetailsFragment fragment = new AlbumDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        album_id = getArguments().getLong("_ID");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_album_details, container, false);
        squareImageView = rootView.findViewById(R.id.aaimg);
        imageView = rootView.findViewById(R.id.album_art);
        atrname = rootView.findViewById(R.id.atrname);
        ade = rootView.findViewById(R.id.albumDetails);
        collapsingToolbarLayout = rootView.findViewById(R.id.collapsed_layout);
        recyclerView = rootView.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        album = new AlbumLoader().getAlbum(getActivity(),album_id);
            setDetails();
            setAlbumlist();


        return rootView;
    }

    private void setAlbumlist() {
        songList = AlbumSongLoader.getAllAlbumSongs(getActivity(),album_id);
        adapter = new AlbumSongAdapter(getActivity(),songList);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);

    }

    private void setDetails() {
        collapsingToolbarLayout.setTitle(album.albumName);
        atrname.setText(album.albumName);
        ade.setText(album.artistName+" "+album.year+" songs: "+album.numSong);
        ImageLoader.getInstance().displayImage(getImage(album.id).toString(),imageView,
                new DisplayImageOptions.Builder().cacheInMemory(true).showImageOnLoading(R.drawable.note)
                        .resetViewBeforeLoading(true).build());

        ImageLoader.getInstance().displayImage(getImage(album.id).toString(),squareImageView,
                new DisplayImageOptions.Builder().cacheInMemory(true).showImageOnLoading(R.drawable.note)
                        .resetViewBeforeLoading(true).build());
    }
}
