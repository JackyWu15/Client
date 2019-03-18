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

    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mIBookAidlInterface = IBookAidlInterface.Stub.asInterface( service );
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public void get(View view) {
        if(mIBookAidlInterface!=null){
            List<Book> booKList = null;
            try {
                booKList = mIBookAidlInterface.getBookList();
                for(Book book:booKList){
                    Log.i( "data", "get: "+book );
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }

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
        unbindService( mServiceConnection );
        super.onDestroy();
    }
}
