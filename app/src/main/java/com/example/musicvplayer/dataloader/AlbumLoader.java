package com.example.musicvplayer.dataloader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.musicvplayer.models.Album;

import java.util.ArrayList;
import java.util.List;

public class AlbumLoader {

    public List<Album> getAlbums(Context context, Cursor cursor){

             List<Album> albums = new ArrayList<>();
             if(cursor!=null && cursor.moveToFirst()){
                 do{
                     albums.add(new Album(cursor.getLong(0), cursor.getString(1),
                             cursor.getLong(2), cursor.getString(3), cursor.getInt(4),
                             cursor.getInt(5)));
                 } while ((cursor.moveToNext()));
                if (cursor!=null){
                    cursor.close();
                }


             }
             return albums;
    }

    public Album getAlbum(Context context, long id){
        return  album(makeCursor(context, "_id=?",new String[]{String.valueOf(id)}));
    }

    private Album album(Cursor cursor) {
        Album album = new Album();
        if (cursor.moveToFirst()&& cursor!=null){
               album = new Album(cursor.getLong(0), cursor.getString(1),
                        cursor.getLong(2), cursor.getString(3), cursor.getInt(4),
                        cursor.getInt(5));
                if (cursor!=null){
                cursor.close();
            }
        }
        return album;
    }

    public List<Album> albumList(Context context){
        return getAlbums(context,makeCursor(context,null, null));
    }

     public static Cursor makeCursor(Context context, String selection,String[] selectionArg){

         Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
         String[] projection = new String[]{
               MediaStore.Audio.Albums._ID ,//0
               MediaStore.Audio.Albums.ALBUM,//1
               MediaStore.Audio.Albums.ARTIST_ID,//2
               MediaStore.Audio.Albums.ARTIST,//3
               MediaStore.Audio.Albums.NUMBER_OF_SONGS,//4
               MediaStore.Audio.Albums.FIRST_YEAR,//5

         };
         String sortOrder = MediaStore.Audio.Albums.DEFAULT_SORT_ORDER;
        Cursor cursor = context.getContentResolver().query(uri,projection,selection,selectionArg,sortOrder);
        return cursor;
     }
}
