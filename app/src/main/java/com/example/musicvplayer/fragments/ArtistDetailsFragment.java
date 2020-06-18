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
import com.example.musicvplayer.dataloader.ArtistLoader;
import com.example.musicvplayer.dataloader.ArtistSongLoader;
import com.example.musicvplayer.models.Artist;
import com.example.musicvplayer.models.Song;
import com.example.musicvplayer.widgets.SquareImageView;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import static com.example.musicvplayer.adapters.SongAdapter.getImage;

public class ArtistDetailsFragment extends Fragment {
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private long artistId;
    private List<Song> songList = new ArrayList<>();
    private Artist artist;
    private ImageView imageView;
    private SquareImageView squareImageView;
    private TextView atrname, ade;
    private RecyclerView recyclerView;
    private AlbumSongAdapter adapter;


    public static ArtistDetailsFragment newInstance(long artist_id) {

        Bundle args = new Bundle();
        args.putLong("_ID",artist_id);

        ArtistDetailsFragment fragment = new ArtistDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        artistId = getArguments().getLong("_ID");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_artist_details, container, false);
        squareImageView = rootView.findViewById(R.id.artistimg);
        imageView = rootView.findViewById(R.id.bigartist);
        atrname = rootView.findViewById(R.id.artistrname);
        ade = rootView.findViewById(R.id.artistDetails);
        collapsingToolbarLayout = rootView.findViewById(R.id.artistcollapsed_layout);
        recyclerView = rootView.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        artist = new ArtistLoader().getArtist(getActivity(),artistId);
        setDetails();
        setAlbumlist();
        return rootView;
    }

    private void setAlbumlist() {
        songList = ArtistSongLoader.getAllArtistSongs(getActivity(),artistId);
        adapter = new AlbumSongAdapter(getActivity(),songList);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);

    }

    private void setDetails() {
        collapsingToolbarLayout.setTitle(artist.artistName);
        atrname.setText(artist.artistName);
        ade.setText(" songs: "+artist.songCount);
        ImageLoader.getInstance().displayImage(getImage(artist.id).toString(),imageView,
                new DisplayImageOptions.Builder().cacheInMemory(true).showImageOnLoading(R.drawable.note)
                        .resetViewBeforeLoading(true).build());

        ImageLoader.getInstance().displayImage(getImage(artist.id).toString(),squareImageView,
                new DisplayImageOptions.Builder().cacheInMemory(true).showImageOnLoading(R.drawable.note)
                        .resetViewBeforeLoading(true).build());
    }
}
