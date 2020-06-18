package com.example.musicvplayer.music;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.example.musicvplayer.MusicAIDL;
import com.example.musicvplayer.util.VxUtil;

import java.util.Arrays;
import java.util.WeakHashMap;

public class PlayerServices {

    public static MusicAIDL mRemote = null;
    private static final String TAG = "PlayerServices";
    private static final WeakHashMap<Context,ServiceBinder> mHashMap;
    private static long[] emptyList = null;

    static {
        mHashMap = new WeakHashMap<>();
    }
    public static final  ServiceToken bindToService(Context context,ServiceConnection serviceConnection){
        Activity realActivity = ((Activity)context).getParent();
        if(realActivity==null){
            realActivity = (Activity) context;
        }
        ContextWrapper mWrapper = new ContextWrapper(realActivity);
        mWrapper.startService(new Intent(mWrapper,MusicService.class));
        ServiceBinder binder = new ServiceBinder(serviceConnection,mWrapper.getApplicationContext());
       if (mWrapper.bindService(new Intent().setClass(mWrapper,MusicService.class),binder,0)){
           mHashMap.put(mWrapper,binder);
           return new ServiceToken(mWrapper);
       }
       return null;
    }

    public static final boolean isPlayBackServiceConnected(){
        return mRemote != null;
    }

    public static void unBindToService(ServiceToken token){
        if(token==null){
            return;
        }
        ContextWrapper mWrapper = token.contextWrapper;
        ServiceBinder  binder = mHashMap.remove(mWrapper);
        if(binder==null){
            return;
        }
        mWrapper.unbindService(binder);
        if(mHashMap.isEmpty()){
            binder=null;
        }
    }

    //------------------All method-------------------------
    public static void pause(){
        if (mRemote!=null){
            try {
                mRemote.pause();
            } catch (RemoteException e){
                e.printStackTrace();
            }
        }
    }

    public static final boolean isPlaying(){
        if (mRemote!=null){
            try {
                return mRemote.isPlaying();
            } catch (final RemoteException ignored){
            }
        }
        return false;
    }

    public static void playOrPause(){
        try {
            if (mRemote!=null){
                if(mRemote.isPlaying()){
                    mRemote.pause();
                } else {
                    mRemote.play();
                }
            }
        } catch (RemoteException e){
         }
    }

    public static void playAll(long[] list, int position, long sourceId, VxUtil.IdType type) throws RemoteException{
        if (list.length == 0 && list == null && mRemote == null){
            return;
        }
        try {
            long audioId = getAudioId();
            int currentPos = getCurrentPos();
            if (position == currentPos && audioId == list[position] && position != -1) {
                long[] idList = getSaveIdList();
                if (Arrays.equals(idList, list)) {
                    play();
                }
            }

            if (position < 0) {
                position = 0;
            }
            mRemote.open(list, position, sourceId, type.mId);
            play();
        } catch (RemoteException e){
            } catch (IllegalStateException ignore){
            ignore.printStackTrace();
        }

    }

    private static long[] getSaveIdList() throws RemoteException {
        if (mRemote!=null){
            mRemote.getsaveIdList();
        }
        return emptyList;
    }

    private static void play() {
        if (mRemote!=null){
            try {
                mRemote.play();
            } catch (RemoteException e){
                e.printStackTrace();
            }
        }
    }


    private static int getCurrentPos() throws RemoteException {
        if (mRemote!=null){
            return mRemote.getCurrentPos();
        }
        return -1;
    }

    private static long getAudioId() throws RemoteException {
        if (mRemote!=null){
           return mRemote.getAudioId();
        }
        return -1;
    }


    //-------------------All method---------end

    public static class ServiceToken{
        private ContextWrapper contextWrapper;

        public ServiceToken(ContextWrapper contextWrapper) {
            this.contextWrapper = contextWrapper;
        }
    }
        public static final class ServiceBinder implements ServiceConnection{
        private ServiceConnection mService;
        private Context mContext;

        public ServiceBinder(ServiceConnection mService, Context mContext) {
            this.mService = mService;
            this.mContext = mContext;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            mRemote = MusicAIDL.Stub.asInterface(iBinder);
            if(mService!=null){
                mService.onServiceConnected(name,iBinder);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if(mService!=null){
                mService.onServiceDisconnected(name);
            }
            mRemote = null;
        }
    }

}
