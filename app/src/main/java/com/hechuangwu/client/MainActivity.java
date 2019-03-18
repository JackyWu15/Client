package com.hechuangwu.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.hechuangwu.server.aidl.Book;
import com.hechuangwu.server.aidl.IBookAidlInterface;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    private IBookAidlInterface mIBookAidlInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

    }

    public void bind(View view) {
        Intent intent = new Intent( );
        intent.setPackage( "com.hechuangwu.server" );
        intent.setAction( "com.hechuangwu.server.action" );
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
}
