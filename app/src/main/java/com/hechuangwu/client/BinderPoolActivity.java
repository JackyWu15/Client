package com.hechuangwu.client;

import android.app.Activity;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Toast;

import com.hechuangwu.server.aidl.BinderPool;
import com.hechuangwu.server.aidl.IAdditionlInterface;
import com.hechuangwu.server.aidl.ISubtractionInterface;

/**
 * Created by cwh on 2019/3/27.
 * 功能:
 */
public class BinderPoolActivity extends Activity {

    private BinderPool mBinderPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_binderpool );
    }
    public void bind(View view) {
        new Thread( new Runnable() {
            @Override
            public void run() {
                mBinderPool = BinderPool.getInstance( BinderPoolActivity.this );
            }
        } ).start();
    }
    public void add(View view) {
        try {
            IBinder iBinder = mBinderPool.queryBinder( BinderPool.BINDER_ADD );
            IAdditionlInterface addition = IAdditionlInterface.Stub.asInterface( iBinder );
            int result = addition.addition( 1, 2 );
            Toast.makeText( this,result+"",Toast.LENGTH_LONG ).show();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void sub(View view) {
        try {
            IBinder iBinder = mBinderPool.queryBinder( BinderPool.BINDER_SUB );
            ISubtractionInterface subtraction = ISubtractionInterface.Stub.asInterface( iBinder );
            int result  = subtraction.subtraction( 5, 1 );
            Toast.makeText( this,result+"",Toast.LENGTH_LONG ).show();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }


}
