package com.hechuangwu.client;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import com.hechuangwu.server.aidl.Book;
import com.hechuangwu.server.aidl.IBookAidlInterface;
import com.hechuangwu.server.aidl.IBookListenerlInterface;
import com.hechuangwu.utils.Config;

import java.util.List;
import java.util.Random;

/**
 * Created by cwh on 2019/3/18.
 * 功能:
 */
public class AIDLActivity extends Activity {

    private IBookAidlInterface mIBookAidlInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_aidl );

    }


    public void bind(View view) {
        Intent intent = new Intent( );
        intent.setPackage( Config.PACKAGE_NAME );
        intent.setAction( Config.SERVER_AIDL );
        bindService( intent,mServiceConnection,Context.BIND_AUTO_CREATE );
    }

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient(){
        @Override
        public void binderDied() {
            if(mIBookAidlInterface!=null){
                mIBookAidlInterface.asBinder().unlinkToDeath( mDeathRecipient ,0);
                mIBookAidlInterface = null;
                Log.i( "data", "binderDied: >>>>>>>>>>>>>>>>>>>" );
                bind( null );

            }
        }
    };

    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mIBookAidlInterface = IBookAidlInterface.Stub.asInterface( service );
            try {
                service.linkToDeath( mDeathRecipient,0 );
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            try {
                mIBookAidlInterface.registerBookListener(mIBookListenerlInterface);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private IBookListenerlInterface mIBookListenerlInterface = new IBookListenerlInterface.Stub() {
        @Override
        public void addNewBookListener(final Book book) throws RemoteException {
            //bundle位于线程池中
            runOnUiThread( new Runnable() {
                @Override
                public void run() {
                    Log.i( "data", "addNewBookListener: "+book );
                }
            } );

        }
    };



    public void get(View view) {
        if(mIBookAidlInterface!=null){
            //处理服务端做耗时操作
                new Thread( new Runnable() {
                    @Override
                    public void run() {
                        List<Book> booKList  = null;
                        try {
                            booKList = mIBookAidlInterface.getBookList();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        for(Book book:booKList){
                            Log.i( "data", "get: "+book );
                        }
                    }
                } ).start();

        }
    }

    public void add(View view) {
        Book book = new Book( new Random().nextInt( 10 ), "欧里庇得斯", "100000$" );
        try {
            mIBookAidlInterface.addBook( book );
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        if(mIBookAidlInterface!=null&&mIBookAidlInterface.asBinder().isBinderAlive()){
            try {
                mIBookAidlInterface.unRegisterBookListener( mIBookListenerlInterface );
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            unbindService( mServiceConnection );
        }
        super.onDestroy();
    }

    public void back(View view) {
        finish();
    }
}
