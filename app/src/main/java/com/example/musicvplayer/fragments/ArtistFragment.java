package com.example.musicvplayer.fragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicvplayer.R;
import com.example.musicvplayer.adapters.ArtistAdapter;
import com.example.musicvplayer.adapters.GridSpacingItemDecoration;
import com.example.musicvplayer.dataloader.ArtistLoader;

public class ArtistFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArtistAdapter adapter;
    int spanCount = 2;
    int spacing = 30;
    boolean includeEdge = true;
       @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
           View view = inflater.inflate(R.layout.fragment_artist, container, false);;
           recyclerView =(RecyclerView)view.findViewById(R.id.artistrecycler);
           recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),spanCount));
           recyclerView.setHasFixedSize(true);
           new LoadData().execute("");
           return view;
    }

    public class LoadData extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {

            if(getActivity()!=null){
                adapter = new ArtistAdapter(getActivity(),new ArtistLoader().artistList(getActivity()));
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String s) {
            recyclerView.setAdapter(adapter);
            if(getActivity()!=null){
                recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount,spacing,includeEdge));
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
}
