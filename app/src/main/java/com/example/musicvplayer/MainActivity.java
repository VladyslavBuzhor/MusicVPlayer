package com.example.musicvplayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.Manifest;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;



import com.example.musicvplayer.fragments.MainFragment;
import com.example.musicvplayer.music.PlayerServices;


import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


import static com.example.musicvplayer.music.PlayerServices.isPlaying;
import static com.example.musicvplayer.music.PlayerServices.mRemote;


public class MainActivity extends AppCompatActivity implements ServiceConnection {
    private static final int KEY_PER = 123;
    private static final String TAG = "MainActivity";
    private PlayerServices.ServiceToken token;
      @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]
                    {Manifest.permission.READ_EXTERNAL_STORAGE}, KEY_PER);
            return;

        } else {
            UiInitialize();
        }
   }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
          switch (requestCode){
              case KEY_PER:
                  if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                      UiInitialize();
                  }
             default:
                  super.onRequestPermissionsResult(requestCode, permissions, grantResults);
          }
    }

    private void UiInitialize(){
        token =  PlayerServices.bindToService(this,this);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
        Fragment fragment = new MainFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, fragment);
        transaction.commitAllowingStateLoss();
      }

    @Override
    protected void onResume() {
     if (token == null) {
         token = PlayerServices.bindToService(this,this);
     }
     if (isPlaying()){
         Log.v(TAG, "Playing");
     }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(token!=null){
            PlayerServices.unBindToService(token);
            token = null;
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder iBinder) {
        mRemote = MusicAIDL.Stub.asInterface(iBinder);
        if (isPlaying()){
            Log.v(TAG,"Playing");
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mRemote = null;
    }
}
