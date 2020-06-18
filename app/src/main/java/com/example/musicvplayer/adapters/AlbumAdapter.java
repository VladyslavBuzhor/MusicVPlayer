package com.example.musicvplayer.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicvplayer.fragments.AlbumDetailsFragment;
import com.example.musicvplayer.R;
import com.example.musicvplayer.models.Album;
import com.example.musicvplayer.widgets.SquareImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import static com.example.musicvplayer.adapters.SongAdapter.getImage;


public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {
  private Activity context;
  private List<Album> albumList;

    public AlbumAdapter(Activity context, List<Album> albumList) {
        this.context = context;
        this.albumList = albumList;
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid,parent,false);
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        Album album = albumList.get(position);
        if(album!=null){
            holder.albumT.setText(album.albumName);
            holder.albumA.setText(album.artistName);
            ImageLoader.getInstance().displayImage(getImage(album.id).toString(),holder.img,
                    new DisplayImageOptions.Builder().cacheInMemory(true).showImageOnLoading(R.drawable.note).resetViewBeforeLoading(true).build());
        }
    }

    @Override
    public int getItemCount() {
        return albumList!=null?albumList.size():0;
    }

    public class AlbumViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {
        private SquareImageView img;
        private TextView albumT, albumA;

        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.albumimg);
            albumT = itemView.findViewById(R.id.album_title);
            albumA = itemView.findViewById(R.id.album_artist);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            long albumId = albumList.get(getAdapterPosition()).id;
            FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            Fragment fragment;
            transaction.setCustomAnimations(R.anim.layout_fad_in,R.anim.layout_fad_out,
                    R.anim.layout_fad_in, R.anim.layout_fad_out);
            fragment = AlbumDetailsFragment.newInstance(albumId);
            transaction.hide(((AppCompatActivity)context).getSupportFragmentManager()
                   .findFragmentById(R.id.main_container));
            transaction.add(R.id.main_container,fragment);
            transaction.addToBackStack(null).commit();
        }
    }
}
